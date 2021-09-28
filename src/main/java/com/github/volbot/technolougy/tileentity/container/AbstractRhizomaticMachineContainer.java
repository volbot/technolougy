package com.github.volbot.technolougy.tileentity.container;

import com.github.volbot.technolougy.registry.LouDeferredRegister;
import com.ibm.icu.text.AlphabeticIndex;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.*;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.IIntArray;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class AbstractRhizomaticMachineContainer extends Container {

    private final RhizomaticMachineStateData data;
    protected final World level;
    private final IRecipeType recipeType;
    private int max = -1;

    protected AbstractRhizomaticMachineContainer(ContainerType<?> containerType, IRecipeType<? extends AbstractCookingRecipe> recipeType, int containerID, PlayerInventory inventory, IItemHandler iItemHandler, IIntArray iIntArray) {
        super(containerType, containerID);
        this.data = (RhizomaticMachineStateData) iIntArray;
        this.level = inventory.player.level;
        this.recipeType = recipeType;
        this.addSlot(new RhizomeMachineSlot(iItemHandler, 0, 61, 35) {
            @Override
            public boolean mayPlace(@Nonnull ItemStack stack) {
                return canSmelt(stack);
            }
        });
        this.addSlot(new RhizomeMachineSlot(iItemHandler, 1, 117, 35) {
            @Override
            public boolean mayPlace(@Nonnull ItemStack stack) {
                return false;
            }
        });
        this.addSlot(new RhizomeMachineSlot(iItemHandler, 2, 11, 53) {
            @Override
            public boolean mayPlace(@Nonnull ItemStack stack) {
                return isFuel(stack);
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

        this.addDataSlots(data);
    }

    @OnlyIn(Dist.CLIENT)
    public int getBurnProgress() {
        int prog = this.data.get(0);
        if(max ==-1){
            max = this.data.get(1);
        }
        if(max !=0 && prog!=0){
            System.out.println("PROG: "+prog+" | GOAL: "+ max +" | CALC: "+(prog*24)/ max);
            return (prog*24)/ max;
        } else {
            return 0;
        }
        //return goal != 0 && prog != 0 ? prog * 24 / goal : 0;
    }

    public ItemStack quickMoveStack(PlayerEntity player, int islot) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(islot);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if(islot==0||islot==2){
                if (!this.moveItemStackTo(itemstack1, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (islot!=1){
                if (this.canSmelt(itemstack1)) {
                    if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.isFuel(itemstack1)) {
                    if (!this.moveItemStackTo(itemstack1, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                if (islot < 30) {
                    if (!this.moveItemStackTo(itemstack1, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (islot < 39 && !this.moveItemStackTo(itemstack1, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 3, 39, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, itemstack1);
        }
        return itemstack;
    }

    /*
    public ItemStack quickMoveStack(PlayerEntity player, int islot) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(islot);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (islot == 2) {
                if (!this.moveItemStackTo(itemstack1, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (islot != 1 && islot != 0) {
                if (this.canSmelt(itemstack1)) {
                    if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.isFuel(itemstack1)) {
                    if (!this.moveItemStackTo(itemstack1, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (islot >= 3 && islot < 30) {
                    if (!this.moveItemStackTo(itemstack1, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (islot >= 30 && islot < 39 && !this.moveItemStackTo(itemstack1, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 3, 39, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

     */

    @OnlyIn(Dist.CLIENT)
    public int getFluidQuantity() {
        int curr = this.data.get(2);
        if(max ==-1){
            max = this.data.get(3);
        }
        if(max !=0 && curr!=0){
            return (curr*24)/ max;
        } else {
            return 0;
        }
        //return goal != 0 && prog != 0 ? prog * 24 / goal : 0;
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return true;
    }

    protected boolean canSmelt(ItemStack stack) {
        return this.level.getRecipeManager().getRecipeFor(this.recipeType, new Inventory(stack), this.level).isPresent();
    }

    protected boolean isFuel(ItemStack stack) {
        Item item = stack.getItem();
        if(item instanceof BucketItem) {
            BucketItem bucket = (BucketItem) item;
            if (bucket.getFluid().isSame(LouDeferredRegister.sugarWaterFluid.get())) {
                return true;
            }
        }
        return false;
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


