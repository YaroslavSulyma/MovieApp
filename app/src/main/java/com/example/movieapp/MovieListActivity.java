package com.example.movieapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.movieapp.adapters.IOnMovieListener;
import com.example.movieapp.adapters.MovieRecyclerView;
import com.example.movieapp.models.MovieModel;
import com.example.movieapp.viewModels.MovieListViewModel;

import java.util.List;

public class MovieListActivity extends AppCompatActivity implements IOnMovieListener {

    //Recycler View

    private RecyclerView recyclerView;
    private MovieRecyclerView movieRecyclerViewAdapter;

    private MovieListViewModel movieListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mive_list);
        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recycler_view);


        movieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);

        //Calling the observers
        observeAnyChange();
        configureRecyclerView();

        searchMovieApi("Fast", 1);

    }

    //Observing any changes
    private void observeAnyChange() {
        movieListViewModel.getMovies().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                //observing for any data changes
                if (movieModels != null) {
                    for (MovieModel movieModel : movieModels) {
                        //Get the data
                        Log.v("Tag", "onChanged:" + movieModel.toString());

                        movieRecyclerViewAdapter.setmMovies(movieModels);
                    }
                }
            }
        });
    }

    //4- Calling method in Main Activity
    private void searchMovieApi(String query, int pageNumber) {
        movieListViewModel.searchMovieApi(query, pageNumber);
    }

    //5-initialising Recycler View
    private void configureRecyclerView() {
        movieRecyclerViewAdapter = new MovieRecyclerView(this);
        recyclerView.setAdapter(movieRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onMovieListener(int position) {
        Toast.makeText(this, "The position is: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCategoryClick(String category) {

    }



    /*private void getRetrofitResponse() {
        MovieApi movieApi = Servicey.getMovieApi();

        Call<MovieSearchResponse> responseCall = movieApi.searchMovie(
                Credentials.API_KEY,
                "Jack Reacher",
                1
        );

        responseCall.enqueue(new Callback<MovieSearchResponse>() {
            @Override
            public void onResponse(Call<MovieSearchResponse> call, Response<MovieSearchResponse> response) {
                if (response.code() == 200) {
                    Log.v("Tag", "the response " + response.body().toString());

                    List<MovieModel> movies = new ArrayList<>(response.body().getMovies());

                    for (MovieModel movie : movies) {
                        Log.v("Tag", "The list " + movie.getRelease_date());
                    }
                } else {
                    try {
                        Log.v("Tag", "ERROR" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieSearchResponse> call, Throwable t) {

            }
        });
    }

    private void getRetrofitResponseAccordingToID() {
        MovieApi movieApi = Servicey.getMovieApi();

        Call<MovieModel> responseCall = movieApi.getMovie(
                343611,
                Credentials.API_KEY
        );

        responseCall.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                if (response.code() == 200) {
                    MovieModel movie = response.body();
                    Log.v("Tag", "The response of the movie " + movie.getTitle());
                } else {
                    try {
                        Log.v("Tag", "ERROR");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {

            }
        });
    }*/
}