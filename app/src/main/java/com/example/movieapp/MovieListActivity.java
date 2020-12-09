package com.example.movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.movieapp.adapters.IOnMovieListener;
import com.example.movieapp.adapters.MovieRecyclerView;
import com.example.movieapp.models.MovieModel;
import com.example.movieapp.viewModels.MovieListViewModel;

import java.util.List;

public class MovieListActivity extends AppCompatActivity implements IOnMovieListener {

    //Recycler View

    private RecyclerView recyclerView;
    private MovieRecyclerView movieRecyclerViewAdapter;

    //View Model
    private MovieListViewModel movieListViewModel;

    boolean isPopular = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mive_list);
        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //SearchView
        setupSearchView();
        recyclerView = findViewById(R.id.recycler_view);


        movieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);

        //Calling the observers
        observeAnyChange();
        configureRecyclerView();
        observePopularMovies();

        // Getting popular movies
        movieListViewModel.searchMoviePop(1);
    }

    private void observePopularMovies() {
        movieListViewModel.getPop().observe(this, new Observer<List<MovieModel>>() {
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


    //5-initialising Recycler View
    private void configureRecyclerView() {
        movieRecyclerViewAdapter = new MovieRecyclerView(this);
        recyclerView.setAdapter(movieRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        //RecyclerView pagination
        //loading next pages of api response
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    // here we need to display the next search result on the page of api
                    movieListViewModel.searchNextPage();
                }
            }
        });
    }

    @Override
    public void onMovieListener(int position) {
        //Toast.makeText(this, "The position is: " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MovieDetails.class);
        intent.putExtra("movie", movieRecyclerViewAdapter.getSelectedMovie(position));
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(String category) {

    }

    //Get data from searchView & query the api to get the results (Movies)
    private void setupSearchView() {
        final SearchView searchView = findViewById(R.id.search_movie);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                movieListViewModel.searchMovieApi(query, 1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPopular = false;
            }
        });
    }
}