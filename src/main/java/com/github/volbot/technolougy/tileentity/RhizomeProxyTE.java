package com.github.volbot.technolougy.tileentity;

import com.github.volbot.technolougy.block.PointBlock;
import com.github.volbot.technolougy.block.RhizomeUtils;
import com.github.volbot.technolougy.registry.LouDeferredRegister;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import java.util.ArrayList;

public class RhizomeProxyTE extends TileEntity implements ITickableTileEntity {

    private BlockPos rhizomeHolder;
    private boolean selfdestruct = false;

    public RhizomeProxyTE(){
        super(LouDeferredRegister.rhizomeProxyTE.get());
        setRhizomeHolder(new BlockPos(0,0,0));
    }

    private int debugint = 0;

    @Override
    public void tick() {
        if(rhizomeHolder==null){
            System.out.println("proxy searching...");
            BlockPos neighbor = RhizomeUtils.findRhizomeNeighbors(getLevel(),getBlockPos());
            if(neighbor==null){
                neighbor = RhizomeUtils.askProxyNeighbors(getLevel(),getBlockPos());
                if(neighbor==null) {
                    getLevel().setBlockEntity(getBlockPos(),new RhizomeTE());
                    this.setRemoved();
                } else {
                    getLevel().setBlockEntity(getBlockPos(),new RhizomeProxyTE(neighbor));
                    this.setRemoved();
                }
            } else {
                getLevel().setBlockEntity(getBlockPos(),new RhizomeProxyTE(neighbor));
                this.setRemoved();
            }
        }
        if(debugint != 10){
            debugint++;
        } else {
            debugint = 0;
            //System.out.println("POS: "+getBlockPos());
            System.out.println("HOLDER: " + getRhizomeHolder());
            //System.out.println(getBlockPos());
            //System.out.println(getLevel());
        }

    }

    public RhizomeProxyTE(BlockPos neighbor) {
        super(LouDeferredRegister.rhizomeProxyTE.get());
        if(neighbor!=null){
            this.setRhizomeHolder(neighbor);
        }
        /*
        if (rhizomeHolder == null) {
            System.out.println("proxy removed");
            this.selfdestruct=true;
        }
         */
    }

    public void isProxy(String s) {
        System.out.println(this.rhizomeHolder);
    }

    public void setRhizomeHolder(BlockPos rhizomeHolder) {
        this.rhizomeHolder = rhizomeHolder;
    }

    public BlockPos getRhizomeHolder() {
        return this.rhizomeHolder;
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        nbt.putLong("rhizomeHolder",rhizomeHolder.asLong());
        return nbt;
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        setRhizomeHolder(BlockPos.of(nbt.getLong("rhizomeHolder")));
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT updateTag = super.getUpdateTag();
        updateTag.putLong("rhizomeHolder",rhizomeHolder.asLong());
        return updateTag;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        super.handleUpdateTag(state, tag);
    }
}
