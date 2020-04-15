package com.example.android.wearable.datalayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.android.wearable.datalayer.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Path;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class UploadVideo extends AppCompatActivity {

    private Button cancel, choose, upload;
    private VideoView video;
    private EditText filename;
    private TextView name;
    public static FirebaseUser VideoUser;

    private ProgressBar progressBar;
    private DrawerLayout drawer;
    public static StorageReference storageReference;
    public static Uri videoUrl;
    public static List<Uri> videoURI = new ArrayList<>();
    private DatabaseReference databaseReference;
    public static StorageTask mUploadTask;
    public static StorageReference fileReferance;
    Intent intent1;
    Button previous;

    String AdvisorName;

    private static final int REQUEST_TAKE_GALLERY_VIDEO = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);

        VideoUser = FirebaseAuth.getInstance().getCurrentUser();


        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


        drawer = findViewById(R.id.drawer2_layout);

        ActionBarDrawerToggle toggle2 = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle2);
        toggle2.syncState();

        //cancel = findViewById(R.id.cancel);
        upload = findViewById(R.id.upload);
        choose = findViewById(R.id.choose);

        //  FirebaseUser  user=FirebaseAuth.getInstance().getCurrentUser()


        //video = findViewById(R.id.video);
        filename = findViewById(R.id.title);
        name = findViewById(R.id.name);


        progressBar = findViewById(R.id.progress_bar);

        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {

                    Toast.makeText(UploadVideo.this, "جاري التحميل يرجى الإنتظار ..", Toast.LENGTH_SHORT).show();


                } else {
                    UploadsFile();

                }

            }
        });

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();

            }
        });


        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "اختر فديو"), REQUEST_TAKE_GALLERY_VIDEO);


    }


    public void previous(View view) {
        Intent intent2 = new Intent(this, Videos.class);
        startActivity(intent2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_GALLERY_VIDEO && resultCode == RESULT_OK
                && data.getData() != null && data.getData() != null) {
            //  Uri uri=getRealPathFromURI();
            videoUrl = data.getData();


            //video.setVideoURI(videoUrl);


        }

    }


    private String getFileEctention(Uri uri) {
        ContentResolver CR = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(CR.getType(uri));
    }

    private void UploadsFile() {
        if (videoUrl != null) {
            fileReferance = storageReference.child("uploads/" + System.currentTimeMillis() +
                    "." + getFileEctention(videoUrl));
            mUploadTask = fileReferance.putFile(videoUrl)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    User user=dataSnapshot.getValue(User.class);
                                    AdvisorName=user.getfirstName();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            }) ;

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(UploadVideo.this, "تم التحميل بنجاح", Toast.LENGTH_LONG);

                            taskSnapshot
                                    .getStorage()
                                    .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {


                                    VideoAdapter2 upload = new VideoAdapter2(filename.getText().toString().trim(),
                                            uri.toString(), FirebaseAuth.getInstance().getCurrentUser().getUid().toString(),
                                            AdvisorName);
                                    String uploadId = databaseReference.push().getKey();
                                    databaseReference.child(uploadId).setValue(upload);

                                    //adding Uri of the video into an arraylist

                                    videoURI.add(videoUrl);
                                    // progressBar.setIndeterminate(false);
                                }
                            });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(UploadVideo.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    })

                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressBar.setProgress((int) progress);
                        }
                    });


        } else {
            Toast.makeText(this, "لم يتم اختيار فديو", Toast.LENGTH_LONG).show();
        }

    }


}