package com.github.volbot.technolougy.tileentity.container;

import com.github.volbot.technolougy.tileentity.AbstractRhizomaticMachineTE;
import net.minecraft.util.IIntArray;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class RhizomaticMachineStateData implements IIntArray {
    private AbstractRhizomaticMachineTE handler;
    public RhizomaticMachineStateData(AbstractRhizomaticMachineTE handler) {
        this.handler=handler;
    }

    @Override
    public int get(int i) {
        switch (i) {
            case 0:
                return handler.getCookingProgress();
            case 1:
                return handler.getCookingTotalTime();
            case 2:
                return handler.getFluidInTank(0).getAmount();
            case 3:
                return handler.getTankCapacity(0);
            default:
                return 0;
        }
    }

    @Override
    public void set(int i, int val) {
        switch (i) {
            case 0:
                handler.setCookingProgress(val);
                break;
            case 1:
                handler.setCookingTotalTime(val);
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
