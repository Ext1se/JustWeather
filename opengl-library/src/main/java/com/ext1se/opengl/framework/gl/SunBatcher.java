package com.ext1se.opengl.framework.gl;

import com.ext1se.opengl.framework.impl.GLGraphics;

import javax.microedition.khronos.opengles.GL10;

public class SunBatcher {
    float[] verticesBuffer;
    int bufferIndex;
    final VerticesBack vertices;
    int numSprites;
    float width, height;

    public SunBatcher(GLGraphics glGraphics) {
//        width = glGraphics.getWidth();
       // height = glGraphics.getHeight();
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

    public void drawSpriteR(float r, float g, float b) {

        float z = 0;


        float x1 = 4;
        float y1 = 16;
        float x2 = 5f;
        float y2 = 16;
        float x3 = 9;
        float y3 = 0;
        float x4 = 0;
        float y4 = 0;

        verticesBuffer[bufferIndex++] = x1;
        verticesBuffer[bufferIndex++] = y1;
        verticesBuffer[bufferIndex++] = z;
        verticesBuffer[bufferIndex++] = r;
        verticesBuffer[bufferIndex++] = g;
        verticesBuffer[bufferIndex++] = b;
        verticesBuffer[bufferIndex++] = 0.8f;

        verticesBuffer[bufferIndex++] = x2;
        verticesBuffer[bufferIndex++] = y2;
        verticesBuffer[bufferIndex++] = z;
        verticesBuffer[bufferIndex++] = r;
        verticesBuffer[bufferIndex++] = g;
        verticesBuffer[bufferIndex++] = b;
        verticesBuffer[bufferIndex++] = 0.8f;


        verticesBuffer[bufferIndex++] = x3;
        verticesBuffer[bufferIndex++] = y3;
        verticesBuffer[bufferIndex++] = z;
        verticesBuffer[bufferIndex++] = r;
        verticesBuffer[bufferIndex++] = g;
        verticesBuffer[bufferIndex++] = b;
        verticesBuffer[bufferIndex++] = 0.2f;

        verticesBuffer[bufferIndex++] = x4;
        verticesBuffer[bufferIndex++] = y4;
        verticesBuffer[bufferIndex++] = z;
        verticesBuffer[bufferIndex++] = r;
        verticesBuffer[bufferIndex++] = g;
        verticesBuffer[bufferIndex++] = b;
        verticesBuffer[bufferIndex++] = 0.2f;
    }

    public void drawSprite1(float r, float g, float b) {

        float z = 0;

        float x1 = 2.7f;
        float y1 = 16;
        float x2 = 2.8f;
        float y2 = 16;
        float x3 = 0;
        float y3 = 9;
        float x4 = 0;
        float y4 = 10;

        verticesBuffer[bufferIndex++] = x1;
        verticesBuffer[bufferIndex++] = y1;
        verticesBuffer[bufferIndex++] = z;
        verticesBuffer[bufferIndex++] = r;
        verticesBuffer[bufferIndex++] = g;
        verticesBuffer[bufferIndex++] = b;
        verticesBuffer[bufferIndex++] = 0.8f;

        verticesBuffer[bufferIndex++] = x2;
        verticesBuffer[bufferIndex++] = y2;
        verticesBuffer[bufferIndex++] = z;
        verticesBuffer[bufferIndex++] = r;
        verticesBuffer[bufferIndex++] = g;
        verticesBuffer[bufferIndex++] = b;
        verticesBuffer[bufferIndex++] = 0.8f;


        verticesBuffer[bufferIndex++] = x3;
        verticesBuffer[bufferIndex++] = y3;
        verticesBuffer[bufferIndex++] = z;
        verticesBuffer[bufferIndex++] = r;
        verticesBuffer[bufferIndex++] = g;
        verticesBuffer[bufferIndex++] = b;
        verticesBuffer[bufferIndex++] = 0.2f;

        verticesBuffer[bufferIndex++] = x4;
        verticesBuffer[bufferIndex++] = y4;
        verticesBuffer[bufferIndex++] = z;
        verticesBuffer[bufferIndex++] = r;
        verticesBuffer[bufferIndex++] = g;
        verticesBuffer[bufferIndex++] = b;
        verticesBuffer[bufferIndex++] = 0.2f;
    }


    public void drawSprite2(float r, float g, float b) {

        float z = 0;

        float x1 = 4;
        float y1 = 16;
        float x2 = 4.1f;
        float y2 = 16;
        float x3 = 4.8f;
        float y3 = 0;
        float x4 = 3.8f;
        float y4 = 0;

        verticesBuffer[bufferIndex++] = x1;
        verticesBuffer[bufferIndex++] = y1;
        verticesBuffer[bufferIndex++] = z;
        verticesBuffer[bufferIndex++] = r;
        verticesBuffer[bufferIndex++] = g;
        verticesBuffer[bufferIndex++] = b;
        verticesBuffer[bufferIndex++] = 0.8f;

        verticesBuffer[bufferIndex++] = x2;
        verticesBuffer[bufferIndex++] = y2;
        verticesBuffer[bufferIndex++] = z;
        verticesBuffer[bufferIndex++] = r;
        verticesBuffer[bufferIndex++] = g;
        verticesBuffer[bufferIndex++] = b;
        verticesBuffer[bufferIndex++] = 0.8f;


        verticesBuffer[bufferIndex++] = x3;
        verticesBuffer[bufferIndex++] = y3;
        verticesBuffer[bufferIndex++] = z;
        verticesBuffer[bufferIndex++] = r;
        verticesBuffer[bufferIndex++] = g;
        verticesBuffer[bufferIndex++] = b;
        verticesBuffer[bufferIndex++] = 0.2f;

        verticesBuffer[bufferIndex++] = x4;
        verticesBuffer[bufferIndex++] = y4;
        verticesBuffer[bufferIndex++] = z;
        verticesBuffer[bufferIndex++] = r;
        verticesBuffer[bufferIndex++] = g;
        verticesBuffer[bufferIndex++] = b;
        verticesBuffer[bufferIndex++] = 0.2f;
    }

    public void drawSprite3(float r, float g, float b) {

        float z = 0;

        float x1 = 6.5f;
        float y1 = 16;
        float x2 = 7;
        float y2 = 16;
        float x3 = 12;
        float y3 = 0;
        float x4 = 8;
        float y4 = 0;

        verticesBuffer[bufferIndex++] = x1;
        verticesBuffer[bufferIndex++] = y1;
        verticesBuffer[bufferIndex++] = z;
        verticesBuffer[bufferIndex++] = r;
        verticesBuffer[bufferIndex++] = g;
        verticesBuffer[bufferIndex++] = b;
        verticesBuffer[bufferIndex++] = 0.8f;

        verticesBuffer[bufferIndex++] = x2;
        verticesBuffer[bufferIndex++] = y2;
        verticesBuffer[bufferIndex++] = z;
        verticesBuffer[bufferIndex++] = r;
        verticesBuffer[bufferIndex++] = g;
        verticesBuffer[bufferIndex++] = b;
        verticesBuffer[bufferIndex++] = 0.8f;


        verticesBuffer[bufferIndex++] = x3;
        verticesBuffer[bufferIndex++] = y3;
        verticesBuffer[bufferIndex++] = z;
        verticesBuffer[bufferIndex++] = r;
        verticesBuffer[bufferIndex++] = g;
        verticesBuffer[bufferIndex++] = b;
        verticesBuffer[bufferIndex++] = 0.2f;

        verticesBuffer[bufferIndex++] = x4;
        verticesBuffer[bufferIndex++] = y4;
        verticesBuffer[bufferIndex++] = z;
        verticesBuffer[bufferIndex++] = r;
        verticesBuffer[bufferIndex++] = g;
        verticesBuffer[bufferIndex++] = b;
        verticesBuffer[bufferIndex++] = 0.2f;
    }


    public void drawSprite4(float r, float g, float b) {

        float z = 0;

        float x1 = 8;
        float y1 = 16;
        float x2 = 8.1f;
        float y2 = 16;
        float x3 = 4.8f;
        float y3 = 3;
        float x4 = -2.8f;
        float y4 = 3;

        verticesBuffer[bufferIndex++] = x1;
        verticesBuffer[bufferIndex++] = y1;
        verticesBuffer[bufferIndex++] = z;
        verticesBuffer[bufferIndex++] = r;
        verticesBuffer[bufferIndex++] = g;
        verticesBuffer[bufferIndex++] = b;
        verticesBuffer[bufferIndex++] = 0.5f;

        verticesBuffer[bufferIndex++] = x2;
        verticesBuffer[bufferIndex++] = y2;
        verticesBuffer[bufferIndex++] = z;
        verticesBuffer[bufferIndex++] = r;
        verticesBuffer[bufferIndex++] = g;
        verticesBuffer[bufferIndex++] = b;
        verticesBuffer[bufferIndex++] = 0.5f;


        verticesBuffer[bufferIndex++] = x3;
        verticesBuffer[bufferIndex++] = y3;
        verticesBuffer[bufferIndex++] = z;
        verticesBuffer[bufferIndex++] = r;
        verticesBuffer[bufferIndex++] = g;
        verticesBuffer[bufferIndex++] = b;
        verticesBuffer[bufferIndex++] = 0.0f;

        verticesBuffer[bufferIndex++] = x4;
        verticesBuffer[bufferIndex++] = y4;
        verticesBuffer[bufferIndex++] = z;
        verticesBuffer[bufferIndex++] = r;
        verticesBuffer[bufferIndex++] = g;
        verticesBuffer[bufferIndex++] = b;
        verticesBuffer[bufferIndex++] = 0.0f;
    }

    public void drawSprite(float x1, float x2, float x3, float x4, float y1, float y2, float y3, float y4) {
        int r = 1; int g = 1; int b = 1;
        //float r = 1; float g = 0.9f; float b = 0.6f;
        float z = 0;

        verticesBuffer[bufferIndex++] = x1;
        verticesBuffer[bufferIndex++] = y1;
        verticesBuffer[bufferIndex++] = z;
        verticesBuffer[bufferIndex++] = r;
        verticesBuffer[bufferIndex++] = g;
        verticesBuffer[bufferIndex++] = b;
        verticesBuffer[bufferIndex++] = 0.5f;

        verticesBuffer[bufferIndex++] = x2;
        verticesBuffer[bufferIndex++] = y2;
        verticesBuffer[bufferIndex++] = z;
        verticesBuffer[bufferIndex++] = r;
        verticesBuffer[bufferIndex++] = g;
        verticesBuffer[bufferIndex++] = b;
        verticesBuffer[bufferIndex++] = 0.5f;


        verticesBuffer[bufferIndex++] = x3;
        verticesBuffer[bufferIndex++] = y3;
        verticesBuffer[bufferIndex++] = z;
        verticesBuffer[bufferIndex++] = r;
        verticesBuffer[bufferIndex++] = g;
        verticesBuffer[bufferIndex++] = b;
        verticesBuffer[bufferIndex++] = 0.0f;

        verticesBuffer[bufferIndex++] = x4;
        verticesBuffer[bufferIndex++] = y4;
        verticesBuffer[bufferIndex++] = z;
        verticesBuffer[bufferIndex++] = r;
        verticesBuffer[bufferIndex++] = g;
        verticesBuffer[bufferIndex++] = b;
        verticesBuffer[bufferIndex++] = 0.0f;
    }

}