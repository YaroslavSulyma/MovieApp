package com.example.movieapp.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.R;

public class PopularViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //Widgets
    ImageView imageView;
    RatingBar ratingBar;

    //Click Listener
    IOnMovieListener iOnMovieListener;

    public PopularViewHolder(@NonNull View itemView, IOnMovieListener iOnMovieListener) {
        super(itemView);

        this.iOnMovieListener = iOnMovieListener;

        imageView = itemView.findViewById(R.id.movie_img);
        ratingBar = itemView.findViewById(R.id.rating_bar);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        iOnMovieListener.onMovieListener(getAdapterPosition());
    }
}
