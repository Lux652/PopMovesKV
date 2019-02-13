package com.example.lux.popmovieskv.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.lux.popmovieskv.models.FavMovie;
import com.example.lux.popmovieskv.models.Movie;

import java.util.List;

@Dao
public interface FavMovieDao {
    @Query("SELECT * FROM FavMovie")
    List<FavMovie> getFavMovies();

    @Query("SELECT * from FavMovie where id = :id")
     FavMovie loadMovieById(int id);

    @Insert
    void insertFavoriteMovie (FavMovie movie);

    @Delete
    void deleteFavoriteMovie(FavMovie favMovie);
}
