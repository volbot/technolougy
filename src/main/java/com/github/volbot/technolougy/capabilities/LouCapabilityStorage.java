package com.github.volbot.technolougy.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;

public class LouCapabilityStorage implements Capability.IStorage<IFluidHandler> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<IFluidHandler> capability, IFluidHandler instance, Direction side) {
        CompoundNBT nbt = new CompoundNBT();
        System.out.println("WROTE "+instance.getFluidInTank(0).getAmount()+" B OF FLUID");
        nbt.put("fluid", instance.getFluidInTank(0).writeToNBT(nbt));
        return nbt;
    }

    @Override
    public void readNBT(Capability<IFluidHandler> capability, IFluidHandler instance, Direction side, INBT inbt) {
        CompoundNBT nbt = (CompoundNBT) inbt;
        System.out.println("READ "+((FluidStack)nbt.get("fluid")).getAmount()+" B OF FLUID");
        instance.fill((FluidStack)nbt.get("fluid"), IFluidHandler.FluidAction.EXECUTE);
    }
}
