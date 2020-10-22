package com.NHAS.Infantime.data.repository;

import com.NHAS.Infantime.data.entities.Disease;
import com.NHAS.Infantime.data.remote.ENTInfoAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DiseaseRepository {

    private ExecutorService executor = Executors.newFixedThreadPool(4);
    private ENTInfoAPI entInfoAPI;

    public DiseaseRepository(String key) {
        entInfoAPI = new ENTInfoAPI(key);
    }

    public Future<List<String>> getAllSymptoms(){
        return executor.submit(()->{
            // Call to get all symptoms from the database
            String result = entInfoAPI.listSymptom();

            List<String> symptomList = new ArrayList<>();
            // Parse the json result
            try{
                JSONArray resultJson = new JSONArray(result);
                int length = resultJson.length();
                if (length > 0){
                    for (int i = 0; i < length; i++){
                        symptomList.add(resultJson.getJSONObject(i).getString("SYM_DES"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return symptomList;
        });
    }

    public Future<List<Disease>> fetchDiseaseBySymptom(String symptom){
        return executor.submit(()->{
            // Call to get disease by symptom
            String result = entInfoAPI.listDiseaseBySymptoms(symptom);

            List<Disease> diseaseList = new ArrayList<>();
            // Parse the json result
            try{
                JSONArray resultJson = new JSONArray(result);
                int length = resultJson.length();
                if(length > 0){
                    for(int i = 0; i < length; i++){
                        JSONObject thisDisease = resultJson.getJSONObject(i);
                        diseaseList.add(new Disease(thisDisease.getInt("DISEASE_ID"),
                                thisDisease.getString("DISEASE_NAME"),
                                thisDisease.getString("COMMON_NAME")));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return diseaseList;
        });
    }

    public Future<List<Disease>> fetchAllDisease(){
        return executor.submit(() ->{
            // Call to get all diseases from the database
            String result = entInfoAPI.listDisease();

            List<Disease> diseaseList = new ArrayList<>();
            // Parse the json result
            try{
                JSONArray resultJson = new JSONArray(result);
                int length = resultJson.length();
                if(length > 0){
                    for(int i = 0; i < length; i++){
                        JSONObject thisDisease = resultJson.getJSONObject(i);
                        diseaseList.add(new Disease(thisDisease.getInt("DISEASE_ID"),
                                thisDisease.getString("DISEASE_NAME"),
                                thisDisease.getString("COMMON_NAME")));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return diseaseList;
        });
    }
}
