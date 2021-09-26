package com.github.volbot.technolougy.tileentity.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.IIntArray;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class AbstractRhizomaticMachineContainer extends Container {

    private final IIntArray data;
    protected final World level;
    private final IRecipeType recipeType;


    protected AbstractRhizomaticMachineContainer(ContainerType<?> containerType, IRecipeType<? extends AbstractCookingRecipe> recipeType, int containerID, PlayerInventory inventory, IItemHandler iItemHandler, IIntArray iIntArray) {
        super(containerType, containerID);
        this.data = iIntArray;
        this.level = inventory.player.level;
        this.recipeType = recipeType;
        this.addSlot(new RhizomeMachineSlot(iItemHandler, 0, 56, 35) {
            @Override
            public boolean mayPlace(@Nonnull ItemStack stack) {
                return canSmelt(stack);
            }
        });
        this.addSlot(new RhizomeMachineSlot(iItemHandler, 1, 116, 35) {
            @Override
            public boolean mayPlace(@Nonnull ItemStack stack) {
                return false;
            }
        });

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
        }

        this.addDataSlots(iIntArray);
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return true;
    }

    protected boolean canSmelt(ItemStack stack) {
        return this.level.getRecipeManager().getRecipeFor(this.recipeType, new Inventory(stack), this.level).isPresent();
    }

    protected class RhizomeMachineSlot extends SlotItemHandler {

        public RhizomeMachineSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Nonnull
        @Override
        public ItemStack remove(int amount) {
            System.out.println("REMOVAL AMOUNT: "+amount);
            return this.getItemHandler().extractItem(this.getSlotIndex(), amount, false);
        }
    }
}


