package com.kalmac.filibrary.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kalmac.filibrary.DateFormatter;
import com.kalmac.filibrary.FilmActivity;
import com.kalmac.filibrary.MovieDetailResults;
import com.kalmac.filibrary.MovieResults;
import com.kalmac.filibrary.PicassoLoader;
import com.kalmac.filibrary.R;
import com.kalmac.filibrary.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LibraryFragment extends Fragment {



    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseFirestore firebaseDatabase;

    LinearLayout libraryLinear;
    View view;
    LayoutInflater rInflater;
    ViewGroup rContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rContainer = container;
        rInflater = inflater;
        view = rInflater.inflate(R.layout.fragment_library,  rContainer, false);


        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        initComponents();


        return view;
    }

    private void initComponents(){
        libraryLinear = (LinearLayout) view.findViewById(R.id.libraryLinear);
    }

    @Override
    public void onResume() {
        super.onResume();
        setLibaryView();
    }



    private void setLibaryView(){
        libraryLinear.removeAllViews();
        CollectionReference users = firebaseDatabase.collection("users");
        DocumentReference documentReference = firebaseDatabase.collection("users").document(currentUser.getUid());

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                List<String> filmIdsStringList = new ArrayList<>();
                List<Long> filmLongList;
                if (task.isSuccessful()){
                    DocumentSnapshot ds = task.getResult();;
                    filmLongList = (List<Long>) ds.get("liked_film_ids");

                    ;
                    for (int i=0; i<filmLongList.size(); i++){
                        filmIdsStringList.add(filmLongList.get(i).toString());
                    }

                }

                for (int i=0; i<filmIdsStringList.size(); i++){
                    RetrofitClient retrofitClient = new RetrofitClient();
                    Call<MovieDetailResults> call = retrofitClient.apiInterface.getMovieDetails(Integer.parseInt(filmIdsStringList.get(i)), retrofitClient.API_KEY, retrofitClient.LANGUAGE);
                    call.enqueue(new Callback<MovieDetailResults>() {
                        @Override
                        public void onResponse(Call<MovieDetailResults> call, Response<MovieDetailResults> response) {
                            MovieDetailResults detailResults = response.body();
                            View filmItem = rInflater.inflate(R.layout.item_film_library,  rContainer, false);

                            ImageButton imB = filmItem.findViewById(R.id.filmPoster);
                            TextView filmT = filmItem.findViewById(R.id.filmT);
                            TextView filmReleaseDate = filmItem.findViewById(R.id.filmReleaseDate);
                            RatingBar rb = (RatingBar) filmItem.findViewById(R.id.ratingBar);
                            TextView rt = (TextView) filmItem.findViewById(R.id.filmRuntime);
                            PicassoLoader.LoadImageToImageButton(detailResults.getPosterPath(),imB, 300, 350);
                            filmT.setText(detailResults.getTitle());
                            filmReleaseDate.setText(DateFormatter.FormatDate(detailResults.getReleaseDate()));
                            rb.setRating(detailResults.getVoteAverage().floatValue() / 2);
                            TextView filmID = (TextView) filmItem.findViewById(R.id.filmId);
                            filmID.setText(detailResults.getId().toString());
                            rt.setText(detailResults.getRuntime().toString()  + " Minutes");

                            imB.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Uri uri = Uri.parse("http://www.filab-filmapp.com/" + filmID.getText().toString());
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    intent.putExtra("filmdID", filmID.getText().toString());
                                    startActivity(intent);
                                }
                            });
                            libraryLinear.addView(filmItem);
                        }

                        @Override
                        public void onFailure(Call<MovieDetailResults> call, Throwable t) {

                        }
                    });
                }
            }
        });

    }
}