package com.example.android.wearable.datalayer;

import com.google.firebase.database.Exclude;
import java.io.*;

public class VideoAdapter2 {

    private String name;
    private String userId;
    private String videoUrl;
    private String mKey;
    private String advisorName;

    public VideoAdapter2(){

    }

    public VideoAdapter2(String name, String videoUrl,String userId,String advisorName) {
        if(name.trim().equals("")){
            name="بدون عنوان";
        }
        this.name = name;
        this.videoUrl = videoUrl;
        this.userId=userId;
        this.advisorName=advisorName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getAdvisorName() {
        return advisorName;
    }

    public String getUserId() {
        return userId;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
    @Exclude
    public String getmKey(){
        return mKey;
    }
    @Exclude

    public void setmKey(String key){
        mKey=key;
    }
}