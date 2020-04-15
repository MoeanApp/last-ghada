/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.wearable.datalayer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.wearable.activity.WearableActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.wear.ambient.AmbientModeSupport;
import androidx.wear.widget.WearableRecyclerView;

import com.example.android.wearable.datalayer.DataLayerScreen.DataLayerScreenData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.CapabilityClient;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Displays {@link WearableRecyclerView} with three main rows of data showing off various features
 * of the Data Layer APIs:
 *
 * <p>
 *
 * <ul>
 *   <li>Row 1: Shows a log of DataItems received from the phone application using {@link
 *       MessageClient}
 *   <li>Row 2: Shows a photo sent from the phone application using {@link DataClient}
 *   <li>Row 3: Displays two buttons to check the connected phone and watch devices using the {@link
 *       CapabilityClient}
 * </ul>
 */
public class WMainActivity extends WearableActivity
        implements SensorEventListener, MessageClient.OnMessageReceivedListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {


    private static final String TAG = "MainActivity";
    private TextView tv_steps;
    Button BtnStart, BtnStop, go;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    //TextView go;
    private Sensor accel;
    private static final String TEXT_NUM_STEPS = "Number of Steps: ";
    private int numSteps = 0;

    private static final String CAPABILITY_1_NAME = "capability_1";
    private static final String CAPABILITY_2_NAME = "capability_2";

    ArrayList<DataLayerScreenData> mDataLayerScreenData;
    private TextView HeartRateTxt;
    private boolean isSensorPresent = false;
    private SensorManager mSensorManager;
    private Sensor mSensor;

    private WearableRecyclerView mWearableRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private CustomRecyclerAdapter mCustomRecyclerAdapter;
    String nun;
    private DatabaseReference mHeartRef;
    private DatabaseReference mStepRef;


    private TextView lat, lng;
    double latt, loong;
    private Button updateLocation, seeMap;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private boolean mRequestLocationUpdates = false;
    private LocationRequest mLocationRequest;
    private static int UPDATE_INTERVAL = 10000;
    private static int FATEST_INTERVAL = 5000;
    private static int DISPLACEMENT = 10;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference longRef, latRef, databaseReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enables Always-on

        setContentView(R.layout.activity_main);

        updateLocation = (Button) findViewById(R.id.buttonLocationUpdates);

        lat = (TextView) findViewById(R.id.latitude);
        lng = (TextView) findViewById(R.id.longitude);

        if (checkPlayServices()) {
            buildGoogleApiClient();
            createLocationRequest();
        }
        updateLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

/*
                latt = Double.parseDouble(lat.getText().toString());
                loong = Double.parseDouble(lng.getText().toString());
 */
/*
                latt=23.29517333;
                loong=-315.3515625;
                Map map = new Map(latt, loong);
                FirebaseDatabase.getInstance().getReference("Map")
                        .setValue(map);
 */


                togglePeriodLocationUpdates();

            }
        });


       /* seeMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(i);
                finish();
            }
        });
        */


        FirebaseApp.initializeApp(this);


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


        BtnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                //  togglePeriodLocationUpdates();


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


        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
                    @Override
                    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                        if ("uid".equalsIgnoreCase(s)) {
                            setupRef(sharedPreferences.getString("uid", null));
                        }
                    }
                });


        if (PreferenceManager.getDefaultSharedPreferences(this).getString("uid", null) != null) {
            setupRef(PreferenceManager.getDefaultSharedPreferences(this).getString("uid", null));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
            displayLocation();
            Log.d("onStart ", "mGoogleApiClient is built");
        }
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        Log.d("GoogleApiClient", " is built");
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
        if (mGoogleApiClient.isConnected() && mRequestLocationUpdates) {
            startLocationUpdates();
        }
        if (isSensorPresent) {
            mSensorManager.registerListener((SensorEventListener) this, mSensor, 3);
        }


        // Instantiates clients without member variables, as clients are inexpensive to create and
        // won't lose their listeners. (They are cached and shared between GoogleApi instances.)
//        Wearable.getDataClient(this).addListener(this);
        Wearable.getMessageClient(this).addListener(this);
//        Wearable.getCapabilityClient(this)
//                .addListener(this, Uri.parse("wear://"), CapabilityClient.FILTER_REACHABLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
        if (isSensorPresent) {
            mSensorManager.unregisterListener((SensorEventListener) this);

        }

        // Wearable.getDataClient(this).removeListener(this);
        Wearable.getMessageClient(this).removeListener(this);
        //Wearable.getCapabilityClient(this).removeListener(this);
    }

    private void setupRef(String uid) {
        mHeartRef = FirebaseDatabase.getInstance().getReference("measures")
                .child(uid)
                .child("heartrate");

//        mHeartRef.setValue(120);

        mStepRef = FirebaseDatabase.getInstance().getReference("measures")
                .child(uid)
                .child("steps");
        latRef = FirebaseDatabase.getInstance().getReference("measures").
                child(uid).child("lat");
        longRef = FirebaseDatabase.getInstance().getReference("measures").
                child(uid).child("lng");

        /*latRef.setValue(20);
        longRef.setValue(30);
         */


//        mStepRef.setValue(90);
    }

    /*
     * Sends data to proper WearableRecyclerView logger row or if the item passed is an asset, sends
     * to row displaying Bitmaps.
     */
    // @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.d(TAG, "onDataChanged(): " + dataEvents);

        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                String path = event.getDataItem().getUri().getPath();
                if (DataLayerListenerService.IMAGE_PATH.equals(path)) {
                    DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                    Asset photoAsset =
                            dataMapItem.getDataMap().getAsset(DataLayerListenerService.IMAGE_KEY);
                    // Loads image on background thread.
                    new LoadBitmapAsyncTask().execute(photoAsset);

                } else if (DataLayerListenerService.COUNT_PATH.equals(path)) {
                    Log.d(TAG, "Data Changed for COUNT_PATH");
                    mCustomRecyclerAdapter.appendToDataEventLog(
                            "DataItem Changed", event.getDataItem().toString());
                } else {
                    Log.d(TAG, "Unrecognized path: " + path);
                }

            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                mCustomRecyclerAdapter.appendToDataEventLog(
                        "DataItem Deleted", event.getDataItem().toString());

            } else {
                mCustomRecyclerAdapter.appendToDataEventLog(
                        "Unknown data event type", "Type = " + event.getType());
            }
        }
    }

    private void displayLocation() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        } else {
            //Do Your Stuff

        }


        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Log.d("LastLocation ", "found");
        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
           /* lat.setText(latitude + "");
            lng.setText(longitude + "");
            */

            latRef.setValue(latitude);
            longRef.setValue(longitude);



            /*Map map = new Map(longitude, latitude);
            FirebaseDatabase.getInstance().getReference("Map")
                    .setValue(map);
             */


        } /*else {
            lat.setText("0.0");
            lng.setText("0.0");
        }
        */


    }

    private void togglePeriodLocationUpdates() {
        if (!mRequestLocationUpdates) {
           // BtnStart.setText(getString(R.string.btn_stop_location_updates));

            mRequestLocationUpdates = true;

            startLocationUpdates();

        } else {
//            BtnStart.setText(getString(R.string.btn_start_location_updates));

            mRequestLocationUpdates = false;

            stopLocationUpdates();
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
        Log.d("LocationRequest", " is created");
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    protected void startLocationUpdates() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        } else {
            //Do Your Stuff

        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        Log.d("Location was ", "updates");
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }


    /*
     * Triggered directly from buttons in recycler_row_capability_discovery.xml to check
     * capabilities of connected devices.
     */
    public void onCapabilityDiscoveryButtonClicked(View view) {
        switch (view.getId()) {
            case R.id.capability_2_btn:
                showNodes(CAPABILITY_2_NAME);
                break;
            case R.id.capabilities_1_and_2_btn:
                showNodes(CAPABILITY_1_NAME, CAPABILITY_2_NAME);
                break;
            default:
                Log.e(TAG, "Unknown click event registered");
        }
    }

    /*
     * Sends data to proper WearableRecyclerView logger row.
     */
    @Override
    public void onMessageReceived(MessageEvent event) {
        //   Log.d(TAG, "onMessageReceived: " + event);
        //  mCustomRecyclerAdapter.appendToDataEventLog("Message", event.toString());
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

    /*
     * Sends data to proper WearableRecyclerView logger row.
     */
    // @Override
    public void onCapabilityChanged(CapabilityInfo capabilityInfo) {
        Log.d(TAG, "onCapabilityChanged: " + capabilityInfo);
        mCustomRecyclerAdapter.appendToDataEventLog(
                "onCapabilityChanged", capabilityInfo.toString());
    }

    /**
     * Find the connected nodes that provide at least one of the given capabilities.
     */
    private void showNodes(final String... capabilityNames) {

        Task<Map<String, CapabilityInfo>> capabilitiesTask =
                Wearable.getCapabilityClient(this)
                        .getAllCapabilities(CapabilityClient.FILTER_REACHABLE);

        capabilitiesTask.addOnSuccessListener(
                new OnSuccessListener<Map<String, CapabilityInfo>>() {
                    @Override
                    public void onSuccess(Map<String, CapabilityInfo> capabilityInfoMap) {
                        Set<Node> nodes = new HashSet<>();

                        if (capabilityInfoMap.isEmpty()) {
                            showDiscoveredNodes(nodes);
                            return;
                        }
                        for (String capabilityName : capabilityNames) {
                            CapabilityInfo capabilityInfo = capabilityInfoMap.get(capabilityName);
                            if (capabilityInfo != null) {
                                nodes.addAll(capabilityInfo.getNodes());
                            }
                        }
                        showDiscoveredNodes(nodes);
                    }
                });
    }

    private void showDiscoveredNodes(Set<Node> nodes) {
        List<String> nodesList = new ArrayList<>();
        for (Node node : nodes) {
            nodesList.add(node.getDisplayName());
        }
        Log.d(
                TAG,
                "Connected Nodes: "
                        + (nodesList.isEmpty()
                        ? "No connected device was found for the given capabilities"
                        : TextUtils.join(",", nodesList)));
        String msg;
        if (!nodesList.isEmpty()) {
            msg = getString(R.string.connected_nodes, TextUtils.join(", ", nodesList));
        } else {
            msg = getString(R.string.no_device);
        }
        Toast.makeText(WMainActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        if (mRequestLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;

        Toast.makeText(getApplicationContext(), "Location changed!", Toast.LENGTH_SHORT).show();

        displayLocation();
    }

    /*
     * Extracts {@link android.graphics.Bitmap} data from the
     * {@link com.google.android.gms.wearable.Asset}
     */
    private class LoadBitmapAsyncTask extends AsyncTask<Asset, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Asset... params) {

            if (params.length > 0) {

                Asset asset = params[0];

                Task<DataClient.GetFdForAssetResponse> getFdForAssetResponseTask =
                        Wearable.getDataClient(getApplicationContext()).getFdForAsset(asset);

                try {
                    // Block on a task and get the result synchronously. This is generally done
                    // when executing a task inside a separately managed background thread. Doing
                    // this on the main (UI) thread can cause your application to become
                    // unresponsive.
                    DataClient.GetFdForAssetResponse getFdForAssetResponse =
                            Tasks.await(getFdForAssetResponseTask);

                    InputStream assetInputStream = getFdForAssetResponse.getInputStream();

                    if (assetInputStream != null) {
                        return BitmapFactory.decodeStream(assetInputStream);

                    } else {
                        Log.w(TAG, "Requested an unknown Asset.");
                        return null;
                    }

                } catch (ExecutionException exception) {
                    Log.e(TAG, "Failed retrieving asset, Task failed: " + exception);
                    return null;

                } catch (InterruptedException exception) {
                    Log.e(TAG, "Failed retrieving asset, interrupt occurred: " + exception);
                    return null;
                }

            } else {
                Log.e(TAG, "Asset must be non-null");
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            if (bitmap != null) {
                Log.d(TAG, "Setting background image on second page..");
                int imageAssetItemIndex = mCustomRecyclerAdapter.setImageAsset(bitmap);

                // Moves RecyclerView to appropriate row to show new image sent over.
                if (imageAssetItemIndex > -1) {
                    mWearableRecyclerView.scrollToPosition(imageAssetItemIndex);
                }
            }
        }
    }

    //    @Override
    public AmbientModeSupport.AmbientCallback getAmbientCallback() {
        return new MyAmbientCallback();
    }

    /**
     * Customizes appearance for Ambient mode. (We don't do anything minus default.)
     */
    private class MyAmbientCallback extends AmbientModeSupport.AmbientCallback {
        /**
         * Prepares the UI for ambient mode.
         */
        @Override
        public void onEnterAmbient(Bundle ambientDetails) {
            super.onEnterAmbient(ambientDetails);
        }

        /**
         * Updates the display in ambient mode on the standard interval. Since we're using a custom
         * refresh cycle, this method does NOT update the data in the display. Rather, this method
         * simply updates the positioning of the data in the screen to avoid burn-in, if the display
         * requires it.
         */
        @Override
        public void onUpdateAmbient() {
            super.onUpdateAmbient();
        }

        /**
         * Restores the UI to active (non-ambient) mode.
         */
        @Override
        public void onExitAmbient() {
            super.onExitAmbient();
        }
    }
}