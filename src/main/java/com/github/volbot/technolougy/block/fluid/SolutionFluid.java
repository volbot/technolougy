package com.github.volbot.technolougy.block.fluid;

import net.minecraft.fluid.FluidState;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class SolutionFluid extends ForgeFlowingFluid {

    protected SolutionFluid(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isSource(FluidState fluidState) {
        return false;
    }

    @Override
    public int getAmount(FluidState fluidState) {
        return 0;
    }
}
