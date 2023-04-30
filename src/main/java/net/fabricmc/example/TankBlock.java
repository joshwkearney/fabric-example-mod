package net.fabricmc.example;

import java.util.Collections;
import java.util.List;

import alexiil.mc.lib.attributes.AttributeList;
import alexiil.mc.lib.attributes.AttributeProvider;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TankBlock extends Block implements BlockEntityProvider, AttributeProvider {// implements BlockEntityProvider, AttributeProvider {
    //public static final VoxelShape SHAPE = VoxelShapes.fullCube();

    public TankBlock() {
        super(FabricBlockSettings.copy(Blocks.GLASS).nonOpaque());
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TankBlockEntity(pos, state);
    }

    @Override
    public void addAllAttributes(World world, BlockPos pos, BlockState state, AttributeList<?> to) {
        if (world.getBlockEntity(pos) instanceof TankBlockEntity tank) {
            to.offer(tank.fluidInventory);
        }
    }

   /* @Override
    public VoxelShape getOutlineShape(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1,
                                      ShapeContext verticalEntityPosition_1) {
        return SHAPE;
    }

    @Override
    public boolean isSideInvisible(BlockState thisState, BlockState otherState, Direction side) {
        if (otherState.getBlock() == this) {
            return true;
        }

        return false;
    }*/

    /*@Override
    public void addAllAttributes(World world, BlockPos pos, BlockState state, AttributeList<?> to) {
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof TankTile) {
            TankTile tank = (TankTile) be;
            to.offer(tank.fluidInv, SHAPE);
        }
    }*/
}
