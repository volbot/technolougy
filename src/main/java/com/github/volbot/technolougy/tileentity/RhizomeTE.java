package com.github.volbot.technolougy.tileentity;

import com.github.volbot.technolougy.capabilities.LouCapabilityProvider;
import com.github.volbot.technolougy.registry.LouDeferredRegister;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RhizomeTE extends TileEntity implements ITickableTileEntity, IFluidHandler {

    private FluidStack waterTank;
    private int waterTankLimit;

    protected FluidHandlerItemStack handler;
    private final LazyOptional<IFluidHandler> fluidHandlerLazyOptional = LazyOptional.of(() -> handler);

    @CapabilityInject(IFluidHandler.class)
    public static Capability<IFluidHandler> FLUID_CAP = null;

    public RhizomeTE(TileEntityType<?> prop) {
        super(prop);
        waterTank = new FluidStack(Fluids.WATER,0);
        waterTankLimit = 100000;
    }

    public RhizomeTE() {
        super(LouDeferredRegister.rhizomeTE.get());
        waterTank = new FluidStack(Fluids.WATER,0);
        waterTankLimit = 100000;
        this.FLUID_CAP = LouCapabilityProvider.FLUID_CAP;
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        fluidHandlerLazyOptional.invalidate();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == FLUID_CAP ? fluidHandlerLazyOptional.cast() : LazyOptional.empty();
    }

    public static Capability<IFluidHandler> get() {
        return FLUID_CAP;
    }

    private int debugint = 0;

    @Override
    public void tick() {
        if(debugint==12) {
            fill(new FluidStack(Fluids.WATER, 1), FluidAction.EXECUTE);
            System.out.println(getFluidInTank(0).getAmount());
            debugint=0;
        } else {
            debugint++;
        }
    }

    @Override
    public int getTanks() {
        return 0;
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return waterTank;
    }

    @Override
    public int getTankCapacity(int tank) {
        return 0;
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        return false;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        int quantity = resource.getAmount();
        int initialQuantity = waterTank.getAmount();
        if(initialQuantity!=waterTankLimit) {
            if (initialQuantity + quantity > waterTankLimit) {
                waterTank.setAmount(waterTankLimit);
                return waterTankLimit - initialQuantity;
            } else {
                waterTank.setAmount(initialQuantity + quantity);
                return quantity;
            }
        }
        return 0;
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return null;
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return null;
    }
}
