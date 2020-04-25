package ch.epfl.sdp.utils;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import ch.epfl.sdp.db.DatabaseObjectBuilder;

public class MockStringBuilder extends DatabaseObjectBuilder<String> {

    public MockStringBuilder() {
        super("mock");
    }

    @Override
    public boolean hasLocation() {
        return false;
    }

    @Override
    public String buildFromMap(@NonNull Map<String, Object> data) {
        checkRequiredFields(data);
        return (String) data.get("mock");
    }

    @Override
    public Map<String, Object> serializeToMap(@NonNull String object) {
        Map<String, Object> result = new HashMap<>();
        result.put("mock", object);
        return result;
    }

    @Override
    public double getLatitude(@NonNull String object) {
        return 0;
    }

    @Override
    public double getLongitude(@NonNull String object) {
        return 0;
    }
}