package com.github.volbot.technolougy.events;

import com.github.volbot.technolougy.Main;
import com.github.volbot.technolougy.capabilities.LouCapabilityProvider;
import com.github.volbot.technolougy.tileentity.RhizomeTE;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE, modid= Main.MODID)
public class LouEventHandler {

    public static final ResourceLocation PACKET_CAPABILITY_LOCATION = new ResourceLocation(Main.MODID, "fluid_cap");

    @SubscribeEvent
    @SuppressWarnings("unchecked")
    public static void onAttachCapabilities(final AttachCapabilitiesEvent<TileEntity> e) {
        final TileEntity te = e.getObject();
        if (e.getCapabilities().containsKey(PACKET_CAPABILITY_LOCATION)) {
            return;
        }
        if(te instanceof RhizomeTE) {
            e.addCapability(PACKET_CAPABILITY_LOCATION, new LouCapabilityProvider());
        }
    }

}
