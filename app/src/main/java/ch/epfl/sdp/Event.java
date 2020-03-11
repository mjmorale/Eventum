package ch.epfl.sdp;

import android.icu.text.Transliterator;
import android.location.Location;

import androidx.annotation.NonNull;

import java.util.Date;

public class Event {

    public Event(String title, String description, Date date) {
        this(title, description, date, R.mipmap.ic_launcher);
    }
    public Event(String title, String description, Date date, int imageID) {
        this(title, description, date, imageID, new Location(""));
    }
    public Event(String title, String description, Date date, int imageID, Location location){
        if (title == null || description == null || date == null)
            throw new IllegalArgumentException();
        this.title = title;
        this.description = description;
        this.date = date;
        this.imageID = imageID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null)
            throw new IllegalArgumentException();
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null)
            throw new IllegalArgumentException();
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        if (date == null)
            throw new IllegalArgumentException();
        this.date = date;
    }

    @NonNull
    public Location getLocation() {
        return location;
    }

    @NonNull
    public int getImageID() {
        return imageID;
    }

    public void setImageID(@NonNull int imageName) {
        this.imageID = imageName;
    }

    @NonNull
    private String description;

    @NonNull
    private Date date;

    @NonNull
    private String title;

    @NonNull
    private int imageID;

    @NonNull
    private Location location;
}
