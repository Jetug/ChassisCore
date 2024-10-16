package com.jetug.chassis_core.client.resources;

import com.google.gson.Gson;
import com.jetug.chassis_core.common.data.json.ChassisConfig;
import com.jetug.chassis_core.common.data.json.EquipmentConfig;
import com.jetug.chassis_core.common.data.json.ItemConfig;
import com.jetug.chassis_core.common.data.json.ModelConfigBase;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.jetug.chassis_core.client.render.utils.ResourceHelper.getResourceName;

public class ModResourceManager {
    private static final String CONFIG_DIR = "config/model/";
    private static final String EQUIPMENT_DIR = CONFIG_DIR + "equipment";
    private static final String FRAME_DIR = CONFIG_DIR + "chassis";
    private static final String ITEM_DIR = CONFIG_DIR + "item";

    private final Map<String, EquipmentConfig> equipmentConfig = new HashMap<>();
    private final Map<String, ItemConfig> itemConfig = new HashMap<>();
    private final Map<String, ChassisConfig> frameConfig = new HashMap<>();


    @Nullable
    public ItemConfig getItemConfig(String itemId){
        return itemConfig.get(itemId);
    }

    @Nullable
    public EquipmentConfig getEquipmentConfig(String itemId){
        return equipmentConfig.get(itemId);
    }

    @Nullable
    public ChassisConfig getFrameConfig(String frameId){
        return frameConfig.get(frameId);
    }

    public void loadConfigs(){
        loadEquipment();
        loadFrame();
        loadItem();
    }

    private void loadEquipment() {
        for (ResourceLocation config : getJsonResources(EQUIPMENT_DIR)) {
            var settings = getConfig(config, EquipmentConfig.class);
            if(settings == null) continue;

            if(isNotEmpty(settings.parent)){
                var parent = getConfig(new ResourceLocation(settings.parent), EquipmentConfig.class);

                try {
                    var fields = settings.getClass().getFields();
                    for (var field : fields){
                        var obj = field.get(settings);

                        var isEmptyString = (obj instanceof String str && str.equals(""));

                        if(obj == null || isEmptyString || isEmptyArray(obj)){
                            field.set(settings, parent.getClass().getField(field.getName()).get(parent));
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            equipmentConfig.put(settings.name, settings);
        }
    }

    private void loadFrame() {
        for (ResourceLocation config : getJsonResources(FRAME_DIR)) {
            var settings = getConfig(config, ChassisConfig.class);
            if(settings != null)
                frameConfig.put(settings.name, settings);
        }
    }

    private void loadItem() {
        for (ResourceLocation config : getJsonResources(ITEM_DIR)) {
            var settings = getConfig(config, ItemConfig.class);
            if(settings != null)
                itemConfig.put(settings.name, settings);
        }
    }

    private static boolean isEmptyArray(Object obj){
        if (obj.getClass().isArray())
            return 0 == Array.getLength(obj);
        return false;
    }

    public static boolean isNotEmpty(String string){
        return string != null && !string.equals("");
    }

    private static Collection<ResourceLocation> getJsonResources(String path){
        return Minecraft.getInstance().getResourceManager()
                .listResources(path, fileName -> fileName.endsWith(".json"));
    }

    @Nullable
    private EquipmentConfig getEquipmentConfig(ResourceLocation resourceLocation){
        try {
            var readIn = getBufferedReader(resourceLocation);
            var settings = new Gson().fromJson(readIn, EquipmentConfig.class);
            settings.name = getResourceName(resourceLocation);
            return settings;
        }
        catch (Exception e) {
            return null;
        }
    }

    @Nullable
    private ChassisConfig getFrameConfig(ResourceLocation resourceLocation){
        try {
            var readIn = getBufferedReader(resourceLocation);
            var settings = new Gson().fromJson(readIn, ChassisConfig.class);
            settings.name = getResourceName(resourceLocation);
            return settings;
        }
        catch (Exception e) {
            return null;
        }
    }

    @Nullable
    private <T extends ModelConfigBase> T getConfig(ResourceLocation resourceLocation, Class<T> classOfT){
        try {
            var readIn = getBufferedReader(resourceLocation);
            var settings = new Gson().fromJson(readIn, classOfT);
            settings.name = getResourceName(resourceLocation);
            return settings;
        }
        catch (Exception e) {
            return null;
        }
    }

    private BufferedReader getBufferedReader(ResourceLocation resourceLocation) throws IOException {
        return getBufferedReader(Minecraft.getInstance().getResourceManager().getResource(resourceLocation).getInputStream());
    }

    private static BufferedReader getBufferedReader(InputStream stream){
        return new BufferedReader(new InputStreamReader(stream));
    }
}
