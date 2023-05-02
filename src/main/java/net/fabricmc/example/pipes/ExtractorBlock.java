package net.fabricmc.example.pipes;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import static net.fabricmc.example.ExampleMod.EXTRACTOR_PIPE_BLOCK_ENTITY;

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

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ExtractorPipeBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return this.checkType(type, EXTRACTOR_PIPE_BLOCK_ENTITY, (world2, pos, state2, entity) -> {
            if (world.isClient) {
                entity.tickClient();
            }
            else {
                entity.tickServer();
            }
        });
    }
}
