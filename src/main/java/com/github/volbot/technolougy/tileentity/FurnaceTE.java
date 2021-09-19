package com.github.volbot.technolougy.tileentity;

import com.github.volbot.technolougy.registry.LouDeferredRegister;
import net.minecraft.tileentity.TileEntity;

public class FurnaceTE extends TileEntity {
    public FurnaceTE() {
        super(LouDeferredRegister.furnaceTE.get());
    }
}
