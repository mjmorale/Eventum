package ch.epfl.sdp.ui.event;

import android.location.Address;

public class GeoSearchResult {
    private Address mAddress;

    public GeoSearchResult(Address address) { this.mAddress = address; }

    public String getAddress() {
        StringBuilder display_address = new StringBuilder();
        display_address.append(mAddress.getAddressLine(0)).append("\n");
        for(int i = 1; i < mAddress.getMaxAddressLineIndex(); i++) {
            display_address.append(mAddress.getAddressLine(i)).append(", ");
        }
        return display_address.toString();
    }

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
