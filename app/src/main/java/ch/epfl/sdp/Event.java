package ch.epfl.sdp;

import androidx.annotation.NonNull;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Event implements Serializable {
    private String mDescription;
    private Date mDate;
    private String mTitle;
    private String mImageId;
    private String mAddress;
    private double mLatitude;
    private double mLongitude;
    static private SimpleDateFormat mFormatter = new SimpleDateFormat("dd/MM/yyyy");

    public Event(@NonNull String title,
                 @NonNull String description,
                 @NonNull Date date,
                 @NonNull String address,
                 @NonNull LatLng location,
                 @NonNull String imageId) {
        mTitle = title;
        mDescription = description;
        mDate = date;
        mAddress = address;

        mLatitude = location.latitude;
        mLongitude = location.longitude;

        mImageId = imageId;

    }

    /**
     * This constructor is here for the
     * @param title Event title
     * @param description Description for the event
     * @param date The date where the event is happening
     * @param address The address of where the event is happening
     * @param latitude The latitude of where the event is happening
     * @param longitude The longitude of where the event is happening
     * @param imageId An URL to an image for the Event
     */
    public Event(@NonNull String title,
                 @NonNull String description,
                 @NonNull Date date,
                 @NonNull String address,
                 double latitude,
                 double longitude,
                 @NonNull String  imageId) {
        mTitle = title;
        mDescription = description;
        mDate = date;
        mAddress = address;
        mLatitude = latitude;
        mLatitude = longitude;
        mImageId = imageId;
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

    public String getImageId() {
        return mImageId;
    }

    public String getAddress() {
        return mAddress;
    }

    @Override
    public String toString(){
        return "Title:" + mTitle + "\nDescription:" + mDescription + "\nDate:" + mDate + "\nAddress:"
                + mAddress + "\nLatitude:" + mLatitude + "\nLongitude:" + mLongitude
                + "\nimageID:" + mImageId;
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
                && mImageId.equals(obj.mImageId);
    }
}
