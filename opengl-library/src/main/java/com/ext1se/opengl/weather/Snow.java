package com.ext1se.opengl.weather;

import com.ext1se.opengl.framework.DynamicGameObject;

import java.util.Random;

public class Snow extends DynamicGameObject {
    public static final float SNOW_MOVE_VELOCITY_Y = 1.0f;
    public static final float SNOW_MOVE_VELOCITY_X = 0.01f;
    public static final float SNOW_MOVE_ROTATE = 10.0f;
    public static final float SNOW_WIDTH = 0.5f;
    public static final float SNOW_HEIGHT = 0.5f;

    float stateTime;
    float width, height;
    float rotation;
    int type;

    public Snow(float x, float y) {
        super(x, y, SNOW_WIDTH, SNOW_HEIGHT);
        Random r = new Random();
        width = r.nextFloat() / 1.2f + 0.2f;
        height = width;
        type = r.nextInt(3);
        rotation = r.nextInt(180);
        stateTime = 0;
    }

    public void update(float deltaTime) {
        position.x = position.x + velocity.x;

        if (position.x < (-SNOW_WIDTH))
            position.x = WorldWeather.WORLD_WIDTH + SNOW_WIDTH;
        if (position.x > WorldWeather.WORLD_WIDTH + SNOW_WIDTH)
            position.x = -SNOW_WIDTH;
        if (position.y <= -height)
            position.y = WorldWeather.WORLD_HEIGHT + height;
        else
            position.y = position.y - SNOW_MOVE_VELOCITY_Y * deltaTime;

        if (rotation < 360)
            rotation += deltaTime * SNOW_MOVE_ROTATE;
        else
            rotation = 0;

        stateTime += deltaTime;
    }
}
