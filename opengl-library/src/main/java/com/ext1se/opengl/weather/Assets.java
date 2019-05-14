package com.ext1se.opengl.weather;

import com.ext1se.opengl.framework.gl.Texture;
import com.ext1se.opengl.framework.gl.TextureRegion;
import com.ext1se.opengl.framework.impl.GLGame;

public class Assets {
    public static Texture weather;

    public static TextureRegion snow1;
    public static TextureRegion snow2;
    public static TextureRegion snow3;
    public static TextureRegion snow4;
    public static TextureRegion snow5;
    public static TextureRegion snow6;
    public static TextureRegion snow7;
    public static TextureRegion snow8;

    public static TextureRegion rain1;
    public static TextureRegion rain2;
    public static TextureRegion rain3;
    public static TextureRegion rain4;
    public static TextureRegion rain5;
    public static TextureRegion rain6;
    public static TextureRegion rain7;
    public static TextureRegion rain8;

    public static TextureRegion cloud1;
    public static TextureRegion cloud2;
    public static TextureRegion cloud3;

    public static TextureRegion star;
    public static TextureRegion comet;

    public static void load(GLGame game) {
        weather = new Texture(game, "weather.png");
        snow1 = new TextureRegion(weather, 0, 0, 64, 64);
        snow2 = new TextureRegion(weather, 64, 0, 64, 64);
        snow3 = new TextureRegion(weather, 128, 0, 64, 64);
        snow4 = new TextureRegion(weather, 192, 0, 64, 64);
        snow5 = new TextureRegion(weather, 256, 0, 64, 64);
        snow6 = new TextureRegion(weather, 320, 0, 64, 64);
        snow7 = new TextureRegion(weather, 384, 0, 64, 64);
        snow8 = new TextureRegion(weather, 448, 0, 64, 64);

        rain1 = new TextureRegion(weather, 0, 64, 64, 64);
        rain2 = new TextureRegion(weather, 64, 64, 64, 64);
        rain3 = new TextureRegion(weather, 128, 64, 64, 64);
        rain4 = new TextureRegion(weather, 192, 64, 64, 64);
        rain5 = new TextureRegion(weather, 256, 64, 64, 64);
        rain6 = new TextureRegion(weather, 320, 64, 64, 64);
        rain7 = new TextureRegion(weather, 384, 64, 64, 64);
        rain8 = new TextureRegion(weather, 448, 64, 64, 64);

        cloud1 = new TextureRegion(weather, 0, 128, 256, 128);
        cloud2 = new TextureRegion(weather, 256, 128, 256, 128);
        cloud3 = new TextureRegion(weather, 0, 256, 256, 256);

        star = new TextureRegion(weather, 256, 256, 128, 128);
        comet = new TextureRegion(weather, 384, 256, 128, 128);
    }
    
    public static void reload() {
        weather.reload();
    }
}
