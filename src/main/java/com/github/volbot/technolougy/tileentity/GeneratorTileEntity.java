package com.github.volbot.technolougy.tileentity;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.energy.IEnergyStorage;

public class GeneratorTileEntity extends TileEntity implements ITickableTileEntity, IEnergyStorage {

    public GeneratorTileEntity(TileEntityType<?> prop) {
        super(prop);
    }

    protected int storedenergy = 0;

    @CapabilityInject(IEnergyStorage.class)
    static Capability<IEnergyStorage> ENERGY_STORAGE_CAPABILITY = null;

    @Override
    public void tick() {
        /*debug
        receiveEnergy(10, false);
        System.out.println(getEnergyStored());
         */
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
}
