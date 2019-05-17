package com.example.coren.sherb;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class ProfileActivity extends Main implements View.OnClickListener{
    EditText editTextPassword;
    private User User;
    TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        email=(TextView)findViewById(R.id.email);

       editTextPassword=(EditText)findViewById(R.id.EditText_Mdp);
        //auth = FirebaseAuth.getInstance();

        email.setText(User.getPseudo());

        findViewById(R.id.deactivatebutton).setOnClickListener(this);
        findViewById(R.id.changebutton).setOnClickListener(this);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(3).setChecked(true);
    }


    @Override public void onClick (View view) {
        switch(view.getId()) {

            case R.id.deactivatebutton:
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.changebutton:
                break;

        }
    }


}
