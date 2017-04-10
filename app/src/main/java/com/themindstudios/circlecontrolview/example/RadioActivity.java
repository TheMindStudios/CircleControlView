package com.themindstudios.circlecontrolview.example;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.themindstudios.circlecontrolview.widget.CircleControlView;
import com.themindstudios.circlecontrolview.widget.Properties;

public class RadioActivity extends AppCompatActivity {

    private TextView tvFM;
    private int prevValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);

        tvFM = (TextView) findViewById(R.id.radio_tv_fm);

        final CircleControlView circleControlView = (CircleControlView) findViewById(R.id.radio_cv_fm);
        circleControlView.setOnValueChangedCallback(onValueChangedCallback);

        final Drawable pressedBackground = ContextCompat.getDrawable(this, R.drawable.bg_btn_radio_pressed);
        circleControlView.setPressedBackground(pressedBackground);

        final Properties properties = CircleControlView.newPropertiesBuilder()
                .minValue(700)
                .value(1000)
                .maxValue(1200)
                .numberOfCircles(2)
                .build();

        circleControlView.setProperties(properties);

        tvFM.setText(String.valueOf(circleControlView.getValue() / 10d));
    }

    private CircleControlView.OnValueChangedCallback onValueChangedCallback = new CircleControlView.OnValueChangedCallback() {
        @Override
        public void onValueChanged(int value) {
            if (prevValue == value) return;
            prevValue = value;

            tvFM.setText(String.valueOf(value / 10d));
        }
    };
}
