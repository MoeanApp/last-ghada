package com.example.android.wearable.datalayer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.wearable.datalayer.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


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
                    mEmail.setError("الرجاء إدخال البريد الإلكتروني");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("الرجاء إدخال كلمة المرور");
                    return;
                }

                if (password.length() < 6) {
                    mPassword.setError("يجب ان تكون كلمة المرور أطول من 6 أحرف");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);


                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            String g = FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).toString();
                            String uidd = g.substring(41);
                            checRole(uidd);

                            Toast.makeText(Login.this, "تم تسجيل الدخول بنجاح", Toast.LENGTH_SHORT).show();
                          //  startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            //  onAuthSuccess(task.getResult().getUser());


                        } else {
                            Toast.makeText(Login.this, "الرجاء التأكد من البيانات المدخلة" , Toast.LENGTH_SHORT).show();
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
                passwordRestDialoag.setTitle("استرجاع كلمة المرور                         ");
                passwordRestDialoag.setMessage("الرجاء إدخال البريد الإلكتروني ليتم إرسال رابط استرجاع كلمة المرور :");
                passwordRestDialoag.setView(restEmail);
                passwordRestDialoag.setPositiveButton("إرسال", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail = restEmail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this, "تم إرسال الرابط بنجاح", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "حدث خطأ ما! الرجاء إعادة المحاولة" , Toast.LENGTH_SHORT).show();

                            }
                        });


                    }
                });
                passwordRestDialoag.setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                passwordRestDialoag.create().show();

            }
        });

    }




    private void checRole(String uidd) {
        DatabaseReference mDatabase;

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Users").child(uidd).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User u = dataSnapshot.getValue(User.class);
                        String g = u.getRole();


                        if (g .equals("استشاري تربوي") || g.equals("استشاري طبي")) {
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        }
                        else if (g.equals("مقدم رعاية")){
                            startActivity(new Intent(getApplicationContext(), WhoIsMoeanCaregiver.class));

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }


                });

        //}
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

    }}