package com.jetug.chassis_core.common.network.managers;

import com.google.common.collect.ImmutableMap;
import com.jetug.chassis_core.ChassisCore;
import com.jetug.chassis_core.common.config.ChassisConfig;
import com.jetug.chassis_core.common.foundation.entity.Chassis;
import com.jetug.chassis_core.common.network.PacketHandler;
import com.jetug.chassis_core.common.network.packet.S2CMessageUpdateChassisConfig;
import com.jetug.chassis_core.modules.config.utils.ConfigUtils;
import com.mrcrayfish.framework.api.data.login.ILoginData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nullable;
import java.util.*;

@Mod.EventBusSubscriber(modid = ChassisCore.MOD_ID)
public class NetworkChassisManager extends SimplePreparableReloadListener<Map<EntityType<Chassis>, ChassisConfig>> {
    public static final String PATH = "chassis";
//    private static final List<ChassisBase> clientRegisteredAmmo = new ArrayList<>();
    private static NetworkChassisManager instance;

    private Map<ResourceLocation, ChassisConfig> registeredAmmo = new HashMap<>();

    @SubscribeEvent
    public static void onServerStopped(ServerStoppedEvent event) {
        NetworkChassisManager.instance = null;
    }

    @SubscribeEvent
    public static void addReloadListenerEvent(AddReloadListenerEvent event) {
        NetworkChassisManager networkGunManager = new NetworkChassisManager();
        event.addListener(networkGunManager);
        NetworkChassisManager.instance = networkGunManager;
    }

    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        if (event.getPlayer() == null) {
            PacketHandler.getPlayChannel().sendToAll(new S2CMessageUpdateChassisConfig());
        }
    }

    @Override
    protected Map<EntityType<Chassis>, ChassisConfig> prepare(ResourceManager manager, ProfilerFiller profiler) {
        return ConfigUtils.getConfigMap(manager, ForgeRegistries.ENTITY_TYPES, (v) -> true, ChassisConfig.class, PATH);
    }

    @Override
    protected void apply(Map<EntityType<Chassis>, ChassisConfig> objects, ResourceManager resourceManager, ProfilerFiller profiler) {
        var builder = ImmutableMap.<ResourceLocation, ChassisConfig>builder();

        objects.forEach((chassis, ammo) -> {
            Validate.notNull(ForgeRegistries.ENTITY_TYPES.getKey((chassis)));
            builder.put(ForgeRegistries.ENTITY_TYPES.getKey(chassis), ammo);
            Configs.CHASSIS_CONFIGS.put(chassis, new ConfigSupplier<>(ammo));
        });

        this.registeredAmmo = builder.build();
    }


    public void writeRegisteredAmmo(FriendlyByteBuf buffer) {
        buffer.writeVarInt(this.registeredAmmo.size());
        this.registeredAmmo.forEach((id, ammo) -> {
            buffer.writeResourceLocation(id);
            buffer.writeNbt(ammo.serializeNBT());
        });
    }

    public static ImmutableMap<ResourceLocation, ChassisConfig> readRegisteredAmmo(FriendlyByteBuf buffer) {
        var size = buffer.readVarInt();

        if (size > 0) {
            ImmutableMap.Builder<ResourceLocation, ChassisConfig> builder = ImmutableMap.builder();

            for (int i = 0; i < size; i++) {
                var id = buffer.readResourceLocation();
                var ammo = ChassisConfig.create(buffer.readNbt());
                builder.put(id, ammo);
            }
            return builder.build();
        }
        return ImmutableMap.of();
    }

    public static boolean updateRegisteredAmmo(S2CMessageUpdateChassisConfig message) {
        return updateRegisteredAmmo(message.getRegisteredAmmo());
    }

    /**
     * Updates registered projectile from data provided by the server
     *
     * @return true if all registered projectile were able to update their corresponding projectile item
     */
    private static boolean updateRegisteredAmmo(Map<ResourceLocation, ChassisConfig> registeredAmmo) {
//        clientRegisteredAmmo.clear();
        if (registeredAmmo != null) {
            for (Map.Entry<ResourceLocation, ChassisConfig> entry : registeredAmmo.entrySet()) {
                var item = ForgeRegistries.ENTITY_TYPES.getValue(entry.getKey());

//                if (!(item instanceof EntityType<ChassisBase>)) {
//                    return false;
//                }

                Configs.CHASSIS_CONFIGS.put((EntityType<Chassis>) item, new ConfigSupplier<>(entry.getValue()));

//                ((EntityType<ChassisBase>) item).setConfig(new NetworkManager.Supplier<>(entry.getValue()));
//                clientRegisteredAmmo.add((EntityType<ChassisBase>) item);
            }
            return true;
        }
        return false;
    }

    /**
     * Gets a map of all the registered projectile objects. Note, this is an immutable map.
     *
     * @return a map of registered projectile objects
     */
    public Map<ResourceLocation, ChassisConfig> getRegisteredAmmo() {
        return this.registeredAmmo;
    }

//    /**
//     * Gets a list of all the projectile registered on the client side. Note, this is an immutable list.
//     *
//     * @return a map of projectile registered on the client
//     */
//    public static List<EntityType<ChassisBase>> getClientRegisteredAmmo() {
//        return ImmutableList.copyOf(clientRegisteredAmmo);
//    }

    /**
     * Gets the network projectile manager. This will be null if the client isn't running an integrated
     * server or the client is connected to a dedicated server.
     *
     * @return the network projectile manager
     */
    @Nullable
    public static NetworkChassisManager get() {
        return instance;
    }

    public static class Supplier {
        private ChassisConfig config;

        private Supplier(ChassisConfig projectile) {
            this.config = projectile;
        }

        public ChassisConfig getAmmo() {
            return this.config;
        }
    }

    public static class LoginData implements ILoginData {
        @Override
        public void writeData(FriendlyByteBuf buffer) {
            Validate.notNull(NetworkChassisManager.get());
            NetworkChassisManager.get().writeRegisteredAmmo(buffer);
        }

        @Override
        public Optional<String> readData(FriendlyByteBuf buffer) {
            var registeredAmmo = NetworkChassisManager.readRegisteredAmmo(buffer);
            NetworkChassisManager.updateRegisteredAmmo(registeredAmmo);
            return Optional.empty();
        }
    }
}
