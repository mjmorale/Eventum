package ch.epfl.sdp.ui.createevent;

import android.location.Address;

import com.google.android.gms.maps.model.LatLng;

/**
 * A result from a geo search
 */
public class GeoSearchResult {
    private Address mAddress;

    /**
     * Constructor of the geo search result
     *
     * @param address
     */
    public GeoSearchResult(Address address) { this.mAddress = address; }

    /**
     * Method to get the address of the location from the geo search result
     *
     * @return the address of the location
     */
    public String getAddress() {
        StringBuilder display_address = new StringBuilder();
        display_address.append(mAddress.getAddressLine(0)).append("\n");
        for(int i = 1; i < mAddress.getMaxAddressLineIndex(); i++) {
            display_address.append(mAddress.getAddressLine(i)).append(", ");
        }
        return display_address.toString();
    }

    /**
     * Method to get the location from the geo search result
     *
     * @return the location (latitude, longitude)
     */
    public LatLng getLocation() {
        return new LatLng(mAddress.getLatitude(), mAddress.getLongitude());
    }

    /**
     * Method to get the name and the address of the location in a single string
     *
     * @return a string describing the location (name and address)
     */
    public String toString() {
        StringBuilder display_address = new StringBuilder();
        if(mAddress.getFeatureName() != null) {
            display_address.append(mAddress).append(", ");
        }
        for(int i = 0; i < mAddress.getMaxAddressLineIndex(); i++) {
            display_address.append(mAddress.getAddressLine(i));
        }
        return display_address.toString();
    }
}
