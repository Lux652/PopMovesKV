package com.example.lux.popmovieskv.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lux.popmovieskv.R;
import com.example.lux.popmovieskv.listeners.OnMoviesClick;
import com.example.lux.popmovieskv.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private List<Movie> movies;
    private String IMAGE_URL = "http://image.tmdb.org/t/p/w500";
    private OnMoviesClick mClick;

    public MoviesAdapter(List<Movie> movies, OnMoviesClick mClick){
        this.movies = movies;
        this.mClick = mClick;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent,false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder viewHolder, int position) {

        viewHolder.bind(movies.get(position));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView rating;
        ImageView poster;
        TextView releaseDate;
        TextView genre;
        Movie movie;


        public MovieViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_movie_title);
            rating = itemView.findViewById(R.id.item_movie_rating);
            releaseDate = itemView.findViewById(R.id.item_movie_release_date);
            poster = itemView.findViewById(R.id.item_movie_poster);
            genre = itemView.findViewById(R.id.item_movie_genre);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    mClick.onClick(movie);
                }
            });

        }

        public void bind(Movie movie){
            this.movie = movie;
            releaseDate.setText(movie.getReleaseDate());
            title.setText(movie.getTitle());
            rating.setText(movie.getVoteAverage());
            genre.setText("Genre");
            Glide.with(itemView)
                    .load(IMAGE_URL + movie.getPosterPath())
                    .apply(RequestOptions.placeholderOf(R.drawable.placeholder))
                    .into(poster);
        }
    }
}
