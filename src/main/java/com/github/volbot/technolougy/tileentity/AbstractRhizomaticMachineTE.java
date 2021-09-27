package com.github.volbot.technolougy.tileentity;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.*;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractRhizomaticMachineTE extends AbstractRhizomaticTankTE implements IItemHandlerModifiable, IRecipeHolder, IRecipeHelperPopulator, INamedContainerProvider {

    public AbstractRhizomaticMachineTE(TileEntityType type) {
        super(type);
        init();
    }

    protected NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);

    protected int cookingProgress;
    protected int cookingTotalTime;
    protected int maxStackSize;

    protected IRecipeType<? extends AbstractCookingRecipe> recipeType = null;

    @CapabilityInject(IItemHandler.class)
    public static Capability<IItemHandler> ITEM_CAP = null;

    private LazyOptional<IItemHandler> item_instance = LazyOptional.of(ITEM_CAP::getDefaultInstance);

    protected final IIntArray dataAccess = new IIntArray() {
        public int get(int i) {
            switch (i) {
                case 0:
                    return AbstractRhizomaticMachineTE.this.cookingProgress;
                case 1:
                    return AbstractRhizomaticMachineTE.this.cookingTotalTime;
                default:
                    return 0;
            }
        }

        public void set(int i, int val) {
            switch (i) {
                case 0:
                    AbstractRhizomaticMachineTE.this.cookingProgress = val;
                case 1:
                    AbstractRhizomaticMachineTE.this.cookingTotalTime = val;
            }
        }

        public int getCount() {
            return 4;
        }
    };

    @Override
    protected void init() {
        super.init();
        this.cookingProgress = 0;
    }

    public IIntArray getDataAccess() {
        return dataAccess;
    }

    @Override
    public void tick() {
        super.tick();
        if (!getLevel().isClientSide()) { //Running on server
            ItemStack fluidBucket = getStackInSlot(2);
            if(!fluidBucket.isEmpty()){
                setStackInSlot(2,Items.BUCKET.getDefaultInstance());
                this.fill(new FluidStack(((BucketItem)fluidBucket.getItem()).getFluid(),1000),FluidAction.EXECUTE);
            }
            ItemStack input = getStackInSlot(0);
            if (!input.isEmpty()) { //Machine can run
                IRecipe<?> recipe = getLevel().getRecipeManager().getRecipeFor(this.recipeType, new Inventory(input), this.level).orElse(null);
                if (canBurn(recipe) && drain(10, FluidAction.SIMULATE).getAmount() == 10) { //Recipe is valid
                    cookingProgress++;
                    if (this.cookingProgress == this.cookingTotalTime) { //Done cooking
                        this.cookingProgress = 0;
                        drain(10, FluidAction.EXECUTE);
                        extractItem(0, 1, false);
                        this.setRecipeUsed(recipe);
                        insertItem(1, new ItemStack(recipe.getResultItem().getItem(), 1), false);
                    }
                }
            } else {
                this.cookingProgress = 0;
            }
        }
    }

    protected boolean canBurn(@Nullable IRecipe<?> irecipe) {
        if (!this.items.get(0).isEmpty() && irecipe != null) {
            ItemStack itemstack = irecipe.getResultItem();
            if (itemstack.isEmpty()) {
                return false;
            }
            ItemStack itemstack1 = this.items.get(1);
            if (itemstack1.isEmpty()) {
                return true;
            } else if (!itemstack1.sameItem(itemstack)) {
                return false;
            } else if (itemstack1.getCount() + itemstack.getCount() <= this.getMaxStackSize() && itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize()) {
                return true;
            } else {
                return itemstack1.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize();
            }
        } else {
            return false;
        }
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int i) {
        return this.items.get(i);
    }

    public int getMaxStackSize() {
        return maxStackSize;
    }

    @Override
    public void fillStackedContents(RecipeItemHelper recipeItemHelper) {
        for (ItemStack itemstack : this.items) {
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
        if (cap instanceof IItemHandler) {
            /*
            if(side==Direction.UP){
                return item_instance.cast();
            } else {
                return item_instance.cast();
            }

             */
            return item_instance.cast();
        } else {
            return super.getCapability(cap, side);
        }
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        item_instance.invalidate();
    }

    @Override
    public int getSlots() {
        return this.items.size();
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        ItemStack curr = getStackInSlot(slot);
        if (stack.isEmpty()) {
            if (!simulate) {
                this.items.set(slot, stack);
            }
            return curr;
        }
        if (!curr.isEmpty() && curr.getCount() != this.maxStackSize && curr.getItem().equals(stack.getItem())) {
            if (this.maxStackSize - curr.getCount() > stack.getCount()) {
                stack.setCount(curr.getCount() + stack.getCount());
            } else {
                stack.setCount(this.maxStackSize);
            }
        }
        if (!simulate) {
            this.items.set(slot, stack);
        }
        return curr;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack stack = getStackInSlot(slot);
        ItemStack returnStack = ItemStack.EMPTY;
        if(!stack.isEmpty()) {
            returnStack = stack;
            if(!simulate){
                if (stack.getCount() <= amount) {
                    stack = ItemStack.EMPTY;
                } else {
                    stack.setCount(stack.getCount() - amount);
                }
                this.items.set(slot, stack);
            }
        }
        return returnStack;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        return serializeNBT();
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
        for (String s : compoundnbt.getAllKeys()) {
            this.recipesUsed.put(new ResourceLocation(s), compoundnbt.getInt(s));
        }
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        serializeNBT(nbt);
        return nbt;
    }

    public CompoundNBT serializeNBT(CompoundNBT nbt) {
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

    @Nullable
    @Override
    public abstract Container createMenu(int containerID, PlayerInventory inventory, PlayerEntity player);

    @Override
    public abstract ITextComponent getDisplayName();

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        this.items.set(slot, stack);
    }
}