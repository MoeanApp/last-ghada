package com.example.android.wearable.datalayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class childprofile extends AppCompatActivity {

    private static final String TAG = "activity_childprofile";
    //for saving the name
    //EditText name;
    Button button;
    int number;

    //FirebaseDatabase database;
    DatabaseReference ref;
    ProgressDialog prog;

    //for calculaing age
    FloatingActionButton fb;
    TextView date;
    TextView age;

    //test
    TextView test;
    int themonth;

    // for qui
    TextView first;
    TextView second;
    TextView third;
    TextView forth;

    //for radio
    RadioGroup group1;
    RadioButton b1;
    RadioButton b11;

    RadioGroup group2;
    RadioButton b2;
    RadioButton b22;

    RadioGroup group3;
    RadioButton b3;
    RadioButton b33;

    RadioGroup group4;
    RadioButton b4;
    RadioButton b44;

    TextView what;
    boolean answered;

    //for menu and button nav
    private DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    BottomNavigationView bottomNavigationView;
    NavigationView navigationView;
    Intent intent2;
   // Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_childprofile);


        //extera
        Toolbar toolbar = findViewById(R.id.tool_bar2);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.child);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        Log.d(TAG, "onCreate:started");

        first = (TextView) findViewById(R.id.first);
        second = (TextView)findViewById(R.id.second);
        third = (TextView)findViewById(R.id.third);
        forth = (TextView) findViewById(R.id.forth);



        // name = findViewById(R.id.name);
        button = findViewById(R.id.button);
        prog = new ProgressDialog(this);
        prog.setMessage("سيتم عرض البيانات ..");
        ref = FirebaseDatabase.getInstance().getReference().child("child");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savename();
            }
        });


        fb = findViewById(R.id.fb);
        date = findViewById(R.id.date);
        age = findViewById(R.id.age);

        // radio
        group1=findViewById(R.id.group1);
        b1=findViewById(R.id.b1);
        b11=findViewById(R.id.b11);

        group2=findViewById(R.id.group2);
        b2=findViewById(R.id.b2);
        b22=findViewById(R.id.b22);

        group3=findViewById(R.id.group3);
        b3=findViewById(R.id.b3);
        b33=findViewById(R.id.b33);

        group4=findViewById(R.id.group4);
        b4=findViewById(R.id.b4);
        b44=findViewById(R.id.b44);

        what = findViewById(R.id.what);


        //test
        test = findViewById(R.id.test);

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dd = new DatePickerDialog(v.getContext(), datePickerListener, year, month, day);
                dd.getDatePicker().setMaxDate(new Date().getTime());
                dd.show();
            }
        });


        navigationView = findViewById(R.id.nav_drawer2);

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
                } else if (menuItem.getItemId() == R.id.nav_signout2){
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

    public void savename(){
        if (!answered) {


            /*String thename = name.getText().toString().trim();
            prog.show();
            children child = new children(thename);
            ref.push().setValue(child);
            prog.dismiss();
            Toast.makeText(getApplication(), "تم الحفظ بنجاح", Toast.LENGTH_LONG).show();*/

            if (b1.isChecked() || b2.isChecked() || b3.isChecked() ||b4.isChecked()) {
                // String thename = name.getText().toString().trim();
                // prog.show();
                //children child = new children(thename);
                // ref.push().setValue(child);
                // prog.dismiss();
                Toast.makeText(getApplication(), "تم عرض النتيجة بنجاح", Toast.LENGTH_LONG).show();
                what.setText("يبدو أن طفلك يعاني من مشكله تؤثر على مستوى نموه ! الرجاء مراجعة المختص في أسرع وقت");

            } else if(b11.isChecked() || b22.isChecked() || b33.isChecked() ||b44.isChecked()) {
                // String thename = name.getText().toString().trim();
                //prog.show();
                // children child = new children(thename);
                // ref.push().setValue(child);
                // prog.dismiss();
                Toast.makeText(getApplication(), "تم عرض النتيجة بنجاح", Toast.LENGTH_LONG).show();
                what.setText("تظهر نتيجة ايجاباتك أن طفلك لا يعاني من مشاكل في مستوى النمو ، لاداعي لمراجعه المختص");
            }
            else {
                Toast.makeText(childprofile.this, "الرجاء الإجابة على الأسئلة",Toast.LENGTH_LONG).show();
            }
        }

    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, day);
            String format = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
            date.setText(format);
            test.setText(Integer.toString(calculateAge(c.getTimeInMillis())*12));
            age.setText(Integer.toString(calculateAge(c.getTimeInMillis())));

            String q = (String) test.getText();
            int qq = Integer.parseInt(q);

            number=Integer.parseInt(test.getText().toString());
            //for qui
            if (qq <= 12) {
                ref = FirebaseDatabase.getInstance().getReference().child("questions").child("Age1");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String a= (String) dataSnapshot.child("q1").getValue();
                        String b=dataSnapshot.child("q2").getValue().toString();
                        String c=dataSnapshot.child("q3").getValue().toString();
                        String d=dataSnapshot.child("q4").getValue().toString();
                        //Toast.makeText(getApplication(),a,Toast.LENGTH_LONG).show();

                        first.setText(a);
                        second.setText(b);
                        third.setText(c);
                        forth.setText(d);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            else if (qq >= 12 && qq <= 24){
                ref = FirebaseDatabase.getInstance().getReference().child("questions").child("Age2");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String a=dataSnapshot.child("q1").getValue().toString();
                        String b=dataSnapshot.child("q2").getValue().toString();
                        String c=dataSnapshot.child("q3").getValue().toString();
                        String d=dataSnapshot.child("q4").getValue().toString();

                        first.setText(a);
                        second.setText(b);
                        third.setText(c);
                        forth.setText(d);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            else if (qq >= 24 && qq <= 168){
                ref = FirebaseDatabase.getInstance().getReference().child("questions").child("Age3");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String a=dataSnapshot.child("q1").getValue().toString();
                        String b=dataSnapshot.child("q2").getValue().toString();
                        String c=dataSnapshot.child("q3").getValue().toString();
                        String d=dataSnapshot.child("q4").getValue().toString();

                        first.setText(a);
                        second.setText(b);
                        third.setText(c);
                        forth.setText(d);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }


        }
    };

    int calculateAge(Long date){
        Calendar dob = Calendar.getInstance();
        dob.setTimeInMillis(date);
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR)- dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_MONTH)< dob.get(Calendar.DAY_OF_MONTH)){
            age--;
        }
        return age;
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

    public void videos() {
        intent2 = new Intent(this, videoscare.class);
        startActivity(intent2);

    }

    public void signout(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(childprofile.this,Login_or_signin.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

    }
}
