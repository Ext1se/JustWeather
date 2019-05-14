package com.ext1se.weather.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.util.AttributeSet;

import com.ext1se.weather.R;
import com.ext1se.weather.data.model.Weather;
import com.ext1se.weather.ui.WeatherDesign;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;


public class ChartWeatherView extends LineChartView {

    private LineChartData mLineChartData;
    private boolean mHasAxes = true;
    private boolean mHasAxesNames = false;
    private boolean mHasLines = true;
    private boolean mHasPoints = true;
    private ValueShape mShape = ValueShape.CIRCLE;
    private boolean mIsFilled = true;
    private boolean mHasLabels = true;
    private boolean mHasDescription = true;
    private boolean mIsCubic = true;
    private boolean mHasLabelForSelected = false;
    private boolean mHasGradientToTransparent = true;

    private int mScale = 40;

    public ChartWeatherView(Context context) {
        super(context);
    }

    public ChartWeatherView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChartWeatherView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void generateData(List<Weather> weathers) {
        float scaledDensity = getResources().getDisplayMetrics().scaledDensity;

        int countValues = weathers.size() + 2;
        String[][] axisValues = getAxisValues(weathers);
        String[] hours = axisValues[0];
        String[] dates = axisValues[1];
        float[] temps = new float[weathers.size()];

        List<Line> lines = new ArrayList<Line>();
        List<AxisValue> axisValuesHour = new ArrayList<AxisValue>();
        List<AxisValue> axisValuesDate = new ArrayList<AxisValue>();
        List<PointValue> pointValues = new ArrayList<PointValue>();
        List<Bitmap> pointImages = new ArrayList<Bitmap>();
        WeatherDesign design = new WeatherDesign();

        pointValues.add(new PointValue(0, (float) weathers.get(0).getCondition().getTemp()));
        pointImages.add(null);
        axisValuesHour.add(new AxisValue(0).setLabel(hours[0]));
        axisValuesDate.add(new AxisValue(0).setLabel(dates[0]));

        for (int i = 0; i < weathers.size(); i++) {
            Weather weather = weathers.get(i);
            design.createWeatherState(weather);
            float temp = weather.getCondition().getTemp();
            temps[i] = temp;
            pointValues.add(new PointValue(i + 1, temp));
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), design.getWeatherImage());
            int size = ChartUtils.sp2px(scaledDensity, mScale);
            bitmap = Bitmap.createScaledBitmap(bitmap, size, size, false);
            pointImages.add(bitmap);
            axisValuesHour.add(new AxisValue(i + 1).setLabel(hours[i + 1]));
            axisValuesDate.add(new AxisValue(i + 1).setLabel(dates[i + 1]));
        }

        pointValues.add(new PointValue(countValues - 1, weathers.get(countValues - 3).getCondition().getTemp()));
        pointImages.add(null);
        axisValuesHour.add(new AxisValue(countValues - 1).setLabel(hours[countValues - 1]));
        axisValuesDate.add(new AxisValue(countValues - 1).setLabel(dates[countValues - 1]));

        //int color = getResources().getColor(R.color.colorBlackLight);
        int color = Color.WHITE;
        Line line = new Line(pointValues, pointImages);
        line.setColor(ChartUtils.COLORS[0]);
        line.setShape(mShape);
        line.setCubic(mIsCubic);
        line.setFilled(mIsFilled);
        line.setHasLabels(mHasLabels);
        line.setHasDescription(mHasDescription);
        line.setHasLabelsOnlyForSelected(mHasLabelForSelected);
        line.setHasLines(mHasLines);
        line.setHasPoints(mHasPoints);
        line.setHasGradientToTransparent(mHasGradientToTransparent);
        line.setPointRadius(8);
        line.setImageSize(mScale);
        line.setLabelBackgroundColor(Color.TRANSPARENT);
        line.setColor(color);
        line.setLabelTextColor(color);
        lines.add(line);

        mLineChartData = new LineChartData(lines);
        mLineChartData.setValueLabelsTextColor(color);
        mLineChartData.setAxisXBottom(new Axis(axisValuesDate).setHasLines(false).setHasSeparationLine(false).setTextColor(color));
        mLineChartData.setAxisXBottomAdd(new Axis(axisValuesHour).setHasLines(true).setTextColor(color));
        mLineChartData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(3));
        mLineChartData.setBaseValue(Float.NEGATIVE_INFINITY);
        setLineChartData(mLineChartData);

        Arrays.sort(temps);
        float min = temps[0];
        float max = temps[temps.length - 1];
        setViewport(min, max, countValues);
    }

    private void setViewport(float min, float max, int count) {
        int countPoints = 5;
        float stepTop = 12.0f;
        float stepBot = 5.0f;
        float paddingLeft = 0.5f;
        float paddingRight = 0.5f;
        Viewport view = new Viewport(getMaximumViewport());
        view.bottom = min - stepBot;
        view.top = max + stepTop;
        view.left = paddingLeft;
        view.right = count - 1 - paddingRight;
        setMaximumViewport(view);
        view.left = paddingLeft; //0.5
        view.right = countPoints + paddingRight; //5.5
        setCurrentViewport(view);
    }

    private String[][] getAxisValues(List<Weather> weathers) {
        int numValues = weathers.size() + 2;
        String[][] values = new String[2][numValues];
        values[0][0] = " ";
        values[1][0] = " ";
        values[0][numValues - 1] = " ";
        values[1][numValues - 1] = " ";
        String date = " ";
        for (int i = 0; i < weathers.size(); i++) {
            long day = weathers.get(i).getDateMillis();
            String h = DateFormat.format("HH:mm", new Date(day)).toString();
            values[0][i + 1] = h;
            String d = DateFormat.format("d MMM", new Date(day)).toString();
            if (d.equals(date)) {
                d = " ";
            } else {
                date = d;
            }
            values[1][i + 1] = d;
        }
        values[0][1] = getResources().getString(R.string.now);
        return values;
    }

    public void setColor(int color) {
        Line line = getLineChartData().getLines().get(0);
        line.setColor(color);
        line.setAreaColor(color);
        line.setLabelTextColor(color);
        //line.setLabelBackgroundColor(color);
        if (getLineChartData().getAxisXBottom() == null) return;
        getLineChartData().setValueLabelsTextColor(color);
        getLineChartData().getAxisXBottom().setTextColor(color);
        getLineChartData().getAxisXBottomAdd().setTextColor(color);
    }

    public void setAreaColor(int color) {
        Line line = getLineChartData().getLines().get(0);
        line.setAreaColor(color);
    }
}
