package com.kalmac.filibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
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



//        ArrayList<Film> arrayOfFilms = new ArrayList<Film>();
//        CustomAdapter adapter = new CustomAdapter(this, arrayOfFilms);
//        ListView filmList = (ListView) findViewById(R.id.filmList);
//        filmList.setAdapter(adapter);
//
//
//

//

//

//
//        call.enqueue(new Callback<MovieResults>() {
//            @Override
//            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
//                MovieResults results = response.body();
//                List<MovieResults.ResultsDTO> listOfMovies = results.getResults();
//                //MovieResults.ResultsDTO firstMovie = listOfMovies.get(0);
//
//                //filmName.setText(firstMovie.getTitle());
//
//
//
//                //get names
//                //List<String> filmNames = null;
//                //for (int i=0; i<listOfMovies.size(); i++){
//                //    MovieResults.ResultsDTO Movie = listOfMovies.get(i);
//                //    filmNames.add(Movie.getTitle());
//               // }
//                //get release year
//                //List<String> filmReleaseYears = null;
//                //for (int i=0; i<listOfMovies.size(); i++){
//                //    MovieResults.ResultsDTO Movie = listOfMovies.get(i);
//                //    filmReleaseYears.add(Movie.getReleaseDate());
//               // }
//                //get posters
//                //List<Image> filmPosters = null;
//                //for (int i=0; i<listOfMovies.size(); i++){
//                 //   MovieResults.ResultsDTO Movie = listOfMovies.get(i);
//                //    filmPosters.add(Picasso.get().load(IMAGE_URL + Movie.getPosterPath()).into(Image);)
//                //}
//
//                //add films to list
//                for (int i=0; i<listOfMovies.size(); i++){
//                    MovieResults.ResultsDTO Movie = listOfMovies.get(i);
//                    Film newFilm = new Film(Movie.getTitle(), Movie.getReleaseDate());
//                    adapter.add(newFilm);
//                }
//
//            }
//
//
//
//            @Override
//            public void onFailure(Call<MovieResults> call, Throwable t) {
//
//            }
//        });

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