package com.example.coren.sherb;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
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
import java.util.HashMap;


public class CompteActivity extends Main implements View.OnClickListener {

    private HashMap<String, String> params = new HashMap<String,String>();
    private ArrayList<String> emails;
    private ArrayList<String> pwd;
    private ArrayList<User> User = new ArrayList<User>();
    EditText editTextEmail, editTextPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.creer_compte);

        editTextEmail=(EditText)findViewById(R.id.EditText_Email);
        editTextPassword=(EditText)findViewById(R.id.EditText_Mdp);

        findViewById(R.id.createbutton).setOnClickListener(this);
        pwd = new ArrayList<>();
        emails = new ArrayList<>();

        new RequestTaskUser(new RequestTaskUser.AsyncResponse() {
            @Override
            public void processFinish(JSONArray output) {
                if (output != null) {
                    try {
                        Log.i("data", "refresh");
                        Log.e("App", "Success MAIN: " + output.getString(0));
                        for (int i = 0; i < output.length(); i++) {

                            JSONObject jsonObject = output.getJSONObject(i);
                            emails.add(jsonObject.getString("adr_mail"));
                            pwd.add(jsonObject.getString("mdp"));
                        }

                    } catch (JSONException ex) {
                        Log.e("App", "Failure MAIN");
                    }
                }
            }
        }).execute();

    }

    private void registerUser(){
        String Email= editTextEmail.getText().toString().trim();
        final String PassWord= editTextPassword.getText().toString().trim();

        if(Email.isEmpty()){
            editTextEmail.setError("Veuillez entrer votre adresse mail");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            editTextEmail.setError("Entrez une adresse mail valide");
            editTextEmail.requestFocus();
            return;
        }

        if(PassWord.isEmpty()){
            editTextPassword.setError("Veuillez entrer votre mot de passe");
            editTextPassword.requestFocus();
            return;
        }

        if(PassWord.length()<6) {
            editTextPassword.setError("Entrez un mot de passe de 6 caractères minimum");
            editTextPassword.requestFocus();
            return;
        }


        for (int i = 0; i<emails.size();i++) {
            if (Email.equals(emails.get(i)) && PassWord.equals(pwd.get(i))  ) {
                Toast.makeText(getApplicationContext(), "Tu as déjà un compte", Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                final RequestQueue queue = Volley.newRequestQueue(this);
                final String url = "http://10.234.10.196:3000/users"; // your URL

                params.put("adr_mail", editTextEmail.getText().toString());// the entered data as the JSON body.
                params.put("mdp", editTextPassword.getText().toString());

                JsonObjectRequest jsObjRequest = new
                        JsonObjectRequest(Request.Method.POST,
                        url,
                        new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i(getClass().getName(), "response success : "+response.toString());
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
                        .setContentTitle("Compte")
                        .setContentText("Votre compte a été créé, vous pourrez l'utiliser à votre prochaine connexion")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(0, mBuilder.build());

                SharedPreferences pref = getApplicationContext().getSharedPreferences(MY_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("emails", editTextEmail.getText().toString());
                editor.putString("pwd", editTextPassword.getText().toString());
                editor.apply();
                if (myprefs_name() != null /*les conditions sont respectées*/) {
                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("parties",parties);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        }
    }


    @Override public void onClick (View view) {
        switch(view.getId()) {
            case R.id.mon_compte:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.createbutton :
                registerUser();

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
