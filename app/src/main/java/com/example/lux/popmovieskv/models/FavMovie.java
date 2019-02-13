package com.example.lux.popmovieskv.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity
public class FavMovie implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "poster_path")
    public String posterPath;

    @ColumnInfo(name = "backdrop_path")
    public String backdropPath;

    @ColumnInfo(name = "overview")
    public String overview;

    @ColumnInfo(name = "release_date")
    public String releaseDate;

    @ColumnInfo(name = "vote_average")
    public String voteAverage;

    public FavMovie(){}

    protected FavMovie(Parcel in){
        id = in.readInt();
        title = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        voteAverage = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FavMovie> CREATOR = new Creator<FavMovie>() {
        @Override
        public FavMovie createFromParcel(Parcel in) {
            return new FavMovie(in);
        }

        @Override
        public FavMovie[] newArray(int size) {
            return new FavMovie[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(posterPath);
        parcel.writeString(backdropPath);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
        parcel.writeString(voteAverage);

    }

    public int getId() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

}