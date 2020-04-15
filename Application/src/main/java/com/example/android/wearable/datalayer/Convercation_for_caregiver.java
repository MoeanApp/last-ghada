package com.example.android.wearable.datalayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.android.wearable.datalayer.Fragment.ChatsFragment;
import com.example.android.wearable.datalayer.Fragment.UsersFragment;
import com.example.android.wearable.datalayer.Model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Convercation_for_caregiver extends AppCompatActivity {

    NavigationView navigationView;

    ActionBarDrawerToggle toggle;
    Intent intent2;
    CircleImageView profile_img;
    TextView username;

    FirebaseUser firebaseUser;

    DatabaseReference referance;


    BottomNavigationView bottomNavigationView;



    converAdapter adapter;

    private DrawerLayout drawer;

    private static final String TAG = "activity_convercation_for_caregiver";


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convercation_for_caregiver);

        Toolbar toolbar = findViewById(R.id.tool_bar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


        profile_img = findViewById(R.id.profile_image2);
        username = findViewById(R.id.username2);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        referance = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        referance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getfirstName());

                // profile_img.setImageResource(R.drawable.user);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        TabLayout tabLayout=findViewById(R.id.tab_layout2);
        ViewPager viewPager=findViewById(R.id.View_pager2);

        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());


        viewPagerAdapter.addFragment(new UsersFragment(),"الإستشاريون");
        viewPagerAdapter.addFragment(new ChatsFragment(),"المحادثات");

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);









    }
    class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment>fragments;
        private ArrayList<String> titles;



        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
            this.fragments=new ArrayList<>();
            this.titles=new ArrayList<>();

        }

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }


        public void addFragment(Fragment fragment,String title){
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }



}