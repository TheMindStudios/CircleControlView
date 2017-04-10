package com.themindstudios.circlecontrolview;

public class ValuesChecker {

    private String message = "";

    public boolean areValuesValid(int minValue, int maxValue, int numberOfCircles) {
        if (minValue < 0) {
            message = "Min value can\'t be less than 0";
            return false;
        }

        if (maxValue <= 0) {
            message = "Max value can\'t be less than 0 or equal to it";
            return false;
        }

        if (minValue >= maxValue) {
            message = "Min value can\'t equal to the max value or get bigger it";
            return false;
        }

        if (numberOfCircles < 1) {
            message = "Number of circles can\'t be less than 1";
            return false;
        }

        return true;
    }

    public boolean isValueValid(int value, int minValue, int maxValue) {
        if (value > maxValue) {
            message = "Current value can\'t be bigger than max value";
            return false;
        }

        if (value < minValue) {
            message = "Current value can\'t be less than min value";
            return false;
        }

        return true;
    }

    public String getMessage() {
        return message;
    }

}
