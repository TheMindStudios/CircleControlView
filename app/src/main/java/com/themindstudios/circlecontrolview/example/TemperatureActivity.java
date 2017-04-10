package com.themindstudios.circlecontrolview.example;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.themindstudios.circlecontrolview.widget.CircleControlView;

public class TemperatureActivity extends AppCompatActivity {

    private RelativeLayout rlRoot;
    private CircleControlView temperatureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);

        rlRoot = (RelativeLayout) findViewById(R.id.root_view);
        rlRoot.setBackgroundColor(Color.parseColor("#FF9D6E"));

        temperatureView = (CircleControlView) findViewById(R.id.temperature_cv_degree);
        temperatureView.setOnValueChangedCallback(onValueChangedCallback);

        setBackgroundColor(temperatureView.getValue());
    }

    private CircleControlView.OnValueChangedCallback onValueChangedCallback = new CircleControlView.OnValueChangedCallback() {

        @Override
        public void onValueChanged(int value) {
            setBackgroundColor(value);
        }
    };

    private void setBackgroundColor(int currentValue) {
        final float coefficient = currentValue / (float) (temperatureView.getMaxValue());
        final int color = ColorUtils.blendARGB(Color.parseColor("#FF9D6E"), Color.parseColor("#6ABFFF"), coefficient);
        rlRoot.setBackgroundColor(color);
    }
}
