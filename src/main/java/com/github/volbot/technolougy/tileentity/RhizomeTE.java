package com.github.volbot.technolougy.tileentity;

import com.github.volbot.technolougy.registry.LouDeferredRegister;

public class RhizomeTE extends AbstractRhizomaticTankTE {

    public RhizomeTE() {
        super(LouDeferredRegister.rhizomeTE.get());
        init();
    }

    @Override
    protected void init() {
        dataAccess.fluidTankCapacity = 10000;
    }
    @Override
    public void tick() {
        super.tick();
    }
}
