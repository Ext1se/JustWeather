package com.ext1se.opengl.weather;

import android.content.res.Configuration;

import com.ext1se.opengl.framework.math.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldWeather {
    public static final int WEATHER_CLEAR = 0;
    public static final int WEATHER_SNOW = 1;
    public static final int WEATHER_RAIN = 2;
    public static final int WEATHER_CLOUD = 3;
    public static final int WEATHER_SUN = 4;
    public static final int WEATHER_NIGHT = 5;

    public static int WORLD_WIDTH = 9;
    public static int WORLD_HEIGHT = 16;
    public static final Vector2 gravity = new Vector2(0, -10f);

    public final List<Snow> snows;
    public final List<Rain> rains;
    public final List<Cloud> clouds;
    public final List<Star> stars;

    public int state;
    private int orientation;

    public WorldWeather(int state, int orientation) {
        this.orientation = orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            WORLD_WIDTH = 9;
            WORLD_HEIGHT = 16;
        } else {
            WORLD_WIDTH = 16;
            WORLD_HEIGHT = 9;
        }
        this.state = state;
        this.snows = new ArrayList<Snow>();
        this.rains = new ArrayList<Rain>();
        this.clouds = new ArrayList<Cloud>();
        this.stars = new ArrayList<Star>();
        generateWeather();
    }

    private void generateWeather() {
        switch (state) {
            case (WEATHER_CLEAR):
                break;
            case (WEATHER_SUN):
                break;
            case (WEATHER_NIGHT):
                generateStar();
                break;
            case (WEATHER_SNOW):
                generateSnow();
                break;
            case (WEATHER_RAIN):
                generateRain();
                break;
            case (WEATHER_CLOUD):
                generateCloud();
                break;
        }
    }

    private void generateSnow() {
        float x, y;
        int size = (int) Math.ceil((float)WORLD_WIDTH / 2);
        Random random = new Random();
        for (int i = 0; i < WORLD_HEIGHT / 2; i++) {

            if (i % 2 == 0) {
                for (int j = 0; j < size; j++) {
                    y = WORLD_HEIGHT + i * 2 + random.nextFloat() - 0.5f;
                    x = j * 2 + 1;
                    Snow snow = new Snow(x, y);
                    snows.add(snow);
                }
            } else {
                for (int j = 0; j < size; j++) {
                    y = WORLD_HEIGHT + i * 2 + random.nextFloat() - 0.5f;
                    x = j * 2;
                    Snow snow = new Snow(x, y);
                    snows.add(snow);
                }
            }
        }
    }

    private void generateRain() {
        float x, y;
        int size = (int) Math.ceil((float)WORLD_WIDTH / 2);
        Random random = new Random();
        for (int i = 0; i < WORLD_HEIGHT; i++) {
            if (i % 2 == 0) {
                for (int j = 0; j < size; j++) {
                    y = WORLD_HEIGHT + i + random.nextFloat() - 0.5f;
                    x = j * 2 + 1 + random.nextFloat() - 0.5f;
                    Rain rain = new Rain(x, y);
                    rains.add(rain);
                }
            } else {
                for (int j = 0; j < size; j++) {
                    y = WORLD_HEIGHT + i + random.nextFloat() - 0.5f;
                    x = j * 2 + random.nextFloat() - 0.5f;
                    Rain rain = new Rain(x, y);
                    rains.add(rain);
                }
            }
        }
    }

    private void generateLightRain() {
        float x, y;
        Random random = new Random();
        for (int i = 0; i < WORLD_HEIGHT / 2; i++) {
            if (i % 2 == 0) {
                for (int j = 0; j < 4; j++) {
                    y = WORLD_HEIGHT + i * 2 + random.nextFloat() - 0.5f;
                    x = j * 2 + 2 + random.nextFloat() - 0.5f;
                    Rain rain = new Rain(x, y);
                    rains.add(rain);
                }
            } else {
                for (int j = 0; j < 3; j++) {
                    y = WORLD_HEIGHT + i * 2 + random.nextFloat() - 0.5f;
                    x = j * 3 + 3 + random.nextFloat() - 0.5f;
                    Rain rain = new Rain(x, y);
                    rains.add(rain);
                }
            }
        }
    }

    private void generateMiddleRain() {
        float x, y;
        Random random = new Random();
        for (int i = 0; i < WORLD_HEIGHT; i++) {
            if (i % 2 == 0) {
                for (int j = 0; j < 10; j++) {
                    y = WORLD_HEIGHT + i + random.nextFloat() - 0.5f;
                    x = j + 1 + random.nextFloat() - 0.5f;
                    Rain rain = new Rain(x, y);
                    rains.add(rain);
                }
            } else {
                for (int j = 0; j < 10; j++) {
                    y = WORLD_HEIGHT + i + random.nextFloat() - 0.5f;
                    x = j + random.nextFloat() - 0.5f;
                    Rain rain = new Rain(x, y);
                    rains.add(rain);
                }
            }
        }
    }

    private void generateStrongRain() {
        float x, y;
        Random random = new Random();
        for (int i = 0; i < WORLD_HEIGHT; i++) {
            if (i % 2 == 0) {
                for (int j = 0; j < 30; j++) {
                    y = WORLD_HEIGHT + i + random.nextFloat() - 0.5f;
                    x = (float) j / 3 + 1 + random.nextFloat() - 0.5f;
                    Rain rain = new Rain(x, y);
                    rains.add(rain);
                }
            } else {
                for (int j = 0; j < 30; j++) {
                    y = WORLD_HEIGHT + i + random.nextFloat() - 0.5f;
                    x = (float) j / 3 + random.nextFloat() - 0.5f;
                    Rain rain = new Rain(x, y);
                    rains.add(rain);
                }
            }
        }
    }

    private void generateCloud() {
        float x, y;
        Random random = new Random();
        for (int i = 0; i < WORLD_HEIGHT; i += 2) {
            y = i;
            if (i % 4 == 0)
                x = WORLD_WIDTH;
            else
                x = 0;

            Cloud cloud = new Cloud(x, y);
            clouds.add(cloud);
        }
    }

    private void generateStar() {
        float x, y;
        Random random = new Random();
        for (int i = 0; i < WORLD_HEIGHT; i += 2) {
            int step = (i % 4 == 0) ? 1 : 2;
            y = i + random.nextFloat();
            x = step + random.nextFloat();
            stars.add(new Star(x, y));

            y = i + random.nextFloat();
            x = WORLD_WIDTH - step - random.nextFloat();
            stars.add(new Star(x, y));
        }

        stars.add(new Star());
        stars.add(new Star());
        stars.add(new Star());
    }

    public void update(float deltaTime, float accelX) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            accelX = 0;
        }
        switch (state) {
            case (WEATHER_CLEAR):
                break;
            case (WEATHER_NIGHT):
                updateStar(deltaTime);
                break;
            case (WEATHER_SNOW):
                updateSnows(deltaTime, accelX);
                break;
            case (WEATHER_RAIN):
                updateRains(deltaTime, accelX);
                break;
            case (WEATHER_CLOUD):
                updateClouds(deltaTime);
                break;
        }
    }

    private void updateSnows(float deltaTime, float accelX) {
        int len = snows.size();
        for (int i = 0; i < len; i++) {
            Snow snow = snows.get(i);
            snow.velocity.x = -accelX * Snow.SNOW_MOVE_VELOCITY_X;
            snow.update(deltaTime);
        }
    }

    private void updateRains(float deltaTime, float accelX) {
        int len = rains.size();
        for (int i = 0; i < len; i++) {
            Rain rain = rains.get(i);
            rain.velocity.x = -accelX * Rain.RAIN_MOVE_VELOCITY_X;
            rain.update(deltaTime, -accelX * 10);
        }
    }

    private void updateClouds(float deltaTime) {
        int len = clouds.size();
        for (int i = 0; i < len; i++) {
            Cloud cloud = clouds.get(i);
            cloud.update(deltaTime);
        }
    }

    private void updateStar(float deltaTime) {
        int len = stars.size();
        for (int i = 0; i < len; i++) {
            Star star = stars.get(i);
            star.update(deltaTime);
            if (star.position.x < -1 || star.position.y < -1) {
                stars.remove(star);
                stars.add(new Star());
            }

        }
    }

    public void restart(int state) {
        snows.clear();
        rains.clear();
        clouds.clear();
        stars.clear();
        this.state = state;
        generateWeather();
    }
}