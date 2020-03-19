package ch.epfl.sdp;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public class Event {
    private String mDescription;
    private Date mDate;
    private String mTitle;
    private int mImageID;
    private LatLng mLocation;

    public Event(@NonNull String title, @NonNull String description, @NonNull Date date) {
        this(title, description, date, R.mipmap.ic_launcher);
    }

    public Event(@NonNull String title, @NonNull String description, @NonNull Date date, @NonNull int imageID){
        this(title,description,date,imageID,new LatLng(46.520553, 6.567821));
    }

    public Event(String title, String description, Date date, int imageID, LatLng location){
        if (title == null || description == null || date == null || location == null)
            throw new IllegalArgumentException();
        this.mTitle = title;
        this.mDescription = description;
        this.mDate = date;
        this.mImageID = imageID;
        this.mLocation = location;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public Date getDate() {
        return mDate;
    }

    public LatLng getLocation() {
        return mLocation;
    }

    public int getImageID() {
        return mImageID;
    }

    public void setTitle(String title) {
        if (title == null)
            throw new IllegalArgumentException();
        this.mTitle = title;
    }

    public void setDescription(String description) {
        if (description == null)
            throw new IllegalArgumentException();
        this.mDescription = description;
    }

    public void setDate(Date date) {
        if (date == null)
            throw new IllegalArgumentException();
        this.mDate = date;
    }

    public void setLocation(LatLng location) {
        if (location == null)
            throw new IllegalArgumentException();
        this.mLocation = location;
    }

    public void setImageID(@NonNull int imageName) {
        this.mImageID = imageName;
    }

}
