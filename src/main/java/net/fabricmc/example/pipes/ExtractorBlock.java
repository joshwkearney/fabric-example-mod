package net.fabricmc.example.pipes;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

public class ExtractorBlock extends PipeBlock {
    public static final DirectionProperty FACING = Properties.FACING;

    public ExtractorBlock() {
        super();

        this.setDefaultState(
                this.getStateManager()
                .getDefaultState()
                .with(Properties.FACING, Direction.NORTH));
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);

        builder.add(Properties.FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        var dir = context.getSide().getOpposite();

        return super.getPlacementState(context).with(Properties.FACING, dir);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState,
                                                WorldAccess world, BlockPos pos, BlockPos posFrom) {
        return super
                .getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom)
                .with(FACING, state.get(FACING));
    }
}
