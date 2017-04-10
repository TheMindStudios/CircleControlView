package com.themindstudios.circlecontrolview.controller;


import com.themindstudios.circlecontrolview.Quarter;
import com.themindstudios.circlecontrolview.RotationDirection;

class CircleRotationDirectionController {

    private OnClockwiseChangedListener onClockwiseChangedListener;
    private OnQuarterChangedListener onQuarterChangedListener;

    private RotationDirection currentDirection = RotationDirection.UNKNOWN;
    private RotationDirection prevDirection = RotationDirection.UNKNOWN;

    private Quarter currentQuarter = Quarter.UNKNOWN;
    private Quarter prevQuarter = Quarter.UNKNOWN;

    private double currentAngle;
    private double prevAngle;

    public void setOnClockwiseChangedListener(OnClockwiseChangedListener onClockwiseChangedListener) {
        this.onClockwiseChangedListener = onClockwiseChangedListener;
    }

    void setOnQuarterChangedListener(OnQuarterChangedListener onQuarterChangedListener) {
        this.onQuarterChangedListener = onQuarterChangedListener;
    }

    void trackDirection(double xRelativeToViewCenter, double yRelativeToViewCenter) {
        final Quarter quarter = getTouchPositionQuarter(xRelativeToViewCenter, yRelativeToViewCenter);
        final double angle = Math.round(-Math.toDegrees(Math.atan2(xRelativeToViewCenter, yRelativeToViewCenter)) + 180);

        if (!currentQuarter.equals(quarter)) {
            prevQuarter = currentQuarter;
            currentQuarter = quarter;

            if (onQuarterChangedListener != null) {
                onQuarterChangedListener.onQuarterChanged(currentQuarter);
            }
        }

        if (currentAngle != angle) {
            prevAngle = currentAngle;
            currentAngle = angle;
        }

        trackDirectionRelativeToAngleDelta();

        if (onClockwiseChangedListener != null) {
            if (!currentDirection.equals(prevDirection)) {
                onClockwiseChangedListener.onClockwiseChanged(currentDirection);
            }
        }
    }

    private void trackDirectionRelativeToAngleDelta() {
        final double deltaAngle = currentAngle - prevAngle;

        if (prevQuarter.equals(Quarter.FOURTH) && currentQuarter.equals(Quarter.FIRST) &&
                currentDirection.equals(RotationDirection.CLOCKWISE)) {
            prevDirection = currentDirection;
            return;
        }

        if (prevQuarter.equals(Quarter.FIRST) && currentQuarter.equals(Quarter.FOURTH) &&
                currentDirection.equals(RotationDirection.ANTICLOCKWISE)) {
            prevDirection = currentDirection;
            return;
        }

        if (deltaAngle == 0) return;

        prevDirection = currentDirection;
        currentDirection = deltaAngle > 0 ? RotationDirection.CLOCKWISE : RotationDirection.ANTICLOCKWISE;
    }

    private Quarter getTouchPositionQuarter(double xRelativeToViewCenter, double yRelativeToViewCenter) {
        if (xRelativeToViewCenter > 0) {
            return yRelativeToViewCenter < 0 ? Quarter.FIRST : Quarter.SECOND;
        } else {
            return yRelativeToViewCenter > 0 ? Quarter.THIRD : Quarter.FOURTH;
        }
    }

    interface OnClockwiseChangedListener {
        void onClockwiseChanged(RotationDirection direction);
    }

    interface OnQuarterChangedListener {
        void onQuarterChanged(Quarter quarter);
    }

}
