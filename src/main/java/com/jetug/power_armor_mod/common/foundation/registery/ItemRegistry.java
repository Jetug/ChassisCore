package com.jetug.power_armor_mod.common.foundation.registery;

import com.jetug.power_armor_mod.common.foundation.ModCreativeModeTab;
import com.jetug.power_armor_mod.common.foundation.item.*;
import com.jetug.power_armor_mod.common.data.enums.*;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static com.jetug.power_armor_mod.common.foundation.registery.ArmorMaterialRegistry.*;
import static com.jetug.power_armor_mod.common.data.constants.Global.*;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static final RegistryObject<Item> PA_FRAME = ITEMS.register("pa_frame", ArmorFrameItem::new);

    public static final RegistryObject<FrameArmorItem> PA_HELMET = ITEMS.register("pa_helmet", () ->
            new FrameArmorItem(IRON, BodyPart.HELMET, 0.9f));

    public static final RegistryObject<FrameArmorItem> PA_BODY = ITEMS.register("pa_body", () ->
            new FrameArmorItem(IRON, BodyPart.BODY_ARMOR, 0.9f));

    public static final RegistryObject<FrameArmorItem> PA_RIGHT_ARM = ITEMS.register("pa_right_arm", () ->
            new FrameArmorItem(IRON, BodyPart.RIGHT_ARM_ARMOR, 0.9f));

    public static final RegistryObject<FrameArmorItem> PA_LEFT_ARM = ITEMS.register("pa_left_arm", () ->
            new FrameArmorItem(IRON, BodyPart.LEFT_ARM_ARMOR, 0.9f));

    public static final RegistryObject<FrameArmorItem> PA_RIGHT_LEG = ITEMS.register("pa_right_leg", () ->
            new FrameArmorItem(IRON, BodyPart.RIGHT_LEG_ARMOR, 0.9f));

    public static final RegistryObject<FrameArmorItem> PA_LEFT_LEG = ITEMS.register("pa_left_leg", () ->
            new FrameArmorItem(IRON, BodyPart.LEFT_LEG_ARMOR, 0.9f));

    public static final RegistryObject<FrameArmorItem> ARMOR_LIGHT_HELMET = ITEMS.register("armor_light_helmet", () ->
            new FrameArmorItem(IRON, BodyPart.HELMET, 1f));

    public static final RegistryObject<FrameArmorItem> ARMOR_LIGHT_BODY = ITEMS.register("armor_light_body", () ->
            new FrameArmorItem(IRON, BodyPart.BODY_ARMOR, 1f));

    public static final RegistryObject<FrameArmorItem> ARMOR_LIGHT_RIGHT_ARM = ITEMS.register("armor_light_right_arm", () ->
            new FrameArmorItem(IRON, BodyPart.RIGHT_ARM_ARMOR, 1f));

    public static final RegistryObject<FrameArmorItem> ARMOR_LIGHT_LEFT_ARM = ITEMS.register("armor_light_left_arm", () ->
            new FrameArmorItem(IRON, BodyPart.LEFT_ARM_ARMOR, 1f));

    public static final RegistryObject<FrameArmorItem> ARMOR_LIGHT_RIGHT_LEG = ITEMS.register("armor_light_right_leg", () ->
            new FrameArmorItem(IRON, BodyPart.RIGHT_LEG_ARMOR, 1f));

    public static final RegistryObject<FrameArmorItem> ARMOR_LIGHT_LEFT_LEG = ITEMS.register("armor_light_left_leg", () ->
            new FrameArmorItem(IRON, BodyPart.LEFT_LEG_ARMOR, 1f));

    public static final RegistryObject<Item> ENGINE = ITEMS.register("engine", () ->
            new EngineItem(100, 6, 1f));

    public static final RegistryObject<Item> ENGINE_MEDIUM = ITEMS.register("engine_medium", () ->
            new EngineItem(150, 3, 1.5f));

    public static final RegistryObject<Item> JETPACK = ITEMS.register("jetpack", () ->
            new JetpackItem( 3, 1.5f, 4));

    public static final RegistryObject<HandItem> HAND = ITEMS.register("hand", () ->
            new HandItem(new Item.Properties().tab(ModCreativeModeTab.MY_TAB)));

    public static final RegistryObject<Item> FRAME_BODY = ITEMS.register("frame_body", () ->
            new FramePartItem(BodyPart.BODY_FRAME));

    public static final RegistryObject<Item> FRAME_RIGHT_ARM = ITEMS.register("frame_right_arm", () ->
            new FramePartItem(BodyPart.RIGHT_ARM_FRAME));

    public static final RegistryObject<Item> FRAME_LEFT_ARM = ITEMS.register("frame_left_arm", () ->
            new FramePartItem(BodyPart.LEFT_ARM_FRAME));

    public static final RegistryObject<Item> FRAME_RIGHT_LEG = ITEMS.register("frame_right_leg", () ->
            new FramePartItem(BodyPart.RIGHT_LEG_FRAME));

    public static final RegistryObject<Item> FRAME_LEFT_LEG = ITEMS.register("frame_left_leg", () ->
            new FramePartItem(BodyPart.LEFT_LEG_FRAME));

    

    public static final RegistryObject<Item> CAST_HELMET = ITEMS.register("cast_helmet", () ->
            new CastItem(PA_HELMET.get()));

    public static final RegistryObject<Item> CAST_BODY = ITEMS.register("cast_body", () ->
            new CastItem(PA_BODY.get()));

    public static final RegistryObject<Item> CAST_RIGHT_ARM = ITEMS.register("cast_right_arm", () ->
            new CastItem(PA_RIGHT_ARM.get()));

    public static final RegistryObject<Item> CAST_LEFT_ARM = ITEMS.register("cast_left_arm", () ->
            new CastItem(PA_LEFT_ARM.get()));

    public static final RegistryObject<Item> CAST_RIGHT_LEG = ITEMS.register("cast_right_leg", () ->
            new CastItem(PA_RIGHT_LEG.get()));

    public static final RegistryObject<Item> CAST_LEFT_LEG = ITEMS.register("cast_left_leg", () ->
            new CastItem(PA_LEFT_LEG.get()));


    //tutor
//    public static final RegistryObject<Item> CITRINE = ITEMS.register("citrine",
//            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MY_TAB)));
//
//    public static final RegistryObject<Item> RAW_CITRINE = ITEMS.register("raw_citrine",
//            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MY_TAB)));
//
//    public static final RegistryObject<Item> GEM_CUTTER_TOOL = ITEMS.register("gem_cutter_tool",
//            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MY_TAB).durability(32)));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }

    private static <I extends Item> I registerItem(final String name, final Supplier<? extends I> sup){
        return ITEMS.register(name, sup).get();
    }
}
