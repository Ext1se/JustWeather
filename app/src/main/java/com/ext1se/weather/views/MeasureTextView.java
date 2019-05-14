package com.ext1se.weather.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.MeasureFormat;
import android.icu.util.Measure;
import android.icu.util.MeasureUnit;
import android.icu.util.ULocale;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableString;
import android.util.AttributeSet;

import com.ext1se.weather.R;
import com.ext1se.weather.utils.SpanUtils;

import java.math.BigDecimal;

public class MeasureTextView extends AppCompatTextView {

    private static final int UNIT_METRIC = 0;
    private static final int UNIT_IMPERIAL = 1;

    public static final int MEASURE_HUMIDITY = 0;
    public static final int MEASURE_TEMPERATURE = 1;
    public static final int MEASURE_PRESSURE = 2;
    public static final int MEASURE_WIND = 3;

    private final float mRatio = 0.65f;
    private int mUnits = UNIT_METRIC;

    public MeasureTextView(Context context) {
        super(context);
    }

    public MeasureTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MeasureTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setMeasureText(float value, int typeMeasure) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String units = sharedPreferences.getString("key_units", getContext().getResources().getStringArray(R.array.units_format_value)[0]); //metric
        if (units.equals(getContext().getResources().getStringArray(R.array.units_format_value)[0])) {
            mUnits = UNIT_METRIC;
        } else {
            mUnits = UNIT_IMPERIAL;
        }
        BigDecimal val = new BigDecimal(value);
        value = val.setScale(1, BigDecimal.ROUND_HALF_EVEN).floatValue();
        setText(getMeasureString(typeMeasure, value));
        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            setText(getMeasureStringN(typeMeasure, value));
        } else {
            setText(getMeasureString(typeMeasure, value));
        }
        */
    }

    /*
    TODO: проблема с MeasureFormat
    Для Английской локализации странное поведение: символ Цельсия не показывается, а Фаренгейд показывается,
    поэтому показ сивмола температуры с разным размером сбивается
    + все значения пишутся слитно без пробела между значением и ед. изм.
     */
    @TargetApi(Build.VERSION_CODES.N)
    private CharSequence getMeasureStringN(int typeMeasure, float value) {
        MeasureFormat measureFormat = MeasureFormat.getInstance(ULocale.getDefault(), MeasureFormat.FormatWidth.NARROW);
        Measure measure;
        switch (typeMeasure) {
            case (MEASURE_TEMPERATURE):
                if (mUnits == UNIT_METRIC) {
                    measure = new Measure(value, MeasureUnit.CELSIUS);
                } else {
                    measure = new Measure(value, MeasureUnit.FAHRENHEIT);
                }
                String measureString = measureFormat.format(measure);
                SpannableString measureSpannable = new SpannableString(measureString);
                measureSpannable.setSpan(new SpanUtils(mRatio, measureString), measureString.length() - 2, measureString.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                return measureString;
            case (MEASURE_PRESSURE):
                measure = new Measure(value, MeasureUnit.HECTOPASCAL);
                return getResources().getString(R.string.pressure) + ": " + measureFormat.format(measure);
            case (MEASURE_WIND):
                if (mUnits == UNIT_METRIC) {
                    measure = new Measure(value, MeasureUnit.METER_PER_SECOND);
                } else {
                    measure = new Measure(value, MeasureUnit.MILE_PER_HOUR);
                }
                return getResources().getString(R.string.wind) + ": " + measureFormat.format(measure);
            default:
                return getResources().getString(R.string.humidity) + ": " + value + " " + getResources().getString(R.string.percent);
        }
    }

    private CharSequence getMeasureString(int typeMeasure, float value) {
        String unit;
        switch (typeMeasure) {
            case (MEASURE_TEMPERATURE):
                if (mUnits == UNIT_METRIC) {
                    unit = getResources().getString(R.string.celsius);
                } else {
                    unit = getResources().getString(R.string.fahrenheit);
                }
                SpannableString measureSpannable = new SpannableString(value + " " + unit);
                measureSpannable.setSpan(new SpanUtils(mRatio, measureSpannable.toString()), measureSpannable.length() - 2, measureSpannable.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                return measureSpannable;
            case (MEASURE_PRESSURE):
                return getResources().getString(R.string.pressure) + ": " + value + " " + getResources().getString(R.string.pressure_pascal);
            case (MEASURE_WIND):
                if (mUnits == UNIT_METRIC) {
                    unit = getResources().getString(R.string.meter_per_second);
                } else {
                    unit = getResources().getString(R.string.mile_per_hour);
                }
                return getResources().getString(R.string.wind) + ": " + value + " " + unit;
            case (MEASURE_HUMIDITY):
                return getResources().getString(R.string.humidity) + ": " + value + " " + getResources().getString(R.string.percent);
            default:
                return null;
        }
    }
}
