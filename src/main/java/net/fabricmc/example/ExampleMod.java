package net.fabricmc.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.example.pipes.ExtractorBlock;
import net.fabricmc.example.pipes.PipeBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ExampleMod implements ModInitializer {
	public static final String MODID = "examplemod";

	public static final Block TANK_BLOCK = new TankBlock();

	public static final Block PIPE_BLOCK = new PipeBlock();

	public static final Block EXTRACTOR_PIPE_BLOCK = new ExtractorBlock();

	public static final BlockEntityType<TankBlockEntity> TANK_BLOCK_ENTITY = FabricBlockEntityTypeBuilder
			.create(TankBlockEntity::new, TANK_BLOCK)
			.build();

	public static final BlockEntityType<TankBlockEntity> PIPE_BLOCK_ENTITY = FabricBlockEntityTypeBuilder
			.create(TankBlockEntity::new, PIPE_BLOCK)
			.build();

	@Override
	public void onInitialize() {
		Registry.register(Registries.BLOCK, new Identifier(MODID, "tank"), TANK_BLOCK);
		Registry.register(Registries.BLOCK, new Identifier(MODID, "pipe"), PIPE_BLOCK);
		Registry.register(Registries.BLOCK, new Identifier(MODID, "extractor_pipe"), EXTRACTOR_PIPE_BLOCK);

		Registry.register(
				Registries.ITEM,
				new Identifier(MODID, "tank"),
				new BlockItem(TANK_BLOCK, new FabricItemSettings()));

		Registry.register(
				Registries.ITEM,
				new Identifier(MODID, "pipe"),
				new BlockItem(PIPE_BLOCK, new FabricItemSettings()));

		Registry.register(
				Registries.ITEM,
				new Identifier(MODID, "extractor_pipe"),
				new BlockItem(EXTRACTOR_PIPE_BLOCK, new FabricItemSettings()));

		Registry.register(
				Registries.BLOCK_ENTITY_TYPE,
				new Identifier(MODID, "tank_block_entity"),
				TANK_BLOCK_ENTITY);

		Registry.register(
				Registries.BLOCK_ENTITY_TYPE,
				new Identifier(MODID, "pipe_block_entity"),
				PIPE_BLOCK_ENTITY);

		FluidStorage.SIDED.registerForBlockEntity((myTank, direction) -> myTank.fluid, TANK_BLOCK_ENTITY);
	}
}

