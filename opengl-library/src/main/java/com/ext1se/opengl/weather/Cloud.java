package com.ext1se.opengl.weather;

import com.ext1se.opengl.framework.DynamicGameObject;

import java.util.Random;

public class Cloud extends DynamicGameObject {
    public static final float CLOUD_MOVE_VELOCITY_Y = 0f;
    public static final float CLOUD_MOVE_VELOCITY_X = 2f;
    public static final float CLOUD_WIDTH = 2f;
    public static final float CLOUD_HEIGHT = 1f;

    float stateTime;
    float width, height;
    float speed;

    float minSize, maxSize;
    int rotation;

    public Cloud(float x, float y) {
        super(x, y, CLOUD_WIDTH, CLOUD_HEIGHT);
        Random r = new Random();
        height = r.nextFloat() * 1.7f  + 0.5f;
        width = height;
        stateTime = 0;
        speed = r.nextFloat() / 10 + 0.1f;
        minSize = width;
        maxSize = minSize + 1.2f;
        if (r.nextBoolean())
            rotation = 1;
        else
            rotation = -1;
    }

    public void update(float deltaTime) {
        //position.x = position.x + speed * deltaTime;
        if(position.x < - width)
            position.x = WorldWeather.WORLD_WIDTH + width;
        if(position.x > WorldWeather.WORLD_WIDTH + width)
            position.x = -width;


        width = width + speed * deltaTime;
        height = width;

        if (width >= maxSize)
            speed *= -1;
        if (width <= minSize)
            speed *= -1;

        stateTime += deltaTime;
    }
}
