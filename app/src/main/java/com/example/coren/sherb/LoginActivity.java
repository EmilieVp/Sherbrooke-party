package com.example.coren.sherb;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.HashMap;

public class LoginActivity extends Main implements View.OnClickListener {

    private ArrayList<String> emails;
    private ArrayList<String> pwd;

    private String wrongName = "Veuillez entrer un nom d'utilisateur valide";
    EditText editTextEmail, editTextPassword;
    private String userName = null;
    private Button mEnter = null;
    TextView warningText = null;
    private User User;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        editTextEmail = (EditText) findViewById(R.id.EditText_Email);
        editTextPassword = (EditText) findViewById(R.id.EditText_Mdp);
        findViewById(R.id.creer_compte).setOnClickListener(this);
        findViewById(R.id.connexionbutton).setOnClickListener(this);

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

        /*if (myprefs_name() != null) {
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("parties",parties);
            intent.putExtras(bundle);
            startActivity(intent);
        }*/
    }

    private void userLogin(){


        String Email= editTextEmail.getText().toString().trim();
        String PassWord= editTextPassword.getText().toString().trim();

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

        for (int i = 0; i<emails.size();i++) {
            if(Email.equals(emails.get(i)) && !PassWord.equals(pwd.get(i))){
                editTextPassword.setError("Mot de passe erroné");
                editTextPassword.requestFocus();
                return;
            }
            else if (Email.equals(emails.get(i)) && PassWord.equals(pwd.get(i))  ) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences(MY_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("emails", Email);
                editor.putString("pwd", PassWord);
                editor.apply();
                if (myprefs_name() != null /*les conditions sont respectées*/) {
                    Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("parties",parties);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                return;
            }
            else{
                Toast.makeText(getApplicationContext(), "Tu n'as pas de compte", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.creer_compte:
                startActivity(new Intent(this, CompteActivity.class));
                break;
            case R.id.connexionbutton:
                userLogin();
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
