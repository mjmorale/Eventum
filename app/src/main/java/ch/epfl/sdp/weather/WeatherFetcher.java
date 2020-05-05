package ch.epfl.sdp.weather;

public interface WeatherFetcher {

    Weather getWeather() throws Exception;
}
