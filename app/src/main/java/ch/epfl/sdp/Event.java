package ch.epfl.sdp;

import androidx.annotation.NonNull;
import com.google.android.gms.maps.model.LatLng;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Event {
    private String mDescription;
    private Date mDate;
    private String mTitle;
    private String mImageId;
    private String mAddress;
    private LatLng mLocation;
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
        mLocation = location;
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
        return mLocation;
    }

    public String getImageId() {
        return mImageId;
    }

    public String getAddress() {
        return mAddress;
    }
}
