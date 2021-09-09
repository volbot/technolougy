package com.github.volbot.technolougy.block;

import com.github.volbot.technolougy.tileentity.RhizomeTE;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RhizomeUtils {
    public static BlockPos findRhizomeNeighbors(World world, BlockPos startPoint) {
        BlockPos[] neighbors = new BlockPos[]{
                startPoint.above(),
                startPoint.below(),
                startPoint.north(),
                startPoint.south(),
                startPoint.east(),
                startPoint.west()
        };
        for(BlockPos pos : neighbors){
            if(world.getBlockState(pos).getBlock()==null){

            } else if(world.getBlockEntity(pos) instanceof RhizomeTE){
                return pos;
            } else {
                return findRhizomeNeighbors(world, pos);
            }
        }
        return null;
    }
}
