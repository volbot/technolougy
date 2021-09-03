package com.github.volbot.technolougy.events;

import com.github.volbot.technolougy.Main;
import com.github.volbot.technolougy.capabilities.LouCapabilityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE, modid= Main.MODID)
public class LouEventHandler {

    @SubscribeEvent
    public static void onAttachCapabilities(final AttachCapabilitiesEvent<TileEntity> e) {
        final TileEntity te = e.getObject();
        if (!(LouCapabilityProvider.get()==null)) {
            e.addCapability(new ResourceLocation("rhizome_te", "fluid"), new LouCapabilityProvider());
        }
    }

}
