package com.github.volbot.technolougy.tileentity;

import com.github.volbot.technolougy.registry.LouDeferredRegister;
import com.github.volbot.technolougy.tileentity.container.RhizomaticMachineStateData;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AbstractRhizomaticTankTE extends RhizomeProxyTE implements IFluidHandler, ICapabilityProvider {

    protected FluidStack fuelTank;

    @CapabilityInject(IFluidHandler.class)
    public static Capability<IFluidHandler> FLUID_CAP = null;

    private LazyOptional<IFluidHandler> instance = LazyOptional.of(FLUID_CAP::getDefaultInstance);

    public AbstractRhizomaticTankTE(TileEntityType type){
        super(type);
    }

    protected void init() {
        fuelTank = new FluidStack(LouDeferredRegister.sugarWaterFluid.get(),0);
    }

    @Override
    public void tick() {
        super.tick();
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
        return fuelTank;
    }

    @Override
    public int getTankCapacity(int tank) {
        return dataAccess.fluidTankCapacity;
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
        if(initialQuantity!=dataAccess.fluidTankCapacity) {
            if (initialQuantity + quantity > dataAccess.fluidTankCapacity) {
                if(!action.simulate()) {
                    fuelTank.setAmount(dataAccess.fluidTankCapacity);
                    dataAccess.fluidQuantity=dataAccess.fluidTankCapacity;
                    setChanged();
                }
                return dataAccess.fluidTankCapacity - initialQuantity;
            } else {
                if(!action.simulate()) {
                    fuelTank.setAmount(initialQuantity + quantity);
                    dataAccess.fluidQuantity=initialQuantity + quantity;
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
                    dataAccess.fluidQuantity=0;
                }
                drained.setAmount(initialQuantity);
            } else {
                if(!action.simulate()) {
                    fuelTank.setAmount(initialQuantity - quantity);
                    dataAccess.fluidQuantity=initialQuantity-quantity;
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
                    dataAccess.fluidQuantity=0;
                }
                drained.setAmount(initialQuantity);
            } else {
                if(!action.simulate()) {
                    fuelTank.setAmount(initialQuantity - quantity);
                    dataAccess.fluidQuantity=initialQuantity-quantity;
                }
                drained.setAmount(quantity);
            }
        }
        return drained;
    }

    protected RhizomaticMachineStateData dataAccess;

    public IIntArray getDataAccess() {
        return dataAccess;
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        CompoundNBT nbt1 = new CompoundNBT();
        nbt1 = fuelTank.writeToNBT(nbt1);
        nbt.put("Fluid",nbt1);
        return nbt;
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        deserializeNBT(nbt);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        CompoundNBT nbt1 = nbt.getCompound("Fluid");
        FluidStack loadStack = FluidStack.loadFluidStackFromNBT(nbt1);
        this.fill(loadStack,FluidAction.EXECUTE);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt = fuelTank.writeToNBT(nbt);
        return nbt;
    }
}
