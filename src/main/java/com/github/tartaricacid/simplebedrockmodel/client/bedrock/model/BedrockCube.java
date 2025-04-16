package com.github.tartaricacid.simplebedrockmodel.client.bedrock.model;

import com.github.tartaricacid.simplebedrockmodel.SimpleBedrockModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Vector3f;

public interface BedrockCube {
    int NUM_CUBE_FACES = 6;
    int VERTEX_X1_Y1_Z1 = 0,
            VERTEX_X2_Y1_Z1 = 1,
            VERTEX_X2_Y2_Z1 = 2,
            VERTEX_X1_Y2_Z1 = 3,
            VERTEX_X1_Y1_Z2 = 4,
            VERTEX_X2_Y1_Z2 = 5,
            VERTEX_X2_Y2_Z2 = 6,
            VERTEX_X1_Y2_Z2 = 7;
    int[][] VERTEX_ORDER = new int[][]{
            {VERTEX_X2_Y1_Z2, VERTEX_X1_Y1_Z2, VERTEX_X1_Y1_Z1, VERTEX_X2_Y1_Z1},
            {VERTEX_X2_Y2_Z1, VERTEX_X1_Y2_Z1, VERTEX_X1_Y2_Z2, VERTEX_X2_Y2_Z2},
            {VERTEX_X2_Y1_Z1, VERTEX_X1_Y1_Z1, VERTEX_X1_Y2_Z1, VERTEX_X2_Y2_Z1},
            {VERTEX_X1_Y1_Z2, VERTEX_X2_Y1_Z2, VERTEX_X2_Y2_Z2, VERTEX_X1_Y2_Z2},
            {VERTEX_X1_Y1_Z1, VERTEX_X1_Y1_Z2, VERTEX_X1_Y2_Z2, VERTEX_X1_Y2_Z1},
            {VERTEX_X2_Y1_Z2, VERTEX_X2_Y1_Z1, VERTEX_X2_Y2_Z1, VERTEX_X2_Y2_Z2},
    };

    /**
     * Compiles the cube's vertices and adds them to the provided vertex consumer
     * <p>
     * 编译 Cube 的顶点并将它们添加到提供的 VertexConsumer 中
     *
     * @param pose     the current pose of the rendering context
     * @param consumer the vertex consumer to which the compiled vertices are added
     * @param texU     the U coordinate for texture mapping
     * @param texV     the V coordinate for texture mapping
     * @param red      the red color component (0.0 to 1.0)
     * @param green    the green color component (0.0 to 1.0)
     * @param blue     the blue color component (0.0 to 1.0)
     * @param alpha    the alpha (transparency) component (0.0 to 1.0)
     */
    void compile(PoseStack.Pose pose, Vector3f[] normals, VertexConsumer consumer, int texU, int texV, float red, float green, float blue, float alpha);

    /**
     * Do backface culling for the cube
     * <p>
     * 计算Cube需要背面剔除的面
     *
     * @param vertices  the 8 vertices of the cube
     * @param normals   the 6 normals of the cube
     * @return the bitmask of the faces to be rendered
     */
    default int getRenderFace(Vector3f[] vertices, Vector3f[] normals) {
        int faces = 0b111111;
        if (!SimpleBedrockModel.FACE_CULLING) return faces;

        // Do backface culling
        if ((vertices[VERTEX_X2_Y1_Z2].x + vertices[VERTEX_X1_Y1_Z1].x) * normals[1].x
                + (vertices[VERTEX_X2_Y1_Z2].y + vertices[VERTEX_X1_Y1_Z1].y) * normals[1].y
                + (vertices[VERTEX_X2_Y1_Z2].z + vertices[VERTEX_X1_Y1_Z1].z) * normals[1].z < 0) {
            faces &= ~0b000001; // Down
        }
        if ((vertices[VERTEX_X2_Y2_Z1].x + vertices[VERTEX_X1_Y2_Z2].x) * normals[1].x
                + (vertices[VERTEX_X2_Y2_Z1].y + vertices[VERTEX_X1_Y2_Z2].y) * normals[1].y
                + (vertices[VERTEX_X2_Y2_Z1].z + vertices[VERTEX_X1_Y2_Z2].z) * normals[1].z > 0) {
            faces &= ~0b000010; // Up
        }
        if ((vertices[VERTEX_X2_Y1_Z1].x + vertices[VERTEX_X1_Y2_Z1].x) * normals[3].x
                + (vertices[VERTEX_X2_Y1_Z1].y + vertices[VERTEX_X1_Y2_Z1].y) * normals[3].y
                + (vertices[VERTEX_X2_Y1_Z1].z + vertices[VERTEX_X1_Y2_Z1].z) * normals[3].z < 0) {
            faces &= ~0b000100; // North
        }
        if ((vertices[VERTEX_X1_Y1_Z2].x + vertices[VERTEX_X2_Y2_Z2].x) * normals[3].x
                + (vertices[VERTEX_X1_Y1_Z2].y + vertices[VERTEX_X2_Y2_Z2].y) * normals[3].y
                + (vertices[VERTEX_X1_Y1_Z2].z + vertices[VERTEX_X2_Y2_Z2].z) * normals[3].z > 0) {
            faces &= ~0b001000; // South
        }
        if ((vertices[VERTEX_X1_Y1_Z1].x + vertices[VERTEX_X1_Y2_Z2].x) * normals[5].x
                + (vertices[VERTEX_X1_Y1_Z1].y + vertices[VERTEX_X1_Y2_Z2].y) * normals[5].y
                + (vertices[VERTEX_X1_Y1_Z1].z + vertices[VERTEX_X1_Y2_Z2].z) * normals[5].z < 0) {
            faces &= ~0b010000; // West
        }
        if ((vertices[VERTEX_X2_Y1_Z2].x + vertices[VERTEX_X2_Y2_Z1].x) * normals[5].x
                + (vertices[VERTEX_X2_Y1_Z2].y + vertices[VERTEX_X2_Y2_Z1].y) * normals[5].y
                + (vertices[VERTEX_X2_Y1_Z2].z + vertices[VERTEX_X2_Y2_Z1].z) * normals[5].z > 0) {
            faces &= ~0b100000; // East
        }
        return faces;
    }
}
