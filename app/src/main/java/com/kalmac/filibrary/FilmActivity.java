package com.kalmac.filibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.SortedList;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilmActivity extends AppCompatActivity {


    ImageView filmPoster;
    TextView filmT;
    TextView filmReleaseDate;
    TextView filmCategory;
    TextView filmOverview;
    TextView filmRuntime;
    RatingBar filmRatingBar;


    LinearLayout creditLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film);

        filmPoster = findViewById(R.id.filmPoster);
        filmT = findViewById(R.id.filmT);
        filmReleaseDate = findViewById(R.id.filmR);
        filmCategory = findViewById(R.id.filmCategory);
        filmRatingBar = findViewById(R.id.filmRatingBar);
        filmOverview = findViewById(R.id.filmOverview);
        filmRuntime = findViewById(R.id.filmRuntime);

        creditLayout = findViewById(R.id.creditLayout);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            RetrofitClient retrofitClient = new RetrofitClient();
            Call<MovieDetailResults> call = retrofitClient.apiInterface.getMovieDetails(extras.getInt("filmdID"),retrofitClient.API_KEY, retrofitClient.LANGUAGE);
            call.enqueue(new Callback<MovieDetailResults>() {
                @Override
                public void onResponse(Call<MovieDetailResults> call, Response<MovieDetailResults> response) {


                    MovieDetailResults detailResults = response.body();
                    PicassoLoader.LoadImageToImageView(detailResults.getPosterPath(), filmPoster, 300, 350);
                    filmT.setText(detailResults.getTitle());
                    filmReleaseDate.setText(DateFormatter.FormatDate(detailResults.getReleaseDate()));
                    List<MovieDetailResults.GenresDTO> genresDTO = detailResults.getGenres();
                    for (int i=0; i<genresDTO.size(); i++){
                        filmCategory.setText(genresDTO.get(i).getName());

                    }
                    filmRatingBar.setRating(detailResults.getVoteAverage().floatValue() / 2);
                    filmOverview.setText(detailResults.getOverview());
                    filmRuntime.setText(detailResults.getRuntime() + " Minutes");
                }

                @Override
                public void onFailure(Call<MovieDetailResults> call, Throwable t) {
                    Log.d("STATE", "failed");
                }
            });

            RetrofitClient retrofitClient1 = new RetrofitClient();
            Call<MovieCreditResults> creditResultsCall = retrofitClient.apiInterface.getMovieCredits(extras.getInt("filmdID"),retrofitClient.API_KEY, retrofitClient.LANGUAGE);

            creditResultsCall.enqueue(new Callback<MovieCreditResults>() {
                @Override
                public void onResponse(Call<MovieCreditResults> call, Response<MovieCreditResults> response) {

                    MovieCreditResults creditResults = response.body();
                    List<MovieCreditResults.CastDTO> castResults = creditResults.getCast();
                    for (int i=0; i<castResults.size(); i++){
                        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View v = vi.inflate(R.layout.item_actor, null);
                        if(v == null)
                            Log.d("STATE", "deneme");
                        ImageView profileP = (ImageView)  v.findViewById(R.id.profilePhoto);
                        TextView profileName = (TextView) v.findViewById(R.id.profileName);

                        PicassoLoader.LoadImageToImageView(castResults.get(i).getProfilePath(), profileP, 30, 50);
                        profileName.setText(castResults.get(i).getName());

                        creditLayout.addView(v);
                    }

                }

                @Override
                public void onFailure(Call<MovieCreditResults> call, Throwable t) {

                }
            });

        }
    }
}