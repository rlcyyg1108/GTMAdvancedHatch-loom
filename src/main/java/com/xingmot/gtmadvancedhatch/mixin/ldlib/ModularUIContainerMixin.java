package com.xingmot.gtmadvancedhatch.mixin.ldlib;

import com.xingmot.gtmadvancedhatch.api.gui.IUnlimitedStack;

import com.lowdragmc.lowdraglib.gui.modular.ModularUIContainer;

import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ModularUIContainer.class)
public class ModularUIContainerMixin {

    @Redirect(method = "mergeItemStack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getMaxStackSize()I"))
    private static int mergeItemStack(ItemStack instance, @Local(name = "slot") Slot slot) {
        if (slot instanceof IUnlimitedStack) {
            return slot.getMaxStackSize(instance);
        } else return instance.getMaxStackSize();
    }
}
