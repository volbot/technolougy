package com.github.volbot.technolougy;

import com.github.volbot.technolougy.registry.ClientRegistry;
import com.github.volbot.technolougy.registry.LouDeferredRegister;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("technolougy")
public class Main {
    public static final String MODID = "technolougy";

    public Main() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        LouDeferredRegister.FLUIDS.register(FMLJavaModLoadingContext.get().getModEventBus());
        LouDeferredRegister.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        LouDeferredRegister.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        LouDeferredRegister.TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        LouDeferredRegister.CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) { }

    private void doClientStuff(final FMLClientSetupEvent event) {
        ClientRegistry.onClientSetupEvent(event);
    }

}
