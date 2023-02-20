/*
 * Copyright (c) 2019-2022 Team Galacticraft
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.jetug.power_armor_mod.mixin;

import com.jetug.power_armor_mod.common.foundation.registery.ItemRegistry;
import com.jetug.power_armor_mod.common.util.enums.ActionType;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.netty.buffer.Unpooled;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

import static com.jetug.power_armor_mod.common.network.PacketSender.doServerAction;
import static com.jetug.power_armor_mod.common.util.constants.Gui.TAB_HEIGHT;
import static com.jetug.power_armor_mod.common.util.constants.Gui.TAB_WIDTH;
import static com.jetug.power_armor_mod.common.util.constants.Resources.PLAYER_INVENTORY_TABS;
import static com.jetug.power_armor_mod.common.util.extensions.PlayerExtension.isWearingPowerArmor;

/**
 * @author <a href="https://github.com/TeamGalacticraft">TeamGalacticraft</a>
 */
@Mixin(CreativeModeInventoryScreen.class)
@OnlyIn(Dist.CLIENT)
public abstract class CreativeInventoryScreenMixin extends EffectRenderingInventoryScreen<InventoryMenu> {
    public CreativeInventoryScreenMixin(InventoryMenu screenHandler, Inventory playerInventory, Component textComponent) {
        super(screenHandler, playerInventory, textComponent);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    public void mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> ci) {
        if(!isWearingPowerArmor()) return;
//        if (GCPlayerInventoryScreen.isCoordinateBetween((int) Math.floor(mouseX), leftPos + 30, leftPos + 59)
//                && GCPlayerInventoryScreen.isCoordinateBetween((int) Math.floor(mouseY), topPos - 26, topPos)) {
//
//
//        }
        //doServerAction(ActionType.OPEN_GUI);
        var rect = new Rectangle(leftPos + 30, topPos - TAB_HEIGHT, TAB_WIDTH, TAB_HEIGHT);
        if(rect.contains(mouseX, mouseY)){
            doServerAction(ActionType.OPEN_GUI);
        }
    }

    @Inject(method = "renderBg", at = @At("TAIL"))
    public void drawBackground(PoseStack matrices, float v, int i, int i1, CallbackInfo callbackInfo) {
        if(!isWearingPowerArmor()) return;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, PLAYER_INVENTORY_TABS);
        this.blit(matrices, this.leftPos, this.topPos - 28, 0, 0, 57, 32);
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void render(PoseStack matrices, int mouseX, int mouseY, float v, CallbackInfo callbackInfo) {
        if(!isWearingPowerArmor()) return;
        Lighting.setupFor3DItems();
        this.itemRenderer.renderAndDecorateItem(Items.CRAFTING_TABLE.getDefaultInstance(), this.leftPos + 6, this.topPos - 20);
        this.itemRenderer.renderAndDecorateItem(ItemRegistry.PA_FRAME.get().getDefaultInstance(), this.leftPos + 35, this.topPos - 20);
        Lighting.setupForFlatItems();
    }
}
