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

        if (!blockEntity.hasResources()) {
            return;
        }

        matrices.push();
        matrices.translate(0.5, 0.5, 0.5);

        for (var resource : blockEntity.getResources()) {
            matrices.push();

            // Get the direction this item is supposed to be travelling
            var dir = (resource.ticksInPipe < resource.ticksPerPipe / 2)
                    ? resource.fromDirection.getOpposite()
                    : resource.toDirection;

            // Get a vector pointing from the center of our block to the edge
            // in the correct direction, scaled by the time this item has been
            // in the pipe
            var dirVec = dir.getVector();
            var relativePos = new Vec3d(dirVec.getX(), dirVec.getY(), dirVec.getZ())
                    .multiply(0.5)
                    .multiply((2*(resource.ticksInPipe + tickDelta) - resource.ticksPerPipe) / resource.ticksPerPipe);

            // Make sure there is a render location for this item
            if (!resourceRenderLocations.containsKey(resource.id)) {
                resourceRenderLocations.put(
                        resource.id,
                        blockEntity.getPos().toCenterPos().add(relativePos));
            }

            var renderLoc = resourceRenderLocations.get(resource.id);
            var correctLoc = blockEntity.getPos().toCenterPos().add(relativePos);
            var diff = correctLoc.subtract(renderLoc);

            // We are only going to follow the correct path a little ways. This has the effect
            // of smoothing out the animation if we miss a frame, and it gives us a curving
            // motion on corners. Because we are calculating the position from the client side
            // we need to take average fps into account to get the same look at all framerates.
            // The formula used here was calculated empirically
            var delta = diff.multiply(0.3);

            // Calculate a new position relative to the center of this block, but
            // based on the previous position and not where the item is supposed to
            // be. This smoothes out the motion
            var newRelativePos = renderLoc
                    .add(delta)
                    .subtract(blockEntity.getPos().toCenterPos());

            // Update the location
            resourceRenderLocations.put(resource.id, renderLoc.add(delta));
            matrices.translate(newRelativePos.x, newRelativePos.y, newRelativePos.z);
            matrices.scale(4f / 16, 4f / 16, 4f / 16);

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

        matrices.pop();
    }
}