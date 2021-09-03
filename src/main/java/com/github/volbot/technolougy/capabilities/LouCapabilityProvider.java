package com.github.volbot.technolougy.capabilities;

import com.github.volbot.technolougy.tileentity.RhizomeTE;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LouCapabilityProvider implements ICapabilitySerializable<CompoundNBT> {

    @CapabilityInject(IFluidHandler.class)
    public static Capability<IFluidHandler> FLUID_CAP = null;

    public LouCapabilityProvider() {
        this.registerCapabilities();
    }

    public void registerCapabilities() {

    }

    public static Capability<IFluidHandler> get() {
        return FLUID_CAP;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == FLUID_CAP) {
            return LazyOptional.of(RhizomeTE::new).cast();
        } else return LazyOptional.empty();

    }

    @Override
    public CompoundNBT serializeNBT() {
        return null;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }

    private static class CapabilityWorker<S extends CompoundNBT, C extends INBTSerializable<S>> implements Capability.IStorage<C> {

        private final Capability<C> cap;

        public CapabilityWorker(final Capability<C> capability) {
            this.cap = capability;
        }

        @Nullable
        @Override
        public INBT writeNBT(Capability<C> capability, C instance, Direction side) {
            if(this.cap != capability){
                return null;
            }
            return instance.serializeNBT();
        }

        @Override
        @SuppressWarnings("unchecked")
        public void readNBT(Capability<C> capability, C instance, Direction side, INBT nbt) {
            if(this.cap != capability) {
                return;
            }
            instance.deserializeNBT((S) nbt);
        }
    }
}
