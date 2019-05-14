package com.ext1se.weather.utils;

import android.graphics.Rect;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

public class SpanUtils extends MetricAffectingSpan {
    private double mRatio = 0.5;
    private String mText = "-23,5";

    public SpanUtils() {
    }

    public SpanUtils(double ratio, String text) {
        mRatio = ratio;
        mText = text;
    }

    public SpanUtils(double ratio) {
        mRatio = ratio;
    }

    @Override
    public void updateDrawState(TextPaint paint) {
        Rect bounds = new Rect();
        paint.getTextBounds(mText, 0, mText.length(), bounds);
        int shift = bounds.top - bounds.bottom;
        paint.setTextSize((int)(paint.getTextSize() * mRatio));
        paint.getTextBounds(mText, 0, mText.length(), bounds);
        shift += bounds.bottom - bounds.top;
        paint.baselineShift += shift;
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        updateDrawState(paint);
    }
}
