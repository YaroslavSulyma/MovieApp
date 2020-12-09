package com.example.movieapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;
import com.example.movieapp.models.MovieModel;
import com.example.movieapp.utils.Credentials;

import java.util.List;

public class MovieRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MovieModel> mMovies;
    private IOnMovieListener iOnMovieListener;

    private static final int DISPLAY_POP = 1;
    private static final int DISPLAY_SEARCH = 2;

    public MovieRecyclerView(IOnMovieListener iOnMovieListener) {
        this.iOnMovieListener = iOnMovieListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == DISPLAY_SEARCH) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
            return new MovieViewHolder(view, iOnMovieListener);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_movies_layout, parent, false);
            return new PopularViewHolder(view, iOnMovieListener);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int itemView = getItemViewType(position);
        if (itemView == DISPLAY_SEARCH) {
            ((MovieViewHolder) holder).ratingBar.setRating(mMovies.get(position).getVote_average() / 2);
            Glide.with(holder.itemView.getContext())
                    .load("https://image.tmdb.org/t/p/w500/" + mMovies.get(position).getPoster_path())
                    .into(((MovieViewHolder) holder).imageView);
        } else {
            ((PopularViewHolder) holder).ratingBar.setRating(mMovies.get(position).getVote_average() / 2);
            Glide.with(holder.itemView.getContext())
                    .load("https://image.tmdb.org/t/p/w500/" + mMovies.get(position).getPoster_path())
                    .into(((PopularViewHolder) holder).imageView);
        }

    }

    @Override
    public int getItemCount() {
        if (mMovies != null) {
            return mMovies.size();
        }
        return 0;
    }

    public void setmMovies(List<MovieModel> mMovies) {
        this.mMovies = mMovies;
        notifyDataSetChanged();
    }

    //Getting the id of the movie click
    public MovieModel getSelectedMovie(int position) {
        if (mMovies != null) {
            if (mMovies.size() > 0) {
                return mMovies.get(position);
            }
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (Credentials.POPULAR) {
            return DISPLAY_POP;
        } else {
            return DISPLAY_SEARCH;
        }
    }
}
