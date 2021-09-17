package com.github.volbot.technolougy.block;

import com.github.volbot.technolougy.tileentity.RhizomeProxyTE;
import com.github.volbot.technolougy.tileentity.RhizomeTE;
import jdk.nashorn.internal.ir.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBiomeReader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.ArrayList;

public class RhizomeUtils {
    public static BlockPos findRhizomeNeighbors(IBlockReader world, BlockPos startPoint) {
        BlockPos[] neighbors = new BlockPos[]{
                startPoint.above(),
                startPoint.below(),
                startPoint.north(),
                startPoint.south(),
                startPoint.east(),
                startPoint.west()
        };
        for(BlockPos pos : neighbors){
            if(
                    pos!=null &&
                    world.getBlockEntity(pos)!=null &&
                    world.getBlockEntity(pos) instanceof RhizomeTE)
            {
                return pos;
            }
        }
        return null;
    }

    public static BlockPos askProxyNeighbors(IBlockReader world, BlockPos startPoint) {
        BlockPos[] neighbors = new BlockPos[]{
                startPoint.above(),
                startPoint.below(),
                startPoint.north(),
                startPoint.south(),
                startPoint.east(),
                startPoint.west()
        };
        for(BlockPos pos : neighbors){
            if(world.getBlockEntity(pos)!=null){
                TileEntity TE = world.getBlockEntity(pos);
                if(TE instanceof RhizomeProxyTE){
                    return ((RhizomeProxyTE) TE).getRhizomeHolder();
                }
            }
        }
        return null;
    }

    public static BlockPos findRhizomeNeighbors(IBlockReader world, BlockPos startPoint, boolean recursive) {
        return findRhizomeNeighbors(world,startPoint,new ArrayList<>());
    }

    private static BlockPos findRhizomeNeighbors(IBlockReader world, BlockPos startPoint, ArrayList<BlockPos> visited) {
        BlockPos[] neighbors = new BlockPos[]{
                startPoint.above(),
                startPoint.below(),
                startPoint.north(),
                startPoint.south(),
                startPoint.east(),
                startPoint.west()
        };
        for(BlockPos neighbor : neighbors){
            System.out.println("VISITING "+neighbor);
            if(!visited.contains(neighbor)) {
                System.out.println("NEIGHBOR IS NEW");
                visited.add(neighbor);
                if(world.getBlockEntity(neighbor) != null) {
                    System.out.println("NEIGHBOR HAS ENTITY");
                    if (world.getBlockEntity(neighbor) instanceof RhizomeTE) {
                        System.out.println("NEIGHBOR IS RHIZOME");
                        return neighbor;
                    } else if (world.getBlockEntity(neighbor) instanceof RhizomeProxyTE) {
                        System.out.println("NEIGHBOR KNOWS OF RHIZOME, FOLLOWING:");
                        return findRhizomeNeighbors(world, neighbor, visited);
                    }
                }
                continue;
            }
        }
        return null;
    }
}
