package com.themindstudios.circlecontrolview.widget;

final public class Properties {

    private int minValue;
    private int maxValue;
    private int numberOfCircles;
    private int value;

    Properties(CircleControlView.Builder builder) {
        this.minValue = builder.minValue;
        this.maxValue = builder.maxValue;
        this.numberOfCircles = builder.numberOfCircles;
        this.value = builder.value;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getNumberOfCircles() {
        return numberOfCircles;
    }

    public void setNumberOfCircles(int numberOfCircles) {
        this.numberOfCircles = numberOfCircles;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
