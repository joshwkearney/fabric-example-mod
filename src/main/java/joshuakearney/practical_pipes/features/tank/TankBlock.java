package joshuakearney.practical_pipes.features.tank;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorageUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TankBlock extends Block implements BlockEntityProvider {
    public TankBlock() {
        super(FabricBlockSettings.copy(Blocks.GLASS).nonOpaque());
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TankBlockEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
                              BlockHitResult hit) {

        if (player.world.isClient) {
            return ActionResult.SUCCESS;
        }

        if (!(world.getBlockEntity(pos) instanceof TankBlockEntity tank)) {
            return ActionResult.PASS;
        }

        if (FluidStorageUtil.interactWithFluidStorage(tank.fluid, player, hand)) {
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }
}
