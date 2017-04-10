package com.themindstudios.circlecontrolview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.RotateAnimation;

import com.themindstudios.circlecontrolview.R;
import com.themindstudios.circlecontrolview.ValuesChecker;
import com.themindstudios.circlecontrolview.controller.CircleGestureController;

public class CircleControlView extends View {

    //Default values
    private final int defaultMinValue = 0;
    private final int defaultMaxValue = 100;
    private final int defaultNumberOfCircles = 1;
    private final int defaultValue = 0;
    private Properties properties;

    private CircleGestureController circleGestureController;
    private RotateAnimation rotateAnimation;
    private int currentNumberOfCircles;

    private ValuesChecker valuesChecker;
    private OnValueChangedCallback onValueChangedCallback;

    //Angle and number of circles values
    private double prevAngle;

    private double anglePerValue;

    //User's parameters
    private int minValue;
    private int maxValue;
    private int numberOfCircles;
    private int value;

    //Default & pressed backgrounds
    private Drawable pressedBackground;
    private Drawable defaultBackground;

    //Rotation enabled/disabled by user
    private boolean isRotationEnabled = true;

    public CircleControlView(Context context) {
        super(context);
    }

    public CircleControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleControlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        valuesChecker = new ValuesChecker();

        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleControlView, 0, 0);

        minValue = typedArray.getInt(R.styleable.CircleControlView_minValue, defaultMinValue);
        maxValue = typedArray.getInt(R.styleable.CircleControlView_maxValue, defaultMaxValue);
        value = typedArray.getInt(R.styleable.CircleControlView_currentValue, defaultValue);
        numberOfCircles = typedArray.getInt(R.styleable.CircleControlView_numberOfCircles, defaultNumberOfCircles);
        defaultBackground = typedArray.getDrawable(R.styleable.CircleControlView_android_background);
        pressedBackground = typedArray.getDrawable(R.styleable.CircleControlView_drawablePressedState);

        setViewBackground(defaultBackground);
        typedArray.recycle();

        properties = newPropertiesBuilder()
                .minValue(minValue)
                .maxValue(maxValue)
                .numberOfCircles(numberOfCircles)
                .value(value)
                .build();

        if (!valuesChecker.areValuesValid(minValue, maxValue, numberOfCircles)) {
            throw new IllegalArgumentException(valuesChecker.getMessage());
        }

        if (!valuesChecker.isValueValid(properties.getValue(), properties.getMinValue(), properties.getMaxValue())) {
            throw new IllegalArgumentException(valuesChecker.getMessage());
        }

        defineAnglePerValue();

        this.circleGestureController = new CircleGestureController();
        this.circleGestureController.setMaxNumberOfCircles(numberOfCircles);
        this.circleGestureController.setActionEventCallback(onActionEventCallback);

        if (value != minValue) {
            this.circleGestureController.setCurrentAngle(getAngleForValue());
        }

        setOnTouchListener(onTouchListener);
    }

    public void setProperties(Properties properties) {
        this.properties = properties;

        if (!valuesChecker.areValuesValid(properties.getMinValue(), properties.getMaxValue(), properties.getNumberOfCircles())) {
            throw new IllegalArgumentException(valuesChecker.getMessage());
        }

        if (!valuesChecker.isValueValid(properties.getValue(), properties.getMinValue(), properties.getMaxValue())) {
            throw new IllegalArgumentException(valuesChecker.getMessage());
        }

        minValue = properties.getMinValue();
        maxValue = properties.getMaxValue();
        value = properties.getValue();
        numberOfCircles = properties.getNumberOfCircles();

        defineAnglePerValue();

        currentNumberOfCircles = countNumberOfPassedCircles();

        if (circleGestureController != null) {
            this.circleGestureController.setMaxNumberOfCircles(numberOfCircles);
            if (value != minValue) {
                this.circleGestureController.setCurrentAngle(getAngleForValue());
            }
        }
    }

    public static Builder newPropertiesBuilder() {
        return new Builder();
    }

    public void setOnValueChangedCallback(OnValueChangedCallback onValueChangedCallback) {
        this.onValueChangedCallback = onValueChangedCallback;
    }

    public void setRotationEnabled(boolean isRotationEnabled) {
        this.isRotationEnabled = isRotationEnabled;
    }

    public boolean getRotationEnabled() {
        return isRotationEnabled;
    }

    public int getMinValue() {
        return properties.getMinValue();
    }

    public int getMaxValue() {
        return properties.getMaxValue();
    }

    public int getNumberOfCircles() {
        return properties.getNumberOfCircles();
    }

    public int getValue() {
        return properties.getValue();
    }

    public Drawable getPressedBackground() {
        return pressedBackground;
    }

    public void setPressedBackground(Drawable pressedBackground) {
        this.pressedBackground = pressedBackground;
        setViewBackground(pressedBackground);
    }

    public Drawable getDefaultBackground() {
        return defaultBackground;
    }

    public void setDefaultBackground(Drawable defaultBackground) {
        this.defaultBackground = defaultBackground;
        setViewBackground(defaultBackground);
    }

    private OnTouchListener onTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return isRotationEnabled && circleGestureController != null
                    && circleGestureController.trackGestures(view, motionEvent);

        }
    };

    private void setViewBackground(Drawable background) {
        if (background == null) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(background);
            return;
        }
        setBackgroundDrawable(background);
    }

    private CircleGestureController.OnActionEventCallback onActionEventCallback = new CircleGestureController.OnActionEventCallback() {

        @Override
        public void onActionEvent(int motionEventAction, double angle) {

            switch (motionEventAction) {
                case MotionEvent.ACTION_MOVE:
                    calculateValue(angle);

                    startAnimation(prevAngle, angle, 0);
                    prevAngle = angle;
                    break;
                case MotionEvent.ACTION_DOWN:
                    setViewBackground(pressedBackground);
                    break;
                case MotionEvent.ACTION_UP:
                    setViewBackground(defaultBackground);
                    break;
                default:
                    break;
            }
        }
    };

    private void calculateValue(double angle) {

        this.value = (int) (angle * anglePerValue) + minValue;

        if (onValueChangedCallback != null) {
            onValueChangedCallback.onValueChanged(value);
        }
    }

    private double getAngleForValue() {

        final int absolute = maxValue - minValue;
        final double numberOfValuesPerCircle = absolute / numberOfCircles;
        final int differenceTowardsWhichWeCount = value - minValue;
        final int currentNumberOfPassedCircles = countNumberOfPassedCircles();
        final double currentValue = differenceTowardsWhichWeCount - numberOfValuesPerCircle * currentNumberOfPassedCircles;

        return (currentValue * 360) / numberOfValuesPerCircle + (360 * currentNumberOfPassedCircles);
    }

    private void defineAnglePerValue() {
        final int absoluteValue = maxValue - minValue;
        this.anglePerValue = (double) absoluteValue / (360 * numberOfCircles);
    }

    private int countNumberOfPassedCircles() {
        if (value == maxValue) {
            return numberOfCircles;
        }

        return maxValue / value;
    }

    /**
     * Method to rotate image around its axis.
     * isFillEnabled and fillAfter - to image at that position in which we left it
     */
    private void startAnimation(double fromDegrees, double toDegrees, long durationMillis) {
        rotateAnimation = new RotateAnimation((float) fromDegrees, (float) toDegrees,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(durationMillis);
        rotateAnimation.setFillEnabled(true);
        rotateAnimation.setFillAfter(true);
        startAnimation(rotateAnimation);
    }

    public interface OnValueChangedCallback {
        void onValueChanged(int value);
    }

    public static class Builder {
        int minValue;
        int maxValue;
        int numberOfCircles;
        int value;

        private Builder() {
        }

        public Builder minValue(int minValue) {
            this.minValue = minValue;
            return this;
        }

        public Builder maxValue(int maxValue) {
            this.maxValue = maxValue;
            return this;
        }

        public Builder numberOfCircles(int numberOfCircles) {
            this.numberOfCircles = numberOfCircles;
            return this;
        }

        public Builder value(int value) {
            this.value = value;
            return this;
        }

        public Properties build() {
            return new Properties(this);
        }
    }
}
