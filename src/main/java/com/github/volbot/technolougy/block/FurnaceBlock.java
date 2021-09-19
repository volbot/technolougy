package com.github.volbot.technolougy.block;

import com.github.volbot.technolougy.tileentity.FurnaceTE;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class FurnaceBlock extends PointBlock{
    public FurnaceBlock(Properties prop) {
        super(prop);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new FurnaceTE();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
}
