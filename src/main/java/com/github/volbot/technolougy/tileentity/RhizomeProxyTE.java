package com.github.volbot.technolougy.tileentity;

import com.github.volbot.technolougy.block.ConnectionBlock;
import com.github.volbot.technolougy.block.PointBlock;
import com.github.volbot.technolougy.block.RhizomeUtils;
import com.github.volbot.technolougy.registry.LouDeferredRegister;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class RhizomeProxyTE extends TileEntity implements ITickableTileEntity {

    private BlockPos n1;
    private BlockPos n2;

    public RhizomeProxyTE(){
        super(LouDeferredRegister.rhizomeProxyTE.get());
    }

    private int debugint = 0;

    @Override
    public void tick() {
        if(getLevel().isClientSide()) {
            if (debugint != 10) {
                debugint++;
            } else {
                debugint = 0;
                System.out.println("R1: " + n1 + "  -  R2: " + n2);
            }
        }
    }

    public void setRhizomeHolders(@Nullable BlockPos n1, @Nullable BlockPos n2) {
        if(n1!=null) {
            this.n1 = n1;
        }
        if(n2!=null) {
            this.n2 = n2;
        }
    }

    public BlockPos[] getRhizomeHolders() {
        return new BlockPos[]{ this.n1 , this.n2 };
    }

    public void searchConnections() {
        BlockPos[] connections = RhizomeUtils.searchConnections(getLevel(),getBlockPos());
        BlockPos[] initialHolders = getRhizomeHolders();
        RhizomeProxyTE proxy = (RhizomeProxyTE) getLevel().getBlockEntity(getBlockPos());
        System.out.println(proxy==null);
        if(connections.length==0) {

        } else if (connections.length==1){
            proxy.setRhizomeHolders(connections[0],null);
        } else if (connections.length==2){
            proxy.setRhizomeHolders(connections[0],connections[1]);
        } else {
            System.out.println("TOO MANY CONNECTIONS");
        }
        BlockPos[] afterHolders = getRhizomeHolders();
        if(!initialHolders.equals(afterHolders)){
            notifyNeighbors(getBlockPos());
        }
    }

    private void searchConnections(BlockPos messenger) {
        BlockPos[] connections = RhizomeUtils.searchConnections(getLevel(),getBlockPos());
        BlockPos[] initialHolders = getRhizomeHolders();
        RhizomeProxyTE proxy = (RhizomeProxyTE) getLevel().getBlockEntity(getBlockPos());
        System.out.println(proxy==null);
        if(connections.length==0) {

        } else if (connections.length==1){
            proxy.setRhizomeHolders(connections[0],null);
        } else if (connections.length==2){
            proxy.setRhizomeHolders(connections[0],connections[1]);
        } else {
            System.out.println("TOO MANY CONNECTIONS");
        }
        BlockPos[] afterHolders = getRhizomeHolders();
        if(!initialHolders.equals(afterHolders)){
            notifyNeighbors(messenger);
        }
    }

    public void notifyNeighbors(BlockPos messenger) {
        BlockPos[] neighbors = new BlockPos[]{
                getBlockPos().above(),
                getBlockPos().below(),
                getBlockPos().north(),
                getBlockPos().south(),
                getBlockPos().east(),
                getBlockPos().west()
        };
        System.out.println("MESSENGER: "+messenger);
        for(BlockPos neighbor : neighbors) {
            System.out.println("CHECKING: "+neighbor);
            if(messenger.equals(neighbor)) {
                System.out.println("MESSENGER");
                continue;
            }
            if(getLevel().getBlockState(neighbor).getBlock() instanceof ConnectionBlock){
                ((RhizomeProxyTE)getLevel().getBlockEntity(neighbor)).searchConnections(getBlockPos());
            }
        }



    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        if(n1!=null) {
            nbt.putLong("n1", n1.asLong());
        } else {
            nbt.putLong("n1", 0);
        }
        if(n2!=null) {
            nbt.putLong("n2", n2.asLong());
        } else {
            nbt.putLong("n2", 0);
        }
        return nbt;
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        long tag1 = nbt.getLong("n1");
        long tag2 = nbt.getLong("n2");
        if(tag1!=0){
            if(tag2!=0){
                this.setRhizomeHolders(BlockPos.of(tag1), BlockPos.of(tag2));
            } else {
                this.setRhizomeHolders(BlockPos.of(tag1), null);
            }
        }    }

    /*
    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT updateTag = super.getUpdateTag();
        if(n1!=null) {
            updateTag.putLong("n1", n1.asLong());
        } else {
            updateTag.putLong("n1", 0);
        }
        if(n2!=null) {
            updateTag.putLong("n2", n2.asLong());
        } else {
            updateTag.putLong("n2", 0);
        }
        return updateTag;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        super.handleUpdateTag(state, tag);
        long tag1 = tag.getLong("n1");
        long tag2 = tag.getLong("n2");
        if(tag1!=0){
            if(tag2!=0){
                this.setRhizomeHolders(BlockPos.of(tag1), BlockPos.of(tag2));
            } else {
                this.setRhizomeHolders(BlockPos.of(tag1), null);
            }
        }
    }
    */

    @Override
    public SUpdateTileEntityPacket getUpdatePacket(){
        CompoundNBT nbtTag = new CompoundNBT();
        if(n1!=null) {
            nbtTag.putLong("n1", n1.asLong());
        } else {
            nbtTag.putLong("n1", 0);
        }
        if(n2!=null) {
            nbtTag.putLong("n2", n2.asLong());
        } else {
            nbtTag.putLong("n2", 0);
        }
        return new SUpdateTileEntityPacket(getBlockPos(), -1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt){
        CompoundNBT tag = pkt.getTag();
        long tag1 = tag.getLong("n1");
        long tag2 = tag.getLong("n2");
        if(tag1!=0){
            if(tag2!=0){
                this.setRhizomeHolders(BlockPos.of(tag1), BlockPos.of(tag2));
            } else {
                this.setRhizomeHolders(BlockPos.of(tag1), null);
            }
        }
    }
}
