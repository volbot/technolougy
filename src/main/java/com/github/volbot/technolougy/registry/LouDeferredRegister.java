package com.github.volbot.technolougy.registry;

import com.github.volbot.technolougy.Main;
import com.github.volbot.technolougy.block.ConnectionBlock;
import com.github.volbot.technolougy.block.FurnaceBlock;
import com.github.volbot.technolougy.block.PointBlock;
import com.github.volbot.technolougy.block.fluid.RhizomeFuelFluid;
import com.github.volbot.technolougy.tileentity.FurnaceTE;
import com.github.volbot.technolougy.tileentity.RhizomeProxyTE;
import com.github.volbot.technolougy.tileentity.RhizomeTE;
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

    public static final ResourceLocation FLUID_STILL = new ResourceLocation("minecraft:block/water_still");
    public static final ResourceLocation FLUID_FLOWING = new ResourceLocation("minecraft:block/water_flow");

    public static RegistryObject<ForgeFlowingFluid> sugarWaterFluid = FLUIDS.register("sugar_water", () ->
            new RhizomeFuelFluid.Source(makeFluidProperties())
    );
    public static RegistryObject<ForgeFlowingFluid> sugarWaterFluidFlowing = FLUIDS.register("sugar_water_flowing", () ->
            new RhizomeFuelFluid.Flowing(makeFluidProperties())
    );

    //register blocks
    public static final net.minecraftforge.registries.DeferredRegister<Block> BLOCKS = net.minecraftforge.registries.DeferredRegister.create(ForgeRegistries.BLOCKS, Main.MODID);
    public static final RegistryObject<Block> pointGen = BLOCKS.register("point_gen", () -> new PointBlock(Block.Properties.of(Material.HEAVY_METAL)));
    public static final RegistryObject<Block> connectionBlock = BLOCKS.register("connection_block", () -> new ConnectionBlock(Block.Properties.of(Material.HEAVY_METAL)));
    public static final RegistryObject<Block> furnaceBlock = BLOCKS.register("furnace_block", () -> new FurnaceBlock(Block.Properties.of(Material.HEAVY_METAL)));


    public static RegistryObject<FlowingFluidBlock> sugarWaterFluidBlock = BLOCKS.register("sugar_water_fluid_block", () ->
            new FlowingFluidBlock(sugarWaterFluid, Block.Properties.of(Material.WATER).noDrops())
    );

    //register items
    public static final net.minecraftforge.registries.DeferredRegister<Item> ITEMS = net.minecraftforge.registries.DeferredRegister.create(ForgeRegistries.ITEMS, Main.MODID);
    public static final RegistryObject<Item> pointBlockItem = ITEMS.register("point_block",
            () -> new BlockItem( pointGen.get(),
                    new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS))
    );

    public static final RegistryObject<Item> connectionBlockItem = ITEMS.register("connection_block",
            () -> new BlockItem( connectionBlock.get(),
                    new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS))
    );

    public static final RegistryObject<Item> furnaceBlockItem = ITEMS.register("furnace_block",
            () -> new BlockItem( furnaceBlock.get(),
                    new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS))
    );

    public static RegistryObject<Item> sugarWaterFluidBucket = ITEMS.register("sugar_water_bucket", () ->
            new BucketItem(sugarWaterFluid, new Item.Properties().stacksTo(1).tab(ItemGroup.TAB_BUILDING_BLOCKS).craftRemainder(Items.BUCKET))
    );

    //register tile entities

    public static final net.minecraftforge.registries.DeferredRegister<TileEntityType<?>> TILE_ENTITIES = net.minecraftforge.registries.DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MODID);

    public static final RegistryObject<TileEntityType<RhizomeTE>> rhizomeTE = TILE_ENTITIES.register("rhizome_te",
            () -> TileEntityType.Builder.of(RhizomeTE::new, pointGen.get())
                    .build(null)
    );

    public static final RegistryObject<TileEntityType<RhizomeProxyTE>> rhizomeProxyTE = TILE_ENTITIES.register("rhizome_proxy_te",
            () -> TileEntityType.Builder.of(RhizomeProxyTE::new, connectionBlock.get())
                    .build(null)
    );

    public static final RegistryObject<TileEntityType<FurnaceTE>> furnaceTE = TILE_ENTITIES.register("furnace_te",
            () -> TileEntityType.Builder.of(FurnaceTE::new, furnaceBlock.get())
                    .build(null)
    );

    /*
        Method to initialize the game data of the Solution fluid.
     */
    private static ForgeFlowingFluid.Properties makeFluidProperties()
    {
        return new ForgeFlowingFluid.Properties(sugarWaterFluid, sugarWaterFluidFlowing,
                FluidAttributes.builder(FLUID_STILL, FLUID_FLOWING).color(0xFF4B92DB))
                .bucket(sugarWaterFluidBucket).block(sugarWaterFluidBlock);
    }
}
