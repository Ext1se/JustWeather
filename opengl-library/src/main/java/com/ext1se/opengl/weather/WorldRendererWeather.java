package com.ext1se.opengl.weather;

import android.content.res.Configuration;

import com.ext1se.opengl.framework.gl.AmbientLight;
import com.ext1se.opengl.framework.gl.BackgroundBatcher;
import com.ext1se.opengl.framework.gl.Camera2D;
import com.ext1se.opengl.framework.gl.DirectionalLight;
import com.ext1se.opengl.framework.gl.SpriteBatcher;
import com.ext1se.opengl.framework.gl.SunBatcher;
import com.ext1se.opengl.framework.impl.GLGraphics;

import javax.microedition.khronos.opengles.GL10;

public class WorldRendererWeather {
    int stateWeather;
    private static final int FRUSTUM_WIDTH = 9;
    private static final int FRUSTUM_HEIGHT = 16;
    GLGraphics glGraphics;
    WorldWeather world;
    Camera2D cam;
    SpriteBatcher batcher;
    BackgroundBatcher backgroundBatcher;
    SunBatcher sunBatcher1;
    SunBatcher sunBatcher2;
    SunBatcher sunBatcher3;
    DirectionalLight directionalLight;
    AmbientLight ambientLight;
    float r1, g1, b1, r2, g2, b2;

    public WorldRendererWeather(GLGraphics glGraphics, int orientation, SpriteBatcher batcher, WorldWeather world) {
        this.glGraphics = glGraphics;
        this.world = world;
        this.stateWeather = world.state;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            this.cam = new Camera2D(glGraphics, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
            backgroundBatcher = new BackgroundBatcher(glGraphics, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
        } else {
            this.cam = new Camera2D(glGraphics, FRUSTUM_HEIGHT, FRUSTUM_WIDTH);
            backgroundBatcher = new BackgroundBatcher(glGraphics, FRUSTUM_HEIGHT, FRUSTUM_WIDTH);
        }
        this.batcher = batcher;
        sunBatcher1 = new SunBatcher(glGraphics);
        sunBatcher2 = new SunBatcher(glGraphics);
        sunBatcher3 = new SunBatcher(glGraphics);
        directionalLight = new DirectionalLight();
        directionalLight.setDirection(1, 1, 0);
        ambientLight = new AmbientLight();
        ambientLight.setColor(0.2f, 0.2f, 0.2f, 1.0f);
    }

    public void restart(WorldWeather world, int colorTop, int colorBottom) {
        this.world = world;
        this.stateWeather = world.state;
        setColors(colorTop, colorBottom);
    }

    private void setColor(int color) {
        r1 = android.graphics.Color.red(color) * (float) 1 / 255;
        g1 = android.graphics.Color.green(color) * (float) 1 / 255;
        b1 = android.graphics.Color.blue(color) * (float) 1 / 255;
        r2 = 1;
        g2 = 1;
        b2 = 1;
    }

    public void setColors(int colorTop, int colorBottom) {
        r1 = android.graphics.Color.red(colorTop) * (float) 1 / 255;
        g1 = android.graphics.Color.green(colorTop) * (float) 1 / 255;
        b1 = android.graphics.Color.blue(colorTop) * (float) 1 / 255;
        r2 = android.graphics.Color.red(colorBottom) * (float) 1 / 255;
        g2 = android.graphics.Color.green(colorBottom) * (float) 1 / 255;
        b2 = android.graphics.Color.blue(colorBottom) * (float) 1 / 255;
    }

    public void render() {
        cam.setViewportAndMatrices();
        renderGradient();
        if (stateWeather == WorldWeather.WEATHER_SUN) {
            return;
        }
        if (stateWeather != WorldWeather.WEATHER_CLEAR) {
            renderObjects();
        }
    }


    private void renderGradient() {
        backgroundBatcher.beginBatch();
        backgroundBatcher.drawSprite(r2, g2, b2, r1, g1, b1);
        backgroundBatcher.endBatch();

    }

    public void renderObjects() {
        GL10 gl = glGraphics.getGL();
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        batcher.beginBatch(Assets.weather);
        switch (stateWeather) {
            case (WorldWeather.WEATHER_NIGHT):
                renderStars();
                break;
            case (WorldWeather.WEATHER_SNOW):
                renderSnows();
                break;
            case (WorldWeather.WEATHER_RAIN):
                renderRains();
                break;
            case (WorldWeather.WEATHER_CLOUD):
                renderClouds();
                break;
        }
        batcher.endBatch();
        gl.glDisable(GL10.GL_TEXTURE_2D);
        gl.glDisable(GL10.GL_BLEND);
    }

    private void renderSnows() {
        int len = world.snows.size();
        for (int i = 0; i < len; i++) {
            Snow snow = world.snows.get(i);
            switch (snow.type) {
                case (0):
                    batcher.drawSprite(snow.position.x, snow.position.y, snow.width, snow.height, snow.rotation, Assets.snow3);
                    break;
                case (1):
                    batcher.drawSprite(snow.position.x, snow.position.y, snow.width, snow.height, snow.rotation, Assets.snow4);
                    break;
                case (2):
                    batcher.drawSprite(snow.position.x, snow.position.y, snow.width, snow.height, snow.rotation, Assets.snow5);
                    break;
            }
        }
    }

    private void renderRains() {
        int len = world.rains.size();
        for (int i = 0; i < len; i++) {
            Rain rain = world.rains.get(i);
            batcher.drawSprite(rain.position.x, rain.position.y, rain.width, rain.height, rain.rotation, Assets.rain7);
        }
    }

    private void renderClouds() {
        int len = world.clouds.size();
        for (int i = 0; i < len; i++) {
            Cloud cloud = world.clouds.get(i);
            batcher.drawSprite(cloud.position.x, cloud.position.y, cloud.width * cloud.rotation, cloud.height, Assets.cloud3);
        }
    }

    private void renderStars() {
        int len = world.stars.size();
        for (int i = 0; i < len; i++) {
            Star star = world.stars.get(i);
            if (!star.falling)
                batcher.drawSprite(star.position.x, star.position.y, star.width, star.height, Assets.star);
            else
                batcher.drawSprite(star.position.x, star.position.y, star.width, star.height, Assets.comet);
        }
    }
}
