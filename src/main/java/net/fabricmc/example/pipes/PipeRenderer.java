package net.fabricmc.example.pipes;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.example.pipes.PipeBlockEntity;
import net.fabricmc.example.render.fluid.FluidRenderFace;
import net.fabricmc.example.render.fluid.FluidVolumeRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;

import java.util.EnumSet;

@Environment(EnvType.CLIENT)
public class PipeRenderer implements BlockEntityRenderer<PipeBlockEntity> {
    private static ItemStack stack = new ItemStack(Items.JUKEBOX, 1);

    public PipeRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(PipeBlockEntity blockEntity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {

        final var itemSize = 4d / 16;

        if (blockEntity.resources.isEmpty()) {
            return;
        }

        matrices.push();

        // Calculate the current offset in the y value
        //double offset = Math.sin((blockEntity.getWorld().getTime() + tickDelta) / 8.0) / 4.0;

        // Move the item
        matrices.translate(0.5, (8 - 4/2 - 1) / 16d, 0.5);

        // Rotate the item
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((blockEntity.getWorld().getTime() + tickDelta) * 4));

        MinecraftClient
                .getInstance()
                .getItemRenderer()
                .renderItem(
                        blockEntity.resources.stream().findAny().get().items,
                        ModelTransformationMode.GROUND,
                        light,
                        overlay,
                        matrices,
                        vertexConsumers,
                        blockEntity.getWorld(),
                        0);

        // Mandatory call after GL calls
        matrices.pop();
    }
}