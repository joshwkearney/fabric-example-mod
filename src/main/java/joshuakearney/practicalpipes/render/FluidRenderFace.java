/*
 * Copyright (c) 2019 AlexIIL
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package joshuakearney.practicalpipes.render;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

public final class FluidRenderFace {
    public static final int FULL_LIGHT = 0x00F0_00F0;

    public final double x0, y0, z0, u0, v0;
    public final double x1, y1, z1, u1, v1;
    public final double x2, y2, z2, u2, v2;
    public final double x3, y3, z3, u3, v3;
    public final float nx, ny, nz;

    public int light = FULL_LIGHT;
    public final boolean flowing;

    public FluidRenderFace(double _x0, double _y0, double _z0, double _u0, double _v0,
                           double _x1, double _y1, double _z1, double _u1, double _v1,
                           double _x2, double _y2, double _z2, double _u2, double _v2,
                           double _x3, double _y3, double _z3, double _u3, double _v3,
                           int light, float nx, float ny, float nz, boolean flowing) {

        x0 = _x0;   x1 = _x1;   x2 = _x2;   x3 = _x3;
        y0 = _y0;   y1 = _y1;   y2 = _y2;   y3 = _y3;
        z0 = _z0;   z1 = _z1;   z2 = _z2;   z3 = _z3;
        u0 = _u0;   u1 = _u1;   u2 = _u2;   u3 = _u3;
        v0 = _v0;   v1 = _v1;   v2 = _v2;   v3 = _v3;

        this.nx = nx;
        this.ny = ny;
        this.nz = nz;

        this.light = light;
        this.flowing = flowing;
    }

    public static List<FluidRenderFace> cuboid(double x0, double y0, double z0, double x1, double y1, double z1,
                                               double textureScale, EnumSet<Direction> faces, int light,
                                               boolean flowing) {

        var list = new ArrayList<FluidRenderFace>();

        for (Direction face : faces) {
            list.add(createFlatFace(x0, y0, z0, x1, y1, z1, textureScale, face, light, flowing));
        }

        return list;
    }

    public static FluidRenderFace createFlatFace(double x0, double y0, double z0, double x1, double y1, double z1,
                                                 double textureScale, Direction face, int light, boolean flowing) {

        switch (face) {
            case DOWN:
                return createFlatFaceY(x0, y0, z0, x1, y1, z1, textureScale, false, light, flowing);
            case UP:
                return createFlatFaceY(x0, y0, z0, x1, y1, z1, textureScale, true, light, flowing);
            case NORTH:
                return createFlatFaceZ(x0, y0, z0, x1, y1, z1, textureScale, false, light, flowing);
            case SOUTH:
                return createFlatFaceZ(x0, y0, z0, x1, y1, z1, textureScale, true, light, flowing);
            case WEST:
                return createFlatFaceX(x0, y0, z0, x1, y1, z1, textureScale, false, light, flowing);
            case EAST:
                return createFlatFaceX(x0, y0, z0, x1, y1, z1, textureScale, true, light, flowing);
            default: {
                throw new IllegalStateException("Unknown Direction " + face);
            }
        }
    }

    private static FluidRenderFace createFlatFaceX(double x0, double y0, double z0, double x1, double y1, double z1,
                                                   double textureScale, boolean positive, int light, boolean flowing) {
        final double s = textureScale;

        if (positive) {
            return new FluidRenderFace(
                    x1, y0, z0, z0 * s, y0 * s,
                    x1, y1, z0, z0 * s, y1 * s,
                    x1, y1, z1, z1 * s, y1 * s,
                    x1, y0, z1, z1 * s, y0 * s,
                    light, +1, 0, 0, flowing);
        }
        else {
            return new FluidRenderFace(
                    x0, y0, z0, z0 * s, y0 * s,
                    x0, y0, z1, z1 * s, y0 * s,
                    x0, y1, z1, z1 * s, y1 * s,
                    x0, y1, z0, z0 * s, y1 * s,
                    light, -1, 0, 0, flowing);
        }
    }

    private static FluidRenderFace createFlatFaceY(double x0, double y0, double z0, double x1, double y1, double z1,
                                                   double textureScale, boolean positive, int light, boolean flowing) {
        final double s = textureScale;

        if (positive) {
            return new FluidRenderFace(
                    x0, y1, z0, x0 * s, z0 * s,
                    x0, y1, z1, x0 * s, z1 * s,
                    x1, y1, z1, x1 * s, z1 * s,
                    x1, y1, z0, x1 * s, z0 * s,
                    light, 0, +1, 0, flowing);
        }
        else {
            return new FluidRenderFace(
                    x0, y0, z0, x0 * s, z0 * s,
                    x1, y0, z0, x1 * s, z0 * s,
                    x1, y0, z1, x1 * s, z1 * s,
                    x0, y0, z1, x0 * s, z1 * s,
                    light, 0, -1, 0, flowing);
        }
    }

    private static FluidRenderFace createFlatFaceZ(double x0, double y0, double z0, double x1, double y1, double z1,
                                                   double textureScale, boolean positive, int light, boolean flowing) {

        final double s = textureScale;

        if (positive) {
            return new FluidRenderFace(
                    x0, y0, z1, x0 * s, y0 * s,
                    x1, y0, z1, x1 * s, y0 * s,
                    x1, y1, z1, x1 * s, y1 * s,
                    x0, y1, z1, x0 * s, y1 * s,
                    light, 0, 0, +1, flowing);
        }
        else {
            return new FluidRenderFace(
                    x0, y0, z0, x0 * s, y0 * s,
                    x0, y1, z0, x0 * s, y1 * s,
                    x1, y1, z0, x1 * s, y1 * s,
                    x1, y0, z0, x1 * s, y0 * s,
                    light, 0, 0, -1, flowing);
        }
    }

    @Override
    public String toString() {
        return "FluidRenderFace { " + (flowing ? "flowing" : "still")//
            + "\n  " + x0 + " " + y0 + " " + z0 + " " + u0 + " " + v0//
            + "\n  " + x1 + " " + y1 + " " + z1 + " " + u1 + " " + v1//
            + "\n  " + x2 + " " + y2 + " " + z2 + " " + u2 + " " + v2//
            + "\n  " + x3 + " " + y3 + " " + z3 + " " + u3 + " " + v3//
            + "\n}"//
        ;
    }

    public float getU(Sprite still, Sprite flowing, double u) {
        Sprite s = still;
        if (this.flowing) {
            s = flowing;
            u = 0.25 + u * 0.5;
        }
        return MathHelper.lerp((float) u, s.getMinU(), s.getMaxU());
    }

    public float getV(Sprite still, Sprite flowing, double v) {
        Sprite s = still;
        if (this.flowing) {
            s = flowing;
            v = 0.25 + v * 0.5;
        }
        return MathHelper.lerp((float) v, s.getMinV(), s.getMaxV());
    }
}
