package com.jetug.power_armor_mod.common.foundation.loot;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jetug.power_armor_mod.common.data.constants.Global;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.Deserializers;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import java.util.List;

public class RelicLootModifier extends LootModifier {
    private static final Gson GSON = Deserializers.createFunctionSerializer().create();

    private final LootItem entry;
    private final LootItemFunction[] functions;

    public RelicLootModifier(LootItemCondition[] conditionsIn, LootItem entry, LootItemFunction[] functions) {
        super(conditionsIn);

        this.entry = entry;
        this.functions = functions;
    }

    @Nonnull
    @Override
    public List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        entry.expand(context, generator -> generator.createItemStack(LootItemFunction.decorate(
                LootItemFunctions.compose(this.functions), generatedLoot::add, context), context));

        return generatedLoot;
    }

    private static class Serializer extends GlobalLootModifierSerializer<RelicLootModifier> {
        @Override
        public RelicLootModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] conditions) {
            return new RelicLootModifier(conditions, GSON.fromJson(GsonHelper.getAsJsonObject(object, "entry"), LootItem.class),
                    object.has("functions") ? GSON.fromJson(GsonHelper.getAsJsonArray(object,
                            "functions"), LootItemFunction[].class) : new LootItemFunction[0]);
        }

        @Override
        public JsonObject write(RelicLootModifier instance) {
            JsonObject object = makeConditions(instance.conditions);

            object.add("entry", GSON.toJsonTree(instance.entry, LootItem.class));
            object.add("functions", GSON.toJsonTree(instance.functions, LootItemFunction[].class));

            return object;
        }
    }

    @Mod.EventBusSubscriber(modid = Global.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class EventHandler {
        @SubscribeEvent
        public static void registerModifierSerializers(@Nonnull final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
            event.getRegistry().register(new Serializer()
                    .setRegistryName(new ResourceLocation(Global.MOD_ID, "relic_gen")));
        }
    }
}