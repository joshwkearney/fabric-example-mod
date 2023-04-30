package net.fabricmc.example;

import alexiil.mc.lib.attributes.AttributeList;
import alexiil.mc.lib.attributes.AttributeProvider;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static net.fabricmc.example.ExampleMod.TANK_BLOCK;

public class ExampleMod implements ModInitializer, AttributeProvider {
	public static final String MODID = "examplemod";

	public static final Block TANK_BLOCK = new TankBlock();

	public static final BlockEntityType<TankBlockEntity> TANK_BLOCK_ENTITY = FabricBlockEntityTypeBuilder
			.create(TankBlockEntity::new, TANK_BLOCK)
			.build();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		Registry.register(Registries.BLOCK, new Identifier(MODID, "tank"), TANK_BLOCK);

		Registry.register(
				Registries.ITEM,
				new Identifier(MODID, "tank"),
				new BlockItem(TANK_BLOCK, new FabricItemSettings()));

		Registry.register(
				Registries.BLOCK_ENTITY_TYPE,
				new Identifier(MODID, "tank_block_entity"),
				TANK_BLOCK_ENTITY);
	}

	@Override
	public void addAllAttributes(World world, BlockPos pos, BlockState state, AttributeList<?> to) {
		if (world.getBlockEntity(pos) instanceof TankBlockEntity tank) {
			to.offer(tank.fluidInventory);
		}
	}
}

