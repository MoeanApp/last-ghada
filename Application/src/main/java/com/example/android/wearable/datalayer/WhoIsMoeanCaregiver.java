package com.example.android.wearable.datalayer;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

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

import org.hugoandrade.calendarviewapp.ui.SplashActivity;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class WhoIsMoeanCaregiver extends AppCompatActivity {

    private DrawerLayout drawer;

    ActionBarDrawerToggle toggle;
    BottomNavigationView bottomNavigationView;
    NavigationView navigationView;
    FirebaseAuth firebaseAuth;
    Intent intent2;
    Button b1;
    private static final String TAG = "WhoIsMoeanCaregiver";

    //private static final String UPDATE_UID = "/update-uid";
    TextView care_name, care_type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_who_is_moean_caregiver2);

        //MainActivity.mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Toolbar toolbar = findViewById(R.id.tool_bar2);
        setSupportActionBar(toolbar);

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

        drawer = findViewById(R.id.caregiver_layout);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        Log.d(TAG, "onCreate:started");


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.nav_profile2) {
                    profile();
                    return true;
                } else if (menuItem.getItemId() == R.id.nav_who_is_moean2) {
                    Whoismoean();
                    return true;
                } else if (menuItem.getItemId() == R.id.nav_video2) {
                    videos();
                    return true;
                } else if (menuItem.getItemId() == R.id.nav_advising) {
                    consult();
                } else if (menuItem.getItemId() == R.id.nav_signout2) {
                    signout();
                    return true;
                }


                return false;
            }
        });

        bottomNavigationView = findViewById(R.id.bottom2_nav);
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.nav_location) {
                    Locaion();
                } else if (menuItem.getItemId() == R.id.nav_calender) {
                    calendar();
                }

            }
        });


    }


    public void twitter(View view) {
        Intent twitterintent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/MoeanApp"));
        startActivity(twitterintent);
    }

    public void profile() {
        intent2 = new Intent(this, childprofile.class);
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

    public void calendar() {
        intent2 = new Intent(this, SplashActivity.class);
        startActivity(intent2);
    }

    public void videos() {
        intent2 = new Intent(this, videoscare.class);
        startActivity(intent2);

    }

    public void signout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(WhoIsMoeanCaregiver.this, Login_or_signin.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

    }

}