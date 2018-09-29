package com.solo.movinfo.data.db;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.solo.movinfo.data.model.Movie;
import com.solo.movinfo.utils.Constants;

@Database(entities = Movie.class, version = 1)
public abstract class MoviesDatabase extends RoomDatabase {
    private static MoviesDatabase sMoviesDatabase = null;

    private static final Object sLOCK = new Object();

    public abstract FavoriteMovieDao favoriteMovieModel();

    public static MoviesDatabase getInstance(Context context) {
        if (sMoviesDatabase == null) {
            synchronized (sLOCK) {
                sMoviesDatabase = Room.databaseBuilder(context, MoviesDatabase.class,
                        Constants.DB_NAME)
                        .build();
            }
        }

        return sMoviesDatabase;
    }

}
