package com.kalmac.filibrary.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.kalmac.filibrary.ApiInterface;
import com.kalmac.filibrary.DateFormatter;
import com.kalmac.filibrary.MovieResults;
import com.kalmac.filibrary.PicassoLoader;
import com.kalmac.filibrary.R;
import com.kalmac.filibrary.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {



    ImageButton ib;
    TextView tv;
    LinearLayout lineerLay;
    LinearLayout lineerLayPopular;
    SwitchCompat trendSwitch;

    View view;
    LayoutInflater rInflater;
    ViewGroup rContainer;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rContainer = container;
        rInflater = inflater;
        view = rInflater.inflate(R.layout.fragment_home,  rContainer, false);
        tv = (TextView) view.findViewById(R.id.textView4);
        ib = (ImageButton) view.findViewById(R.id.button6);
        lineerLay = (LinearLayout) view.findViewById(R.id.lineerLay);
        lineerLayPopular = (LinearLayout) view.findViewById(R.id.lineerLayPopular);



        //        btn = (Button) view.findViewById(R.id.button3);
//        textView = (TextView) view.findViewById(R.id.homeText);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                textView.setText("RETARD");
//            }
//        });

        setDafaultViewForTrending();
        setDefaultViewForPopular();

        trendSwitch = (SwitchCompat) view.findViewById(R.id.trendSwitch);
        trendSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true){
                    trendSwitch.setText("This Week");
                    setWeeklyViewForTrending();
                }
                else{
                    trendSwitch.setText("Today");
                    setDafaultViewForTrending();
                }
            }
        });

        return  view;
    }

    private void setDafaultViewForTrending(){

        lineerLay.removeAllViews();
        RetrofitClient retrofitClient = new RetrofitClient();
        Call<MovieResults> call = retrofitClient.apiInterface.getDailyTrendingMovies(retrofitClient.API_KEY);
        call.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                MovieResults results = response.body();
                List<MovieResults.ResultsDTO> listOfMovies = results.getResults();
                for (int i=0; i< listOfMovies.size(); i++){
                    View filmItem = rInflater.inflate(R.layout.item_film,  rContainer, false);

                    ImageButton imB = filmItem.findViewById(R.id.filmPoster);
                    TextView filmT = filmItem.findViewById(R.id.filmT);
                    TextView filmReleaseDate = filmItem.findViewById(R.id.filmReleaseDate);
                    RatingBar rb = (RatingBar) filmItem.findViewById(R.id.ratingBar);
                    TextView userScore = (TextView) filmItem.findViewById(R.id.userScore);

                    PicassoLoader.LoadImageToImageButton(listOfMovies.get(i).getPosterPath(),imB, 300, 350);
                    filmT.setText(listOfMovies.get(i).getTitle());
                    filmReleaseDate.setText(DateFormatter.FormatDate(listOfMovies.get(i).getReleaseDate()));
                    rb.setRating(listOfMovies.get(i).getVoteAverage().floatValue() / 2);
                    userScore.setText("%" +  (float)(listOfMovies.get(i).getVoteAverage() * 10));
                    lineerLay.addView(filmItem);

                }

            }

            @Override
            public void onFailure(Call<MovieResults> call, Throwable t) {

            }
        });
    }

    private void setWeeklyViewForTrending(){
        lineerLay.removeAllViews();
        RetrofitClient retrofitClient = new RetrofitClient();
        Call<MovieResults> call = retrofitClient.apiInterface.getWeeklyTrendingMovies(retrofitClient.API_KEY);
        call.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                MovieResults results = response.body();
                List<MovieResults.ResultsDTO> listOfMovies = results.getResults();
                for (int i=0; i< listOfMovies.size(); i++){
                    View filmItem = rInflater.inflate(R.layout.item_film,  rContainer, false);

                    ImageButton imB = filmItem.findViewById(R.id.filmPoster);
                    TextView filmT = filmItem.findViewById(R.id.filmT);
                    TextView filmReleaseDate = filmItem.findViewById(R.id.filmReleaseDate);
                    RatingBar rb = (RatingBar) filmItem.findViewById(R.id.ratingBar);
                    TextView userScore = (TextView) filmItem.findViewById(R.id.userScore);

                    PicassoLoader.LoadImageToImageButton(listOfMovies.get(i).getPosterPath(),imB, 300, 350);
                    filmT.setText(listOfMovies.get(i).getTitle());
                    filmReleaseDate.setText(DateFormatter.FormatDate(listOfMovies.get(i).getReleaseDate()));
                    rb.setRating(listOfMovies.get(i).getVoteAverage().floatValue() / 2);
                    userScore.setText("%" +  (float)(listOfMovies.get(i).getVoteAverage() * 10));
                    lineerLay.addView(filmItem);

                }

            }

            @Override
            public void onFailure(Call<MovieResults> call, Throwable t) {

            }
        });
    }

    private void setDefaultViewForPopular(){
        lineerLayPopular.removeAllViews();
        RetrofitClient retrofitClient = new RetrofitClient();
        Call<MovieResults> call = retrofitClient.apiInterface.getPopularMovies(retrofitClient.API_KEY, retrofitClient.LANGUAGE, retrofitClient.PAGE);
        call.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                MovieResults results = response.body();
                List<MovieResults.ResultsDTO> listOfMovies = results.getResults();
                for (int i=0; i< listOfMovies.size(); i++){
                    View filmItem = rInflater.inflate(R.layout.item_film,  rContainer, false);

                    ImageButton imB = filmItem.findViewById(R.id.filmPoster);
                    TextView filmT = filmItem.findViewById(R.id.filmT);
                    TextView filmReleaseDate = filmItem.findViewById(R.id.filmReleaseDate);
                    RatingBar rb = (RatingBar) filmItem.findViewById(R.id.ratingBar);
                    TextView userScore = (TextView) filmItem.findViewById(R.id.userScore);

                    PicassoLoader.LoadImageToImageButton(listOfMovies.get(i).getPosterPath(),imB, 300, 350);
                    filmT.setText(listOfMovies.get(i).getTitle());
                    filmReleaseDate.setText(DateFormatter.FormatDate(listOfMovies.get(i).getReleaseDate()));
                    rb.setRating(listOfMovies.get(i).getVoteAverage().floatValue() / 2);
                    userScore.setText("%" +  (float)(listOfMovies.get(i).getVoteAverage() * 10));
                    lineerLayPopular.addView(filmItem);

                }

            }

            @Override
            public void onFailure(Call<MovieResults> call, Throwable t) {

            }
        });
    }

}