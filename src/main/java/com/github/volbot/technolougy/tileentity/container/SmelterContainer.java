package com.github.volbot.technolougy.tileentity.container;

import com.github.volbot.technolougy.registry.LouDeferredRegister;
import com.github.volbot.technolougy.tileentity.SmelterTE;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeBookCategory;
import net.minecraft.util.IIntArray;
import net.minecraftforge.common.extensions.IForgeContainerType;

public class SmelterContainer extends AbstractRhizomaticMachineContainer{
    public SmelterContainer(int containerID, PlayerInventory inventory, SmelterTE smelterTE, IIntArray dataAccess) {
        super(LouDeferredRegister.smelterContainer.get(), IRecipeType.SMELTING,  containerID, inventory, smelterTE, dataAccess);
    }

    public SmelterContainer(int containerID, PlayerInventory inventory){
        super(LouDeferredRegister.smelterContainer.get(), IRecipeType.SMELTING, containerID, inventory);
    }
}
