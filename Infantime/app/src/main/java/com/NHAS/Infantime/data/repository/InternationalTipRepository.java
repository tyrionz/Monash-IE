package com.NHAS.Infantime.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.NHAS.Infantime.data.entities.InternationalTip;
import com.NHAS.Infantime.data.local.dao.Tip.InternationalTipDAO;
import com.NHAS.Infantime.data.local.database.TravelDatabase;
import com.NHAS.Infantime.data.remote.ENTInfoAPI;
import com.NHAS.Infantime.util.Enum.TipEnum;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class InternationalTipRepository {

    private ExecutorService executor = Executors.newFixedThreadPool(4);
    private InternationalTipDAO dao;
    private LiveData<List<InternationalTip>> tipList;

    public InternationalTipRepository(Application application) {
        TravelDatabase database = TravelDatabase.getInstance(application);
        dao = database.internationalTipDAO();
    }

    public LiveData<List<InternationalTip>> getAllTipByTid(long tid) {
        tipList = dao.getAllTipByTid(tid);
        return tipList;
    }

    public void updateInternationalTip(InternationalTip tip) {
        TravelDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.updateTip(tip);
            }
        });
    }

    // Fetch all tips and separate them
    // into different list base on when the tip should be doing
    public Future<List<InternationalTip>> fetchTipByType(String type, String key) {
        return executor.submit(() -> {
            ENTInfoAPI entInfoAPI = new ENTInfoAPI(key);
            // Call to get all tips from the database
            String result = entInfoAPI.listTipByType(type);

            // Parse the result and separate the tips
            List<InternationalTip> internationalTips = new ArrayList<>();
            try {
                JSONArray resultJson = new JSONArray(result);
                int length = resultJson.length();
                if (length > 0) {
                    for (int i = 0; i < length; i++) {
                        JSONObject thisTask = resultJson.getJSONObject(i);
                        String when = thisTask.getString(TipEnum.WHEN.toString());
                        if (when.equals("null")) {
                            when = "During";
                        }
                        internationalTips.add(new InternationalTip(thisTask.getString(TipEnum.TIP_NAME.toString()),
                                thisTask.getString(TipEnum.TIP_DES.toString()),
                                thisTask.getString(TipEnum.PURPOSE.toString()),
                                thisTask.getString(TipEnum.WHEN.toString())));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return internationalTips;
        });
    }
}
