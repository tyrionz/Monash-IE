package com.NHAS.Infantime.data.remote;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AQIInfoAPI {

    private static final String BASE_URL = "https://api.waqi.info/feed/geo:";
    private static String API_KEY;

    private OkHttpClient client = null;
    private String results;


    public AQIInfoAPI(String key){
        client = new OkHttpClient();
        API_KEY =  "/?token=" + key;
    }

    public String getServerity(Double lat, Double lon){
        String geoCodeString = String.valueOf(lat) + ";" + String.valueOf(lon);
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + geoCodeString + API_KEY);
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
