package ch.epfl.sdp.mocks;

import android.content.res.Resources;

import java.io.IOException;

import ch.epfl.sdp.R;
import ch.epfl.sdp.weather.Weather;
import ch.epfl.sdp.weather.WeatherFetcher;

public class MockWeatherFetcher implements WeatherFetcher {

    @Override
    public Weather getWeather() throws Exception {
        //TODO change to return default json
        String data =  Resources.getSystem().getString(R.string.default_weather);
        return new Weather(data);
    }
}
