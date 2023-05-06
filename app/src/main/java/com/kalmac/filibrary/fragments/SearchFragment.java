package com.kalmac.filibrary.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.kalmac.filibrary.DateFormatter;
import com.kalmac.filibrary.FilmActivity;
import com.kalmac.filibrary.MovieResults;
import com.kalmac.filibrary.PicassoLoader;
import com.kalmac.filibrary.R;
import com.kalmac.filibrary.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchFragment extends Fragment {


    LinearLayout linearLayout;
    SearchView searchView;
    LayoutInflater rInflater;
    ViewGroup rContainer;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rContainer = container;
        rInflater = inflater;
        view = rInflater.inflate(R.layout.fragment_search,  rContainer, false);

        initComponents();
        registerEventHandlers();

        return view;
    }
    private void searhFilm(String s){
        RetrofitClient retrofitClient = new RetrofitClient();
        Call<MovieResults> call = retrofitClient.apiInterface.getMovies(retrofitClient.API_KEY, retrofitClient.LANGUAGE, s, retrofitClient.PAGE, retrofitClient.IS_ADULT);

        call.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                MovieResults results = response.body();
                List<MovieResults.ResultsDTO> listOfMovies = results.getResults();

                for (int i = 0; i < listOfMovies.size(); i++) {
                    View filmItem = rInflater.inflate(R.layout.item_film_search, rContainer, false);

                    ImageButton imB = filmItem.findViewById(R.id.filmPoster);
                    TextView filmT = filmItem.findViewById(R.id.filmT);
                    TextView filmReleaseDate = filmItem.findViewById(R.id.filmReleaseDate);
                    RatingBar rb = (RatingBar) filmItem.findViewById(R.id.ratingBar);
                    TextView userScore = (TextView) filmItem.findViewById(R.id.userScore);
                    PicassoLoader.LoadImageToImageButton(listOfMovies.get(i).getPosterPath(), imB, 300, 350);
                    filmT.setText(listOfMovies.get(i).getTitle());
                    filmReleaseDate.setText(DateFormatter.FormatDate(listOfMovies.get(i).getReleaseDate()));
                    rb.setRating(listOfMovies.get(i).getVoteAverage().floatValue() / 2);
                    userScore.setText("%" + (float) (listOfMovies.get(i).getVoteAverage() * 10));
                    TextView filmID = (TextView) filmItem.findViewById(R.id.filmId);
                    filmID.setText(listOfMovies.get(i).getId().toString());

                    imB.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Uri uri = Uri.parse("http://www.filab-filmapp.com/" + filmID.getText().toString());
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                    });


                    linearLayout.addView(filmItem);
                }


            }

            @Override
            public void onFailure(Call<MovieResults> call, Throwable t) {

            }
        });
    }
    private void initComponents(){
        searchView = view.findViewById(R.id.searchView);
        linearLayout = view.findViewById(R.id.searchFilmLinear);
    }
    private void registerEventHandlers(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //searhFilm(s);
                return false;
            }


            @Override
            public boolean onQueryTextChange(String s) {
                clearList();
                searhFilm(s);
                return false;
            }
        });
    }

    private void clearList(){
        linearLayout.removeAllViews();
    }
}