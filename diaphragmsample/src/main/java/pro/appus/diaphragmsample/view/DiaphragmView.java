package pro.appus.diaphragmsample.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import pro.appus.diaphragmsample.R;

/**
 * Created by vladimiryerokhin on 7/13/16
 */
public class DiaphragmView extends View {

    private ExecutorService executor;
    private RectF oval;
    private Paint borderPaint, bgPaint;
    private Path path;

    private float diaphragmSize;
    private float defaultOpeningValue;
    private float openingValue;
    private float radiusOuter;
    private int borderColor;
    private int bgColor;
    private int diaphragmPetalsCount;
    private int diaphragmBorderWidth;

    /**
     * Instantiates a new DiaphragmView and initializes ExecutorService.
     *
     * @param context the context
     */
    public DiaphragmView(Context context) {
        super(context);
        executor = Executors.newSingleThreadExecutor();
        init();
    }

    /**
     * Instantiates a new DiaphragmView.
     *
     * @param context the context
     * @param attrs   the attributes defined in the XML file
     */
    public DiaphragmView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomAttributes(context, attrs);
        executor = Executors.newSingleThreadExecutor();
        init();
    }

    /**
     * Returns the DiaphragmView to its original state.
     */
    public void reset() {
        setOpeningValue(defaultOpeningValue);
        invalidate();
    }

    /**
     * Sets diaphragm's opening value.
     *
     * @param openingValue the opening value (varies from 0.0f to 1.0f)
     */
    public void setOpeningValue(float openingValue) {
        this.openingValue = openingValue;
        invalidate();
    }

    /**
     * Gets current diaphragm's opening value.
     */
    public float getOpeningValue() {
        return openingValue;
    }

    /**
     * Sets diaphragm's petals number and initializes ExecutorService.
     *
     * @param customPetalsCount the number of petals
     */
    public void setDiaphragmPetalsCount(int customPetalsCount) {
        this.diaphragmPetalsCount = customPetalsCount;
        invalidate();
    }

    private void init() {
        path = new Path();

        borderPaint = new Paint();
        borderPaint.setStrokeWidth(diaphragmBorderWidth);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(borderColor);
        borderPaint.setAntiAlias(true);

        bgPaint = new Paint();
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(bgColor);
        bgPaint.setAntiAlias(true);

        oval = new RectF();
    }

    /**
     * Sets custom attributes from XML file.
     *
     * @param context the context
     * @param attrs   the attributes defined in the XML file
     */
    public void setCustomAttributes(Context context, AttributeSet attrs) {
        TypedArray attributeValuesArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DiaphragmView, 0, 0);
        try {
            diaphragmSize = attributeValuesArray.getDimension(R.styleable.DiaphragmView_diaphragm_size, 150);

            borderColor = attributeValuesArray.getColor(R.styleable.DiaphragmView_diaphragm_border_color, Color.BLACK);
            bgColor = attributeValuesArray.getColor(R.styleable.DiaphragmView_diaphragm_background_color, Color.GRAY);

            diaphragmPetalsCount = attributeValuesArray.getInt(R.styleable.DiaphragmView_diaphragm_petals_count, 6);
            diaphragmBorderWidth = attributeValuesArray.getInt(R.styleable.DiaphragmView_diaphragm_border_width, 3);

            defaultOpeningValue = attributeValuesArray.getFloat(R.styleable.DiaphragmView_diaphragm_default_opening_value, 0.85f);
            openingValue = defaultOpeningValue;
        } finally {
            attributeValuesArray.recycle();
        }
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        setDimensions();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension((int) diaphragmSize, (int) diaphragmSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path.reset();

        float sectorAngle = (float) (Math.PI * 2 / diaphragmPetalsCount);
        float radiusInner = radiusOuter * openingValue;

        for (int i = 0; i < diaphragmPetalsCount; i++) {
            float baseAngle = (float) (sectorAngle * i + openingValue * Math.PI);
            float alpha = (float) (Math.PI - (Math.PI - 2 * Math.PI / diaphragmPetalsCount) / 2);
            float offsetAngel = (float) (Math.PI - Math.asin(radiusInner / radiusOuter * Math.sin(alpha)) - alpha);

            path.addArc(oval,
                    (float) (radiansToDegrees((baseAngle + offsetAngel))),
                    (float) radiansToDegrees(sectorAngle));

            path.lineTo((float) (diaphragmSize / 2 + radiusInner * Math.cos(baseAngle)),
                    (float) (diaphragmSize / 2 + radiusInner * Math.sin(baseAngle)));

            path.close();
        }
        canvas.drawPath(path, bgPaint);
        canvas.drawPath(path, borderPaint);
    }

    private void setDimensions() {
        float center_x = diaphragmSize / 2;
        float center_y = diaphragmSize / 2;
        radiusOuter = (diaphragmSize / 2) - diaphragmBorderWidth;
        oval.set(center_x - radiusOuter,
                center_y - radiusOuter,
                center_x + radiusOuter,
                center_y + radiusOuter);
    }

    private double radiansToDegrees(float angle) {
        return angle * 180 / Math.PI;
    }

    /**
     * The method moves the diaphragm petals.
     *
     * @param customDiaphragmShotTime the time over which the petals will move to the
     *                                preset position (in milliseconds)
     * @param customOpenValue         the degree of opening of the diaphragm,
     *                                to which must occur moving petals (varies from 0.0f to 1.0f)
     */
    public void makeShot(final long customDiaphragmShotTime, final float customOpenValue) {
        final Handler h = new Handler();

        executor.submit(new Runnable() {

            public void run() {
                final float step = (Math.abs(openingValue - customOpenValue) / 100);
                try {
                    if (customOpenValue < openingValue) {
                        for (int i = 100; i >= 1; i--) {
                            TimeUnit.MILLISECONDS.sleep(customDiaphragmShotTime / 100);
                            h.post(new Runnable() {
                                public void run() {
                                    setOpeningValue(openingValue - step);
                                }
                            });
                        }
                    } else {
                        for (int i = 100; i >= 1; i--) {
                            TimeUnit.MILLISECONDS.sleep(customDiaphragmShotTime / 100);
                            h.post(new Runnable() {
                                public void run() {
                                    setOpeningValue(openingValue + step);
                                }
                            });
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
