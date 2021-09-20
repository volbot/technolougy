package com.github.volbot.technolougy.tileentity;

import com.github.volbot.technolougy.registry.LouDeferredRegister;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class SmelterTE extends RhizomeTE implements IItemHandler, IRecipeHolder, IRecipeHelperPopulator, ITickableTileEntity {
    protected NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);

    private int cookingProgress;
    private int cookingTotalTime;

    @CapabilityInject(IFluidHandler.class)
    public static Capability<IFluidHandler> FLUID_CAP = null;

    private LazyOptional<IFluidHandler> fluid_instance = LazyOptional.of(FLUID_CAP::getDefaultInstance);

    @CapabilityInject(IItemHandler.class)
    public static Capability<IItemHandler> ITEM_CAP = null;

    private LazyOptional<IItemHandler> item_instance = LazyOptional.of(ITEM_CAP::getDefaultInstance);

    public SmelterTE() {
        super(LouDeferredRegister.smelterTE.get());
    }

    private int debugint = 0;

    @Override
    public void tick() {
        super.tick();
        if(getLevel().isClientSide()) {
            {
                if (debugint == 15) {
                    insertItem(1, Items.ACACIA_LOG.getDefaultInstance(), false);
                    insertItem(0, ItemStack.EMPTY, false);
                } else if (debugint >= 30) {
                    insertItem(0, Items.ACACIA_LOG.getDefaultInstance(), false);
                    insertItem(1, ItemStack.EMPTY, false);
                    System.out.println(getStackInSlot(0) + " " + getStackInSlot(1));
                    debugint = 0;
                } else if (debugint == 10 || debugint == 20) {
                    System.out.println(getStackInSlot(0) + " " + getStackInSlot(1));
                }
                debugint++;
            }
        }
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int i) {
        return this.items.get(i);
    }

    @Override
    public void fillStackedContents(RecipeItemHelper recipeItemHelper) {
        for(ItemStack itemstack : this.items) {
            recipeItemHelper.accountStack(itemstack);
        }
    }

    private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();

    @Override
    public void setRecipeUsed(@Nullable IRecipe<?> recipe) {
        if (recipe != null) {
            ResourceLocation resourcelocation = recipe.getId();
            this.recipesUsed.addTo(resourcelocation, 1);
        }
    }

    @Nullable
    @Override
    public IRecipe<?> getRecipeUsed() {
        return null;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap instanceof IFluidHandler){
            return fluid_instance.cast();
        } else if(cap instanceof IInventory){
            return item_instance.cast();
        }
        return null;
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        fluid_instance.invalidate();
        item_instance.invalidate();
    }

    @Override
    public int getSlots() {
        return this.items.size();
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        //MAKE BETTER
        return this.items.set(slot,stack);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        //MAKE BETTER
        return this.items.remove(slot);
    }

    @Override
    public int getSlotLimit(int slot) {
        return 0;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return false;
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        nbt.putInt("CookTime", this.cookingProgress);
        nbt.putInt("CookTimeTotal", this.cookingTotalTime);
        ItemStackHelper.saveAllItems(nbt, this.items);
        CompoundNBT compoundnbt = new CompoundNBT();
        this.recipesUsed.forEach((p_235643_1_, p_235643_2_) -> {
            compoundnbt.putInt(p_235643_1_.toString(), p_235643_2_);
        });
        nbt.put("RecipesUsed", compoundnbt);
        return nbt;
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        deserializeNBT(nbt);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        this.items = NonNullList.withSize(2, ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, this.items);
        this.cookingProgress = nbt.getInt("CookTime");
        this.cookingTotalTime = nbt.getInt("CookTimeTotal");
        CompoundNBT compoundnbt = nbt.getCompound("RecipesUsed");
        for(String s : compoundnbt.getAllKeys()) {
            this.recipesUsed.put(new ResourceLocation(s), compoundnbt.getInt(s));
        }
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        nbt.putInt("CookTime", this.cookingProgress);
        nbt.putInt("CookTimeTotal", this.cookingTotalTime);
        ItemStackHelper.saveAllItems(nbt, this.items);
        CompoundNBT compoundnbt = new CompoundNBT();
        this.recipesUsed.forEach((p_235643_1_, p_235643_2_) -> {
            compoundnbt.putInt(p_235643_1_.toString(), p_235643_2_);
        });
        nbt.put("RecipesUsed", compoundnbt);
        return nbt;
    }
}
