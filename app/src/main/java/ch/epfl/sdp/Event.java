package ch.epfl.sdp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Data structure that holds all information about an event
 */
public class Event {

    private final String mDescription;
    private final Date mDate;
    private final String mTitle;
    private final String mImageId;
    private final String mAddress;
    private final LatLng mLocation;
    private final String mOrganizerRef;
    static private SimpleDateFormat mFormatter = new SimpleDateFormat("dd/MM/yyyy kk:mm", Locale.getDefault());

    public Event(@NonNull String title,
                 @NonNull String description,
                 @NonNull Date date,
                 @NonNull String address,
                 @NonNull LatLng location,
                 @NonNull String imageId,
                 @NonNull String organizerRef) {
        mTitle = title;
        mDescription = description;
        mDate = date;
        mAddress = address;
        mLocation = location;
        mImageId = imageId;
        mOrganizerRef = organizerRef;
    }

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
        return mLocation;
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
     * @return The event's organizer database reference
     */
    public String getOrganizer() { return mOrganizerRef; }
}
