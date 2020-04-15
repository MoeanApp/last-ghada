package com.example.android.wearable.datalayer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.wearable.datalayer.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import fr.ganfra.materialspinner.MaterialSpinner;


public class Register extends AppCompatActivity implements View.OnClickListener  {
   // public static final String TAG = "TAG";
    EditText mfirstName, mEmail, mPassword;
    EditText mlastName;
    MaterialSpinner mrole;
  //  public static String uid;

    private FirebaseAuth mAuth;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mfirstName = findViewById(R.id.FirstName);
        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.password);
        mlastName = findViewById(R.id.LastName);
        mrole = findViewById(R.id.Role);


        String[] ITEMS = {"استشاري تربوي", "استشاري طبي", "مقدم رعاية"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mrole = findViewById(R.id.Role);
        mrole.setAdapter(adapter);

        progressBar = findViewById(R.id.ProgressBar);
        mAuth = FirebaseAuth.getInstance();
        TextView mLoginBtn=findViewById(R.id.createText);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
        findViewById(R.id.registerBtn).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            //handle the already login user
        }
    }
    private void registerUser() {
        final String email = mEmail.getText().toString().trim();
        final String password = mPassword.getText().toString().trim();
        final String firstName = mfirstName.getText().toString().trim();
        final String lastName = mlastName.getText().toString().trim();
        final String status;

        if (TextUtils.isEmpty(email)) {
            mEmail.setError("الرجاء إدخال البريد الإلكتروني");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            mPassword.setError("الرجاء إدخال كلمة المرور");
            return;
        }
        if (TextUtils.isEmpty(firstName)) {
            mfirstName.setError("الرجاء إدخال الإسم الأول");
            return;
        }
        if (TextUtils.isEmpty(lastName)) {
            mlastName.setError("الرجاء إدخال الإسم الأخير");
            return;
        }

        if (password.length() < 6) {
            mPassword.setError("يجب ان تكون كلمة المرور أطول من 6 أحرف");
            return;}

        if(mrole.getSelectedItem() == null){
            mrole.setError("الرجاء اختيار المهنة");
            return;
        }
            progressBar.setVisibility(View.VISIBLE);

            // register the user in firebase
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


String g;

                g = FirebaseDatabase.getInstance().getReference("Users")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).toString();
                String uid=g.substring(41);
                        //"https://njood-fd693.firebaseio.com/Users/
                        if (task.isSuccessful()) {



                        User user = new User(
                                firstName,
                                lastName,
                                (String) mrole.getSelectedItem(),
                                email,
                                password,
                                uid


                        );

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {

                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {

                                        // Toast.makeText(Register.this, getString(R.ss), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }









                    }
                });






    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerBtn:
                registerUser();
                break;
    }}

}






