package com.example.movieapp.request;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.movieapp.AppExecutors;
import com.example.movieapp.models.MovieModel;
import com.example.movieapp.response.MovieSearchResponse;
import com.example.movieapp.utils.Credentials;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import retrofit2.Call;
import retrofit2.Response;

public class MovieApiClient {

    private MutableLiveData<List<MovieModel>> mMovies;
    private static MovieApiClient instance;

    //Making global runnable
    private RetrieveMoviesRunnable retrieveMoviesRunnable;

    public static MovieApiClient getInstance() {
        if (instance == null) {
            instance = new MovieApiClient();
        }
        return instance;
    }

    public MovieApiClient() {
        mMovies = new MutableLiveData<>();
    }

    public LiveData<List<MovieModel>> getMovies() {
        return mMovies;
    }

    //1-This method we are going to call trough the classes
    public void searchMovieApi(String query, int pageNumber) {
        if (retrieveMoviesRunnable != null) {
            retrieveMoviesRunnable = null;
        }

        retrieveMoviesRunnable = new RetrieveMoviesRunnable(query, pageNumber);
        final Future myHandler = AppExecutors.getInstance().networkIO().submit(retrieveMoviesRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //cancelling the retrofit call
                myHandler.cancel(true);
            }
        }, 3000, TimeUnit.MILLISECONDS);
    }

    // retrieving data from RestAPI by runnable classes
    private class RetrieveMoviesRunnable implements Runnable {

        private String query;
        private int pageNumber;
        boolean cancelRequest;

        public RetrieveMoviesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            //Getting the Response object
            try {
                Response response = getMovies(query, pageNumber).execute();

                if (cancelRequest) {
                    return;
                }
                if (response.code() == 200) {
                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse) response.body()).getMovies());
                    if (pageNumber == 1) {
                        //Sending data to LiveData
                        //PostValue: used for background threads
                        //setValue: not for background thread
                        mMovies.postValue(list);
                    } else {
                        List<MovieModel> currentMovies = mMovies.getValue();
                        currentMovies.addAll(list);
                        mMovies.postValue(currentMovies);
                    }
                } else {
                    String error = response.errorBody().string();
                    Log.v("Tag", "Error:" + error);
                    mMovies.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mMovies.postValue(null);
            }
        }

        //search method/query
        private Call<MovieSearchResponse> getMovies(String query, int pageNumber) {
            return Servicey.getMovieApi().searchMovie(Credentials.API_KEY,
                    query, pageNumber);
        }

        private void cancelRequest() {
            Log.v("Tag", "Cancelling request");
            cancelRequest = true;
        }
    }
}



