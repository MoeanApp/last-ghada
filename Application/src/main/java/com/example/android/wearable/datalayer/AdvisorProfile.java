package com.example.android.wearable.datalayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.moean_p.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class AdvisorProfile extends AppCompatActivity {

    private DrawerLayout drawer2;
    Intent intent2;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;

    Spinner spinner;
    private static final String TAG = "AdvisorProfile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisor_profile);

        spinner=findViewById(R.id.spinner);

        ArrayAdapter<String> myAdapter=new ArrayAdapter<String>(AdvisorProfile.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.types));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(myAdapter);

        Toolbar toolbar=findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        drawer2=findViewById(R.id.drawer3_layout);

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer2,toolbar,R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer2.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_drawer);
navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.nav_profile) {
            profile();
            return true;
        }
        if (menuItem.getItemId() == R.id.nav_consult) {
            consulting();
            return true;
        }

        if (menuItem.getItemId() == R.id.nav_video) {
            videos();
            return true;

        }
        if (menuItem.getItemId() == R.id.nav_who_is_moean) {
            WhoIsMoean();
            return true;
        }
        if (menuItem.getItemId() == R.id.nav_signout) {
            signout();
            return true;
        }
        return false;
    }

});
        bottomNavigationView=findViewById(R.id.bottom2_nav);
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




    }
    public void profile(){

        intent2=new Intent(this,AdvisorProfile.class);
        startActivity(intent2);


    }
    public void consulting(){
        intent2=new Intent(this, Convercation.class);
        startActivity(intent2);

    }
    public void signout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(AdvisorProfile.this, Login_or_signin.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

    }
    public void videos(){
        intent2=new Intent(this,Videos.class);
        startActivity(intent2);
    }

    public void WhoIsMoean(){
        intent2=new Intent(this, MainActivity.class);
        startActivity(intent2);
    }


    @Override
    public void onBackPressed() {
        if (drawer2.isDrawerOpen(GravityCompat.START)) {
            drawer2.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

        }
    }
}
