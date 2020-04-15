package com.example.android.wearable.datalayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.android.wearable.datalayer.Model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class videoscare extends AppCompatActivity implements VideoAdapter3.onItemClickListener  {

    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    private VideoAdapter3 adapter;
    public static VideoAdapter2 upload;

    private FirebaseStorage storage;
    Intent intent2;

    TextView care_name, care_type;


    public static int position1;


    BottomNavigationView bottomNavigationView;
    private List<VideoAdapter2> mUploads;

    private RecyclerView recycleView;


    private DatabaseReference databaseReference;
    private ValueEventListener mDBListener;

    private DrawerLayout drawer;

    public static List<String>ListOfURIVideos;


    //VideoAdapter adapter;

    private VideoView videoView;


    private static final String TAG = "activity_videoscare";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoscare);

        navigationView = findViewById(R.id.nav_drawer2);
        View view=navigationView.getHeaderView(0);
        care_name = view.findViewById(R.id.care_name);
        care_type=view.findViewById(R.id.care_type);

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


        Toolbar toolbar = findViewById(R.id.tool_bar2);
        setSupportActionBar(toolbar);

        ListOfURIVideos=new ArrayList<>();



        recycleView = findViewById(R.id.recycler_view_care_videos);
        videoView=findViewById(R.id.video_view);
        recycleView.setHasFixedSize(true);
        recycleView.setLayoutManager(new LinearLayoutManager(this));


        mUploads = new ArrayList<>();

        adapter = new VideoAdapter3(videoscare.this, mUploads);
        recycleView.setAdapter(adapter);
        adapter.setOnItemClickListener(videoscare.this);


        storage=FirebaseStorage.getInstance();



        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");

        mDBListener= databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUploads.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    upload = postSnapshot.getValue(VideoAdapter2.class);
                    upload.setmKey(postSnapshot.getKey());
                    mUploads.add(upload);
                    ListOfURIVideos.add(upload.getVideoUrl());
                }


                adapter.notifyDataSetChanged();


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(videoscare.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        //videoView = findViewById(R.id.video_view_upload);



        drawer = findViewById(R.id.vid);

        toggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

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
                }
                else if(menuItem.getItemId()==R.id.nav_advising){
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


        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.nav_location) {
                    Locaion();
                }

            }
        });

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

    public void signout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(videoscare.this, Login_or_signin.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

    }
    public void videos() {
        intent2 = new Intent(this, videoscare.class);
        startActivity(intent2);

    }

    @Override
    public void onItemClick(int position) {
        position1=position;

        Intent intent=new Intent(this,Video_Play_Caregiver.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(mDBListener);
    }
}