package com.example.coren.sherb;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DataService extends Service {

    public ArrayList<Party> parties;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // Code to execute when the service is first created
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startid) {
        Log.i("ONSTART", "service start");
        parties = new ArrayList<>();
        new RequestTask(new RequestTask.AsyncResponse() {
            @Override
            public void processFinish(JSONArray output) {
                if (output != null) {
                    try {
                        Log.e("App", "Success MAIN: " + output.getString(0));

                        for (int i = 0; i < output.length(); i++) {

                            JSONObject jsonObject = output.getJSONObject(i);

                            String idHost = jsonObject.getString("idHost");
                            String type = jsonObject.getString("type");
                            String title = jsonObject.getString("title");
                            String description = jsonObject.getString("description");
                            int price = jsonObject.getInt("price");
                            int max_guest = jsonObject.getInt("max_guest");
                            String schedule = jsonObject.getString("schedule");
                            double latitude = jsonObject.getDouble("latitude");
                            double longitude = jsonObject.getDouble("longitude");

                            Party party = new Party(idHost,type,title,description,price,max_guest,schedule,latitude,longitude);
                            parties.add(party);
                        }
                        sendMessageToActivity(parties);
                        } catch (JSONException ex) {
                        Log.e("App", "Failure MAIN", ex);
                    }
                }
            }
        }).execute();
        return START_STICKY;
    }

    private void sendMessageToActivity(ArrayList<Party> p) {
        Intent intent = new Intent("CurrentParties");
        Bundle b = new Bundle();
        b.putSerializable("parties",p);
        intent.putExtras(b);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}