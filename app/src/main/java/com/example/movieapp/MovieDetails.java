package com.example.movieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.movieapp.models.MovieModel;

public class MovieDetails extends AppCompatActivity {

    private ImageView imageViewDetails;
    private RatingBar ratingBarDetails;
    private TextView titleDetails, descriptionDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        imageViewDetails = findViewById(R.id.imageView_details);
        ratingBarDetails = findViewById(R.id.ratingBar_details);
        titleDetails = findViewById(R.id.textView_title_details);
        descriptionDetails = findViewById(R.id.textView_description_details);

        getDataFromIntent();
    }

    private void getDataFromIntent() {
        if (getIntent().hasExtra("movie")) {
            MovieModel movieModel = getIntent().getParcelableExtra("movie");
            Log.v("Tag", "incoming intent: " + movieModel.getMovie_id());
        }
    }

}