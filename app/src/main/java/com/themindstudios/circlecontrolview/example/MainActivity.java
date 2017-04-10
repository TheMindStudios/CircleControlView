package com.themindstudios.circlecontrolview.example;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btnRadio = (Button) findViewById(R.id.btn_radio);
        final Button btnTemperature = (Button) findViewById(R.id.btn_temperature);

        btnRadio.setOnClickListener(onRadioClickListener);
        btnTemperature.setOnClickListener(onTemperatureClickListener);
    }

    private View.OnClickListener onRadioClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            startActivity(new Intent(getBaseContext(), RadioActivity.class));
        }
    };

    private View.OnClickListener onTemperatureClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(getBaseContext(), TemperatureActivity.class));
        }
    };
}
