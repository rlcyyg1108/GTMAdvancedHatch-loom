package com.xingmot.gtmadvancedhatch.api;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.function.Function;

import com.hepdd.gtmthings.api.misc.UnlimitedItemStackTransfer;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

public class MutableLimitAndFilterStackTransfer extends UnlimitedItemStackTransfer {

    @Setter
    @Getter
    private NonNullList<Function<ItemStack, Boolean>> filter;
    public int[] limit;

    public MutableLimitAndFilterStackTransfer(int size, int limit) {
        super(size);
        this.limit = new int[size];
        Arrays.fill(this.limit, limit);
        this.filter = NonNullList.withSize(size, stack -> true);
    }

    public MutableLimitAndFilterStackTransfer(int size, int[] limit, NonNullList<Function<ItemStack, Boolean>> filter) {
        super(size);
        this.limit = limit;
        this.filter = filter;
    }

    public MutableLimitAndFilterStackTransfer(NonNullList<ItemStack> stacks, int[] limit, NonNullList<Function<ItemStack, Boolean>> filter) {
        super(stacks);
        this.limit = limit;
        this.filter = filter;
    }

    public void setSlotLimit(int slot, int limit) {
        this.limit[slot] = limit;
    }

    @Override
    public int getSlotLimit(int slot) {
        return this.limit[slot];
    }

    @Override
    protected int getStackLimit(int slot, @NotNull ItemStack stack) {
        return Math.min(this.limit[slot], stack.getMaxStackSize() * (this.limit[slot] / 64));
    }

    @Override
    public MutableLimitAndFilterStackTransfer copy() {
        NonNullList<ItemStack> copiedStack = NonNullList.withSize(stacks.size(), ItemStack.EMPTY);
        NonNullList<Function<ItemStack, Boolean>> copiedFilter = NonNullList.withSize(filter.size(), stack -> true);
        for (int i = 0; i < stacks.size(); i++) {
            copiedStack.set(i, stacks.get(i).copy());
            copiedFilter.set(i, filter.get(i));
        }
        return new MutableLimitAndFilterStackTransfer(copiedStack, this.limit.clone(), filter);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return this.filter.get(slot).apply(stack);
    }
}
