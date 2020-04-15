package org.hugoandrade.calendarviewapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class EventParsing {

    public String id;
    public String title;
    public Date date;
    public int color;
    public boolean isCompleted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public EventParsing(String id, String title, Date date, int color, boolean isCompleted) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.color = color;
        this.isCompleted = isCompleted;
    }

    public EventParsing(){

    }
}
