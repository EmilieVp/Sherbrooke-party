package com.example.coren.sherb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;


public class Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    protected String MY_PREFS = "my_prefs";
    protected DrawerLayout mDrawerLayout;
    protected ArrayList<Party> parties;

    private final int HOME = 0;
    private final int MAP = 1;
    private final int CREATE = 2;
    private final int MY_PARTIES = 3;
    private final int LIST = 4;
    private final int PROFIL = 5;
    private final int HELP = 6;

    protected String myprefs_name() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        return pref.getString("emails", null);//null is the default value.
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // set item as selected to persist highlight
        item.setChecked(true);
        // close drawer when item is tapped
        mDrawerLayout.closeDrawers();
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_map:
                goSomewhere(MAP);
                break;
            case R.id.nav_poster_soiree:
                goSomewhere(CREATE);
                break;
            case R.id.nav_gérer_soiree:
                goSomewhere(MY_PARTIES);
                break;
            case R.id.nav_list_soiree:
                goSomewhere(LIST);
                break;
            case R.id.nav_profil:
                //goSomewhere(PROFIL);
                break;
            case R.id.nav_aide:
                //goSomewhere(HELP);
                break;
            case R.id.nav_deconnexion:
                disconnect();
                break;
            default:
                break;
        }

        this.mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void disconnect() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        pref.edit().remove("user_name").apply(); //clear pref pseudo
        pref.edit().remove("host").apply();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //clear stack activity
        startActivity(intent);
    }

    protected void goSomewhere(int val){
        Intent intent = new Intent(getApplicationContext(), HostPartyActivity.class);
        switch (val) {
            case HOME:
                //intent = new Intent(getApplicationContext(), MainActivity.class);
                break;
            case MAP:
                if(getClass() == MapsActivity.class){
                    Toast.makeText(this,
                            "Tu es déjà sur cette page",
                            Toast.LENGTH_SHORT).show();
                } else{
                intent = new Intent(getApplicationContext(), MapsActivity.class);}
                break;
            case CREATE:
                intent = new Intent(getApplicationContext(), HostPartyActivity.class);
                break;
            case MY_PARTIES:
                intent = new Intent(getApplicationContext(), MyPartiesActivity.class);
                break;
            case LIST:
                intent = new Intent(getApplicationContext(), ListPartiesActivity.class);
                break;
            case PROFIL:
                //intent = new Intent(getApplicationContext(), ProfileActivity.class);
                break;
            case HELP:
                //intent = new Intent(getApplicationContext(), HelpActivity.class);
                break;
            default:
                break;
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("parties",parties);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
