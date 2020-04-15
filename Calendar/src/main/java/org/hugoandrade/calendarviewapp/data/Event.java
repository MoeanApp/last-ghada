package org.hugoandrade.calendarviewapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Event implements Parcelable {

    public String id;
    public String title;
    public Date date;
    public int color;
    public boolean isCompleted;

    public Event(String id, String title, Date date, int color, boolean isCompleted) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.color = color;
        this.isCompleted = isCompleted;
    }

    public Event(){
    }

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



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
        dest.writeInt(this.color);
        dest.writeByte(this.isCompleted ? (byte) 1 : (byte) 0);
    }

    protected Event(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.color = in.readInt();
        this.isCompleted = in.readByte() != 0;
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}
