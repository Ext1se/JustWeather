package com.ext1se.opengl.weather;

import com.ext1se.opengl.framework.DynamicGameObject;

import java.util.Random;

public class Star extends DynamicGameObject {
    public static final float STAR_WIDTH = 1f;
    public static final float STAR_HEIGHT = 1f;

    public static final float STAR_MOVE_VELOCITY_Y = 4f;
    public static final float STAR_MOVE_VELOCITY_X = 1f;

    float stateTime;
    float width, height;
    float speed;
    float speedX, speedY;

    float minSize, maxSize;
    boolean falling = false;

    public Star(float x, float y) {
        super(x, y, STAR_WIDTH , STAR_HEIGHT);
        Random r = new Random();
        height = r.nextFloat() / 1.5f + 0.2f;
        width = height;
        stateTime = 0;
        speed = r.nextFloat() / 10 + 0.1f;
        minSize = 0.3f;
        maxSize = width + 0.2f;
        falling = false;
    }

    public Star() {
        super( WorldWeather.WORLD_WIDTH + new Random().nextInt((int)WorldWeather.WORLD_HEIGHT),
                WorldWeather.WORLD_HEIGHT + new Random().nextInt((int)WorldWeather.WORLD_HEIGHT), STAR_WIDTH , STAR_HEIGHT);
        height = new Random().nextFloat() / 4f + 0.2f;
        width = height;
        stateTime = 0;
        falling = true;
        speedX = -2.5f - new Random().nextInt(3);
        speedY = -4 - new Random().nextInt(4);
        //velocity.x = STAR_MOVE_VELOCITY_X;
        //velocity.y =
    }


    public void update(float deltaTime) {
        if(!falling) {
            width = width + speed * deltaTime;
            height = width;

            if (width >= maxSize)
                speed *= -1;
            if (width <= minSize)
                speed *= -1;
        }
        else
        {
            velocity.add(-5 * deltaTime, -5  * deltaTime);
            position.x = position.x + velocity.x * deltaTime;
            position.y = position.y + velocity.y * deltaTime;
        }

        stateTime += deltaTime;
    }
}
