package com.github.volbot.technolougy.block;

import com.github.volbot.technolougy.tileentity.SmelterTE;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class SmelterBlock extends AbstractRhizomaticMachineBlock {

    public SmelterBlock(Properties prop) {
        super(prop);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new SmelterTE();
    }
}
