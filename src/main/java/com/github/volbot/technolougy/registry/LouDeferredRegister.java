package com.github.volbot.technolougy.registry;

import com.github.volbot.technolougy.Main;
import com.github.volbot.technolougy.block.SkullGenBlock;
import com.github.volbot.technolougy.block.fluid.SolutionFluid;
import com.github.volbot.technolougy.tileentity.SkullGenTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.github.volbot.technolougy.Main.MODID;

public class LouDeferredRegister {
    //register fluids
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Main.MODID);

    public static final ResourceLocation FLUID_STILL = new ResourceLocation("minecraft:block/brown_mushroom_block");
    public static final ResourceLocation FLUID_FLOWING = new ResourceLocation("minecraft:block/mushroom_stem");

    public static RegistryObject<ForgeFlowingFluid> solutionFluid = FLUIDS.register("solution_fluid", () ->
            new ForgeFlowingFluid.Source(makeProperties())
    );
    public static RegistryObject<ForgeFlowingFluid> solutionFluidFlowing = FLUIDS.register("solution_fluid_flowing", () ->
            new ForgeFlowingFluid.Flowing(makeProperties())
    );

    //register blocks
    public static final net.minecraftforge.registries.DeferredRegister<Block> BLOCKS = net.minecraftforge.registries.DeferredRegister.create(ForgeRegistries.BLOCKS, Main.MODID);
    public static final RegistryObject<Block> skullGen = BLOCKS.register("skull_gen", () -> new SkullGenBlock(Block.Properties.of(Material.HEAVY_METAL)));

    public static RegistryObject<FlowingFluidBlock> solutionFluidBlock = BLOCKS.register("solution_fluid_block", () ->
            new FlowingFluidBlock(solutionFluid, Block.Properties.of(Material.WATER).noDrops())
    );

    //register items
    public static final net.minecraftforge.registries.DeferredRegister<Item> ITEMS = net.minecraftforge.registries.DeferredRegister.create(ForgeRegistries.ITEMS, Main.MODID);
    public static final RegistryObject<Item> skullGenItem = ITEMS.register("skull_gen",
            () -> new BlockItem( skullGen.get(),
                    new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS))
    );

    public static RegistryObject<Item> solutionFluidBucket = ITEMS.register("solution_fluid_bucket", () ->
            new BucketItem(solutionFluid, new Item.Properties().stacksTo(1).tab(ItemGroup.TAB_BUILDING_BLOCKS).craftRemainder(Items.BUCKET))
    );

    //register tile entities
    public static final net.minecraftforge.registries.DeferredRegister<TileEntityType<?>> TILE_ENTITIES = net.minecraftforge.registries.DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MODID);
    public static final RegistryObject<TileEntityType<SkullGenTileEntity>> skullGenTE = TILE_ENTITIES.register("skull_gen_te",
            () -> TileEntityType.Builder.of(SkullGenTileEntity::new, skullGen.get())
                    .build(null)
    );

    private static ForgeFlowingFluid.Properties makeProperties()
    {
        return new ForgeFlowingFluid.Properties(solutionFluid, solutionFluidFlowing,
                FluidAttributes.builder(FLUID_STILL, FLUID_FLOWING).color(0x3F1080FF))
                .bucket(solutionFluidBucket).block(solutionFluidBlock);
    }
}
