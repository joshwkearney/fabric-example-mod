/*
 * Copyright (c) 2019 AlexIIL
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package joshuakearney.practical_pipes.render;

import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;

import java.util.*;

public class FluidVolumeRenderer {
    public static final FluidVolumeRenderer INSTANCE = new FluidVolumeRenderer();

    public void render(FluidVariant fluid, List<FluidRenderFace> faces, VertexConsumerProvider vcp,
                       MatrixStack matrices) {

        var color = FluidVariantRendering.getColor(fluid);
        var sprites = getSprites(fluid);
        var layer = RenderLayers.getFluidLayer(fluid.getFluid().getDefaultState());

        renderFluidHelper(faces, vcp.getBuffer(layer), matrices, sprites[0], sprites[1], color);
    }

    private static Sprite[] getSprites(FluidVariant fluid) {
        final Sprite still;
        final Sprite flowing;

        var handler = FluidRenderHandlerRegistry.INSTANCE.get(fluid.getFluid());

        if (handler == null) {
            var sprites = handler.getFluidSprites(null, null, fluid.getFluid().getDefaultState());
            assert sprites.length == 2;

            still = sprites[0];
            flowing = sprites[1];
        }
        else {
            BlockState state = fluid
                    .getFluid()
                    .getDefaultState()
                    .getBlockState();

            flowing = still = MinecraftClient
                    .getInstance()
                    .getBlockRenderManager()
                    .getModel(state)
                    .getParticleSprite();
        }

        return new Sprite[] { still, flowing };
    }

    private static void renderFluidHelper(List<FluidRenderFace> faces, VertexConsumer vc, MatrixStack matrices,
                                          Sprite still, Sprite flowing, int colour) {

        int a = (colour >>> 24) & 0xFF;
        int r = (colour >> 16) & 0xFF;
        int g = (colour >> 8) & 0xFF;
        int b = (colour >> 0) & 0xFF;

        var _s = still;
        var _f = flowing;

        for (var f : faces) {
            vertex(vc, matrices, f.x0, f.y0, f.z0, f.getU(_s, _f, f.u0), f.getV(_s, _f, f.v0), r, g, b, a, f);
            vertex(vc, matrices, f.x1, f.y1, f.z1, f.getU(_s, _f, f.u1), f.getV(_s, _f, f.v1), r, g, b, a, f);
            vertex(vc, matrices, f.x2, f.y2, f.z2, f.getU(_s, _f, f.u2), f.getV(_s, _f, f.v2), r, g, b, a, f);
            vertex(vc, matrices, f.x3, f.y3, f.z3, f.getU(_s, _f, f.u3), f.getV(_s, _f, f.v3), r, g, b, a, f);
        }
    }

    private static void vertex(VertexConsumer vc, MatrixStack matrices, double x, double y, double z,
                               float u, float v, int r, int g, int b, int a, FluidRenderFace f) {

        vc.vertex(matrices.peek().getPositionMatrix(), (float)x, (float)y, (float)z);
        vc.color(r, g, b, a == 0 ? 0xFF : a);
        vc.texture(u, v);
        vc.overlay(OverlayTexture.DEFAULT_UV);
        vc.light(f.light);
        vc.normal(matrices.peek().getNormalMatrix(), f.nx, f.ny, f.nz);
        vc.next();
    }

    public record ComponentRenderFaces(List<FluidRenderFace> split, List<FluidRenderFace> splitExceptTextures) { }
}