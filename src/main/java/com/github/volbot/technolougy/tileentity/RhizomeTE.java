package com.github.volbot.technolougy.tileentity;

import com.github.volbot.technolougy.registry.LouDeferredRegister;
import com.github.volbot.technolougy.tileentity.container.SmelterContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

public class RhizomeTE extends AbstractRhizomaticTankTE {

    public RhizomeTE() {
        super(LouDeferredRegister.rhizomeTE.get());
        init();
    }

    @Override
    protected void init() {
        super.init();
        dataAccess.fluidTankCapacity=10000;
    }

    @Override
    public void tick() {
        super.tick();
    }
}
