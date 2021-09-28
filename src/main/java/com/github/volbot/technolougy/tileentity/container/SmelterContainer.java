package com.github.volbot.technolougy.tileentity.container;

import com.github.volbot.technolougy.registry.LouDeferredRegister;
import com.github.volbot.technolougy.tileentity.SmelterTE;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.crafting.IRecipeType;

public class SmelterContainer extends AbstractRhizomaticMachineContainer{
    public SmelterContainer(int containerID, PlayerInventory inventory, SmelterTE smelterTE) {
        super(LouDeferredRegister.smelterContainer.get(), IRecipeType.SMELTING, containerID, inventory, smelterTE, smelterTE.getDataAccess());
    }

    public SmelterContainer(int containerID, PlayerInventory inventory){
        super(LouDeferredRegister.smelterContainer.get(), IRecipeType.SMELTING, containerID, inventory, LouDeferredRegister.smelterTE.get().create(), LouDeferredRegister.smelterTE.get().create().getDataAccess());
    }
}
