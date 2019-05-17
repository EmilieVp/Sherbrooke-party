package com.example.coren.sherb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class MyPartiesActivity extends Main{
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private ArrayList<Party> myParties; //affiche une list de ces party avec la condition que idHost == user.getId() qd on clique sur cette party afficher details list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        myParties = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView_Soirée);

        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(2).setChecked(true);
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

            for(Party p : parties){
                if(p.getIdHost().equals(myprefs_name())){
                myParties.add(p);}
            }
            if(myParties != null) {
                mAdapter = new RecyclerViewListAdapter(getApplicationContext(),myParties);
                mRecyclerView.setAdapter(mAdapter);
            } else {
                //tu n'as pas créer de party
            }
        }
    };
}
