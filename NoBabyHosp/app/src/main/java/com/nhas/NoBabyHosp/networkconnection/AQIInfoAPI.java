package com.nhas.NoBabyHosp.networkconnection;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AQIInfoAPI {

    private static final String BASE_URL = "https://api.waqi.info/feed/geo:";
    private static String API_KEY = "/?token=793555a685454d5bed6179dc3abe21b241a372ae";

    private OkHttpClient client = null;
    private String results;


    public AQIInfoAPI(){
        client = new OkHttpClient();
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
