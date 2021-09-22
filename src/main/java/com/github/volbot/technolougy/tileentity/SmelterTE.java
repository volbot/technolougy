package com.github.volbot.technolougy.tileentity;

import com.github.volbot.technolougy.registry.LouDeferredRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.TileEntityType;


public class SmelterTE extends AbstractRhizomaticMachineTE {

    public SmelterTE(TileEntityType type){
        super(type);
        init();
    }

    public SmelterTE() {
        super(LouDeferredRegister.smelterTE.get());
        init();
    }

    @Override
    protected void init() {
        super.init();
        this.fuelTankLimit=5000;
        this.recipeType = IRecipeType.SMELTING;
        this.maxStackSize=64;
        this.cookingTotalTime=15;
        debugint=-1;
    }

    private int debugint;

    @Override
    public void tick() {
        super.tick();
        if(debugint==-1){
            this.items.set(0,new ItemStack(Items.IRON_ORE,64));
            debugint=0;
        }
    }
}
