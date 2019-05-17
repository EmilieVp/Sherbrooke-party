package com.example.coren.sherb;

import android.app.Fragment;
import android.app.ListFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailsActivity extends Main {

    private Party party;
    private DrawerLayout mDrawerLayout;
    private String MY_PREFS = "my_prefs";

    /*
    * si idHost == user.getId() alors on peut update la party. donc modifier le serveur
    * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_details);
        party = (Party) getIntent().getSerializableExtra("party");

        TextView titleTextView = findViewById(R.id.title);
        TextView descriptionTextView = findViewById(R.id.description);
        TextView  type = findViewById(R.id.typeSoirée);
        TextView price = findViewById(R.id.prix);
        TextView nbparticipant = findViewById(R.id.nb_participant);
        TextView horraire = findViewById(R.id.horaire);

        titleTextView.setText(party.getTitre());
        descriptionTextView.setText(party.getDescription());
        type.setText(party.getType());
        //price.setText(party.getPrice());
        //nbparticipant.setText(party.getMax_guest());
        horraire.setText(party.getSchedule());

        /*final EditText descriptionEditText = findViewById(R.id.EditText_Description);
        descriptionEditText.setVisibility(View.INVISIBLE);

        final ImageButton validateDescription = findViewById(R.id.validateImageButton);
        validateDescription.setVisibility(View.INVISIBLE);

        /*SET THE VISIBILITY OF editDescription BUTTON TO VISIBLE ONLY IF THE CURRENT USER IS ADMIN OF THE CURRENT PARTY*//*
        final ImageButton editDescription = findViewById(R.id.editImageButton);
        editDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descriptionTextView.setVisibility(View.INVISIBLE);
                editDescription.setVisibility(View.INVISIBLE);
                descriptionEditText.setVisibility(View.VISIBLE);
                validateDescription.setVisibility(View.VISIBLE);
                validateDescription.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        descriptionTextView.setText(descriptionEditText.getText().toString());
                        descriptionEditText.setVisibility(View.INVISIBLE);
                        validateDescription.setVisibility(View.INVISIBLE);
                        descriptionTextView.setVisibility(View.VISIBLE);
                        editDescription.setVisibility(View.VISIBLE);
                        /*SEND THE MODIFICATION TO THE SERVER*//*
                    }
                });
            }
        });*/

        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "http://192.168.0.38:3000/parties"; // your URL
/*
        Button button = findViewById(R.id.bouton_participer);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RequestTask(new RequestTask.AsyncResponse() {
                    @Override
                    public void processFinish(JSONArray output) {
                        if(output!= null){
                            try {
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
                                    JSONArray jsonArray =jsonObject.getJSONArray("idUsersGuest");
                                    ArrayList<String> participants = new ArrayList<>();
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        participants.add(jsonArray.get(j).toString());
                                    }
                                    Log.i("Compare type",type + " VS " + party.getType());
                                    Log.i("Compare title",title + " VS " + party.getTitre());
                                    Log.i("Compare description",description + " VS " + party.getDescription());

                                    if(type.equals(party.getType()) && title.equals(party.getTitre()) && description.equals(party.getDescription())){
                                        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.DELETE, url, jsonObject, new Response.Listener<JSONObject>() {
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

                                        JSONObject newJsonObject = new JSONObject();
                                        newJsonObject.put("idHost", idHost);
                                        newJsonObject.put("type", type);
                                        newJsonObject.put("title", title);// the entered data as the JSON body.
                                        newJsonObject.put("description", description);
                                        newJsonObject.put("price", price);// the entered data as the JSON body.
                                        newJsonObject.put("max_guest", max_guest);// the entered data as the JSON body.
                                        newJsonObject.put("schedule", schedule);// the entered data as the JSON body.
                                        newJsonObject.put("latitude", latitude);// the entered data as the JSON body.
                                        newJsonObject.put("longitude", longitude);// the entered data as the JSON body.

                                        participants.add(myprefs_name());
                                        JSONArray newJsonArray = new JSONArray();
                                        for(int j = 0;j< participants.size();j++){
                                            newJsonArray.put(participants.get(j));//ceci ne marche pas
                                        }
                                        newJsonObject.put("idUsersGuest",newJsonArray);

                                        JsonObjectRequest newjsObjRequest = new JsonObjectRequest(Request.Method.POST, url, newJsonObject, new Response.Listener<JSONObject>() {
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
                                        queue.add(newjsObjRequest);
                                    }
                                }
                            } catch (JSONException e) {
                                Log.e("App", "Failure DETAILSACTIVITY");
                            }
                        }
                    }
                }).execute();
            }
        });
*/


        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
            parties = (ArrayList<Party>) intent.getSerializableExtra("parties");
        }
    };
}