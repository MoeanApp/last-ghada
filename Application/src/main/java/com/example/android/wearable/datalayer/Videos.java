package com.example.android.wearable.datalayer;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.android.wearable.datalayer.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Path;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

public class Videos extends AppCompatActivity implements VideoAdapter.onItemClickListener {

    ImageButton b2;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    Intent intent2;
    VideoView videoView;
    public static VideoAdapter2 upload;
    public static int position1, pos2;

    BottomNavigationView bottomNavigationView;


    private static final String TAG = "Videos";

    public static List<VideoAdapter2> mUploads;

    public static List<String> ListOfURIVideos;


    public static VideoAdapter adapter;
    private RecyclerView recycleView;

    ProgressBar progress_circle;
    String uid;
    FirebaseUser user;
    TextView Ad_name, Ad_type;


    public static FirebaseStorage storage;


    private DatabaseReference databaseReference;
    private ValueEventListener mDBListener;

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        navigationView = findViewById(R.id.nav_drawer);
        View view=navigationView.getHeaderView(0);
        Ad_name = view.findViewById(R.id.Ad_name);
        Ad_type=view.findViewById(R.id.Ad_type);

        FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        Ad_name.setText(user.getfirstName());
                        Ad_type.setText(user.getRole());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        ListOfURIVideos = new ArrayList<>();
        progress_circle = findViewById(R.id.progress_circle);


        recycleView = findViewById(R.id.recycler_view);
        videoView = findViewById(R.id.video_view);
        recycleView.setHasFixedSize(true);
        recycleView.setLayoutManager(new LinearLayoutManager(this));

        b2 = findViewById(R.id.add);

        mUploads = new ArrayList<>();

        adapter = new VideoAdapter(Videos.this, mUploads);
        recycleView.setAdapter(adapter);
        adapter.setOnItemClickListener(Videos.this);

        storage = FirebaseStorage.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");


        mDBListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUploads.clear();
                // String currnet = dataSnapshot.child(uid).child("uid").toString();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    upload = postSnapshot.getValue(VideoAdapter2.class);
                    upload.setmKey(postSnapshot.getKey());
                    if(upload.getUserId().equals(uid)) {

                        mUploads.add(upload);
                        ListOfURIVideos.add(upload.getVideoUrl());
                        progress_circle.setVisibility(View.GONE);


                    }






                }


                adapter.notifyDataSetChanged();


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Videos.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        drawer = findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        Log.d(TAG, "onCreate:started");

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
           /*     if (menuItem.getItemId() == R.id.nav_profile) {
                    profile();
                    return true;
                }
            */


                if (menuItem.getItemId() == R.id.nav_video) {
                    videos();
                    return true;

                }
                if (menuItem.getItemId() == R.id.nav_who_is_moean) {
                    WhoIsMoean();
                    return true;
                }
                if(menuItem.getItemId()==R.id.nav_consult){
                    consulting();

                }
                if (menuItem.getItemId() == R.id.nav_signout) {
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
                if (menuItem.getItemId() == R.id.videolist33) {
                    videos();
                }
                if (menuItem.getItemId() == R.id.chats) {
                    consulting();
                }

            }
        });
        b2 = findViewById(R.id.add);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Openactivity();
            }
        });
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recycleView);


    }


    public void profile() {

        intent2 = new Intent(this, AdvisorProfile.class);
        startActivity(intent2);


    }
    public void signout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(Videos.this, Login_or_signin.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

    }
    public void consulting() {
        intent2 = new Intent(this, Convercation.class);
        startActivity(intent2);

    }

    public void videos() {
        intent2 = new Intent(this, Videos.class);
        startActivity(intent2);
    }

    public void WhoIsMoean() {
        intent2 = new Intent(this, MainActivity.class);
        startActivity(intent2);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void Openactivity() {
        Intent intent = new Intent(this, UploadVideo.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(int position) {


        position1 = position;

        Intent intent = new Intent(this, Video_Play.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        //Video_Play.videoView.setVideoURI(Uri.parse(ListOfURIVideos.get(position)));

    }


    @Override
    public void onDeleteClick(int position) {

        pos2 = position;

        VideoAdapter2 selectedItem = mUploads.get(position);
        final String selectedKey = selectedItem.getmKey();
        StorageReference VideoRef = storage.getReferenceFromUrl(selectedItem.getVideoUrl());
        VideoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                databaseReference.child(selectedKey).removeValue();
                Toast.makeText(Videos.this, "تم الحذف", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Videos.this, " حدث خطأ ما! يرجى إعادة المحاولة", Toast.LENGTH_SHORT).show();

            }
        });


    }


    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {


            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            VideoAdapter2 selectedItem = mUploads.get(pos2);
            final String selectedKey = selectedItem.getmKey();
            StorageReference VideoRef = storage.getReferenceFromUrl(selectedItem.getVideoUrl());
            VideoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    databaseReference.child(selectedKey).removeValue();
                    Toast.makeText(Videos.this, " تم الحذف", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Videos.this, "حدث خطأ ما! يرجى إعادة المحاولة", Toast.LENGTH_SHORT).show();

                }
            });
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(mDBListener);
    }

    private void datafromfirebase() {
        if (mUploads.size() > 0)
            mUploads.clear();
        storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("uploads");
        StorageReference islandRef = storageReference.child("uploads.txt");

        File rootPath = new File(Environment.getExternalStorageDirectory(), "uploads");
        if (rootPath.exists()) {
            rootPath.mkdirs();

        }
        final File localFle = new File(rootPath, "zzeue.mp4");
        islandRef.getFile(localFle).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.e("firebase", ";local tem File created created" + localFle.toString());

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("firebase", ";local tem file not created created" + e.toString());

            }
        });


    }
}