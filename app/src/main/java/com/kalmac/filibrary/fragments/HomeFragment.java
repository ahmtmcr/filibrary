package com.kalmac.filibrary.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.kalmac.filibrary.ApiInterface;
import com.kalmac.filibrary.DateFormatter;
import com.kalmac.filibrary.FilmActivity;
import com.kalmac.filibrary.MainActivity;
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
    RadioGroup trendSwitch;
//    RadioButton trendButton;
    ProgressBar progressFilms;
    ProgressBar progressBarFilm2;

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
        lineerLay = (LinearLayout) view.findViewById(R.id.lineerLay);
        progressFilms = (ProgressBar) view.findViewById(R.id.progressFilms);
        progressBarFilm2 = (ProgressBar) view.findViewById(R.id.progressFilms1);
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

        trendSwitch = (RadioGroup) view.findViewById(R.id.toggle);
//        trendButton = (RadioButton) view.findViewById(R.id.today);
//        trendButton.setChecked(true);
        trendSwitch.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
              if (i == R.id.today){
                  setDafaultViewForTrending();
              }
              else if (i == R.id.week){
                  setWeeklyViewForTrending();
              }
            }
        });

        return  view;
    }

    private void setDafaultViewForTrending(){
        progressFilms.setVisibility(View.VISIBLE);
        progressFilms.setProgress(0);
        lineerLay.removeAllViews();
        RetrofitClient retrofitClient = new RetrofitClient();
        Call<MovieResults> call = retrofitClient.apiInterface.getDailyTrendingMovies(retrofitClient.API_KEY);
        call.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                progressFilms.setProgress(100);
                progressFilms.setVisibility(View.GONE);
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
                    TextView filmID = (TextView) filmItem.findViewById(R.id.filmId);
                    filmID.setText(listOfMovies.get(i).getId().toString());

                    imB.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Integer id = Integer.parseInt(filmID.getText().toString());
                            Intent i = new Intent(getActivity(), FilmActivity.class);
                            i.putExtra("filmdID", id);
                            startActivity(i);
                        }
                    });

                    lineerLay.addView(filmItem);

                }

            }

            @Override
            public void onFailure(Call<MovieResults> call, Throwable t) {

            }
        });
    }

    private void setWeeklyViewForTrending(){
        progressFilms.setVisibility(View.VISIBLE);
        progressFilms.setProgress(0);
        lineerLay.removeAllViews();
        RetrofitClient retrofitClient = new RetrofitClient();
        Call<MovieResults> call = retrofitClient.apiInterface.getWeeklyTrendingMovies(retrofitClient.API_KEY);
        call.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                progressFilms.setProgress(100);
                progressFilms.setVisibility(View.GONE);
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
        progressBarFilm2.setVisibility(View.VISIBLE);
        progressBarFilm2.setProgress(0);
        lineerLayPopular.removeAllViews();
        RetrofitClient retrofitClient = new RetrofitClient();
        Call<MovieResults> call = retrofitClient.apiInterface.getPopularMovies(retrofitClient.API_KEY, retrofitClient.LANGUAGE, retrofitClient.PAGE);
        call.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                progressBarFilm2.setProgress(100);
                progressBarFilm2.setVisibility(View.GONE);
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