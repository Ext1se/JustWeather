package com.ext1se.opengl.weather;

import com.ext1se.opengl.framework.DynamicGameObject;

import java.util.Random;

public class Rain extends DynamicGameObject {
    public static final float RAIN_MOVE_VELOCITY_Y = 4f;
    public static final float RAIN_MOVE_VELOCITY_X = 0.02f;
    public static final float RAIN_WIDTH = 0.5f;
    public static final float RAIN_HEIGHT = 0.5f;

    float stateTime;
    float width, height;
    float rotation;
    float startVelocity;
    float finishVelocity;

    public Rain(float x, float y) {
        super(x, y, RAIN_WIDTH, RAIN_HEIGHT);
        Random r = new Random();
        width = r.nextFloat() / 5f + 0.1f;
        height = width * 1.25f;
        rotation = 0;
        stateTime = 0;
        startVelocity = width * 8.0f;
        finishVelocity = startVelocity + RAIN_MOVE_VELOCITY_Y;
    }

    public void update(float deltaTime, float rotation) {
        position.x = position.x + velocity.x;
        //velocity.add(WorldWeather.gravity.x * deltaTime, -RAIN_MOVE_VELOCITY_Y / 2  * deltaTime);
        velocity.add(WorldWeather.gravity.x * deltaTime, -startVelocity  * deltaTime);
        if(position.x < -RAIN_WIDTH)
            position.x = WorldWeather.WORLD_WIDTH + RAIN_WIDTH;
        if(position.x > WorldWeather.WORLD_WIDTH + RAIN_WIDTH)
            position.x = -RAIN_WIDTH;
        if(position.y <= 0) {
            position.y = WorldWeather.WORLD_HEIGHT + height;
        }
        else {
            if (Math.abs(velocity.y) <= finishVelocity)
                position.y = position.y + velocity.y * deltaTime;
            else
                position.y = position.y - finishVelocity * deltaTime;
        }

        this.rotation = rotation;
        stateTime += deltaTime;
    }
}
