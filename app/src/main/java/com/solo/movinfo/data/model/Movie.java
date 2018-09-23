package com.solo.movinfo.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Movie implements Parcelable {

    @SerializedName("poster_url")
    private String posterUrl;

    @SerializedName("vote_count")
    private int voteCount;

    @SerializedName("id")
    private String movieId;

    @SerializedName("vote_average")
    private float voteAverage;

    @SerializedName("title")
    private String title;

    @SerializedName("popularity")
    private float popularity;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("original_language")
    private String originalLanguage;

    @SerializedName("original_title")
    private String originalTitle;

    @SerializedName("genre_ids")
    private ArrayList<String> genreIds;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("adult")
    private boolean adult;

    @SerializedName("overview")
    private String overview;

    @SerializedName("release_date")
    private Date releaseDate;

    @Override
    public String toString() {
        return "MovieResponse{" +
                "voteAverage=" + voteAverage +
                ", title='" + title + '\'' +
                '}';
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public String getMovieId() {
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
        movieId = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterUrl);
        dest.writeInt(voteCount);
        dest.writeString(movieId);
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