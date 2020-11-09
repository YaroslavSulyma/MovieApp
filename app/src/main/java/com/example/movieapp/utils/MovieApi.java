package com.example.movieapp.utils;

import com.example.movieapp.models.MovieModel;
import com.example.movieapp.response.MovieResponse;
import com.example.movieapp.response.MovieSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
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

    //Making search with id
    //https://api.themoviedb.org/3/movie/343611?api_key=f06128b33c066b5ca05e964387c3d654
    //Remember that  movie_id=550 is for Jack Reacher
    @GET("3/movie/{movie_id}?")
    Call<MovieModel> getMovie(
            @Path("movie_id") int movie_id,
            @Query("api_key") String api_key
    );
}
