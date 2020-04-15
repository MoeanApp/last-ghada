package org.hugoandrade.calendarviewapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.hugoandrade.calendarviewapp.MainActivity;
import org.hugoandrade.calendarviewapp.R;
import org.hugoandrade.calendarviewapp.data.User;


public class Register extends AppCompatActivity implements View.OnClickListener {
    // public static final String TAG = "TAG";
    EditText mfirstName, mEmail, mPassword;
    EditText mlastName, mrole;
    //  public static String uid;
    TextView mLoginBtn;
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
        mLoginBtn = findViewById(R.id.createText);
        progressBar = findViewById(R.id.ProgressBar);
        mAuth = FirebaseAuth.getInstance();

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
        final String role = mrole.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            mEmail.setError("Email is Required.");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            mPassword.setError("Password is Required.");
            return;
        }
        if (TextUtils.isEmpty(firstName)) {
            mfirstName.setError("firstName is Required.");
            return;
        }
        if (TextUtils.isEmpty(lastName)) {
            mlastName.setError("Password is Required.");
            return;
        }

        if (password.length() < 6) {
            mPassword.setError("Password Must be >= 6 Characters");
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        // register the user in firebase
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                String g = FirebaseDatabase.getInstance().getReference("Users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).toString();
                String uid = g.substring(37);
                //"https://njood-fd693.firebaseio.com/Users/
                if (task.isSuccessful()) {


                    User user = new User(
                            firstName,
                            lastName,
                            role,
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
                                startActivity(new Intent(Register.this, MainActivity.class));
                                finish();
                                // Toast.makeText(Register.this, getString(R.ss), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }


            }
        });


        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.registerBtn) {
            registerUser();
        }
    }


}

