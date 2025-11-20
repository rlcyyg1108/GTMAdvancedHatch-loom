package com.xingmot.gtmadvancedhatch.api;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import com.lowdragmc.lowdraglib.misc.ItemStackTransfer;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

import lombok.Setter;
import org.jetbrains.annotations.NotNull;

public class LockStackTransfer extends ItemStackTransfer {

    @Setter
    private Function<ItemStack, Boolean> filter;
    public final int limit;

    public LockStackTransfer(int size, int limit) {
        super(size);
        this.limit = limit;
    }

    public LockStackTransfer(NonNullList<ItemStack> stacks, int limit) {
        super(stacks);
        this.limit = limit;
    }

    @Override
    public int getSlotLimit(int slot) {
        return this.limit;
    }

    @Override
    protected int getStackLimit(int slot, @NotNull ItemStack stack) {
        return Math.min(this.limit, stack.getMaxStackSize() * (this.limit / 64));
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate, boolean notifyChanges) {
        return super.insertItem(slot, stack, simulate, notifyChanges);
    }

    @Override
    public LockStackTransfer copy() {
        NonNullList<ItemStack> copiedStack = NonNullList.withSize(stacks.size(), ItemStack.EMPTY);
        for (int i = 0; i < stacks.size(); i++) {
            copiedStack.set(i, stacks.get(i).copy());
        }
        var copied = new LockStackTransfer(copiedStack, this.limit);
        copied.setFilter(filter);
        return copied;
    }
}
