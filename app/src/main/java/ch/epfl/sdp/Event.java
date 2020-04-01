package ch.epfl.sdp;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.GeoPoint;

import java.util.Date;
import java.util.Random;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class Event {

    private String mDescription;
    private Date mDate;
    private String mTitle;
    private int mImageID;
    private GeoPoint mLocation;

    public Event(@NonNull String title, @NonNull String description, @NonNull Date date) {
        this(title, description, date, R.mipmap.ic_launcher);
    }

    public Event(@NonNull String title, @NonNull String description, @NonNull Date date, int imageID){
        this(title, description, date, imageID, new GeoPoint(new Random().nextInt(90), new Random().nextInt(180)));
    }

    public Event(@NonNull String title, @NonNull String description, @NonNull Date date, int imageID, @NonNull GeoPoint location) {
        this.mTitle = verifyNotNull(title);
        this.mDescription = verifyNotNull(description);
        this.mDate = verifyNotNull(date);
        this.mImageID = imageID;
        this.mLocation = verifyNotNull(location);
    }

    @NonNull
    public String getTitle() {
        return mTitle;
    }

    @NonNull
    public String getDescription() {
        return mDescription;
    }

    @NonNull
    public Date getDate() {
        return mDate;
    }

    @NonNull
    public GeoPoint getLocation() {
        return mLocation;
    }

    public int getImageID() {
        return mImageID;
    }

    public void setTitle(String title) {
        this.mTitle = verifyNotNull(title);
    }

    public void setDescription(String description) {
        this.mDescription = verifyNotNull(description);
    }

    public void setDate(Date date) {
        this.mDate = verifyNotNull(date);
    }

    public void setLocation(GeoPoint location) {
        this.mLocation = verifyNotNull(location);
    }

    public void setImageID(int imageName) {
        this.mImageID = imageName;
    }
}
