package com.dvt.samsung.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sev_user on 11/3/2016.
 */
public class Song implements Parcelable {
    private String name, fileName, path, artist, album, time;
    private long duration;
//    byte[] aByte;

    public Song(String name, String fileName, String path, String artist, String album, long duration) {
//      ,byte[] aByte) {
        this.name = name;
        this.fileName = fileName;
        this.path = path;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
//      this.aByte=aByte;
        convertDate();
    }

    public Song() {

    }

    protected Song(Parcel in) {
        name = in.readString();
        fileName = in.readString();
        path = in.readString();
        artist = in.readString();
        album = in.readString();
        time = in.readString();
        duration = in.readLong();
//        in.readByteArray(this.getaByte());
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    private void convertDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
        time = dateFormat.format(new Date(duration));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(fileName);
        dest.writeString(path);
        dest.writeString(artist);
        dest.writeString(album);
        dest.writeString(time);
        dest.writeLong(duration);
//      dest.writeByteArray(aByte);
    }

//    public byte[] getaByte() {
//        return aByte;
//    }

//    public void setaByte(byte[] aByte) {
//        this.aByte = aByte;
//    }
}
