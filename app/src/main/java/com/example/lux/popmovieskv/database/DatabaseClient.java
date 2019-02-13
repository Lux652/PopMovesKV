package com.example.lux.popmovieskv.database;

import android.arch.persistence.room.Room;
import android.content.Context;

public class DatabaseClient {

    private static final String DATABASE_NAME = "Movies";
    private static DatabaseClient Instance;

    private MovieDatabase movieDatabase;

    private DatabaseClient(Context context){
        movieDatabase = Room.databaseBuilder(context.getApplicationContext(),
                MovieDatabase.class, DATABASE_NAME)
                .allowMainThreadQueries()
                .build();
    }


    public static synchronized DatabaseClient getInstance(Context context) {
        if (Instance == null) {
            Instance = new DatabaseClient(context);
        }
        return Instance;
    }

    public MovieDatabase getMovieDatabase(){
        return movieDatabase;
    }
}
