package com.kalmac.filibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.SortedList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kalmac.filibrary.fragments.LibraryFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilmActivity extends AppCompatActivity {

    String filmIDString;
    ImageButton goBackToMain;
    ImageButton shareActivityButton;
    ToggleButton favoriteButton;
    ImageView filmPoster;
    TextView filmT;
    TextView filmReleaseDate;
    TextView filmCategory;
    TextView filmOverview;
    TextView filmRuntime;
    RatingBar filmRatingBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore FDb;
    LinearLayout creditLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film);


        handleIntent(getIntent());


        Bundle extras = getIntent().getExtras();
        if(extras != null){
            filmIDString = extras.getString("filmdID");
        }


        initComponents();
        registerEventHandlers();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            setContentView(R.layout.activity_login_register);
        }
        FDb = FirebaseFirestore.getInstance();
        CollectionReference users = FDb.collection("users");
        DocumentReference documentReference = FDb.collection("users").document(currentUser.getUid());

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot ds = task.getResult();;
                    System.out.println(ds.get("liked_film_ids"));
                    List<Long> filmLongList = (List<Long>) ds.get("liked_film_ids");
                    List<String> filmIdsStringList = new ArrayList<>();


                    for (int i=0; i<filmLongList.size(); i++){
                        filmIdsStringList.add(filmLongList.get(i).toString());
                    }
                    if (filmIdsStringList.contains(filmIDString)){
                        favoriteButton.setBackgroundResource(R.drawable.hearth_icon_filled);
                        favoriteButton.setChecked(true);
                    } else{
                        favoriteButton.setBackgroundResource(R.drawable.hearth_icon);
                    }
                }
            }
        });

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!favoriteButton.isChecked()){
                    //zaten favori
                    //favorilerden sil
                    favoriteButton.setBackgroundResource(R.drawable.hearth_icon);
                    Toast.makeText(getApplicationContext(), "Removed From the Library", Toast.LENGTH_LONG).show();
                    documentReference.update("liked_film_ids", FieldValue.arrayRemove(Integer.parseInt(extras.getString("filmdID"))));
                }
                else {
                    //favorilere ekle
                    favoriteButton.setBackgroundResource(R.drawable.hearth_icon_filled);
                    documentReference.update("liked_film_ids", FieldValue.arrayUnion(Integer.parseInt(extras.getString("filmdID"))));
                    Toast.makeText(getApplicationContext(), "Added To Library", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void registerEventHandlers(){
        goBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBackToMainActivity();
            }
        });

        shareActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareFilmActivity(filmIDString);
            }
        });
    }
    private void initComponents(){
        filmPoster = findViewById(R.id.filmPoster);
        filmT = findViewById(R.id.filmT);
        filmReleaseDate = findViewById(R.id.filmR);
        filmCategory = findViewById(R.id.filmCategory);
        filmRatingBar = findViewById(R.id.filmRatingBar);
        filmOverview = findViewById(R.id.filmOverview);
        filmRuntime = findViewById(R.id.filmRuntime);
        creditLayout = findViewById(R.id.creditLayout);
        favoriteButton = findViewById(R.id.favoriteButton);
        goBackToMain = findViewById(R.id.backToMainActivityButton);
        shareActivityButton = findViewById(R.id.shareFilmButton);
    }
    private void setFilm(int filmID){
        RetrofitClient retrofitClient = new RetrofitClient();
        Call<MovieDetailResults> call = retrofitClient.apiInterface.getMovieDetails(filmID,retrofitClient.API_KEY, retrofitClient.LANGUAGE);
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
        Call<MovieCreditResults> creditResultsCall = retrofitClient.apiInterface.getMovieCredits(filmID,retrofitClient.API_KEY, retrofitClient.LANGUAGE);

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

                    PicassoLoader.LoadImageToImageView(castResults.get(i).getProfilePath(), profileP, 30, 30);
                    profileName.setText(castResults.get(i).getName() + " as " + castResults.get(i).getCharacter());

                    creditLayout.addView(v);
                }

            }

            @Override
            public void onFailure(Call<MovieCreditResults> call, Throwable t) {

            }
        });

    }
    private void goBackToMainActivity(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
    private void shareFilmActivity(String filmID){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "http://www.filab-filmapp.com/" + filmID);
        sendIntent.setType("text/plain");
        sendIntent.putExtra("filmdID", filmID);
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    private void handleIntent(Intent intent){
        String appLinkAction = intent.getAction();
        Uri appLinkData = intent.getData();
        if (Intent.ACTION_VIEW.equals(appLinkAction) && appLinkData != null){
            String filmID = appLinkData.getLastPathSegment();
            setFilm(Integer.parseInt(filmID));
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }
}