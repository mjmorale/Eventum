package ch.epfl.sdp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.sdp.db.DatabaseObjectBuilder;
import ch.epfl.sdp.weather.Weather;

public class WeatherDatabaseBuilder extends DatabaseObjectBuilder<Weather> {

    private final long MILLIS_IN_SEC = 1000;

    @Override
    public boolean hasLocation() {
        return false;
    }

    @Nullable
    @Override
    public Weather buildFromMap(@NonNull Map data) {
        return new Weather((String) data.get("json"));
    }

    @Nullable
    @Override
    public Map<String, Object> serializeToMap(@NonNull Weather weather) {
        return new HashMap<String, Object>() {{
            put("json", weather.getString());
            put("date", new Timestamp(new Date(weather.getResponseTimestamp() * MILLIS_IN_SEC)));
        }};
    }

    @Nullable
    @Override
    public LatLng getLocation(@NonNull Weather weather) {
        return null;
    }
}
