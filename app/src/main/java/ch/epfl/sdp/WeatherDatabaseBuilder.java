package ch.epfl.sdp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

import ch.epfl.sdp.db.DatabaseObjectBuilder;
import ch.epfl.sdp.weather.Weather;

public class WeatherDatabaseBuilder extends DatabaseObjectBuilder<Weather> {
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
        }};
    }

    @Nullable
    @Override
    public LatLng getLocation(@NonNull Weather object) {
        return null;
    }
}
