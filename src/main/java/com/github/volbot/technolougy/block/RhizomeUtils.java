package com.github.volbot.technolougy.block;

import com.github.volbot.technolougy.tileentity.RhizomeProxyTE;
import com.github.volbot.technolougy.tileentity.RhizomeTE;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import java.util.ArrayList;

public class RhizomeUtils {

    public static BlockPos[] searchConnections(IBlockReader world, BlockPos startPoint) {
        BlockPos[] neighbors = new BlockPos[]{
                startPoint.above(),
                startPoint.below(),
                startPoint.north(),
                startPoint.south(),
                startPoint.east(),
                startPoint.west()
        };
        ArrayList<BlockPos> rhizomes = new ArrayList<>();
        for(BlockPos neighbor : neighbors){
            Block block = world.getBlockState(neighbor).getBlock();
            if(block instanceof AbstractRhizomaticTankBlock && !rhizomes.contains(neighbor)) {
                System.out.println("RHIZOME FOUND");
                rhizomes.add(neighbor);
            } else if(block instanceof ConnectionBlock) {
                System.out.println("CONNECTION FOUND");
                RhizomeProxyTE proxy = (RhizomeProxyTE) world.getBlockEntity(neighbor);
                BlockPos[] connections = proxy.getRhizomeHolders();
                for(BlockPos connection : connections) {
                    if(connection != null && world.getBlockEntity(connection) instanceof RhizomeTE) {
                        if (!rhizomes.contains(connection) && connection!=startPoint) {
                            rhizomes.add(connection);
                        }
                    }
                }
            }
        }
        BlockPos[] connections = new BlockPos[rhizomes.size()];
        for(int i = 0; i < rhizomes.size(); i++) {
            System.out.println(i+": "+rhizomes.get(i));
            connections[i]=rhizomes.get(i);
        }
        return connections;
    }

    public static BlockPos[] searchConnections(IBlockReader world, BlockPos startPoint, BlockPos ignore) {
        BlockPos[] neighbors = new BlockPos[]{
                startPoint.above(),
                startPoint.below(),
                startPoint.north(),
                startPoint.south(),
                startPoint.east(),
                startPoint.west()
        };
        ArrayList<BlockPos> rhizomes = new ArrayList<>();
        for(BlockPos neighbor : neighbors){
            if(!ignore.equals(neighbor)) {
                Block block = world.getBlockState(neighbor).getBlock();
                if (block instanceof AbstractRhizomaticTankBlock && !rhizomes.contains(neighbor)) {
                    System.out.println("RHIZOME FOUND");
                    rhizomes.add(neighbor);
                } else if (block instanceof ConnectionBlock) {
                    System.out.println("CONNECTION FOUND");
                    RhizomeProxyTE proxy = (RhizomeProxyTE) world.getBlockEntity(neighbor);
                    BlockPos[] connections = proxy.getRhizomeHolders();
                    for (BlockPos connection : connections) {
                        if (connection != null && !rhizomes.contains(connection)) {
                            rhizomes.add(connection);
                        }
                    }
                }
            }
        }
        BlockPos[] connections = new BlockPos[rhizomes.size()];
        for(int i = 0; i < rhizomes.size(); i++) {
            System.out.println(i+": "+rhizomes.get(i));
            connections[i]=rhizomes.get(i);
        }
        return connections;
    }

    public static int countProxies(IBlockReader world, BlockPos startPoint) {
        return countProxies(world,startPoint,new ArrayList<>());
    }

    public static int countProxies(IBlockReader world, BlockPos startPoint, ArrayList<BlockPos> visited) {
        BlockPos[] neighbors = new BlockPos[]{
                startPoint.above(),
                startPoint.below(),
                startPoint.north(),
                startPoint.south(),
                startPoint.east(),
                startPoint.west()
        };
        int count = 0;
        for(BlockPos neighbor : neighbors) {
            if(visited.contains(neighbor)) {
                continue;
            }
            System.out.println("new");
            visited.add(neighbor);
            if(world.getBlockState(neighbor).getBlock() instanceof ConnectionBlock) {
                count+=1+countProxies(world,neighbor,visited);
            }
        }
        return count;
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
            if(!visited.contains(neighbor)) {
                visited.add(neighbor);
                if(world.getBlockEntity(neighbor) != null) {
                    if (world.getBlockEntity(neighbor) instanceof RhizomeTE) {
                        return neighbor;
                    } else if (world.getBlockEntity(neighbor) instanceof RhizomeProxyTE) {
                        return findRhizomeNeighbors(world, neighbor, visited);
                    }
                }
                continue;
            }
        }
        return null;
    }

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

    public static BlockPos[] askProxyNeighbors(IBlockReader world, BlockPos startPoint) {
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
                    return ((RhizomeProxyTE) TE).getRhizomeHolders();
                }
            }
        }
        return null;
    }
}
