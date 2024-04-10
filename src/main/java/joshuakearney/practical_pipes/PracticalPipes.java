package joshuakearney.practical_pipes;

import joshuakearney.practical_pipes.features.pipes.itemPipes.ItemExtractorBlock;
import joshuakearney.practical_pipes.features.pipes.itemPipes.ItemPipeBlock;
import joshuakearney.practical_pipes.features.tank.TankBlock;
import joshuakearney.practical_pipes.features.tank.TankBlockEntity;
import net.fabricmc.api.ModInitializer;
import joshuakearney.practical_pipes.features.pipes.itemPipes.ItemExtractorBlockEntity;
import joshuakearney.practical_pipes.features.pipes.itemPipes.ItemPipeBlockEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class PracticalPipes implements ModInitializer {
	public static final String MODID = "practical_pipes";

	public static final Block TANK_BLOCK = new TankBlock();

	public static final Block ITEM_PIPE_BLOCK = new ItemPipeBlock();

	public static final Block ITEM_EXTRACTOR = new ItemExtractorBlock();

	public static final BlockEntityType<TankBlockEntity> TANK_BLOCK_ENTITY = FabricBlockEntityTypeBuilder
			.create(TankBlockEntity::new, TANK_BLOCK)
			.build();

	public static final BlockEntityType<ItemPipeBlockEntity> ITEM_PIPE_BLOCK_ENTITY = FabricBlockEntityTypeBuilder
			.create(ItemPipeBlockEntity::new, ITEM_PIPE_BLOCK)
			.build();

	public static final BlockEntityType<ItemExtractorBlockEntity> ITEM_EXTRACTOR_BLOCK_ENTITY_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder
			.create(ItemExtractorBlockEntity::new, ITEM_EXTRACTOR)
			.build();

	public static final ItemGroup CREATIVE_TAB = FabricItemGroup
			.builder(new Identifier(MODID, "main_tab"))
			.icon(() -> new ItemStack(ITEM_PIPE_BLOCK))
			.build();

	@Override
	public void onInitialize() {
		Registry.register(Registries.BLOCK, new Identifier(MODID, "tank"), TANK_BLOCK);
		Registry.register(Registries.BLOCK, new Identifier(MODID, "item_pipe"), ITEM_PIPE_BLOCK);
		Registry.register(Registries.BLOCK, new Identifier(MODID, "item_extractor"), ITEM_EXTRACTOR);

		Registry.register(
				Registries.ITEM,
				new Identifier(MODID, "tank"),
				new BlockItem(TANK_BLOCK, new FabricItemSettings()));

		Registry.register(
				Registries.ITEM,
				new Identifier(MODID, "item_pipe"),
				new BlockItem(ITEM_PIPE_BLOCK, new FabricItemSettings()));

		Registry.register(
				Registries.ITEM,
				new Identifier(MODID, "item_extractor"),
				new BlockItem(ITEM_EXTRACTOR, new FabricItemSettings()));

		Registry.register(
				Registries.BLOCK_ENTITY_TYPE,
				new Identifier(MODID, "tank_block_entity"),
				TANK_BLOCK_ENTITY);

		Registry.register(
				Registries.BLOCK_ENTITY_TYPE,
				new Identifier(MODID, "pipe_block_entity"),
				ITEM_PIPE_BLOCK_ENTITY);

		Registry.register(
				Registries.BLOCK_ENTITY_TYPE,
				new Identifier(MODID, "extractor_pipe_block_entity"),
				ITEM_EXTRACTOR_BLOCK_ENTITY_BLOCK_ENTITY_TYPE);

		ItemGroupEvents.modifyEntriesEvent(CREATIVE_TAB).register(entries -> entries.add(TANK_BLOCK));
		ItemGroupEvents.modifyEntriesEvent(CREATIVE_TAB).register(entries -> entries.add(ITEM_PIPE_BLOCK));
		ItemGroupEvents.modifyEntriesEvent(CREATIVE_TAB).register(entries -> entries.add(ITEM_EXTRACTOR));

		FluidStorage.SIDED.registerForBlockEntity((myTank, direction) -> myTank.fluid, TANK_BLOCK_ENTITY);
	}
}

