package com.github.volbot.technolougy.tileentity.container;

import com.github.volbot.technolougy.tileentity.AbstractRhizomaticMachineTE;
import net.minecraft.util.IIntArray;

public class RhizomaticMachineStateData implements IIntArray {
    private AbstractRhizomaticMachineTE handler;
    public RhizomaticMachineStateData(AbstractRhizomaticMachineTE handler) {
        this.handler=handler;
    }

    @Override
    public int get(int i) {
        System.out.println("FUCKING PLEASE");
        switch (i) {
            case 0:
                return handler.getCookingProgress();
            case 1:
                System.out.println("PLEASE GIVE ME THT EOTAL "+ handler.getCookingTotalTime());
                return handler.getCookingTotalTime();
            default:
                return 0;
        }
    }

    @Override
    public void set(int i, int val) {
        switch (i) {
            case 0:
                handler.setCookingProgress(val);
            case 1:
                handler.setCookingTotalTime(val);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
