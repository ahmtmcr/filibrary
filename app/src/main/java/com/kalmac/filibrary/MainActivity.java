package com.kalmac.filibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.kalmac.filibrary.fragments.LibraryFragment;
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

    TabLayout tabLayout;
    ViewPager2 viewPager2;

    AppViewPagerAdapter appViewPagerAdapter;

//    public static String BASE_URL = "https://api.themoviedb.org";
//    public static String IMAGE_URL = "https://image.tmdb.org/t/p/w500";
//    public static int PAGE = 1;
//    public static String API_KEY = "2ed3818008defb09e1fc8aec79baaf00";
//    public static String LANGUAGE = "en-US";
//    public static boolean ADULT = false;
//    public static String QUERY = "Avengers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        registerEventHandlers();

//        //denme
//        LibraryFragment frag = (LibraryFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentLibrary);
//        frag.dene();



    }

    private void initComponents(){
        tabLayout = findViewById(R.id.tabs);
        viewPager2 = findViewById(R.id.viewPager);
        viewPager2.setUserInputEnabled(false);
        appViewPagerAdapter = new AppViewPagerAdapter(this);
        viewPager2.setAdapter(appViewPagerAdapter);


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


    }




}