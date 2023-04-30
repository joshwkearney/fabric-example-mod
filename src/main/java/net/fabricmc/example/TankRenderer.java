package net.fabricmc.example;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.example.render.FluidRenderFace;
import net.fabricmc.example.render.FluidVolumeRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@Environment(EnvType.CLIENT)
public class TankRenderer implements BlockEntityRenderer<TankBlockEntity> {
    private static ItemStack stack = new ItemStack(Items.JUKEBOX, 1);

    public TankRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(TankBlockEntity blockEntity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {


        if (blockEntity.fluid.amount == 0) {
            return;
        }

        matrices.push();

        List<FluidRenderFace> faces = new ArrayList<>();

        double epsilon = 0.001;

        double x0 = 0 / 16d + epsilon;
        double x1 = 16 / 16d - epsilon;

        double y0 = epsilon;
        double y1 = blockEntity.fluid.amount * 1d / blockEntity.fluid.getCapacity();

        double z0 = 0 / 16d + epsilon;
        double z1 = 16 / 16d - epsilon;

        var sides = EnumSet.allOf(Direction.class);
        FluidRenderFace.appendCuboid(x0, y0, z0, x1, y1, z1, 1, sides, faces);

        for (FluidRenderFace face : faces) {
            face.light = light;
        }

        FluidVolumeRenderer.INSTANCE.render(blockEntity.fluid.variant, faces, vertexConsumers, matrices);
        matrices.pop();
    }
}