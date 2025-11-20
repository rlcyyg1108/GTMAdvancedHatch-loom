package com.xingmot.gtmadvancedhatch.api.gui;

import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import com.lowdragmc.lowdraglib.side.item.IItemTransfer;

import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class ConfigSlotWidget extends SlotWidget {

    @Override
    protected Slot createSlot(IItemTransfer itemHandler, int index) {
        return new ConfigWidgetSlotItemTransfer(itemHandler, index, 0, 0);
    }

    public class ConfigWidgetSlotItemTransfer extends WidgetSlotItemTransfer {

        public ConfigWidgetSlotItemTransfer(IItemTransfer itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public int getMaxStackSize(@Nonnull ItemStack stack) {
            return this.getItemHandler().getSlotLimit(this.getSlotIndex());
        }
    }
}
