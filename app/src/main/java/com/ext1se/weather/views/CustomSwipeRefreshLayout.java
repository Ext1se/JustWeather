package com.ext1se.weather.views;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/*
 Проблема: у нас есть график, который имеет горизонтальный скроллинг, и свайп-рефреш, который имеет вертикальный скроллинг.
 Свайп перехватывает событие касания, из-за чего горизонтальный скроллинг прекращается (становится дерганным).
 Данный класс исправляет эту проблему.
 Решение проблемы:
 https://stackoverflow.com/questions/23989910/horizontalscrollview-inside-swiperefreshlayout
 ----
 Есть красивая алтернативная библиотека свайп-рефреша.
 https://github.com/bingoogolapple/BGARefreshLayout-Android
 Проверена. Описанная выше проблема не встречается.
 */

public class CustomSwipeRefreshLayout extends SwipeRefreshLayout {
    private int mTouchSlop;
    private float mPrevX;

    public CustomSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevX = MotionEvent.obtain(event).getX();
                break;

            case MotionEvent.ACTION_MOVE:
                final float eventX = event.getX();
                float xDiff = Math.abs(eventX - mPrevX);
                if (xDiff > mTouchSlop) {
                    return false;
                }
        }
        return super.onInterceptTouchEvent(event);
    }
}