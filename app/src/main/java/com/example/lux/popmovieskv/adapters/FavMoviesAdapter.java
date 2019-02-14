package com.example.lux.popmovieskv.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lux.popmovieskv.R;
import com.example.lux.popmovieskv.listeners.OnFavClick;
import com.example.lux.popmovieskv.listeners.OnMoviesClick;
import com.example.lux.popmovieskv.models.FavMovie;
import com.example.lux.popmovieskv.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class FavMoviesAdapter extends RecyclerView.Adapter<FavMoviesAdapter.FavViewHolder> {

    private Context context;
    private List<FavMovie> favMoviesList;
    private OnFavClick favClick;
    private String IMAGE_URL = "http://image.tmdb.org/t/p/w500";

    public FavMoviesAdapter(Context context, List<FavMovie> favMovies, OnFavClick favClick){
        this.context = context;
        this.favMoviesList = favMovies;
        this.favClick = favClick;
    }


    @NonNull
    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie,parent,false);
        return new FavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavViewHolder favViewHolder, int position) {

        favViewHolder.bind(favMoviesList.get(position));
    }

    @Override
    public int getItemCount() {
        return favMoviesList.size();
    }

    class FavViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView rating;
        ImageView poster;
        TextView releaseDate;
        TextView genre;
        FavMovie favMovie;


        public FavViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_movie_title);
            rating = itemView.findViewById(R.id.item_movie_rating);
            releaseDate = itemView.findViewById(R.id.item_movie_release_date);
            poster = itemView.findViewById(R.id.item_movie_poster);
            genre = itemView.findViewById(R.id.item_movie_genre);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    favClick.OnClick(favMovie);
                }
            });



        }

        public void bind(FavMovie favMovie){
            this.favMovie = favMovie;
            title.setText(favMovie.getTitle());

            rating.setText(String.valueOf(favMovie.getUserRating()));
            releaseDate.setText(favMovie.getReleaseDate());
            genre.setText("Genre");
            Glide.with(itemView)
                    .load(IMAGE_URL + favMovie.getPosterPath())
                    .apply(RequestOptions.placeholderOf(R.drawable.placeholder))
                    .into(poster);

        }

    }



}
