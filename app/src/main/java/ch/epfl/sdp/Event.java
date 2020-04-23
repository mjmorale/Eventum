package ch.epfl.sdp;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class Event implements Serializable {
    private String mDescription;
    private Date mDate;
    private String mTitle;
    private int mImageID;
    private String mAddress;
    private double mLatitude;
    private double mLongitude;
    static private SimpleDateFormat mFormatter = new SimpleDateFormat("dd/MM/yyyy");

    public Event(@NonNull String title,
                 @NonNull String description,
                 @NonNull Date date,
                 @NonNull String address,
                 @NonNull LatLng location,
                 @NonNull int imageID) {
        mTitle = title;
        mDescription = description;
        mDate = date;
        mAddress = address;
        mLatitude = location.latitude;
        mLongitude = location.longitude;
        mImageID = imageID;
    }

    public Event(@NonNull String title,
                 @NonNull String description,
                 @NonNull Date date,
                 @NonNull String address,
                 double latitude,
                 double longitude,
                 @NonNull int imageID) {
        mTitle = title;
        mDescription = description;
        mDate = date;
        mAddress = address;
        mLatitude = latitude;
        mLatitude = longitude;
        mImageID = imageID;
    }



    static public String formatDate(Date date) {
        return mFormatter.format(date);
    }

    static public Date parseDate(String date) throws ParseException {
        return mFormatter.parse(date);
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

    public String getDateStr() {
        return formatDate(mDate);
    }

    public LatLng getLocation() {
        // Necessary so that Event is Serializable
        return new LatLng(mLatitude, mLongitude);
    }

    public int getImageID() {
        return mImageID;
    }

    public String getAddress() {
        return mAddress;
    }

    @Override
    public String toString(){
        return "Title:" + mTitle + "\nDescription:" + mDescription + "\nDate:" + mDate + "\nAddress:"
                + mAddress + "\nLatitude:" + mLatitude + "\nLongitude:" + mLongitude
                + "imageID" + mImageID;
    }

    @Override
    public boolean equals(Object o){
        if(this == o)
            return true;
        if(o == null)
            return false;

        if(getClass() != o.getClass())
            return false;
        Event obj = (Event) o;

        return mTitle.equals(obj.mTitle) && mDescription.equals(obj.mDescription)
                && mDate.equals(obj.mDate) && mAddress.equals(obj.mAddress)
                && mLatitude == obj.mLatitude && mLongitude == obj.mLongitude
                && mImageID == obj.mImageID;
    }
}
