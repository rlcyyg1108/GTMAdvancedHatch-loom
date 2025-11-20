package com.xingmot.gtmadvancedhatch.api;

import com.lowdragmc.lowdraglib.misc.ItemStackTransfer;

import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import javax.annotation.Nonnull;

import lombok.Setter;

/**
 * 画饼：tag匹配，耐久匹配，自动销毁
 */
public class ConfigItemStackTransfer extends ItemStackTransfer {

    @Setter
    private List<Function<ItemStack, Boolean>> filter;
    public final int maxCapacity;
    public int[] capacity;

    public void setCapacity(int slot, int capacity) {
        this.capacity[slot] = Math.max(0, Math.min(capacity, this.maxCapacity));
    }

    public ConfigItemStackTransfer() {
        this(1, 64);
    }

    public ConfigItemStackTransfer(int size, int maxCapacity) {
        super(size);
        this.maxCapacity = maxCapacity;
        this.capacity = new int[size];
        this.filter = new ArrayList<>(size);
        Arrays.fill(this.capacity, maxCapacity);
        for (int i = 0; i < size; i++) {
            this.filter.add(itemStack -> true);
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return capacity[slot];
    }

    @Override
    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
        return capacity[slot];
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return filter.get(slot).apply(stack);
    }
}
