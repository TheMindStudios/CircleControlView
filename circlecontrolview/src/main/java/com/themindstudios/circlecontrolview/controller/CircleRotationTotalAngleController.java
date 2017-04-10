package com.themindstudios.circlecontrolview.controller;

import android.view.MotionEvent;

import com.themindstudios.circlecontrolview.Quarter;

class CircleRotationTotalAngleController {

    private OnTotalAngleChangedListener onTotalAngleChangedListener;

    private int fullCircleCount;
    private int maxNumberOfCircles;

    private Quarter currentQuarter = Quarter.UNKNOWN;
    private Quarter prevQuarter = Quarter.UNKNOWN;

    void setOnTotalAngleChangedListener(OnTotalAngleChangedListener onTotalAngleChangedListener) {
        this.onTotalAngleChangedListener = onTotalAngleChangedListener;
    }

    void calculateTotalAngle(int motionEventAction, double angle) {

        if (currentQuarter.equals(Quarter.FIRST) && prevQuarter.equals(Quarter.FOURTH)) {
            prevQuarter = currentQuarter;

            if (fullCircleCount == maxNumberOfCircles) return;

            fullCircleCount++;
        }

        if (currentQuarter.equals(Quarter.FOURTH) && prevQuarter.equals(Quarter.FIRST)) {
            prevQuarter = currentQuarter;

            if (fullCircleCount < 0) return;

            fullCircleCount--;
        }

        if (fullCircleCount == maxNumberOfCircles) {
            angle = 0;
        }

        if (fullCircleCount < 0) {
            angle = 360;
        }

        final double totalAngle = angle + fullCircleCount * 360;

        if (onTotalAngleChangedListener != null) {
            onTotalAngleChangedListener.onTotalAngleChanged(motionEventAction, totalAngle);
        }
    }

    void changeQuarter(Quarter quarter) {
        this.prevQuarter = currentQuarter;
        this.currentQuarter = quarter;
    }

    void setMaxNumberOfCircles(int maxNumberOfCircles) {
        this.maxNumberOfCircles = maxNumberOfCircles;
    }

    void setCurrentAngle(double angle) {
        this.fullCircleCount = (int) (angle / 360);

        if (onTotalAngleChangedListener != null) {
            onTotalAngleChangedListener.onTotalAngleChanged(MotionEvent.ACTION_MOVE, angle);
        }
    }

    interface OnTotalAngleChangedListener {
        void onTotalAngleChanged(int motionEventAction, double totalAngle);
    }
}
