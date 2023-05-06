package com.kalmac.filibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    ImageButton goToMainButton;
    ImageButton signOutButton;
    ToggleButton tgButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        initComponents();
        registerEventHandlers();
    }

    private void initComponents(){
        goToMainButton = findViewById(R.id.backToMainActi);
        signOutButton = findViewById(R.id.signOut);

        tgButton = findViewById(R.id.toggleDarkMode);

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);;
        boolean darkMode = sharedPref.getBoolean("darkMode", false);

        tgButton.setChecked(darkMode);

    }
    private void registerEventHandlers(){


        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
        goToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToMain();
            }
        });

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean darkMode = sharedPref.getBoolean("darkMode", false);



        tgButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    setDarkThemeOn();
                } else{
                    setDarkThemeOff();
                }
            }
        });
    }

    private void signOut(){
        mAuth.signOut();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
    private void setDarkThemeOn(){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }
    private void setDarkThemeOff(){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
    private void backToMain(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        int currentNightMode = newConfig.uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("darkMode", false);
                editor.apply();
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                SharedPreferences sharedPrefre = this.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editore = sharedPrefre.edit();
                editore.putBoolean("darkMode", true);
                editore.apply();
                break;
        }
    }


}