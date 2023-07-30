package com.kalmac.filibrary;

import android.graphics.Movie;
import android.text.method.MovementMethod;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public static String BASE_URL = "https://api.themoviedb.org";
    public static String API_KEY = "2ed3818008defb09e1fc8aec79baaf00";
    //public static String IMAGE_URL = "https://image.tmdb.org/t/p/w500";
    public static int PAGE = 1;
    public static String LANGUAGE = "en-US";
    public static boolean IS_ADULT = false;
    //public static String QUERY = "Avengers";
    private Retrofit retrofit;
    public ApiInterface apiInterface;


    public RetrofitClient(){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiInterface = retrofit.create(ApiInterface.class);
    }


}
