package com.github.volbot.technolougy.events;

import com.github.volbot.technolougy.Main;
import com.github.volbot.technolougy.capabilities.LouCapabilityProvider;
import com.github.volbot.technolougy.capabilities.LouCapabilityStorage;
import com.github.volbot.technolougy.tileentity.RhizomeTE;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.FMLClientLaunchProvider;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE, modid= Main.MODID)
public class LouEventHandler {

    public static final ResourceLocation PACKET_CAPABILITY_LOCATION = new ResourceLocation(Main.MODID, "fluid_cap");

    @SubscribeEvent
    @SuppressWarnings("unchecked")
    public static void onAttachCapabilities(final AttachCapabilitiesEvent<TileEntity> e) {
        final TileEntity te = e.getObject();
        RhizomeTE rhizome = null;
        if(te instanceof RhizomeTE) {
            rhizome = ((RhizomeTE) te);
            if (!(rhizome.get()==null)) {
                e.addCapability(PACKET_CAPABILITY_LOCATION, new LouCapabilityProvider());
            }
        }
    }

}
