package com.ext1se.opengl.weather;

import android.content.res.Configuration;

import com.ext1se.opengl.framework.Game;
import com.ext1se.opengl.framework.gl.Camera2D;
import com.ext1se.opengl.framework.gl.FPSCounter;
import com.ext1se.opengl.framework.gl.SpriteBatcher;
import com.ext1se.opengl.framework.impl.GLScreen;
import com.ext1se.opengl.framework.math.Vector2;

import javax.microedition.khronos.opengles.GL10;

public class WeatherScreen extends GLScreen {
    Camera2D guiCam;
    Vector2 touchPoint;
    SpriteBatcher batcher;
    WorldWeather world;
    WorldRendererWeather renderer;
    FPSCounter fpsCounter;

    public WeatherScreen(Game game) {
        super(game);
        init();
        world = new WorldWeather(WorldWeather.WEATHER_CLEAR, Configuration.ORIENTATION_PORTRAIT);
        renderer = new WorldRendererWeather(glGraphics, Configuration.ORIENTATION_PORTRAIT, batcher, world);
    }

    public WeatherScreen(Game game, int stateWeather, int colorTop, int colorBottom) {
        super(game);
        init();
        world = new WorldWeather(stateWeather, Configuration.ORIENTATION_PORTRAIT);
        renderer = new WorldRendererWeather(glGraphics, Configuration.ORIENTATION_PORTRAIT, batcher, world);
        renderer.setColors(colorTop, colorBottom);
    }

    public WeatherScreen(Game game, int orientation, int stateWeather, int colorTop, int colorBottom) {
        super(game);
        init();
        world = new WorldWeather(stateWeather, orientation);
        renderer = new WorldRendererWeather(glGraphics, orientation, batcher, world);
        renderer.setColors(colorTop, colorBottom);
    }

    private void init() {
        guiCam = new Camera2D(glGraphics, 320, 480);
        touchPoint = new Vector2();
        batcher = new SpriteBatcher(glGraphics, 1000);
        //fpsCounter = new FPSCounter();
    }

    public void restart(int stateWeather, int colorTop, int colorBottom) {
        world.restart(stateWeather);
        renderer.restart(world, colorTop, colorBottom);
    }


    @Override
    public void update(float deltaTime) {
        if (deltaTime > 0.1f)
            deltaTime = 0.1f;

        updateRunning(deltaTime);
    }

    private void updateRunning(float deltaTime) {
        world.update(deltaTime, game.getInput().getAccelX());
    }

    @Override
    public void present(float deltaTime) {
        GL10 gl = glGraphics.getGL();
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        //gl.glEnable(GL10.GL_TEXTURE_2D);
        //gl.glDisable(GL10.GL_TEXTURE_2D);
        renderer.render();
        guiCam.setViewportAndMatrices();
        //fpsCounter.logFrame();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }

    @Override
    public void restart() {
    }

}
