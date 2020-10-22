package com.NHAS.Infantime.data.remote;


import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ENTInfoAPI {

    private static final String CURRENT_STAGE = "/Dev";
    private static final String BASE_URL = "https://ehgd6i0c8c.execute-api.us-east-1.amazonaws.com" + CURRENT_STAGE;
    private static String API_KEY;

    private OkHttpClient client = null;
    private String results;


    public ENTInfoAPI(String apiKey){
        API_KEY = apiKey;
        client = new OkHttpClient();
    }

    public String listSuburb(){
        final String methodPath = "/listSuburb";
        Request.Builder builder = new Request.Builder();
        builder.addHeader("x-api-key", API_KEY);
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    public String listDisease(){
        final String methodPath = "/listDisease";
        Request.Builder builder = new Request.Builder();
        builder.addHeader("x-api-key", API_KEY);
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }


    public String listDiseaseByID(String dId){
        final String methodPath = "/listDiseaseByID/" + dId;
        Request.Builder builder = new Request.Builder();
        builder.addHeader("x-api-key", API_KEY);
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    public String listDiseaseBySeason(String season){
        final String methodPath = "/listDiseaseBySeason/" + season;
        Request.Builder builder = new Request.Builder();
        builder.addHeader("x-api-key", API_KEY);
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    public String listDiseaseByName(String name){
        final String methodPath = "/listDiseaseByName/" + name;
        Request.Builder builder = new Request.Builder();
        builder.addHeader("x-api-key", API_KEY);
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    public String listFacilityByCatagoryAndSuburb(String catagory, String suburb){
        final String methodPath = "/ListFacilityByCatagoryAndSuburb/" + suburb.replace(" ", "+") + "/" + catagory.replace(" ", "+");
        Request.Builder builder = new Request.Builder();
        builder.addHeader("x-api-key", API_KEY);
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    public String listCatagoryBySuburb(String suburb){
        final String methodPath = "/ListCatagoryBySuburb/" + suburb.replace(" ", "+");
        Request.Builder builder = new Request.Builder();
        builder.addHeader("x-api-key", API_KEY);
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    public String listTipByType(String type){
        final String methodPath = "/listTipByType/" + type.replace(" ", "+");
        Request.Builder builder = new Request.Builder();
        builder.addHeader("x-api-key", API_KEY);
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    public String listTipByTypeAndGeneral(String type){
        final String methodPath = "/listTipByTypeAndGeneral/" + type.replace(" ", "+");
        Request.Builder builder = new Request.Builder();
        builder.addHeader("x-api-key", API_KEY);
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    public String listDiseaseSymptoms(){
        final String methodPath = "/listDiseaseSymptoms";
        Request.Builder builder = new Request.Builder();
        builder.addHeader("x-api-key", API_KEY);
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    public String listDiseaseBySymptoms(String symptom){
        final String methodPath = "/listDiseaseBySymptoms/" + symptom.replace(" ", "+");
        Request.Builder builder = new Request.Builder();
        builder.addHeader("x-api-key", API_KEY);
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    public String listDiseaseSymptomsBySID(String sId){
        final String methodPath = "/listDiseaseSymptomsBySID/" + sId;
        Request.Builder builder = new Request.Builder();
        builder.addHeader("x-api-key", API_KEY);
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    public String listDiseaseSymptomsByDID(String dId){
        final String methodPath = "/listDiseaseSymptomsByDID/" + dId;
        Request.Builder builder = new Request.Builder();
        builder.addHeader("x-api-key", API_KEY);
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    public String listSymptom(){
        final String methodPath = "/listSymptom";
        Request.Builder builder = new Request.Builder();
        builder.addHeader("x-api-key", API_KEY);
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    public String listSymptomByID(String sId){
        final String methodPath = "/listSymptomByID/" + sId;
        Request.Builder builder = new Request.Builder();
        builder.addHeader("x-api-key", API_KEY);
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    public String listSymptomByDescription(String des){
        final String methodPath = "/listSymptomByDescription/" + des;
        Request.Builder builder = new Request.Builder();
        builder.addHeader("x-api-key", API_KEY);
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    public String listSymptomByArea(String area){
        final String methodPath = "/listSymptomByArea/" + area;
        Request.Builder builder = new Request.Builder();
        builder.addHeader("x-api-key", API_KEY);
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    public String listDiseaseTask(){
        final String methodPath = "/listDiseaseTask";
        Request.Builder builder = new Request.Builder();
        builder.addHeader("x-api-key", API_KEY);
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    public String listDiseaseTaskByDID(String dId){
        final String methodPath = "/listDiseaseTaskByDID/" + dId;
        Request.Builder builder = new Request.Builder();
        builder.addHeader("x-api-key", API_KEY);
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    public String listDiseaseTaskByTID(String tId){
        final String methodPath = "/listDiseaseTaskByTID/" + tId;
        Request.Builder builder = new Request.Builder();
        builder.addHeader("x-api-key", API_KEY);
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    public String listTask(){
        final String methodPath = "/listTask";
        Request.Builder builder = new Request.Builder();
        builder.addHeader("x-api-key", API_KEY);
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    public String listTaskByID(String tId){
        final String methodPath = "/listTaskByID/" + tId;
        Request.Builder builder = new Request.Builder();
        builder.addHeader("x-api-key", API_KEY);
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    public String listTaskByDescription(String des){
        final String methodPath = "/listTaskByDescription/" + des;
        Request.Builder builder = new Request.Builder();
        builder.addHeader("x-api-key", API_KEY);
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    public String listDiseaseTip(){
        final String methodPath = "/listDiseaseTip";
        Request.Builder builder = new Request.Builder();
        builder.addHeader("x-api-key", API_KEY);
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    public String listDiseaseTipByDID(String dId){
        final String methodPath = "/listDiseaseTipByDID/" + dId;
        Request.Builder builder = new Request.Builder();
        builder.addHeader("x-api-key", API_KEY);
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    public String listDiseaseTipByTID(String tId){
        final String methodPath = "/listDiseaseTipByTID/" + tId;
        Request.Builder builder = new Request.Builder();
        builder.addHeader("x-api-key", API_KEY);
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    public String listDiseaseTipBySeasonID(String sId){
        final String methodPath = "/listDiseaseTipBySeasonID/" + sId;
        Request.Builder builder = new Request.Builder();
        builder.addHeader("x-api-key", API_KEY);
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    public String listSeason(){
        final String methodPath = "/listSeason";
        Request.Builder builder = new Request.Builder();
        builder.addHeader("x-api-key", API_KEY);
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    public String listSeasonByID(String sId){
        final String methodPath = "/listSeasonByID/" + sId;
        Request.Builder builder = new Request.Builder();
        builder.addHeader("x-api-key", API_KEY);
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    public String listSeasonByName(String sName){
        final String methodPath = "/listSeasonByName/" + sName;
        Request.Builder builder = new Request.Builder();
        builder.addHeader("x-api-key", API_KEY);
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    public String listTip(){
        final String methodPath = "/listTip";
        Request.Builder builder = new Request.Builder();
        builder.addHeader("x-api-key", API_KEY);
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    public String listTipByID(String tId){
        final String methodPath = "/listTipByID/" + tId;
        Request.Builder builder = new Request.Builder();
        builder.addHeader("x-api-key", API_KEY);
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    public String listTipByDescription(String des){
        final String methodPath = "/listTipByDescription/" + des;
        Request.Builder builder = new Request.Builder();
        builder.addHeader("x-api-key", API_KEY);
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    public String listSeasonENTData(){
        final String methodPath = "/listSeasonENTData";
        Request.Builder builder = new Request.Builder();
        builder.addHeader("x-api-key", API_KEY);
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    public String listSeasonENTDatabySeasonID(String sId){
        final String methodPath = "/listSeasonENTDatabySeasonID/" + sId;
        Request.Builder builder = new Request.Builder();
        builder.addHeader("x-api-key", API_KEY);
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    public String listSeasonENTDataByAdmissionNumber(String aNum){
        final String methodPath = "/listSeasonENTDataByAdmissionNumber/" + aNum;
        Request.Builder builder = new Request.Builder();
        builder.addHeader("x-api-key", API_KEY);
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    public String listArea(){
        final String methodPath = "/listArea";
        Request.Builder builder = new Request.Builder();
        builder.addHeader("x-api-key", API_KEY);
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    public String listAreaByID(String aId){
        final String methodPath = "/listAreaByID/" + aId;
        Request.Builder builder = new Request.Builder();
        builder.addHeader("x-api-key", API_KEY);
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    public String listAreaByDescription(String des){
        final String methodPath = "/listAreaByDescription/" + des;
        Request.Builder builder = new Request.Builder();
        builder.addHeader("x-api-key", API_KEY);
        builder.url(BASE_URL + methodPath);
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
