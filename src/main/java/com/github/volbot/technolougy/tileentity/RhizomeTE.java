package com.github.volbot.technolougy.tileentity;

import com.github.volbot.technolougy.registry.LouDeferredRegister;
import net.minecraft.fluid.Fluids;
import net.minecraftforge.fluids.FluidStack;

public class RhizomeTE extends AbstractRhizomaticTankTE {

    public RhizomeTE() {
        super(LouDeferredRegister.rhizomeTE.get());
        init();
    }

    @Override
    protected void init() {
        fuelTank = new FluidStack(Fluids.WATER,0);
        this.fuelTankLimit = 10000;
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
}
