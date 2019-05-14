package com.ext1se.opengl.framework.gl;

import com.ext1se.opengl.framework.impl.GLGraphics;

import javax.microedition.khronos.opengles.GL10;

public class BackgroundBatcher {
    float[] verticesBuffer;
    int bufferIndex;
    final VerticesBack vertices;
    float width, height;

    public BackgroundBatcher(GLGraphics glGraphics, int width, int height) {
        this.width = width;
        this.height = height;
        this.verticesBuffer = new float[4*7];
        this.vertices = new VerticesBack(glGraphics, 4, 6, true, false);
        this.bufferIndex = 0;

        short[] indices = new short[6];
        int len = indices.length;
        short j = 0;
        for (int i = 0; i < len; i += 6, j += 4) {
            indices[i + 0] = (short)(j + 0);
            indices[i + 1] = (short)(j + 1);
            indices[i + 2] = (short)(j + 2);
            indices[i + 3] = (short)(j + 2);
            indices[i + 4] = (short)(j + 3);
            indices[i + 5] = (short)(j + 0);

        }
        vertices.setIndices(indices, 0, indices.length);
    }

    public void beginBatch() {
        bufferIndex = 0;
    }

    public void endBatch() {
        vertices.setVertices(verticesBuffer, 0, bufferIndex);
        vertices.bind();
        vertices.draw(GL10.GL_TRIANGLES, 0, 6);
        vertices.unbind();
    }

    public void drawSprite(float r1, float g1, float b1, float r2, float g2, float b2) {

        float x1 = 0;
        float y1 = 0;
        float z = 0;
        float x2 = width;
        float y2 = height;

        verticesBuffer[bufferIndex++] = x1;
        verticesBuffer[bufferIndex++] = y1;
        verticesBuffer[bufferIndex++] = z;
        verticesBuffer[bufferIndex++] = r1;
        verticesBuffer[bufferIndex++] = g1;
        verticesBuffer[bufferIndex++] = b1;
        verticesBuffer[bufferIndex++] = 1.0f;

        verticesBuffer[bufferIndex++] = x2;
        verticesBuffer[bufferIndex++] = y1;
        verticesBuffer[bufferIndex++] = z;
        verticesBuffer[bufferIndex++] = r1;
        verticesBuffer[bufferIndex++] = g1;
        verticesBuffer[bufferIndex++] = b1;
        verticesBuffer[bufferIndex++] = 1.0f;


        verticesBuffer[bufferIndex++] = x2;
        verticesBuffer[bufferIndex++] = y2;
        verticesBuffer[bufferIndex++] = z;
        verticesBuffer[bufferIndex++] = r2;
        verticesBuffer[bufferIndex++] = g2;
        verticesBuffer[bufferIndex++] = b2;
        verticesBuffer[bufferIndex++] = 1.0f;

        verticesBuffer[bufferIndex++] = x1;
        verticesBuffer[bufferIndex++] = y2;
        verticesBuffer[bufferIndex++] = z;
        verticesBuffer[bufferIndex++] = r2;
        verticesBuffer[bufferIndex++] = g2;
        verticesBuffer[bufferIndex++] = b2;
        verticesBuffer[bufferIndex++] = 1.0f;
    }
}