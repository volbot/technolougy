package com.github.volbot.technolougy.tileentity;

import com.github.volbot.technolougy.block.AbstractRhizomaticTankBlock;
import com.github.volbot.technolougy.block.ConnectionBlock;
import com.github.volbot.technolougy.block.RhizomeUtils;
import com.github.volbot.technolougy.registry.LouDeferredRegister;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class RhizomeProxyTE extends TileEntity implements ITickableTileEntity {

    private BlockPos n1;
    private BlockPos n2;

    public RhizomeProxyTE(TileEntityType type) {
        super(type);
    }

    public RhizomeProxyTE() {
        super(LouDeferredRegister.rhizomeProxyTE.get());
    }

    private int debugint = 0;

    @Override
    public void tick() {
        if (!getLevel().isClientSide())
            if (debugint != 10) {
                debugint++;
            } else {
                debugint = 0;
                System.out.println("pos: " + getBlockPos() + " | n1: " + n1 + " | n2: " + n2);
            }
    }

    public void setRhizomeHolders(@Nullable BlockPos n1, @Nullable BlockPos n2) {
        this.n1 = n1;
        this.n2 = n2;
    }

    public void setRhizomeHolders(BlockPos[] holders) {
        setRhizomeHolders(holders[0], holders[1]);
    }

    public BlockPos[] getRhizomeHolders() {
        return new BlockPos[]{this.n1, this.n2};
    }

    public void searchConnections(@Nullable BlockPos messenger) {
        BlockPos startPoint = getBlockPos();
        BlockPos[] neighbors = new BlockPos[]{
                startPoint.above(),
                startPoint.below(),
                startPoint.north(),
                startPoint.south(),
                startPoint.east(),
                startPoint.west()
        };
        ArrayList<BlockPos> rhizomes = new ArrayList<>();
        for (BlockPos neighbor : neighbors) {
            TileEntity te = level.getBlockEntity(neighbor);
            if (te instanceof AbstractRhizomaticTankTE && !rhizomes.contains(neighbor)) {
                System.out.println("RHIZOME FOUND");
                rhizomes.add(neighbor);
            } else if (te instanceof RhizomeProxyTE) {
                System.out.println("CONNECTION FOUND");
                RhizomeProxyTE proxy = (RhizomeProxyTE) te;
                BlockPos[] connections = proxy.getRhizomeHolders();
                for (BlockPos connection : connections) {
                    if (connection != null) {
                        if (!rhizomes.contains(connection) && !connection.equals(startPoint)) {
                            rhizomes.add(connection);
                        }
                    }
                }
            }
        }
        BlockPos[] initialHolders = getRhizomeHolders();
        if (rhizomes.size() == 0) {
            setRhizomeHolders(null, null);
        } else if (rhizomes.size() == 1) {
            setRhizomeHolders(rhizomes.get(0), null);
        } else if (rhizomes.size() == 2) {
            setRhizomeHolders(rhizomes.get(0), rhizomes.get(1));
        } else {
            System.out.println("TOO MANY CONNECTIONS");
        }
        BlockPos[] afterHolders = getRhizomeHolders();
        if (!initialHolders.equals(afterHolders)) {
            for (BlockPos neighbor : neighbors) {
                if(messenger == null || !messenger.equals(neighbor)) {
                    System.out.println("messenger: "+messenger+" | neighbor: "+neighbor);
                    TileEntity te = level.getBlockEntity(neighbor);
                    if (te instanceof RhizomeProxyTE) {
                        RhizomeProxyTE proxy = (RhizomeProxyTE) te;
                        proxy.searchConnections(startPoint);
                    }
                }
            }
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        if (n1 != null) {
            nbt.putLong("n1", n1.asLong());
        } else {
            nbt.putLong("n1", 0);
        }
        if (n2 != null) {
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
        if (tag1 != 0) {
            if (tag2 != 0) {
                this.setRhizomeHolders(BlockPos.of(tag1), BlockPos.of(tag2));
            } else {
                this.setRhizomeHolders(BlockPos.of(tag1), null);
            }
        }
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT nbtTag = new CompoundNBT();
        nbtTag = this.save(nbtTag);
        return new SUpdateTileEntityPacket(getBlockPos(), -1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT tag = pkt.getTag();
        this.load(getBlockState(), tag);
    }
}
