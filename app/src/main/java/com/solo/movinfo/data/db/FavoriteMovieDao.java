package com.solo.movinfo.data.db;


import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.solo.movinfo.data.model.Movie;

@Dao
public interface FavoriteMovieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addFavoriteMovie(Movie movie);

    @Query("SELECT * FROM movie WHERE id = :id")
    LiveData<Movie> getFavoriteMovieById(int id);

    @Query("SELECT * FROM movie")
    DataSource.Factory<Integer, Movie> getFavoriteMovies();

    @Delete
    void removeFavoriteMovie(Movie movie);

}
