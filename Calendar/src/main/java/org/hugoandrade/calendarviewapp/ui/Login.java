package org.hugoandrade.calendarviewapp.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.hugoandrade.calendarviewapp.MainActivity;
import org.hugoandrade.calendarviewapp.R;


public class Login extends AppCompatActivity {
    EditText mEmail, mPassword;
    Button mLoginBtn;
    TextView mCreateBtn;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    public String id;
    TextView mTextView;
    public static String r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mTextView = findViewById(R.id.textView);
        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        fAuth = FirebaseAuth.getInstance();
        mLoginBtn = findViewById(R.id.loginBtn);
        mCreateBtn = findViewById(R.id.createText);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is Required.");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is Required.");
                    return;
                }

                if (password.length() < 6) {
                    mPassword.setError("Password Must be >= 6 Characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);


                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
/*
                            String g = FirebaseDatabase.getInstance().getReference("Users")
                             .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).toString();
                            String uidd = g.substring(41);
                         checRole(uidd);*/

                            Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            //  onAuthSuccess(task.getResult().getUser());


                        } else {
                            Toast.makeText(Login.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });

            }
        });


        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText restEmail = new EditText(v.getContext());
                AlertDialog.Builder passwordRestDialoag = new AlertDialog.Builder(v.getContext());
                passwordRestDialoag.setTitle("Reast Password!");
                passwordRestDialoag.setMessage("Enter your Email to recive Rest Link!");
                passwordRestDialoag.setView(restEmail);
                passwordRestDialoag.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail = restEmail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this, "Rest link to sent to your email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "Error! Rest lik not sent" + e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });


                    }
                });
                passwordRestDialoag.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                passwordRestDialoag.create().show();

            }
        });




   /* private void checRole(String uidd) {
        DatabaseReference mDatabase;
// ...
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Users").child(uidd).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User u = dataSnapshot.getValue(User.class);
                     String  g = u.role;
                        //   String ola="advisor";
                        if(g=="advisor"){
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));



                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        Toast.makeText(Login.this, g, Toast.LENGTH_SHORT).show();}

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }



                }*/

    }
    // );

       /* private void onAuthSuccess (FirebaseUser user){

            //String username = usernameFromEmail(user.getEmail())
            if (user != null) {
                //Toast.makeText(signinActivity.this, user.getUid(), Toast.LENGTH_SHORT).show();
                DatabaseReference ref;
                ref = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("role");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String value = dataSnapshot.getValue(String.class);

                        if (value == "advisor") {
                            //String jason = (String) snapshot.getValue();
                            Toast.makeText(signinActivity.this, jason, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Login.this, MainActivity.class));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }



    }*/
      /* private void checRole(String uidd) {
           DatabaseReference mDatabase;
// ...
           mDatabase = FirebaseDatabase.getInstance().getReference();
           mDatabase.child("Users").child(uidd).addListenerForSingleValueEvent(
                   new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {
                           User u = dataSnapshot.getValue(User.class);
                           String  g = u.role;
                           //   String ola="advisor";
                           if(g=="advisor"){




                               startActivity(new Intent(getApplicationContext(),MainActivity.class));
                               Toast.makeText(Login.this, g, Toast.LENGTH_SHORT).show();}

                       }
                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {

                       }

                   });}*/

}

