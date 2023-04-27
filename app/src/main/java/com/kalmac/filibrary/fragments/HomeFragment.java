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
    SwitchCompat trendSwitch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        tv = (TextView) view.findViewById(R.id.textView4);
        ib = (ImageButton) view.findViewById(R.id.button6);

        lineerLay = (LinearLayout) view.findViewById(R.id.lineerLay);



        //        btn = (Button) view.findViewById(R.id.button3);
//        textView = (TextView) view.findViewById(R.id.homeText);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                textView.setText("RETARD");
//            }
//        });



        trendSwitch = (SwitchCompat) view.findViewById(R.id.trendSwitch);

        trendSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true){
                    trendSwitch.setText("This Week");
                }
                else{
                    trendSwitch.setText("Today");
                }
            }
        });


        RetrofitClient retrofitClient = new RetrofitClient();
        Call<MovieResults> call = retrofitClient.apiInterface.getMovies(retrofitClient.API_KEY, retrofitClient.LANGUAGE, retrofitClient.QUERY, retrofitClient.PAGE, retrofitClient.IS_ADULT);

        call.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                MovieResults results = response.body();
                List<MovieResults.ResultsDTO> listOfMovies = results.getResults();
                for (int i=0; i< listOfMovies.size(); i++){
                    View filmItem = inflater.inflate(R.layout.item_film, container, false);

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

                //
//                MovieResults.ResultsDTO firstMovie = listOfMovies.get(0);
//                tv.setText(firstMovie.getTitle());
//                PicassoLoader.LoadImageToImageButton(ib, 300, 300);
            }

            @Override
            public void onFailure(Call<MovieResults> call, Throwable t) {

            }
        });
        return  view;
    }

}