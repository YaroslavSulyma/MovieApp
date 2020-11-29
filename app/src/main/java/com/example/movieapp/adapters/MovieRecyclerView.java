package com.example.movieapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;
import com.example.movieapp.models.MovieModel;

import java.util.List;

public class MovieRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MovieModel> mMovies;
    private IOnMovieListener iOnMovieListener;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
        return new MovieViewHolder(parent, iOnMovieListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MovieViewHolder) holder).title.setText(mMovies.get(position).getTitle());
        ((MovieViewHolder) holder).releaseDate.setText(mMovies.get(position).getRelease_date());
        ((MovieViewHolder) holder).duration.setText(mMovies.get(position).getRuntime());
        ((MovieViewHolder) holder).ratingBar.setRating(mMovies.get(position).getVote_average() / 2);
        Glide.with(holder.itemView.getContext())
                .load(mMovies.get(position).getPoster_path())
                .into(((MovieViewHolder) holder).imageView);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public void setmMovies(List<MovieModel> mMovies) {
        this.mMovies = mMovies;
        notifyDataSetChanged();
    }
}
