package com.github.volbot.technolougy.block;

import com.github.volbot.technolougy.tileentity.AbstractRhizomaticMachineTE;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class AbstractRhizomaticMachineBlock extends AbstractRhizomaticTankBlock {

    public AbstractRhizomaticMachineBlock(Properties prop) {
        super(prop);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return null;
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult raytrace) {
        if(super.use(state,world,pos,player,hand,raytrace)==ActionResultType.SUCCESS){
            return ActionResultType.SUCCESS;
        }
        if (world.isClientSide) {
            return ActionResultType.SUCCESS;
        } else {
            this.openContainer(world, pos, player);
            return ActionResultType.CONSUME;
        }
    }

    protected void openContainer(World world, BlockPos pos, PlayerEntity entity) {
        TileEntity te = world.getBlockEntity(pos);
        if (te instanceof AbstractRhizomaticMachineTE) {
            System.out.println("MENU");
            entity.openMenu((INamedContainerProvider) te);
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
}
