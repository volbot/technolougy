package com.github.volbot.technolougy.tileentity;

import com.github.volbot.technolougy.block.ConnectionBlock;
import com.github.volbot.technolougy.block.RhizomeUtils;
import com.github.volbot.technolougy.registry.LouDeferredRegister;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public class RhizomeProxyTE extends TileEntity implements ITickableTileEntity {

    private BlockPos n1;
    private BlockPos n2;

    public RhizomeProxyTE(){
        super(LouDeferredRegister.rhizomeProxyTE.get());
    }

    public RhizomeProxyTE(TileEntityType type){
        super(type);
    }

    private int debugint = 0;

    @Override
    public void tick() {
        if(getLevel().isClientSide())
            if (debugint != 10) {
                debugint++;
            } else {
                debugint = 0;
                System.out.println("pos: "+getBlockPos()+" | n1: "+n1+" | n2: "+n2);
            }
    }

    public void setRhizomeHolders(@Nullable BlockPos n1, @Nullable BlockPos n2) {
        this.n1 = n1;
        this.n2 = n2;
    }

    public void setRhizomeHolders(BlockPos[] holders) {
        setRhizomeHolders(holders[0],holders[1]);
    }

    public BlockPos[] getRhizomeHolders() {
        return new BlockPos[]{ this.n1 , this.n2 };
    }

    public void searchConnections() {
        BlockPos[] connections = RhizomeUtils.searchConnections(getLevel(),getBlockPos());
        BlockPos[] initialHolders = getRhizomeHolders();
        RhizomeProxyTE proxy = (RhizomeProxyTE) getLevel().getBlockEntity(getBlockPos());
        if(connections.length==0) {
            proxy.setRhizomeHolders(null,null);
        } else if (connections.length==1){
            proxy.setRhizomeHolders(connections[0],null);
        } else if (connections.length==2){
            proxy.setRhizomeHolders(connections[0],connections[1]);
        } else {
            System.out.println("TOO MANY CONNECTIONS");
        }
        BlockPos[] afterHolders = getRhizomeHolders();
        if(!initialHolders.equals(afterHolders)){
            getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 2);
            notifyNeighbors(getBlockPos());
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
        for(BlockPos neighbor : neighbors) {
            if(!messenger.equals(neighbor) && getLevel().getBlockState(neighbor).getBlock() instanceof ConnectionBlock){
                ((RhizomeProxyTE)getLevel().getBlockEntity(neighbor)).setRhizomeHolders(this.getRhizomeHolders());
                getLevel().sendBlockUpdated(neighbor, getBlockState(), getBlockState(), 2);
                ((RhizomeProxyTE)getLevel().getBlockEntity(neighbor)).notifyNeighbors(getBlockPos());
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
        }
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket(){
        CompoundNBT nbtTag = new CompoundNBT();
        this.save(nbtTag);
        return new SUpdateTileEntityPacket(getBlockPos(), -1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt){
        CompoundNBT tag = pkt.getTag();
        this.load(getBlockState(),tag);
    }
}
