package com.example.movieapp.request;

import com.example.movieapp.utils.MovieApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.movieapp.utils.Credentials.BASE_URL;

public class Servicey {

    private static final Retrofit.Builder retrofitBuilder = new Retrofit.Builder().
            baseUrl(BASE_URL).
            addConverterFactory(GsonConverterFactory.create());

    private static final Retrofit retrofit = retrofitBuilder.build();

    private static final MovieApi movieApi = retrofit.create(MovieApi.class);

    public MovieApi getMovieApi() {
        return movieApi;
    }

}
