package com.github.volbot.technolougy.block;

import com.github.volbot.technolougy.registry.LouDeferredRegister;
import com.github.volbot.technolougy.tileentity.RhizomeTE;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;

public class PointBlock extends Block {

    public PointBlock(Properties prop) {
        super(prop);
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult raytrace) {
        if(!player.isShiftKeyDown()) {
            ItemStack heldItem = player.getItemBySlot(EquipmentSlotType.MAINHAND);
            RhizomeTE rhizome = (RhizomeTE) world.getBlockEntity(pos);
            if (heldItem.sameItem(LouDeferredRegister.sugarWaterFluidBucket.get().getDefaultInstance())) {
                if (rhizome != null) {
                    if(rhizome.fill(new FluidStack(LouDeferredRegister.sugarWaterFluid.get().getFluid(), 1000), IFluidHandler.FluidAction.EXECUTE)>0
                            && !player.isCreative()) {
                        player.setItemSlot(EquipmentSlotType.MAINHAND, Items.BUCKET.getDefaultInstance());
                    }
                }
                return ActionResultType.sidedSuccess(world.isClientSide);
            } else if (heldItem.sameItem(Items.BUCKET.getDefaultInstance())){
                if(rhizome.drain(1000, IFluidHandler.FluidAction.EXECUTE).getAmount()>0
                        && !player.isCreative()) {
                    player.setItemSlot(EquipmentSlotType.MAINHAND, LouDeferredRegister.sugarWaterFluidBucket.get().getDefaultInstance());
                }
                return ActionResultType.sidedSuccess(world.isClientSide);
            }
            return ActionResultType.sidedSuccess(world.isClientSide);
        }
        return ActionResultType.PASS;
    }

    @Override
    public void playerDestroy(World world, PlayerEntity playerEntity, BlockPos pos, BlockState state, @Nullable TileEntity tileEntity, ItemStack itemStack) {
        super.playerDestroy(world, playerEntity, pos, state, tileEntity, itemStack);
        RhizomeTE rhizome = (RhizomeTE) world.getBlockEntity(pos);
        BlockPos neighbor = RhizomeUtils.findRhizomeNeighbors(world, pos);
        if(rhizome!=null && neighbor!=null) {
            rhizome.setPosition(neighbor);
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world){
        return new RhizomeTE();
    }

}
