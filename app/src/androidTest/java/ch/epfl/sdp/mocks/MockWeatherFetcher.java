package ch.epfl.sdp.mocks;

import android.content.Context;
import android.content.res.Resources;

import ch.epfl.sdp.R;
import ch.epfl.sdp.weather.Weather;
import ch.epfl.sdp.weather.WeatherFetcher;

public class MockWeatherFetcher implements WeatherFetcher {

    @Override
    public void fetch(Context context, onResponseCallback callback) {
        String data =  Resources.getSystem().getString(R.string.default_weather);
        callback.onSuccess(new Weather(data));

    }
}
