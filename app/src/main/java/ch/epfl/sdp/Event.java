package ch.epfl.sdp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


/**
 * Data structure that holds all information about an event
 */
public class Event implements Serializable {

    private final String mDescription;
    private final Date mDate;
    private final String mTitle;
    private final String mImageId;
    private final String mAddress;

    private double mLatitude;
    private double mLongitude;
    private final String mOrganizerRef;
    private final ArrayList<EventCategory> mCategories = new ArrayList<EventCategory>();
    static private SimpleDateFormat mFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    /**
     * Constructor of event
     *
     * @param title Event title
     * @param description Description for the event
     * @param date The date where the event is happening
     * @param address The address of where the event is happening
     * @param location The location of where the event is happening
     * @param imageId An URL to an image for the Event
     * @param organizerRef The reference of the organizer
     * @param categories The categories of the event
     */
    public Event(@NonNull String title,
                 @NonNull String description,
                 @NonNull Date date,
                 @NonNull String address,
                 @NonNull LatLng location,
                 @NonNull String imageId,
                 @NonNull String organizerRef,
                 @NonNull ArrayList<EventCategory> categories) {
        mTitle = title;
        mDescription = description;
        mDate = date;
        mAddress = address;
        mLatitude = location.latitude;
        mLongitude = location.longitude;
        mImageId = imageId;
        mOrganizerRef = organizerRef;
        mCategories.addAll(categories);
    }

    // TO DELETE ??????????????????????????????????????????????????????????????
    /**
     * Constructor of event
     * @param title Event title
     * @param description Description for the event
     * @param date The date where the event is happening
     * @param address The address of where the event is happening
     * @param latitude The latitude of where the event is happening
     * @param longitude The longitude of where the event is happening
     * @param imageId An URL to an image for the Event
     * @param categories The categories of the event
     */
//    public Event(@NonNull String title,
//                 @NonNull String description,
//                 @NonNull Date date,
//                 @NonNull String address,
//                 double latitude,
//                 double longitude,
//                 @NonNull String  imageId,
//                 @NonNull String organizerRef,
//                 @NonNull ArrayList<EventCategory> categories) {
//        mTitle = title;
//        mDescription = description;
//        mDate = date;
//        mAddress = address;
//        mLatitude = latitude;
//        mLatitude = longitude;
//        mImageId = imageId;
//        mOrganizerRef = organizerRef;
//        mCategories.addAll(categories);
//    }

    /**
     * Utility method to convert a Date class into a human readable date using a specified format.
     *
     * @param date to convert
     * @return human readable string of date
     */
    static public String formatDate(Date date) {
        return mFormatter.format(date);
    }

    /**
     * Converts a human readable date into a Date class.
     *
     * @param date human readable date string to convert
     * @return data class
     * @throws ParseException in case the provided string date doesn't not corresponds to the
     * specified format.
     */
    static public Date parseDate(String date) throws ParseException {
        return mFormatter.parse(date);
    }

    /**
     * @return the title associated to this event
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * @return the description associated to this event
     */
    public String getDescription() {
        return mDescription;
    }

    /**
     * @return date class associated to this event
     */
    public Date getDate() {
        return mDate;
    }

    /**
     * @return human readable date associated to this event
     */
    public String getDateStr() {
        return formatDate(mDate);
    }

    /**
     * @return location in latitude and longitude associated to this event
     */
    public LatLng getLocation() {
        // Necessary so that Event is Serializable
        return new LatLng(mLatitude, mLongitude);
    }

    /**
     * @return image identifier for the image associated to this event
     */
    public String getImageId() {
        return mImageId;
    }

    /**
     * @return human readable address associated to this event
     */
    public String getAddress() {
        return mAddress;
    }

    /**
     * @return  the categories of the event
     */
    public ArrayList<EventCategory> getCategories() {
        return mCategories;
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
                && mImageId.equals(obj.mImageId) && mOrganizerRef.equals(obj.mOrganizerRef);
    }
    /**
     * @return The event's organizer database reference
     */
    public String getOrganizer() { return mOrganizerRef; }
}
