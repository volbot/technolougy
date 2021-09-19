package com.github.volbot.technolougy.block;

import com.github.volbot.technolougy.tileentity.RhizomeProxyTE;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ConnectionBlock extends Block {
    public ConnectionBlock(Properties prop) {
        super(prop);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new RhizomeProxyTE();
    }

    @Override
    public void onPlace(BlockState state, World world, BlockPos pos, BlockState state1, boolean b) {
        super.onPlace(state, world, pos, state1, b);
        ((RhizomeProxyTE)world.getBlockEntity(pos)).searchConnections();
    }


    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos pos2, boolean b) {
        super.neighborChanged(state, world, pos, block, pos2, b);
        System.out.println("UPDATE AT "+pos);
        ((RhizomeProxyTE)world.getBlockEntity(pos)).searchConnections();
    }

}
