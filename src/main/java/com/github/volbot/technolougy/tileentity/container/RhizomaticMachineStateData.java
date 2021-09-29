package com.github.volbot.technolougy.tileentity.container;

import net.minecraft.util.IIntArray;

public class RhizomaticMachineStateData implements IIntArray {
    public RhizomaticMachineStateData() {}

    public int cookingProgress;
    public int cookingTotalTime;
    public int fluidQuantity;
    public int fluidTankCapacity;

    @Override
    public int get(int i) {
        switch (i) {
            case 0:
                return cookingProgress;
            case 1:
                return cookingTotalTime;
            case 2:
                return fluidQuantity;
            case 3:
                return fluidTankCapacity;
            default:
                return 0;
        }
    }

    @Override
    public void set(int i, int val) {
        switch (i) {
            case 0:
                this.cookingProgress=val;
                break;
            case 1:
                this.cookingTotalTime=val;
                break;
            case 2:
                this.fluidQuantity=val;
                break;
            case 3:
                this.fluidTankCapacity=val;
                break;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
