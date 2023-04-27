package com.kalmac.filibrary;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("/3/search/movie")
    Call<MovieResults> getMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("query") String filmName,
            @Query("page") int page,
            @Query("include_adult") boolean includeAdult
            );

}
