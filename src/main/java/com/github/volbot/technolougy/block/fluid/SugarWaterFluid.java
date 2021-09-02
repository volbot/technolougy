package com.github.volbot.technolougy.block.fluid;

import net.minecraft.fluid.FluidState;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class SugarWaterFluid extends ForgeFlowingFluid {

    public static int fluidColor = (255 & 0xff) << 24 | (174 & 0xff) << 16 | (198 & 0xff) << 8 | (207 & 0xff);

    protected SugarWaterFluid(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isSource(FluidState fluidState) {
        return true;
    }

    @Override
    public int getAmount(FluidState fluidState) {
        return 0;
    }

}