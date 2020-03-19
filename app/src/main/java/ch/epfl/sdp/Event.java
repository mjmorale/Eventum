package ch.epfl.sdp;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public class Event {

    public Event(String title, String description, Date date, LatLng location) {
        this(title, description, date, R.mipmap.ic_launcher, location);
    }

    public Event(String title, String description, Date date, int imageID, LatLng location){
        if (title == null || description == null || date == null || location == null)
            throw new IllegalArgumentException();
        this.title = title;
        this.description = description;
        this.date = date;
        this.imageID = imageID;
        this.location = location;
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

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        if (location == null)
            throw new IllegalArgumentException();
        this.location = location;
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
    private LatLng location;
}
