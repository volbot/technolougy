package com.github.volbot.technolougy.block;

import com.github.volbot.technolougy.tileentity.RhizomeTE;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class PointBlock extends GeneratorBlock {

    public PointBlock(Properties prop) {
        super(prop);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world){

        return new RhizomeTE();

    }
}
