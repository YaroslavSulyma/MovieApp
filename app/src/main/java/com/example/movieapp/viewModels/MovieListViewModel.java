package com.example.movieapp.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movieapp.models.MovieModel;

import java.util.List;

public class MovieListViewModel extends ViewModel {

    private final MutableLiveData<List<MovieModel>> mMovies = new MutableLiveData<>();

    public MovieListViewModel() {
    }

    public LiveData<List<MovieModel>> getMovies() {
        return mMovies;
    }
}
