package com.solo.movinfo.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.solo.movinfo.data.db.DateConverter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Entity(tableName = "movie")
@TypeConverters(DateConverter.class)
public class Movie implements Parcelable {

    @SerializedName("id")
    @ColumnInfo(name = "id")
    @PrimaryKey
    @NonNull
    private Integer movieId;

    @SerializedName("poster_url")
    @ColumnInfo(name = "poster_url")
    private String posterUrl;

    @SerializedName("vote_count")
    @ColumnInfo(name = "vote_count")
    private int voteCount;

    @SerializedName("vote_average")
    @ColumnInfo(name = "vote_average")
    private float voteAverage;

    @SerializedName("title")
    @ColumnInfo(name = "title")
    private String title;

    @SerializedName("popularity")
    @ColumnInfo(name = "popularity")
    private float popularity;

    @SerializedName("poster_path")
    @ColumnInfo(name = "poster_path")
    private String posterPath;

    @SerializedName("original_language")
    @ColumnInfo(name = "original_language")
    private String originalLanguage;

    @SerializedName("original_title")
    @ColumnInfo(name = "original_title")
    private String originalTitle;

    @SerializedName("genre_ids")
    @Ignore
    private ArrayList<String> genreIds;

    @SerializedName("backdrop_path")
    @ColumnInfo(name = "backdrop_path")
    private String backdropPath;

    @SerializedName("adult")
    @ColumnInfo(name = "adult")
    private boolean adult;

    @SerializedName("overview")
    @ColumnInfo(name = "overview")
    private String overview;

    @SerializedName("release_date")
    @ColumnInfo(name = "release_date")
    private Date releaseDate;

    @Override
    public String toString() {
        return "MovieResponse{" +
                "voteAverage=" + voteAverage +
                ", title='" + title + '\'' +
                '}';
    }

    public Movie() {
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public int getVoteCount() {
        return voteCount;
    }

    @NonNull
    public Integer getMovieId() {
        return movieId;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public float getPopularity() {
        return popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public ArrayList<String> getGenreIds() {
        return genreIds;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public boolean isAdult() {
        return adult;
    }

    public String getOverview() {
        return overview;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public String getFormattedDate() {
        SimpleDateFormat sf = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        sf.applyLocalizedPattern("E, MMM dd, yyyy");
        return sf.format(releaseDate);
    }

    private Movie(Parcel in) {
        posterUrl = in.readString();
        voteCount = in.readInt();
        movieId = in.readInt();
        voteAverage = in.readFloat();
        title = in.readString();
        popularity = in.readFloat();
        posterPath = in.readString();
        originalLanguage = in.readString();
        originalTitle = in.readString();
        if (in.readByte() == 1) {
            genreIds = new ArrayList<>();
            in.readList(genreIds, String.class.getClassLoader());
        } else {
            genreIds = null;
        }
        backdropPath = in.readString();
        adult = in.readByte() != 0;
        overview = in.readString();
        long tmpReleaseDate = in.readLong();
        releaseDate = tmpReleaseDate != -1 ? new Date(tmpReleaseDate) : null;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public void setMovieId(@NonNull Integer movieId) {
        this.movieId = movieId;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setGenreIds(ArrayList<String> genreIds) {
        this.genreIds = genreIds;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterUrl);
        dest.writeInt(voteCount);
        dest.writeInt(movieId);
        dest.writeFloat(voteAverage);
        dest.writeString(title);
        dest.writeFloat(popularity);
        dest.writeString(posterPath);
        dest.writeString(originalLanguage);
        dest.writeString(originalTitle);
        if (genreIds == null) {
            dest.writeByte((byte) (0));
        } else {
            dest.writeByte((byte) (1));
            dest.writeList(genreIds);
        }
        dest.writeString(backdropPath);
        dest.writeByte((byte) (adult ? 1 : 0));
        dest.writeString(overview);
        dest.writeLong(releaseDate != null ? releaseDate.getTime() : -1L);
    }

    @SuppressWarnings("unused")
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}