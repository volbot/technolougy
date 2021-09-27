package com.github.volbot.technolougy.tileentity.container;

import com.github.volbot.technolougy.block.fluid.RhizomeFuelFluid;
import com.github.volbot.technolougy.registry.LouDeferredRegister;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.IIntArray;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
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
        this.addSlot(new RhizomeMachineSlot(iItemHandler, 2, 20, 50) {
            @Override
            public boolean mayPlace(@Nonnull ItemStack stack) {
                if(stack.getItem() instanceof BucketItem){
                    System.out.println("ITEM BUCKET");
                    BucketItem item = (BucketItem)stack.getItem();
                    System.out.println(item.getFluid());
                    if(item.getFluid().isSame(LouDeferredRegister.sugarWaterFluid.get())){
                        System.out.println("BUCKET IS RHIZOME FLUID");
                        return true;
                    } else if(item.getFluid().isSame(FluidStack.EMPTY.getFluid())){
                        System.out.println("BUCKET EMPTY");
                        return true;
                    }
                }
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
            return this.getItemHandler().extractItem(this.getSlotIndex(), amount, false);
        }
    }
}


