package com.example.android.wearable.datalayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_or_signin extends AppCompatActivity {

    Button login;
    TextView regiter;
    Intent intent;
    Toolbar toolbar;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_signin);

        regiter=findViewById(R.id.register);
        login=findViewById(R.id.login);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();



        regiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });

        Toolbar toolbar=findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
    }

    public void Register(){
        intent =new Intent(this,Register.class);
        startActivity(intent);

    }

    public void Login(){
        intent=new Intent(this,Login.class);
        startActivity(intent);

    }
}
