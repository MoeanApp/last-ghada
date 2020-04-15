package com.example.afinal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.android.wearable.datalayer.R;
import com.example.android.wearable.datalayer.StepDetector;
import com.example.android.wearable.datalayer.StepListener;
import com.example.android.wearable.datalayer.steps;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends WearableActivity
        implements SensorEventListener, MessageClient.OnMessageReceivedListener {

    private static final String TAG = "MainActivity";
    private TextView tv_steps;
    Button BtnStart, BtnStop, go, location;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    //TextView go;
    private Sensor accel;
    private static final String TEXT_NUM_STEPS = "Number of Steps: ";
    private int numSteps = 0;

    //heart rate sensor

    private TextView HeartRateTxt;
    private boolean isSensorPresent = false;
    private SensorManager mSensorManager;
    private Sensor mSensor;

    String nun;
    private DatabaseReference mHeartRef;
    private DatabaseReference mStepRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Enables Always-on
        setAmbientEnabled();

        HeartRateTxt = findViewById(R.id.HeartRateTxt);

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.BODY_SENSORS) == PackageManager.PERMISSION_GRANTED) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BODY_SENSORS}, 1);
            }
        }

        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);

        assert mSensorManager != null;
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE) != null) {
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
            mSensorManager.registerListener((SensorEventListener) this, mSensor, 3); //I am using "3" as it is said to provide best accuracy ¯\_(ツ)_/¯
            isSensorPresent = true;
        } else {
            HeartRateTxt.setText("Heart rate sensor is not present!");
        }
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
                if (mStepRef != null) mStepRef.setValue(numSteps);
                nun = Integer.toString(numSteps);

            }
        });

        tv_steps = findViewById(R.id.tv_steps);
        BtnStart = findViewById(R.id.btn_start);
        // BtnStop = findViewById(R.id.btn_stop);
        //location=findViewById(R.id.lll);

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


       /* BtnStop.setOnClickListener(new View.OnClickListener() {
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
        */


        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
                    @Override
                    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                        if ("uid".equalsIgnoreCase(s)) {
                            setupRef(sharedPreferences.getString("uid", null));
                        }
                    }
                });


    }

    private void setupRef(String uid) {
        mHeartRef = FirebaseDatabase.getInstance().getReference("measures")
                .child(uid)
                .child("heartrate");

//        mHeartRef.setValue(120);

        mStepRef = FirebaseDatabase.getInstance().getReference("measures")
                .child(uid)
                .child("steps");

//        mStepRef.setValue(90);
    }

    private void go() {
        Intent intent = new Intent(this, steps.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isSensorPresent) {
            mSensorManager.registerListener((SensorEventListener) this, mSensor, 3);
        }

        Wearable.getMessageClient(this).addListener(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isSensorPresent) {
            mSensorManager.unregisterListener((SensorEventListener) this);
        }

        Wearable.getMessageClient(this).removeListener(this);

    }

    /*
     * Sends data to proper WearableRecyclerView logger row.
     */
    @Override
    public void onMessageReceived(MessageEvent event) {
        Log.d(TAG, "onMessageReceived: " + event.getData());

        PreferenceManager.getDefaultSharedPreferences(this)
                .edit().putString("uid", new String(event.getData()))
                .commit();

        Toast.makeText(this, "Received Message", Toast.LENGTH_SHORT).show();

        setupRef(new String(event.getData()));

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (isSensorPresent) {
            if ((int) event.values[0] != 0) {
                HeartRateTxt.setText("Current heart rate: " + Math.round(event.values[0]) + " BPM");
                if (mHeartRef != null) mHeartRef.setValue(Math.round(event.values[0]));
            }
        }
    }


}

