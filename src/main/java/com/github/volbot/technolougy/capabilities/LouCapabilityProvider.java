package com.github.volbot.technolougy.capabilities;

import com.github.volbot.technolougy.tileentity.RhizomeTE;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.github.volbot.technolougy.tileentity.RhizomeTE.FLUID_CAP;

public class LouCapabilityProvider implements ICapabilitySerializable<CompoundNBT> {

    private LazyOptional<IFluidHandler> instance = LazyOptional.of(FLUID_CAP::getDefaultInstance);

    @CapabilityInject(IFluidHandler.class)
    public static Capability<IFluidHandler> FLUID_CAP = null;

    public LouCapabilityProvider(){
        super();
        /*
        CapabilityManager.INSTANCE.register(
                IFluidHandler.class,
                new LouCapabilityStorage(),
                RhizomeTE::new
        );
         */
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) { return FLUID_CAP.orEmpty(cap, instance); }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        System.out.println("testo");
        FLUID_CAP.getStorage().readNBT(
                FLUID_CAP,
                instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")),
                null,
                nbt
        );
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        System.out.println("testy");
        INBT dat = FLUID_CAP.getStorage().writeNBT(
                FLUID_CAP,
                instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")),
                null
        );
        if (dat!=null) {
            nbt.put("fluidcap", dat);
        }
        return nbt;
    }

    /*
    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("fluid_cap",
                FLUID_CAP.getStorage()
                        .writeNBT(
                                FLUID_CAP,
                                this.fluidHandlerLazyOptional
                                        .orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")),
                                null)
        );
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        FLUID_CAP.getStorage().readNBT(FLUID_CAP, this.fluidHandlerLazyOptional.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null, nbt);
    }
     */
}
