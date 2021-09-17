package com.github.volbot.technolougy.block;

import com.github.volbot.technolougy.registry.LouDeferredRegister;
import com.github.volbot.technolougy.tileentity.RhizomeProxyTE;
import com.github.volbot.technolougy.tileentity.RhizomeTE;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.awt.*;

public class PointBlock extends Block {

    private BlockPos neighborTemp = null;

    public PointBlock(Properties prop) {
        super(prop);
        this.registerDefaultState(this.getStateDefinition().any()
            .setValue(proxy, true)
        );
    }

    static final Property<Boolean> proxy = BooleanProperty.create("proxy");

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult raytrace) {
        if(!player.isShiftKeyDown()) {
            ItemStack heldItem = player.getItemBySlot(EquipmentSlotType.MAINHAND);
            RhizomeTE rhizome = null;
            TileEntity TE = world.getBlockEntity(pos.immutable());
            if(TE instanceof RhizomeTE) {
                rhizome = (RhizomeTE) world.getBlockEntity(pos);
            } else if(TE instanceof RhizomeProxyTE) {
                System.out.println(pos.immutable());
                RhizomeProxyTE proxyTE = (RhizomeProxyTE)world.getBlockEntity(pos.immutable());
                proxyTE.isProxy("pickle");
                //System.out.println(TE==null);
                //RhizomeProxyTE proxyTE = (RhizomeProxyTE) TE;
                System.out.println(proxyTE==null);
                BlockPos holder = proxyTE.getRhizomeHolder();
                System.out.println(holder==null);
                System.out.println(holder);
                rhizome = (RhizomeTE) world.getBlockEntity(holder);
            } else {
                System.out.println("TILE ENTITY NULL AT "+pos.immutable());
                return ActionResultType.FAIL;
            }
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

    /*
    @Override
    public void playerDestroy(World world, PlayerEntity playerEntity, BlockPos pos, BlockState state, @Nullable TileEntity tileEntity, ItemStack itemStack) {
        super.playerDestroy(world, playerEntity, pos, state, tileEntity, itemStack);
        if(pos.equals(rhizomeHolder)){
            RhizomeTE rhizome = (RhizomeTE) world.getBlockEntity(rhizomeHolder);
            BlockPos neighbor = RhizomeUtils.findRhizomeNeighbors(world, pos, true);
            if(rhizome!=null && neighbor!=null) {
                rhizome.setPosition(neighbor);
                RhizomeUtils.updateRhizomeHolders(world,pos,neighbor);
            }
        }
    }

     */

    public static Property<Boolean> getPropertyProxy() {
        return proxy;
    }

    public static void setPropertyProxy(World world){
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(proxy);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world){
        if(state.getValue(proxy).booleanValue()){
            if(neighborTemp!=null) {
                System.out.println("RHIZOME PROXY CREATED");
                RhizomeProxyTE proxyTE;
                BlockPos neighbor = neighborTemp;
                neighborTemp = null;
                return new RhizomeProxyTE(neighbor);
            } else {
                return new RhizomeProxyTE(null);
            }
        } else {
            System.out.println("RHIZOME CREATED");
            return new RhizomeTE();
        }
    }

    /*
    @Override
    public void onPlace(BlockState blockState, World world, BlockPos blockPos, BlockState state, boolean bool) {
        super.onPlace(blockState, world, blockPos, state, bool);
        if(blockState.getValue(proxy)) {
            BlockPos neighbor = RhizomeUtils.findRhizomeNeighbors(world, blockPos, true);
            if (neighbor == null) {
                System.out.println("BPLACED");
                world.setBlockAndUpdate(
                        blockPos,
                        blockState.getBlock().defaultBlockState().setValue(proxy,false)
                );
            } else {
                neighborTemp=neighbor;
            }
        }
    }

     */
}
