package com.ext1se.opengl.framework.impl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;

import com.ext1se.opengl.framework.Audio;
import com.ext1se.opengl.framework.FileIO;
import com.ext1se.opengl.framework.Game;
import com.ext1se.opengl.framework.Graphics;
import com.ext1se.opengl.framework.Input;
import com.ext1se.opengl.framework.Screen;
import com.ext1se.opengl.weather.Assets;
import com.ext1se.opengl.weather.WeatherScreen;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

;

public class GLGame implements Game, Renderer {
    protected enum GLGameState {
        Initialized,
        Running,
        Paused,
        Restarted,
        Finished,
        Idle
    }

    protected Context context;
    protected GLSurfaceView glView;
    protected GLGraphics glGraphics;
    protected FileIO fileIO;
    protected Input input;
    protected Screen screen;
    protected GLGame.GLGameState state = GLGame.GLGameState.Initialized;
    protected Object stateChanged = new Object();
    protected long startTime = System.nanoTime();
    protected boolean firstTimeCreate = true;

    public GLGame(Context context, GLSurfaceView glView) {
        this.context = context;
        this.glView = glView;
        this.glView.setRenderer(this);
        glGraphics = new GLGraphics(glView);
        input = new AndroidInput(context, glView, 1, 1);
        fileIO = new AndroidFileIO(context.getAssets());
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glGraphics.setGL(gl);

 /*       synchronized (stateChanged) {
            if (state == GLGame.GLGameState.Initialized)
                screen = getStartScreen();
            state = GLGame.GLGameState.Running;
            screen.resume();
            startTime = System.nanoTime();
        }*/

        if (firstTimeCreate) {
            Assets.load(this);
            firstTimeCreate = false;
        } else {
            Assets.reload();
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLGame.GLGameState state = null;

        synchronized (stateChanged) {
            state = this.state;
        }

        if (state == GLGame.GLGameState.Running) {
            float deltaTime = (System.nanoTime() - startTime) / 1000000000.0f;
            startTime = System.nanoTime();

            screen.update(deltaTime);
            screen.present(deltaTime);
        }

        if (state == GLGame.GLGameState.Paused) {
            screen.pause();
            synchronized (stateChanged) {
                this.state = GLGame.GLGameState.Idle;
                stateChanged.notifyAll();
            }
        }

        if (state == GLGame.GLGameState.Finished) {
            screen.pause();
            screen.dispose();
            synchronized (stateChanged) {
                this.state = GLGame.GLGameState.Idle;
                stateChanged.notifyAll();
            }
        }
    }

    public GLGraphics getGLGraphics() {
        return glGraphics;
    }

    @Override
    public FileIO getFileIO() {
        return fileIO;
    }

    @Override
    public Graphics getGraphics() {
        throw new IllegalStateException("We are using OpenGL!");
    }

    @Override
    public void setScreen(Screen screen) {
        if (screen == null)
            throw new IllegalArgumentException("Screen must not be null");

        this.screen.pause();
        this.screen.dispose();
        screen.resume();
        screen.update(0);
        this.screen = screen;
    }

    @Override
    public Screen getCurrentScreen() {
        return screen;
    }

    @Override
    public Screen getStartScreen() {
        return new WeatherScreen(this);
    }

    @Override
    public Input getInput() {
        return input;
    }

    @Override
    public Audio getAudio() {
        return null;
    }
}
