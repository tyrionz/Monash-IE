package com.NHAS.Infantime.data.remote;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GoogleMapAPI {

    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/geocode/json?";
    private static final String ADDRESS = "address=";
    private static String API_KEY;

    private OkHttpClient client = null;


    public GoogleMapAPI(String apiKey){
        API_KEY = ",+CA&key=" + apiKey;
        client = new OkHttpClient();
    }

    /**
     *
     * @param address
     * @return
     */
    public LatLng getGeoCode(String address){
        LatLng result = null;
        address = address.replace(" ", "+");
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + ADDRESS + address + API_KEY);
        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            String results = response.body().string();
            JSONObject jsonObject= new JSONObject(results);
            JSONObject geometry = jsonObject.getJSONArray("results")
                    .getJSONObject(0)
                    .getJSONObject("geometry");
            result = new LatLng(geometry.getJSONObject("location").getDouble("lat")
                    , geometry.getJSONObject("location").getDouble("lng"));

        } catch (JSONException | IOException e) {
            result = new LatLng(999,999);
            e.printStackTrace();
        }
        return result;
    }
}
