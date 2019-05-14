package com.ext1se.opengl.weather;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.ext1se.opengl.framework.Screen;
import com.ext1se.opengl.framework.impl.GLGame;

import javax.microedition.khronos.opengles.GL10;

public class WeatherGL extends GLGame {

    private int mStateWeather;
    private int mColorTop;
    private int mColorBottom;

    public WeatherGL(Context context, GLSurfaceView glView) {
        super(context, glView);
    }

    public void updateScreen(int stateWeather, int colorTop, int colorBottom) {
        mStateWeather = stateWeather;
        mColorTop = colorTop;
        mColorBottom = colorBottom;
        if (screen != null) {
            synchronized (stateChanged) {
                state = GLGameState.Restarted;
                stateChanged.notifyAll();
            }
        }
        else{
            int orientation = context.getResources().getConfiguration().orientation;
            screen = new WeatherScreen(this, orientation, stateWeather, colorTop, colorBottom);
            state = GLGame.GLGameState.Running;
        }
    }

    @Override
    public Screen getStartScreen() {
        return new WeatherScreen(this);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        super.onDrawFrame(gl);
        if (state == GLGameState.Restarted) {
            screen.pause();
            synchronized (stateChanged) {
                if (screen instanceof WeatherScreen) {
                    ((WeatherScreen) screen).restart(mStateWeather, mColorTop, mColorBottom);
                }
                this.state = GLGameState.Running;
                stateChanged.notifyAll();
            }
        }
    }
}
