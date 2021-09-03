package com.github.volbot.technolougy.tileentity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.IEnergyStorage;

public class GeneratorTileEntity extends TileEntity implements ITickableTileEntity, IEnergyStorage, INBTSerializable<CompoundNBT> {

    public GeneratorTileEntity(TileEntityType<?> prop) {
        super(prop);
    }

    private int storedenergy = 0;
    protected int debugval = 0;

    @CapabilityInject(IEnergyStorage.class)
    static Capability<IEnergyStorage> ENERGY_STORAGE_CAPABILITY = null;

    @Override
    public void tick() {
        if(debugval==12) {
            receiveEnergy(10, false);
            System.out.println(getEnergyStored());
            debugval=0;
        } else {
            debugval++;
        }
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return storedenergy+=maxReceive;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return storedenergy-=maxExtract;
    }

    @Override
    public int getEnergyStored() {
        return storedenergy;
    }

    @Override
    public int getMaxEnergyStored() {
        return 10000;
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return false;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        System.out.println(getEnergyStored()+" SAVED");
        nbt.putInt("stored", getEnergyStored());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (nbt.contains("stored", Constants.NBT.TAG_INT)) {
            System.out.println(getEnergyStored()+" READ");
            this.receiveEnergy(nbt.getInt("stored"),false);
        }
    }
}