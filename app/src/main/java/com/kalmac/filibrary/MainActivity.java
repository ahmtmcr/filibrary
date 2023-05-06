package com.kalmac.filibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kalmac.filibrary.fragments.LibraryFragment;
import com.kalmac.filibrary.fragments.SearchFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.kalmac.filibrary.fragments.HomeFragment;
public class MainActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FloatingActionButton settingsButton;

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    AppViewPagerAdapter appViewPagerAdapter;



    @Override
    public void onStart(){
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            swtichToLogin();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initComponents();
        registerEventHandlers();

        mAuth = FirebaseAuth.getInstance();
    }

    private void initComponents(){
        tabLayout = findViewById(R.id.tabs);
        viewPager2 = findViewById(R.id.viewPager);
        viewPager2.setUserInputEnabled(false);
        appViewPagerAdapter = new AppViewPagerAdapter(this);
        viewPager2.setAdapter(appViewPagerAdapter);
        settingsButton = findViewById(R.id.floatingActionButton);
    }
    private void registerEventHandlers(){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToSettings();
            }
        });


    }
    private void swtichToLogin(){
        Intent mainToLogin = new Intent(this, LoginActivity.class);
        startActivity(mainToLogin);
    }
    private void switchToSettings(){
        Intent mainToSettings = new Intent(this, SettingsActivity.class);
        startActivity(mainToSettings);
    }




}