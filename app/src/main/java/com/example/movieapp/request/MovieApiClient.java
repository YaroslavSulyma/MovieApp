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

    // LiveData for Search
    private MutableLiveData<List<MovieModel>> mMovies;
    private static MovieApiClient instance;

    //Making global runnable
    private RetrieveMoviesRunnable retrieveMoviesRunnable;

    // LiveData for popular movies
    private MutableLiveData<List<MovieModel>> mMoviesPop;

    //Making popular runnable
    private RetrieveMoviesRunnablePop retrieveMoviesRunnablePOP;

    public static MovieApiClient getInstance() {
        if (instance == null) {
            instance = new MovieApiClient();
        }
        return instance;
    }

    public MovieApiClient() {
        mMovies = new MutableLiveData<>();
        mMoviesPop = new MutableLiveData<>();
    }


    public LiveData<List<MovieModel>> getMovies() {
        return mMovies;
    }

    public LiveData<List<MovieModel>> getMoviesPop() {
        return mMoviesPop;
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

    public void searchMovieApiPop(int pageNumber) {
        if (retrieveMoviesRunnablePOP != null) {
            retrieveMoviesRunnablePOP = null;
        }

        retrieveMoviesRunnablePOP = new RetrieveMoviesRunnablePop(pageNumber);
        final Future myHandler2 = AppExecutors.getInstance().networkIO().submit(retrieveMoviesRunnablePOP);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //cancelling the retrofit call
                myHandler2.cancel(true);
            }
        }, 1000, TimeUnit.MILLISECONDS);
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

    private class RetrieveMoviesRunnablePop implements Runnable {

        private int pageNumber;
        boolean cancelRequest;

        public RetrieveMoviesRunnablePop(int pageNumber) {
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            //Getting the Response object
            try {
                Response response2 = getPop(pageNumber).execute();

                if (cancelRequest) {
                    return;
                }
                if (response2.code() == 200) {
                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse) response2.body()).getMovies());
                    if (pageNumber == 1) {
                        //Sending data to LiveData
                        //PostValue: used for background threads
                        //setValue: not for background thread
                        mMoviesPop.postValue(list);
                    } else {
                        List<MovieModel> currentMovies = mMoviesPop.getValue();
                        currentMovies.addAll(list);
                        mMoviesPop.postValue(currentMovies);
                    }
                } else {
                    String error = response2.errorBody().string();
                    Log.v("Tag", "Error:" + error);
                    mMoviesPop.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mMoviesPop.postValue(null);
            }
        }

        //search method/query
        private Call<MovieSearchResponse> getPop(int pageNumber) {
            return Servicey.getMovieApi().getPopular(Credentials.API_KEY, pageNumber);
        }

        private void cancelRequest() {
            Log.v("Tag", "Cancelling request");
            cancelRequest = true;
        }
    }
}



