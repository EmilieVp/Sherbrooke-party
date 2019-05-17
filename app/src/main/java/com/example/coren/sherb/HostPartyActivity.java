package com.example.coren.sherb;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HostPartyActivity extends Main implements View.OnClickListener {


    EditText EditText_Type, EditText_Title, EditText_Description, EditText_Price, EditText_Schedule, EditText_MaxGuest, EditText_Address;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        EditText_Type = (EditText) findViewById(R.id.EditText_Type);
        EditText_Title = (EditText) findViewById(R.id.EditText_Title);
        EditText_Description = (EditText) findViewById(R.id.EditText_Description);
        EditText_Price = (EditText) findViewById(R.id.EditText_Price);
        EditText_Schedule = (EditText) findViewById(R.id.EditText_Schedule);
        EditText_MaxGuest = (EditText) findViewById(R.id.EditText_MaxGuest);
        EditText_Address = (EditText) findViewById(R.id.EditText_Address);

        findViewById(R.id.Confirm_button).setOnClickListener(this);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(1).setChecked(true);
    }

    private void ConfirmParty(){
        Geocoder geocoder = new Geocoder(getApplicationContext());
        List<Address> addresses= null;
        try {
            addresses = geocoder.getFromLocationName(EditText_Address.getText().toString(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(addresses.size()>0) {
            latitude= addresses.get(0).getLatitude();
            longitude= addresses.get(0).getLongitude();
        }


        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "http://192.168.0.38:3000/parties"; // your URL

        Party p = new Party(myprefs_name(),EditText_Type.getText().toString(),EditText_Title.getText().toString(),EditText_Description.getText().toString(),Integer.parseInt(EditText_Price.getText().toString()),Integer.parseInt(EditText_MaxGuest.getText().toString()),EditText_Schedule.getText().toString(),latitude,longitude);
        parties.add(p);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("idHost", myprefs_name());
            jsonObject.put("type", EditText_Type.getText().toString());
            jsonObject.put("title", EditText_Title.getText().toString());// the entered data as the JSON body.
            jsonObject.put("description", EditText_Description.getText().toString());
            jsonObject.put("price", EditText_Price.getText().toString());// the entered data as the JSON body.
            jsonObject.put("max_guest", EditText_MaxGuest.getText().toString());// the entered data as the JSON body.
            jsonObject.put("schedule", EditText_Schedule.getText().toString());// the entered data as the JSON body.
            jsonObject.put("latitude", latitude);// the entered data as the JSON body.
            jsonObject.put("longitude", longitude);// the entered data as the JSON body.
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            Toast toast = Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG);
            toast.show();
            }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.i(getClass().getName(), "That didn't work!");
        }
        });
        queue.add(jsObjRequest);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Party")
                .setContentText("Votre soirée a été créée")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());


        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("key_one",true);
        bundle.putSerializable("parties",parties);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    @Override public void onClick (View view) {
        switch(view.getId()) {
            case R.id.Confirm_button :
            ConfirmParty();
            break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        startService(new Intent(this, DataService.class));
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("CurrentParties"));
    }

    @Override
    protected void onPause(){
        super.onPause();
        // unregisterReceiver(mMessageReceiver);
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            parties =(ArrayList<Party>) intent.getSerializableExtra("parties");
        }
    };
}
