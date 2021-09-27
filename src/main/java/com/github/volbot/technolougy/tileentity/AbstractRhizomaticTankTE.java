package com.github.volbot.technolougy.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AbstractRhizomaticTankTE extends RhizomeProxyTE implements IFluidHandler, ICapabilityProvider {

    protected FluidStack fuelTank;
    protected int fuelTankLimit;

    @CapabilityInject(IFluidHandler.class)
    public static Capability<IFluidHandler> FLUID_CAP = null;

    private LazyOptional<IFluidHandler> instance = LazyOptional.of(FLUID_CAP::getDefaultInstance);

    public AbstractRhizomaticTankTE(TileEntityType type){
        super(type);
        init();
    }

    protected void init() {
        fuelTank = new FluidStack(Fluids.WATER,0);
    }

    private int debugint = 0;

    @Override
    public void tick() {
        super.tick();
        if(!getLevel().isClientSide()) {
            if (debugint == 10) {
                System.out.println("TANK CONTENTS: "+getFluidInTank(0).getAmount());
                debugint = 0;
            } else {
                debugint++;
            }
        }
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        instance.invalidate();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == FLUID_CAP ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank) {
        if(tank == 0){
            return fuelTank;
        } else {
            return null;
        }
    }

    @Override
    public int getTankCapacity(int tank) {
        return fuelTankLimit;
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        if(tank == 0){
            return true;
        }
        return false;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        int quantity = resource.getAmount();
        int initialQuantity = fuelTank.getAmount();
        if(initialQuantity!=fuelTankLimit) {
            if (initialQuantity + quantity > fuelTankLimit) {
                if(!action.simulate()) {
                    fuelTank.setAmount(fuelTankLimit);
                    setChanged();
                }return fuelTankLimit - initialQuantity;
            } else {
                if(!action.simulate()) {
                    fuelTank.setAmount(initialQuantity + quantity);
                    setChanged();
                }
                return quantity;
            }
        } else {
            return 0;
        }
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        int quantity = resource.getAmount();
        int initialQuantity = fuelTank.getAmount();
        FluidStack drained = new FluidStack(fuelTank.getFluid(), 0);
        if(!(!fuelTank.getFluid().isSame(resource.getFluid())||fuelTank.isEmpty())) {
            if (initialQuantity <= quantity) {
                if(!action.simulate()) {
                    fuelTank.setAmount(0);
                }
                drained.setAmount(initialQuantity);
            } else {
                if(!action.simulate()) {
                    fuelTank.setAmount(initialQuantity - quantity);
                }
                drained.setAmount(quantity);
            }
        }
        return drained;
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        int quantity = maxDrain;
        int initialQuantity = fuelTank.getAmount();
        FluidStack drained = new FluidStack(fuelTank.getFluid(), 0);
        if(!fuelTank.isEmpty()) {
            if (initialQuantity <= quantity) {
                if(!action.simulate()) {
                    fuelTank.setAmount(0);
                }
                drained.setAmount(initialQuantity);
            } else {
                if(!action.simulate()) {
                    fuelTank.setAmount(initialQuantity - quantity);
                }
                drained.setAmount(quantity);
            }
        }
        return drained;
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        return getFluidInTank(0).writeToNBT(nbt);
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        deserializeNBT(nbt);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(nbt);
        this.fill(fluidStack,FluidAction.EXECUTE);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        serializeNBT(nbt);
        return nbt;
    }

    public CompoundNBT serializeNBT(CompoundNBT nbt) {
        return getFluidInTank(0).writeToNBT(nbt);
    }
}
