package com.xingmot.gtmadvancedhatch.mixin.mc.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;

import static com.xingmot.gtmadvancedhatch.util.AHFormattingUtil.formatLongToShort;
import static com.xingmot.gtmadvancedhatch.util.AHUtil.calculateStringScale;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * coded mainly by PORTB from BiggerStacks 1.20.1(Licensed under GNU LGPL v3)
 */
@Pseudo
@Mixin(GuiGraphics.class)
public abstract class ItemRendererMixin {

    // region "delete" all the vanilla item count rendering
    @Redirect(method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
              at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V"))
    private void doNothing1(PoseStack instance, float p_254202_, float p_253782_, float p_254238_) {}

    @Redirect(method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)I"))
    private int doNothing2(GuiGraphics instance, Font p_283343_, String p_281896_, int p_283569_, int p_283418_, int p_281560_, boolean p_282130_) {
        return 0;
    }
    // endregion

    // Inject the new text rendering
    @Inject(method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)I"))
    private void renderText(Font font, ItemStack itemStack, int x, int y, String alternateCount, CallbackInfo ci) {
        var poseStack = ((GuiGraphics) (Object) this).pose();

        String text = alternateCount == null ? formatLongToShort(itemStack.getCount(), 9999) : alternateCount;

        float scale = (float) calculateStringScale(font, text);
        float inverseScale = 1 / scale;
        float xTransform = (x + 16) * inverseScale - font.width(text);
        float yTransform = (y + 16) * inverseScale - font.lineHeight;

        poseStack.scale(scale, scale, 1);
        poseStack.translate(xTransform, yTransform, 200);

        MultiBufferSource.BufferSource bufferSource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        font.drawInBatch(
                text,
                /* pX */ 0, // translation is done by poseStack. Doing it here just makes it harder.
                /* pY */ 0,
                /* pColor */ 16777215,
                /* pDropShadow */ true,
                /* pMatrix */ poseStack.last().pose(),
                /* pBufferSource */ bufferSource,
                /* pTransparent */ Font.DisplayMode.NORMAL,
                /* pBackgroundColour */ 0,
                /* pPackedLightCoords */ 15728880);

        // Without this, the position of the durability bar gets messed up with some mods like EMI
        poseStack.translate(-xTransform, -yTransform, 0);

        bufferSource.endBatch();
    }
}
