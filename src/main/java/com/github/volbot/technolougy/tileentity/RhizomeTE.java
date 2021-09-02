package com.github.volbot.technolougy.tileentity;

import com.github.volbot.technolougy.registry.LouDeferredRegister;
import net.minecraft.tileentity.TileEntityType;

public class RhizomeTE extends GeneratorTileEntity {
    public RhizomeTE(TileEntityType<?> prop) {
        super(prop);
    }

    public RhizomeTE() {
        super(LouDeferredRegister.rhizomeTE.get());
    }
}
