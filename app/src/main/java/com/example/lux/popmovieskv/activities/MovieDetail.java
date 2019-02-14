package com.example.lux.popmovieskv.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lux.popmovieskv.R;
import com.example.lux.popmovieskv.database.DatabaseClient;
import com.example.lux.popmovieskv.models.FavMovie;
import com.example.lux.popmovieskv.models.Movie;
import com.example.lux.popmovieskv.network.RetrofitManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.EXTRA_TEXT;

public class MovieDetail extends AppCompatActivity implements Callback<Movie> {

    public static String MOVIE_ID="movie_id";
    public static final String CHECK = "value";
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
    private Float userRatingValue;
    private String userRatingString;
    private ActionBar toolbar;

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
        toolbar = getSupportActionBar();




        if(getIntent().getBooleanExtra(CHECK,true)){
            movieId = getIntent().getIntExtra(MOVIE_ID,movieId);
            Call<Movie>movieDetails = RetrofitManager.getInstance().service().getMovieDetails(movieId,API_KEY);
            movieDetails.enqueue(MovieDetail.this);
            fab.setImageResource(R.drawable.ic_action_favorite_border);
        }

        else{
            favMovieId = getIntent().getIntExtra(MOVIE_ID,favMovieId);
            final FavMovie favMovie = new FavMovie();
            fab.setImageResource(R.drawable.ic_action_favorite);
            getMovieByID(favMovieId);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    favMovie.setId(favMovieId);
                    deleteMovie(favMovie);
                    Toast.makeText(MovieDetail.this, getString(R.string.msg_del), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch(itemId){
            case R.id.btnShare:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType(getString(R.string.share_type));
                String shareBody = getString(R.string.share_body);
                sharingIntent.putExtra(EXTRA_TEXT,shareBody);
                startActivity(Intent.createChooser(sharingIntent,getString(R.string.share_title)));
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onResponse(Call<Movie> call, Response<Movie> response) {
       final Movie movie = response.body();
        if(response.isSuccessful() && movie!=null){
            movieTitle.setText(movie.getTitle());
            movieDate.setText(movie.getReleaseDate());
            movieRating.setText(movie.getVoteAverage());
            movieOverview.setText(movie.getOverview());
            toolbar.setTitle(movie.getTitle());
            Glide.with(MovieDetail.this)
                    .load(IMAGE_BASE_URL + movie.getBackdropPath())
                    .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                    .into(movieBackdrop);
            Glide.with(MovieDetail.this)
                    .load(IMAGE_BASE_URL + movie.getPosterPath())
                    .apply(RequestOptions.placeholderOf(R.drawable.placeholder))
                    .into(moviePoster);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MovieDetail.this);
                alert.setTitle(getString(R.string.dialog_title));
                // Set an EditText view to get user input
                final EditText input = new EditText(MovieDetail.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                alert.setView(input);
                alert.setPositiveButton(getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        userRatingString = input.getText().toString();
                        userRatingValue=Float.parseFloat(userRatingString);
                        if(userRatingValue<=10 && userRatingValue>=0){
                            saveMovie(movie);
                            Toast.makeText(MovieDetail.this, movie.getTitle() + getString(R.string.msg_add), Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(MovieDetail.this, getString(R.string.dialog_warning), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                alert.setNegativeButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });
                alert.show();
                }
            });
        }
        else{
            Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show();
        }

    }



    @Override
    public void onFailure(Call<Movie> call, Throwable t) {
        Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show();
    }

    private void saveMovie(Movie movie){
        final String Title = movie.getTitle();
        final String Overview = movie.getOverview();
        final Float Rating = userRatingValue;
        final String PosterPath = movie.getPosterPath();
        final String BackdropPath = movie.getBackdropPath();

        class SaveTask extends AsyncTask<Void, Void, Void>{

            @Override
            protected Void doInBackground(Void... voids) {
                FavMovie favMovie = new FavMovie();
                favMovie.setTitle(Title);
                favMovie.setOverview(Overview);
                favMovie.setUserRating(Rating);
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
        final FavMovie favMovies = DatabaseClient
                .getInstance(this.getApplicationContext())
                .getMovieDatabase()
                .favMovieDao()
                .loadMovieById(id);
        movieTitle.setText(favMovies.getTitle());
        movieOverview.setText(favMovies.getOverview());
        movieRating.setText(String.valueOf(favMovies.getUserRating()));
        Glide.with(MovieDetail.this)
                .load(IMAGE_BASE_URL + favMovies.getBackdropPath())
                .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                .into(movieBackdrop);
        Glide.with(MovieDetail.this)
                .load(IMAGE_BASE_URL + favMovies.getPosterPath())
                .apply(RequestOptions.placeholderOf(R.drawable.placeholder))
                .into(moviePoster);
        toolbar.setTitle(favMovies.getTitle());
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
            }
        }
        DM dm = new DM();
        dm.execute();
    }
}
