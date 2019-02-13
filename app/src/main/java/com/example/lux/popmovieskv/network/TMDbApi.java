package com.example.lux.popmovieskv.network;

import com.example.lux.popmovieskv.models.Movie;
import com.example.lux.popmovieskv.models.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDbApi {
    @GET("movie/now_playing")
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey);
    @GET("movie/{movie_id}")
    Call<Movie> getMovieDetails(@Path("movie_id") int id, @Query("api_key") String apiKey);
}
