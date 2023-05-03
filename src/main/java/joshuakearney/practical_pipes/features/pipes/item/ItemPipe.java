package joshuakearney.practical_pipes.features.pipes.item;

import joshuakearney.practical_pipes.features.pipes.PipeConnection;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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