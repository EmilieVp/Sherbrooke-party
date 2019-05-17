package com.example.coren.sherb;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


public class RequestTaskUser extends AsyncTask<Void, Void, JSONArray> {

    public interface AsyncResponse {
        void processFinish(JSONArray output);
    }

    public AsyncResponse delegate = null;

    public RequestTaskUser(AsyncResponse delegate) {
        this.delegate = delegate;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected JSONArray doInBackground(Void... uri) {
        String str = "http://192.168.0.38:3000/users";
        URLConnection urlConn = null;
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(str);
            urlConn = url.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

            StringBuffer buffer = new StringBuffer();
            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null) {
                Log.i("tag1" + getClass().getName(), inputLine);
                buffer.append(inputLine + "\n");
            }
            return new JSONArray(buffer.toString());

        } catch (Exception e) {
            Log.e("App", "dataTask", e);
            return null;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onPostExecute(JSONArray response) {
        delegate.processFinish(response);
    }
}
