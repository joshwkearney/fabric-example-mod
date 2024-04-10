package joshuakearney.practical_pipes.features.pipes.itemPipes;

import joshuakearney.practical_pipes.features.pipes.PipeConnection;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

public interface ItemPipe {
    default PipeConnection getConnection(WorldAccess world, BlockPos pos, Direction dir) {
        pos = pos.offset(dir);

        var block = world.getBlockState(pos).getBlock();
        var entity = world.getBlockEntity(pos);

        if (block instanceof ItemPipe) {
            return PipeConnection.Pipe;
        }
        else if (entity != null && entity instanceof Inventory) {
            return PipeConnection.External;
        }
        else {
            return PipeConnection.None;
        }
    }
}