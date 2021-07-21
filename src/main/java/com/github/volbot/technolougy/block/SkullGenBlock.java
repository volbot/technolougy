package com.github.volbot.technolougy.block;

import com.github.volbot.technolougy.tileentity.SkullGenTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class SkullGenBlock extends GeneratorBlock {

    public SkullGenBlock(Properties prop) {
        super(prop);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world){

        return new SkullGenTileEntity();

    }
}
