package com.github.volbot.technolougy.tileentity;

import com.github.volbot.technolougy.registry.LouDeferredRegister;
import net.minecraft.tileentity.TileEntityType;

public class SkullGenTileEntity extends GeneratorTileEntity {
    public SkullGenTileEntity(TileEntityType<?> prop) {
        super(prop);
    }

    public SkullGenTileEntity() {
        super(LouDeferredRegister.skullGenTE.get());
    }
}
