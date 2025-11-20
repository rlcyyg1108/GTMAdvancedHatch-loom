package com.xingmot.gtmadvancedhatch.common.machines;

import com.xingmot.gtmadvancedhatch.api.ConfigItemStackTransfer;
import com.xingmot.gtmadvancedhatch.util.AHUtil;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IDistinctPart;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredIOPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.ItemHandlerProxyRecipeTrait;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.common.machine.multiblock.part.ItemBusPartMachine;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

// TODO 施工中
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ConfigurableItemBusPartMachine extends ItemBusPartMachine implements IDistinctPart, IMachineLife {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(ConfigurableItemBusPartMachine.class, TieredIOPartMachine.MANAGED_FIELD_HOLDER);
    @Persisted
    protected final NotifiableItemStackHandler circuitInventory;
    protected final ItemHandlerProxyRecipeTrait combinedInventory;
    public final int size;

    public ConfigurableItemBusPartMachine(IMachineBlockEntity holder, int tier, IO io, int size, Object... args) {
        super(holder, tier, io);
        this.size = size;
        this.circuitInventory = this.createCircuitItemHandler(io);
        this.combinedInventory = this.createCombinedItemHandler(io);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    protected static int getSlotCapacity(int tier) {
        if (tier >= 3) return AHUtil.multiplyWithIntegerBounds(64, (1 << (2 * Math.min(13, tier - 1))));
        return 64 * (1 << tier);
    }

    @Override
    protected int getInventorySize() {
        return this.size;
    }

    @Override
    protected NotifiableItemStackHandler createInventory(Object... args) {
        return new NotifiableItemStackHandler(this, this.getInventorySize(), this.io, this.io, slots -> new ConfigItemStackTransfer(slots, getSlotCapacity(this.tier)));
    }
}
