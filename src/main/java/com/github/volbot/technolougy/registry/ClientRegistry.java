package com.github.volbot.technolougy.registry;

import com.github.volbot.technolougy.tileentity.container.SmelterContainerScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientRegistry {
    @SubscribeEvent
    public static void onClientSetupEvent(FMLClientSetupEvent event) {
        System.out.println("reggie");
        ScreenManager.register(LouDeferredRegister.smelterContainer.get(), SmelterContainerScreen::new);
    }
}
