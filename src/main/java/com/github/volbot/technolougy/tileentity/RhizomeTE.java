package com.github.volbot.technolougy.tileentity;

import com.github.volbot.technolougy.registry.LouDeferredRegister;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RhizomeTE extends TileEntity implements ITickableTileEntity, IFluidHandler, ICapabilitySerializable<CompoundNBT>, ICapabilityProvider {

    private FluidStack waterTank;
    private int waterTankLimit;

    @CapabilityInject(IFluidHandler.class)
    public static Capability<IFluidHandler> FLUID_CAP = null;

    private LazyOptional<IFluidHandler> instance = LazyOptional.of(FLUID_CAP::getDefaultInstance);

    public RhizomeTE() {
        super(LouDeferredRegister.rhizomeTE.get());
        waterTank = new FluidStack(Fluids.WATER,0);
        waterTankLimit = 100000;
        System.out.println("RHIZOME APPLIED TO BLOCK");
    }

    private int debugint = 0;

    @Override
    public void tick() {
        if(debugint==12) {
            fill(new FluidStack(Fluids.WATER, 1), FluidAction.EXECUTE);
            setChanged();
            CompoundNBT testnbt = new CompoundNBT();
            System.out.println(FluidStack.loadFluidStackFromNBT(getFluidInTank(0).writeToNBT(testnbt)).getAmount());
            debugint=0;
        } else {
            debugint++;
        }
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return super.getUpdateTag();
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        super.handleUpdateTag(state, tag);
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        System.out.println("testi");
        //nbt.put("fluid_cap", getFluidInTank(0).writeToNBT(nbt));
        return getFluidInTank(0).writeToNBT(nbt);
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        System.out.println("testo");
        deserializeNBT(nbt);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        /*
        FLUID_CAP.getStorage().readNBT(
                FLUID_CAP,
                instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")),
                null,
                nbt
        );
         */
        FluidStack fluidStack = new FluidStack(Fluids.WATER,nbt.getInt("Amount"));
        System.out.println(fluidStack.getAmount()+" B READ");
        this.fill(fluidStack,FluidAction.EXECUTE);
        setChanged();
    }


    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        /*
        INBT dat = FLUID_CAP.getStorage().writeNBT(
                FLUID_CAP,
                instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")),
                null
        );

         */;
        return getFluidInTank(0).writeToNBT(nbt);
    }

    protected FluidHandlerItemStack handler;
    private LazyOptional<IFluidHandler> fluidHandlerLazyOptional = LazyOptional.of(() -> handler);

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
        return true;
    }

    protected void onContentsChanged() {
        this.setChanged();
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
