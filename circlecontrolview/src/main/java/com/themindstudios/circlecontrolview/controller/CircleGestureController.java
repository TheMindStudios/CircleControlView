package com.themindstudios.circlecontrolview.controller;

import android.view.MotionEvent;
import android.view.View;

import com.themindstudios.circlecontrolview.Quarter;

public class CircleGestureController {

    private CircleRotationDirectionController circleRotationDirectionController;
    private CircleRotationTotalAngleController circleRotationTotalAngleController;
    private OnActionEventCallback onActionEventCallback;

    public CircleGestureController() {
        this.circleRotationDirectionController = new CircleRotationDirectionController();
        this.circleRotationTotalAngleController = new CircleRotationTotalAngleController();

        this.circleRotationDirectionController.setOnQuarterChangedListener(onQuarterChangedListener);

        this.circleRotationTotalAngleController.setOnTotalAngleChangedListener(onTotalAngleChangedListener);
    }

    public void setMaxNumberOfCircles(int maxNumberOfCircles) {
        if (circleRotationTotalAngleController != null) {
            circleRotationTotalAngleController.setMaxNumberOfCircles(maxNumberOfCircles);
        }
    }

    public void setCurrentAngle(double angle) {
        if (circleRotationTotalAngleController != null) {
            circleRotationTotalAngleController.setCurrentAngle(angle);
        }
    }

    public boolean trackGestures(View view, MotionEvent motionEvent) {
        if (view == null || motionEvent == null) return false;

        final int viewCenterX = view.getWidth() / 2;
        final int viewCenterY = view.getHeight() / 2;

        final double touchEventX = motionEvent.getX();
        final double touchEventY = motionEvent.getY();

        final double xRelativeToViewCenter = touchEventX - viewCenterX;
        final double yRelativeToViewCenter = touchEventY - viewCenterY;

        final int action = motionEvent.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                actionDownEvent(action);
                break;
            case MotionEvent.ACTION_UP:
                actionUpEvent(action);
                break;
            case MotionEvent.ACTION_MOVE:
                actionMoveEvent(xRelativeToViewCenter, yRelativeToViewCenter, action);
                break;
        }
        return true;
    }

    public void setActionEventCallback(OnActionEventCallback onActionEventCallback) {
        this.onActionEventCallback = onActionEventCallback;
    }

    private void actionDownEvent(int motionEventAction) {
        onActionEventCallback.onActionEvent(motionEventAction, 0);
    }

    private void actionUpEvent(int motionEventAction) {
        onActionEventCallback.onActionEvent(motionEventAction, 0);
    }

    private void actionMoveEvent(double xPos, double yPos, int motionEventAction) {
        final double angle = -Math.toDegrees(Math.atan2(xPos, yPos)) + 180;

        circleRotationDirectionController.trackDirection(xPos, yPos);
        circleRotationTotalAngleController.calculateTotalAngle(motionEventAction, angle);
    }

    private CircleRotationDirectionController.OnQuarterChangedListener onQuarterChangedListener = new CircleRotationDirectionController.OnQuarterChangedListener() {

        @Override
        public void onQuarterChanged(Quarter quarter) {
            circleRotationTotalAngleController.changeQuarter(quarter);
        }
    };

    private CircleRotationTotalAngleController.OnTotalAngleChangedListener onTotalAngleChangedListener = new CircleRotationTotalAngleController.OnTotalAngleChangedListener() {

        @Override
        public void onTotalAngleChanged(int motionEventAction, double totalAngle) {
            if (onActionEventCallback == null) return;

            onActionEventCallback.onActionEvent(motionEventAction, totalAngle);
        }
    };

    public interface OnActionEventCallback {
        void onActionEvent(int motionEventAction, double angle);
    }
}