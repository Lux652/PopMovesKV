package com.example.lux.popmovieskv.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lux.popmovieskv.R;
import com.example.lux.popmovieskv.database.DatabaseClient;
import com.example.lux.popmovieskv.database.FavMovieDao;
import com.example.lux.popmovieskv.models.FavMovie;
import com.example.lux.popmovieskv.models.Movie;
import com.example.lux.popmovieskv.network.RetrofitManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetail extends AppCompatActivity implements Callback<Movie> {

    public static String API_MOVIE_ID="movie_id";
    public static String FAV_MOVIE_ID="movie_id";
    public static final String CHECK = "value";
    public static final String DB = "value";
    public static final String API_KEY = "9aac4d1664cdcf018243622a66c90bf9";
    private String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w500";
    private int movieId;
    private int favMovieId;

    private TextView movieTitle;
    private TextView movieDate;
    private TextView movieRating;
    private TextView movieOverview;
    private ImageView moviePoster;
    private ImageView movieBackdrop;
    private FloatingActionButton fab;
    String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        movieTitle = findViewById(R.id.tv_title);
        movieDate = findViewById(R.id.tv_release_date);
        movieRating = findViewById(R.id.tv_rating);
        movieOverview = findViewById(R.id.tv_overview);
        moviePoster = findViewById(R.id.iv_poster);
        movieBackdrop = findViewById(R.id.iv_backdrop);
        fab = findViewById(R.id.fab);


        if(getIntent().getBooleanExtra(CHECK,true)){
            movieId = getIntent().getIntExtra(API_MOVIE_ID,movieId);
            Call<Movie>movieDetails = RetrofitManager.getInstance().service().getMovieDetails(movieId,API_KEY);
            movieDetails.enqueue(MovieDetail.this);
            fab.setImageResource(R.drawable.ic_action_favorite_border);
        }

        else{
            favMovieId = getIntent().getIntExtra(FAV_MOVIE_ID,favMovieId);
            final FavMovie favMovie = new FavMovie();
            fab.setImageResource(R.drawable.ic_action_favorite);
            getMovieByID(favMovieId);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    favMovie.setId(favMovieId);
                    deleteMovie(favMovie);
                    Toast.makeText(MovieDetail.this, favMovie.getTitle() + " is deleted succesfully", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }



    @Override
    public void onResponse(Call<Movie> call, Response<Movie> response) {
       final Movie movie = response.body();
        if(response.isSuccessful() && movie!=null){
            movieTitle.setText(movie.getTitle());
            movieDate.setText(movie.getReleaseDate());
            movieRating.setText(movie.getVoteAverage());
            movieOverview.setText(movie.getOverview());
            Glide.with(MovieDetail.this)
                    .load(IMAGE_BASE_URL + movie.getBackdropPath())
                    .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                    .into(movieBackdrop);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MovieDetail.this);

                    alert.setTitle("Title");
                    alert.setMessage("Message");

                    // Set an EditText view to get user input
                    final EditText input = new EditText(MovieDetail.this);
                    input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                    alert.setView(input);

                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                           
                            value = input.getText().toString();

                            saveMovie(movie);
                        }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Canceled.
                        }
                    });

                    alert.show();
                }
            });
        }
        else{
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }

    }



    @Override
    public void onFailure(Call<Movie> call, Throwable t) {

    }

    private void saveMovie(Movie movie){
        final String Title = movie.getTitle();
        final String Overview = movie.getOverview();
        final String Rating = value;
        final String PosterPath = movie.getPosterPath();
        final String BackdropPath = movie.getBackdropPath();

        class SaveTask extends AsyncTask<Void, Void, Void>{

            @Override
            protected Void doInBackground(Void... voids) {
                FavMovie favMovie = new FavMovie();
                favMovie.setTitle(Title);
                favMovie.setOverview(Overview);
                favMovie.setVoteAverage(Rating);
                favMovie.setPosterPath(PosterPath);
                favMovie.setBackdropPath(BackdropPath);
                DatabaseClient.getInstance(getApplicationContext()).getMovieDatabase()
                        .favMovieDao()
                        .insertFavoriteMovie(favMovie);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                finish();
            }
        }
        SaveTask st = new SaveTask();
        st.execute();
    }


    private FavMovie getMovieByID(int id){
        FavMovie favMovies = DatabaseClient
                .getInstance(this.getApplicationContext())
                .getMovieDatabase()
                .favMovieDao()
                .loadMovieById(id);
        movieTitle.setText(favMovies.getTitle());
        movieOverview.setText(favMovies.getOverview());
        movieRating.setText(favMovies.getVoteAverage());
        Glide.with(MovieDetail.this)
                .load(IMAGE_BASE_URL + favMovies.getBackdropPath())
                .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                .into(movieBackdrop);

        return favMovies;

    }

    private void deleteMovie(final FavMovie favMovie){
        class DM extends AsyncTask<Void, Void, Void>{
            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(getApplicationContext()).getMovieDatabase()
                        .favMovieDao()
                        .deleteFavoriteMovie(favMovie);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                finish();
                startActivity(new Intent(MovieDetail.this,MainActivity.class));
            }
        }

        DM dm = new DM();
        dm.execute();
    }

    public void RatingDialog(){

    }

}
