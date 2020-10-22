package com.NHAS.Infantime.data.remote;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherInfoAPI {

    private static final String BASE_URL = "http://api.weatherstack.com/";
    private static final String CURRENT = "current";
    private static String API_KEY = "?access_key=";

    private OkHttpClient client = null;
    private String results;

    public WeatherInfoAPI(String key) {
        client = new OkHttpClient();
        API_KEY = "?access_key=" + key;
    }

    public String getCurrentWeather(String city){
        final String methodPath = "&query=" + city.replace(" ", "%20");
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + CURRENT + API_KEY + methodPath);
        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }
}
