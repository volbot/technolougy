package com.github.volbot.technolougy.block;

import com.github.volbot.technolougy.registry.LouDeferredRegister;
import com.github.volbot.technolougy.tileentity.AbstractRhizomaticMachineTE;
import com.github.volbot.technolougy.tileentity.AbstractRhizomaticTankTE;
import com.github.volbot.technolougy.tileentity.RhizomeProxyTE;
import com.github.volbot.technolougy.tileentity.RhizomeTE;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.INamedContainerProvider;
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

public abstract class AbstractRhizomaticTankBlock extends ConnectionBlock {

    private BlockPos neighborTemp = null;

    public AbstractRhizomaticTankBlock(Properties prop) {
        super(prop);
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult raytrace) {
        if(!player.isShiftKeyDown()) {
            ItemStack heldItem = player.getItemBySlot(EquipmentSlotType.MAINHAND);
            AbstractRhizomaticTankTE rhizome = (AbstractRhizomaticTankTE)world.getBlockEntity(pos.immutable());
            if (rhizome == null) {
                System.out.println("RHIZOME NULL AT "+pos);
                return ActionResultType.FAIL;
            }
            if (heldItem.sameItem(LouDeferredRegister.sugarWaterFluidBucket.get().getDefaultInstance())) {
                if (rhizome != null) {
                    if(rhizome.fill(new FluidStack(LouDeferredRegister.sugarWaterFluid.get().getFluid(), 1000), IFluidHandler.FluidAction.EXECUTE)>0
                            && !player.isCreative()) {
                        player.setItemSlot(EquipmentSlotType.MAINHAND, Items.BUCKET.getDefaultInstance());
                    }
                }
                return ActionResultType.SUCCESS;
            } else if (heldItem.sameItem(Items.BUCKET.getDefaultInstance())){
                if(rhizome.drain(1000, IFluidHandler.FluidAction.EXECUTE).getAmount()>0
                        && !player.isCreative()) {
                    player.setItemSlot(EquipmentSlotType.MAINHAND, LouDeferredRegister.sugarWaterFluidBucket.get().getDefaultInstance());
                }
                return ActionResultType.SUCCESS;
            }
            return ActionResultType.FAIL;
        } else {
            if (world.isClientSide) {
                return ActionResultType.SUCCESS;
            } else {
                this.openContainer(world, pos, player);
                return ActionResultType.CONSUME;
            }
        }
    }

    protected void openContainer(World world, BlockPos pos, PlayerEntity entity) {
        TileEntity te = world.getBlockEntity(pos);
        if (te instanceof AbstractRhizomaticTankTE) {
            entity.openMenu((INamedContainerProvider) te);
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public void onPlace(BlockState state, World world, BlockPos pos, BlockState state1, boolean b) {
        super.onPlace(state, world, pos, state1, b);
        BlockPos[] neighbors = new BlockPos[]{
                pos.above(),
                pos.below(),
                pos.north(),
                pos.south(),
                pos.east(),
                pos.west()
        };
        for(BlockPos neighbor : neighbors) {
            if(world.getBlockState(neighbor).getBlock() instanceof ConnectionBlock){
                ((RhizomeProxyTE)world.getBlockEntity(neighbor)).searchConnections(null);
            }
        }
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world){
        return null;
    }
}
