package com.example.android.wearable.datalayer;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.wearable.datalayer.Model.User;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class location extends AppCompatActivity implements MessageClient.OnMessageReceivedListener {

    private DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    BottomNavigationView bottomNavigationView;
    NavigationView navigationView;
    Intent intent2;
    private static final String UPDATE_UID = "/update-uid";
    Button b1;
    public static double mlat, mlong;
    private static final String TAG = "activity_location";
    private TextView calculated_heartrate;
    private TextView calculated_steps;
    double lng, lat;
    Button provide_location;

    TextView care_name, care_type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        MainActivity.mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Toolbar toolbar = findViewById(R.id.tool_bar2);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.location_layout);

        provide_location = findViewById(R.id.provide_location);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Log.d(TAG, "onCreate:started");

        navigationView = findViewById(R.id.nav_drawer2);
        View view = navigationView.getHeaderView(0);
        care_name = view.findViewById(R.id.care_name);
        care_type = view.findViewById(R.id.care_type);
        FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        care_name.setText(user.getfirstName());
                        care_type.setText(user.getRole());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.nav_profile2) {
                    profile();
                    return true;
                } else if (menuItem.getItemId() == R.id.nav_consult) {
                    consult();

                    return true;
                } else if (menuItem.getItemId() == R.id.nav_who_is_moean2) {
                    Whoismoean();
                    return true;


                } else if (menuItem.getItemId() == R.id.nav_video2) {
                    videos();
                    return true;
                } else if (menuItem.getItemId() == R.id.nav_advising) {
                    consult();
                }
                else if (menuItem.getItemId() == R.id.nav_signout2) {
                    signout();
                    return true;
                }


                return false;
            }
        });


        bottomNavigationView = findViewById(R.id.bottom2_nav);

        calculated_heartrate = findViewById(R.id.calculated_heartrate);
        calculated_steps = findViewById(R.id.calculated_steps);

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.nav_location) {
                    Locaion();
                }

            }
        });


        FirebaseDatabase.getInstance().getReference("measures")
                .child(MainActivity.mUid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d(TAG, "onDataChange() called with: dataSnapshot = [" + dataSnapshot + "]");
                        try {
                            Measure measure = dataSnapshot.getValue(Measure.class);
                            if (measure != null) {
                                calculated_heartrate.setText("" + measure.heartrate);
                                calculated_steps.setText("" + measure.steps);
                            }
                        } catch (Exception ignored) {
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG, "onCancelled() called with: databaseError = [" + databaseError + "]");
                    }
                });

        provide_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go();
            }
        });


    }

    private void go() {
        FirebaseDatabase.getInstance().getReference("measures").child(MainActivity.mUid).child("lat")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d(TAG, "onDataChange() called with: dataSnapshot = [" + dataSnapshot + "]");
                        String laatt = dataSnapshot.getValue().toString();
                        mlat = Double.parseDouble(laatt);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG, "onCancelled() called with: databaseError = [" + databaseError + "]");

                    }
                });

        FirebaseDatabase.getInstance().getReference("measures").child(MainActivity.mUid).child("lng")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d(TAG, "onDataChange() called with: dataSnapshot = [" + dataSnapshot + "]");
                        String loong = dataSnapshot.getValue().toString();
                        mlong = Double.parseDouble(loong);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG, "onCancelled() called with: databaseError = [" + databaseError + "]");

                    }
                });


        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void onConnectClicked(View view) {
        onStartWearableActivityClick(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        Wearable.getMessageClient(this).addListener(this);

    }

    @Override
    public void onPause() {
        super.onPause();

        Wearable.getMessageClient(this).removeListener(this);
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        LOGD(
                TAG,
                "onMessageReceived() A message from watch was received:"
                        + messageEvent.getRequestId()
                        + " "
                        + messageEvent.getPath());

    }

    /**
     * Sends an RPC to start a fullscreen Activity on the wearable.
     */
    public void onStartWearableActivityClick(View view) {
        LOGD(TAG, "Generating RPC");

        // Trigger an AsyncTask that will query for a list of connected nodes and send a
        // "start-activity" message to each connected node.
        new location.StartWearableActivityTask().execute();
    }

    @WorkerThread
    private void sendStartActivityMessage(String node) {

        Task<Integer> sendMessageTask =
                Wearable.getMessageClient(this).sendMessage(node,
                        UPDATE_UID, MainActivity.mUid.getBytes());

        try {
            // Block on a task and get the result synchronously (because this is on a background
            // thread).
            Integer result = Tasks.await(sendMessageTask);
            LOGD(TAG, "Message sent: " + result);

        } catch (ExecutionException exception) {
            Log.e(TAG, "Task failed: " + exception);

        } catch (InterruptedException exception) {
            Log.e(TAG, "Interrupt occurred: " + exception);
        }
    }

    @WorkerThread
    private Collection<String> getNodes() {
        HashSet<String> results = new HashSet<>();

        Task<List<Node>> nodeListTask =
                Wearable.getNodeClient(getApplicationContext()).getConnectedNodes();

        try {
            // Block on a task and get the result synchronously (because this is on a background
            // thread).
            List<Node> nodes = Tasks.await(nodeListTask);

            for (Node node : nodes) {
                results.add(node.getId());
            }

        } catch (ExecutionException exception) {
            Log.e(TAG, "Task failed: " + exception);

        } catch (InterruptedException exception) {
            Log.e(TAG, "Interrupt occurred: " + exception);
        }

        return results;
    }

    /**
     * As simple wrapper around Log.d
     */
    private static void LOGD(final String tag, String message) {
        if (Log.isLoggable(tag, Log.DEBUG)) {
            Log.d(tag, message);
        }
    }

    private class StartWearableActivityTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... args) {
            Collection<String> nodes = getNodes();
            for (String node : nodes) {
                sendStartActivityMessage(node);
            }
            return null;
        }
    }


    public void profile() {
        intent2 = new Intent(this, childprofile.class);
        startActivity(intent2);

    }

    public void videos() {
        intent2 = new Intent(this, videoscare.class);
        startActivity(intent2);

    }


    public void consult() {
        intent2 = new Intent(this, Convercation_for_caregiver.class);
        startActivity(intent2);

    }

    public void Whoismoean() {
        intent2 = new Intent(this, WhoIsMoeanCaregiver.class);
        startActivity(intent2);

    }

    public void Locaion() {
        intent2 = new Intent(this, location.class);
        startActivity(intent2);
    }
    public void signout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(location.this, Login_or_signin.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

    }
}