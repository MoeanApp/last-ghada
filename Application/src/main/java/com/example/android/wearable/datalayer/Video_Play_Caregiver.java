package com.example.android.wearable.datalayer;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import java.util.ArrayList;

public class Video_Play_Caregiver extends AppCompatActivity {

    public static VideoView videoView;
    public static ArrayList<String> ListOfURI=new ArrayList<>();


    public static int pos=Videos.position1;

    int index=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video__play__caregiver);

        for(int i=0;i< videoscare.ListOfURIVideos.size();i++){
            ListOfURI.add(videoscare.ListOfURIVideos.get(i));
        }



        videoView = findViewById(R.id.video_view_caregiver);

        final MediaController mediaController=new MediaController(this);
        mediaController.setAnchorView(videoView);

        videoView.setMediaController(mediaController);
        //Uri uri=Uri.fromFile(Videos.mUploads.get(Videos.position1).);
        //videoView.setVideoURI(Uri.parse(Videos.ListOfUri.get(Videos.position1).toString()));

        videoView.setVideoURI(Uri.parse(ListOfURI.get(pos)));

        videoView.requestFocus();
        videoView.start();

    }
}
