package com.example.movieapp.utils;

import com.example.movieapp.response.MovieSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieApi {

    //Search movies
    //https://api.themoviedb.org/3/search/movie?api_key=f06128b33c066b5ca05e964387c3d654&query=Jack+Reacher
    @GET("3/search/movie?")
    Call<MovieSearchResponse> searchMovie(
            @Query("api_key") String key,
            @Query("query") String query,
            @Query("page") String page
    );
}
