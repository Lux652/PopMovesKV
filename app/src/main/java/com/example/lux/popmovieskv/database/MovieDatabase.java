package com.example.lux.popmovieskv.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.lux.popmovieskv.models.FavMovie;

@Database(entities = {FavMovie.class}, version=1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {
    public abstract FavMovieDao favMovieDao();

}
