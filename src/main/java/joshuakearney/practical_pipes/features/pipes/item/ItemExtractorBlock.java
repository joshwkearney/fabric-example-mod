package joshuakearney.practical_pipes.features.pipes.item;

import joshuakearney.practical_pipes.features.pipes.PipeBlock;
import joshuakearney.practical_pipes.features.pipes.PipeConnection;
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

import static joshuakearney.practical_pipes.PracticalPipes.EXTRACTOR_PIPE_BLOCK_ENTITY;

public class ItemExtractorBlock extends PipeBlock<ItemExtractorBlockEntity> implements ItemPipe {
    public ItemExtractorBlock() {
        super(() -> EXTRACTOR_PIPE_BLOCK_ENTITY);
    }

    @Override
    public PipeConnection getConnection(WorldAccess world, BlockPos pos, Direction dir) {
        return ItemPipe.super.getConnection(world, pos, dir);
    }
}
