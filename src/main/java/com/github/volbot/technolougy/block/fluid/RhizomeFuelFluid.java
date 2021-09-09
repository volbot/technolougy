package com.github.volbot.technolougy.block.fluid;

import net.minecraft.block.CauldronBlock;
import net.minecraft.fluid.FluidState;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class RhizomeFuelFluid extends ForgeFlowingFluid {

    public static int fluidColor = (0 & 0xff) << 24 | (0 & 0xff) << 16 | (128 & 0xff) << 8 | (255 & 0xff);

    protected RhizomeFuelFluid(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isSource(FluidState fluidState) {
        return true;
    }

    @Override
    public int getAmount(FluidState state) {
        return state.getValue(LEVEL);
    }

}