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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class PipeRenderer implements BlockEntityRenderer<PipeBlockEntity> {
    private static ItemStack stack = new ItemStack(Items.JUKEBOX, 1);

    private static Map<Integer, Vec3d> resourceRenderLocations = new HashMap<>();


    public PipeRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(PipeBlockEntity blockEntity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {

        final var itemSize = 4d / 16;

        if (!blockEntity.hasResources()) {
            return;
        }

        matrices.push();

        // Calculate the current offset in the y value
        //double offset = Math.sin((blockEntity.getWorld().getTime() + tickDelta) / 8.0) / 4.0;

        matrices.translate(0.5, 0.5, 0.5);

        for (var resource : blockEntity.getResources()) {
            matrices.push();

            Vec3d relativePos = null;

            if (resource.ticksLeftInPipe >= 10) {
                var dirVec = resource.fromDirection.getVector();
                relativePos = new Vec3d(dirVec.getX(), dirVec.getY(), dirVec.getZ());

                //pos = pos.multiply(0.5);
                relativePos = relativePos.multiply(
                        relativePos.length() * (resource.ticksLeftInPipe - tickDelta - 10) / 10);

                // pos = new Vec3d(
                //         Math.abs(pos.x) > 1 ? Math.signum(pos.x) : pos.x,
                //         Math.abs(pos.y) > 1 ? Math.signum(pos.y) : pos.y,
                //        Math.abs(pos.z) > 1 ? Math.signum(pos.z) : pos.z);

                relativePos = relativePos.multiply(0.5);
            }
            else {
                if (resource.ticksLeftInPipe == 0) {
                    var x = 45;
                }

                var dirVec = resource.toDirection.getVector();
                relativePos = new Vec3d(dirVec.getX(), dirVec.getY(), dirVec.getZ());

                relativePos = relativePos.multiply(
                        relativePos.length() * (10 - resource.ticksLeftInPipe + tickDelta) / 10);

                //pos = new Vec3d(
                //       Math.abs(pos.x) > 1 ? Math.signum(pos.x) : pos.x,
                //       Math.abs(pos.y) > 1 ? Math.signum(pos.y) : pos.y,
                //       Math.abs(pos.z) > 1 ? Math.signum(pos.z) : pos.z);

                relativePos = relativePos.multiply(0.5);
            }

            if (!resourceRenderLocations.containsKey(resource.id)) {
                resourceRenderLocations.put(
                        resource.id,
                        blockEntity.getPos().toCenterPos().add(relativePos));
            }

            var renderLoc = resourceRenderLocations.get(resource.id);
            var correctLoc = blockEntity.getPos().toCenterPos().add(relativePos);
            var diff = correctLoc.subtract(renderLoc);
            var delta = diff.multiply(0.05);

            var newRelativePos = renderLoc
                    .add(delta)
                    .subtract(blockEntity.getPos().toCenterPos());

            resourceRenderLocations.put(resource.id, renderLoc.add(delta));

            matrices.translate(newRelativePos.x, newRelativePos.y, newRelativePos.z);


            //matrices.scale(4f / 16, 4f / 16, 4f / 16);

            // Rotate the item
            //matrices.multiply(
            //        RotationAxis.POSITIVE_Y.rotationDegrees(
            //                (blockEntity.getWorld().getTime() + tickDelta) * 4));

            MinecraftClient
                    .getInstance()
                    .getItemRenderer()
                    .renderItem(
                            blockEntity.getResources().iterator().next().items,
                            ModelTransformationMode.NONE,
                            light,
                            overlay,
                            matrices,
                            vertexConsumers,
                            blockEntity.getWorld(),
                            0);

            matrices.pop();
        }

        // Move the item
        //matrices..translate(0.5, (8 - 4/2 - 1) / 16d, 0.5);


        // Mandatory call after GL calls
        matrices.pop();
    }
}