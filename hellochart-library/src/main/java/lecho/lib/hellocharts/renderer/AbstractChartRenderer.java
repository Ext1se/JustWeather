package lecho.lib.hellocharts.renderer;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.Html;

import lecho.lib.hellocharts.R;
import lecho.lib.hellocharts.computator.ChartComputator;
import lecho.lib.hellocharts.model.ChartData;
import lecho.lib.hellocharts.model.SelectedValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;

/**
 * Abstract renderer implementation, every chart renderer extends this class(although it is not required it helps).
 */
public abstract class AbstractChartRenderer implements ChartRenderer {
    public int DEFAULT_LABEL_MARGIN_DP = 4;
    protected Chart chart;
    protected ChartComputator computator;
    /**
     * Paint for value labels.
     */
    protected Paint labelPaint = new Paint();
    //protected Paint descriptionPaint = new Paint();
    /**
     * Paint for labels background.
     */
    protected Paint labelBackgroundPaint = new Paint();
    /**
     * Holds coordinates for label background rect.
     */
    protected RectF labelBackgroundRect = new RectF();
    /**
     * Font metrics for label paint, used to determine text height.
     */
    protected FontMetricsInt fontMetrics = new FontMetricsInt();
    /**
     * If true maximum and current viewport will be calculated when chart data change or during data animations.
     */
    protected boolean isViewportCalculationEnabled = true;
    protected float density;
    protected float scaledDensity;
    protected SelectedValue selectedValue = new SelectedValue();
    protected char[] labelBuffer = new char[64];
    protected int labelOffset;
    protected int labelMargin;
    protected boolean isValueLabelBackgroundEnabled;
    protected boolean isValueLabelBackgroundAuto;

    private Resources resources;

    public AbstractChartRenderer(Context context, Chart chart) {
        this.density = context.getResources().getDisplayMetrics().density;
        this.scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        this.chart = chart;
        this.computator = chart.getChartComputator();

        labelMargin = ChartUtils.dp2px(density, DEFAULT_LABEL_MARGIN_DP);
        labelOffset = labelMargin;

        labelPaint.setAntiAlias(true);
        labelPaint.setStyle(Paint.Style.FILL);
        labelPaint.setTextAlign(Align.LEFT);
        labelPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        labelPaint.setColor(Color.WHITE);

/*        descriptionPaint.setAntiAlias(true);
        descriptionPaint.setStyle(Paint.Style.FILL);
        descriptionPaint.setTextAlign(Align.LEFT);
        descriptionPaint.setTypeface(Typeface.createFromAsset(context.getAssets(), "weather_icons_webfont.ttf"));
        descriptionPaint.setColor(Color.WHITE);*/

        labelBackgroundPaint.setAntiAlias(true);
        labelBackgroundPaint.setStyle(Paint.Style.FILL);

        resources = context.getResources();
    }

    @Override
    public void resetRenderer() {
        this.computator = chart.getChartComputator();
    }

    @Override
    public void onChartDataChanged() {
        final ChartData data = chart.getChartData();

        Typeface typeface = chart.getChartData().getValueLabelTypeface();
        if (null != typeface) {
            labelPaint.setTypeface(typeface);
            //descriptionPaint.setTypeface(typeface);
        }

        labelPaint.setColor(data.getValueLabelTextColor());
        //labelPaint.setTextSize(ChartUtils.sp2px(scaledDensity, data.getValueLabelTextSize()));
        labelPaint.setTextSize(ChartUtils.sp2px(scaledDensity, 14));
        labelPaint.getFontMetricsInt(fontMetrics);

/*        descriptionPaint.setColor(data.getValueLabelTextColor());
        //descriptionPaint.setTextSize(ChartUtils.sp2px(scaledDensity, data.getValueLabelTextSize()));
        descriptionPaint.setTextSize(ChartUtils.sp2px(scaledDensity, 18));*/

        this.isValueLabelBackgroundEnabled = data.isValueLabelBackgroundEnabled();
        this.isValueLabelBackgroundAuto = data.isValueLabelBackgroundAuto();
        this.labelBackgroundPaint.setColor(data.getValueLabelBackgroundColor());

        // Important - clear selection when data changed.
        selectedValue.clear();
    }

    /**
     * Draws label text and label background if isValueLabelBackgroundEnabled is true.
     */
/*    protected void drawLabelTextAndBackground(Canvas canvas, char[] labelBuffer, int startIndex, int numChars,
                                              int autoBackgroundColor) {
        final float textX;
        final float textY;

        if (isValueLabelBackgroundEnabled) {

            if (isValueLabelBackgroundAuto) {
                labelBackgroundPaint.setColor(autoBackgroundColor);
            }

            canvas.drawRect(labelBackgroundRect, labelBackgroundPaint);

            textX = labelBackgroundRect.left + labelMargin;
            textY = labelBackgroundRect.bottom - labelMargin;
        } else {
            textX = labelBackgroundRect.left;
            textY = labelBackgroundRect.bottom;
        }

        canvas.drawText(labelBuffer, startIndex, numChars, textX, textY, labelPaint);
    }*/

    protected void drawLabelTextAndBackground(Canvas canvas, char[] labelBuffer, int startIndex, int numChars,
                                              int autoBackgroundColor, int textColor) {
        final float textX;
        final float textY;

        if (isValueLabelBackgroundEnabled) {

            if (isValueLabelBackgroundAuto) {
                labelBackgroundPaint.setColor(autoBackgroundColor);
            }

            canvas.drawRect(labelBackgroundRect, labelBackgroundPaint);

            textX = labelBackgroundRect.left + labelMargin;
            textY = labelBackgroundRect.bottom - labelMargin;
        } else {
            textX = labelBackgroundRect.left;
            textY = labelBackgroundRect.bottom;
        }

        labelPaint.setColor(textColor);
        String value = String.copyValueOf(labelBuffer, startIndex, numChars) + "\u00B0";
        //canvas.drawText(labelBuffer, startIndex, numChars, textX, textY, labelPaint);
        canvas.drawText(value, textX, textY, labelPaint);
    }

/*    protected void drawLabelTextAndBackground(Canvas canvas, char[] labelBuffer, int startIndex, int numChars, String description,
                                              int autoBackgroundColor, int textColor) {
        final float textX;
        final float textY;

        if (isValueLabelBackgroundEnabled) {

            if (isValueLabelBackgroundAuto) {
                labelBackgroundPaint.setColor(autoBackgroundColor);
            }

            canvas.drawRect(labelBackgroundRect, labelBackgroundPaint);

            textX = labelBackgroundRect.left + labelMargin;
            textY = labelBackgroundRect.bottom - labelMargin;
        } else {
            textX = labelBackgroundRect.left;
            textY = labelBackgroundRect.bottom;
        }

        canvas.drawText(labelBuffer, startIndex, numChars, textX, textY, labelPaint);

        float posX = labelBackgroundRect.left + labelBackgroundRect.width() / 2 - descriptionPaint.getTextSize() / 2;
        float posY = labelBackgroundRect.top - labelMargin / 2;

        descriptionPaint.setColor(textColor);
        canvas.drawText(description, posX, posY, descriptionPaint);
    }*/

    protected void drawLabelImage(Canvas canvas, Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }

        float posX = labelBackgroundRect.left - bitmap.getWidth() / 2 + labelBackgroundRect.width() / 2 ;
        float posY = labelBackgroundRect.top - bitmap.getHeight();

        canvas.drawBitmap(bitmap, posX, posY, null);
    }

    protected void drawLabelImage(Canvas canvas, Integer id) {
        if (id == null) {
            return;
        }
        Bitmap bitmap = BitmapFactory.decodeResource(resources, id);
        int size = ChartUtils.sp2px(scaledDensity, 32);
        bitmap = Bitmap.createScaledBitmap(bitmap, size, size, false);
        float posX = labelBackgroundRect.left - bitmap.getWidth() / 4;;
        float posY = labelBackgroundRect.top - bitmap.getHeight();

        canvas.drawBitmap(bitmap, posX, posY, null);
    }

    @Override
    public boolean isTouched() {
        return selectedValue.isSet();
    }

    @Override
    public void clearTouch() {
        selectedValue.clear();
    }

    @Override
    public Viewport getMaximumViewport() {
        return computator.getMaximumViewport();
    }

    @Override
    public void setMaximumViewport(Viewport maxViewport) {
        if (null != maxViewport) {
            computator.setMaxViewport(maxViewport);
        }
    }

    @Override
    public Viewport getCurrentViewport() {
        return computator.getCurrentViewport();
    }

    @Override
    public void setCurrentViewport(Viewport viewport) {
        if (null != viewport) {
            computator.setCurrentViewport(viewport);
        }
    }

    @Override
    public boolean isViewportCalculationEnabled() {
        return isViewportCalculationEnabled;
    }

    @Override
    public void setViewportCalculationEnabled(boolean isEnabled) {
        this.isViewportCalculationEnabled = isEnabled;
    }

    @Override
    public void selectValue(SelectedValue selectedValue) {
        this.selectedValue.set(selectedValue);
    }

    @Override
    public SelectedValue getSelectedValue() {
        return selectedValue;
    }
}
