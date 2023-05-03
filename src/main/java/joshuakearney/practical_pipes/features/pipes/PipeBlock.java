package joshuakearney.practical_pipes.features.pipes;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public abstract class PipeBlock<T extends PipeBlockEntity> extends BlockWithEntity implements BlockEntityProvider {
    public static final PipeConnectionProperty NORTH = new PipeConnectionProperty("north");
    public static final PipeConnectionProperty SOUTH = new PipeConnectionProperty("south");
    public static final PipeConnectionProperty EAST = new PipeConnectionProperty("east");
    public static final PipeConnectionProperty WEST = new PipeConnectionProperty("west");
    public static final PipeConnectionProperty UP = new PipeConnectionProperty("up");
    public static final PipeConnectionProperty DOWN = new PipeConnectionProperty("down");

    public static final Map<Direction, PipeConnectionProperty> PROPERTY_MAP = Util.make(new HashMap<>(),
            map -> {
                map.put(Direction.NORTH, NORTH);
                map.put(Direction.SOUTH, SOUTH);
                map.put(Direction.EAST, EAST);
                map.put(Direction.WEST, WEST);
                map.put(Direction.UP, UP);
                map.put(Direction.DOWN, DOWN);
            }
    );

    private Supplier<BlockEntityType<T>> entityTypeSupplier;

    public PipeBlock(Supplier<BlockEntityType<T>> entityType) {
        super(FabricBlockSettings
                .of(Material.GLASS)
                .strength(0.3F, 0.3F)
                .sounds(BlockSoundGroup.GLASS)
                .nonOpaque());

        this.entityTypeSupplier = entityType;

        this.setDefaultState(this.getStateManager()
                .getDefaultState()
                .with(NORTH, PipeConnection.None)
                .with(EAST, PipeConnection.None)
                .with(SOUTH, PipeConnection.None)
                .with(WEST, PipeConnection.None)
                .with(UP, PipeConnection.None)
                .with(DOWN, PipeConnection.None));
    }

    public abstract PipeConnection getConnection(WorldAccess world, BlockPos pos, Direction dir);

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState,
                                                WorldAccess world, BlockPos pos, BlockPos posFrom) {
        var connection = this.getConnection(world, pos, direction);

        return state.with(PROPERTY_MAP.get(direction), connection);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        var state = this.getDefaultState();

        for (var dir : Direction.values()) {
            state = state.with(
                    PROPERTY_MAP.get(dir),
                    this.getConnection(context.getWorld(), context.getBlockPos(), dir));
        }

        return state;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        // TODO: Redo this
        return PipeShapeUtil.getShape(state);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return this.entityTypeSupplier.get().instantiate(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return this.checkType(type, this.entityTypeSupplier.get(), (world2, pos, state2, entity) -> {
            if (world.isClient) {
                entity.tickClient();
            }
            else {
                entity.tickServer();
            }
        });
    }
}
