package joshuakearney.practical_pipes.features.tank;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import joshuakearney.practical_pipes.render.FluidRenderFace;
import joshuakearney.practical_pipes.render.FluidVolumeRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;

import java.util.EnumSet;

@Environment(EnvType.CLIENT)
public class TankRenderer implements BlockEntityRenderer<TankBlockEntity> {
    public TankRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(TankBlockEntity blockEntity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {


        if (blockEntity.fluid.amount == 0) {
            return;
        }

        matrices.push();

        double epsilon = 0.001;

        double x0 = 0 / 16d + epsilon;
        double x1 = 16 / 16d - epsilon;

        double y0 = epsilon;
        double y1 = blockEntity.fluid.amount * 1d / blockEntity.fluid.getCapacity();

        double z0 = 0 / 16d + epsilon;
        double z1 = 16 / 16d - epsilon;

        var sides = EnumSet.allOf(Direction.class);
        var faces = FluidRenderFace.cuboid(x0, y0, z0, x1, y1, z1, 1, sides, light,false);

        FluidVolumeRenderer.INSTANCE.render(blockEntity.fluid.variant, faces, vertexConsumers, matrices);
        matrices.pop();
    }
}