package com.example.android.wearable.datalayer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class steps extends FragmentActivity {
    private TextView tv_steps;
    Button BtnStart, BtnStop, go;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    //TextView go;
    private Sensor accel;
    private static final String TEXT_NUM_STEPS = "Number of Steps: ";
    private int numSteps = 0;

    String nun;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        // Get an instance of the SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        assert sensorManager != null;
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(new StepListener() {
            @Override
            public void step(long timeNs) {
                numSteps++;
                tv_steps.setText(TEXT_NUM_STEPS + numSteps);
                nun = Integer.toString(numSteps);

            }
        });

        tv_steps = findViewById(R.id.tv_steps);
        BtnStart = findViewById(R.id.btn_start);
       // BtnStop = findViewById(R.id.btn_stop);


        BtnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                numSteps = 0;
                sensorManager.registerListener(new SensorEventListener() {
                    @Override
                    public void onSensorChanged(SensorEvent sensorEvent) {
                        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                            simpleStepDetector.updateAccel(
                                    sensorEvent.timestamp, sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]);

                        }

                    }

                    @Override
                    public void onAccuracyChanged(Sensor sensor, int i) {

                    }
                }, accel, SensorManager.SENSOR_DELAY_FASTEST);

            }
        });


        BtnStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                sensorManager.unregisterListener(new SensorEventListener() {
                    @Override
                    public void onSensorChanged(SensorEvent sensorEvent) {
                        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                            simpleStepDetector.updateAccel(
                                    sensorEvent.timestamp, sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]);
                        }

                    }

                    @Override
                    public void onAccuracyChanged(Sensor sensor, int i) {

                    }
                });

            }
        });


    }
}
