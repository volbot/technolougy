package com.github.volbot.technolougy.block;

import com.github.volbot.technolougy.tileentity.RhizomeTE;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RhizomeUtils {
    public static BlockPos findRhizomeNeighbors(World world, BlockPos startPoint, boolean recursive) {
        BlockPos[] neighbors = new BlockPos[]{
                startPoint.above(),
                startPoint.below(),
                startPoint.north(),
                startPoint.south(),
                startPoint.east(),
                startPoint.west()
        };
        for(BlockPos pos : neighbors){
            if(!(world.getBlockState(pos).getBlock() instanceof PointBlock)){
                continue;
            } else if(world.getBlockEntity(pos) instanceof RhizomeTE){
                return pos;
            } else {
                if(recursive) {
                    return findRhizomeNeighbors(world, pos, recursive);
                }
            }
        }
        return null;
    }

    public static void updateRhizomeHolders(World world, BlockPos startPoint, BlockPos holder){
        BlockPos neighbor = findRhizomeNeighbors(world, startPoint,true);
        while(neighbor!=null){
            ((PointBlock)world.getBlockState(neighbor).getBlock()).setRhizomeHolder(holder);
            neighbor = findRhizomeNeighbors(world, startPoint,true);
        }
    }
}
