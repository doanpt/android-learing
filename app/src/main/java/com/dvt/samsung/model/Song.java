package com.dvt.samsung.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sev_user on 11/3/2016.
 */
public class Song implements Serializable {
    private String name, fileName, path, artist, album, time;
    private long duration;

    public Song(String name, String fileName, String path, String artist, String album, long duration) {
        this.name = name;
        this.fileName = fileName;
        this.path = path;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        convertDate();
    }

    public Song() {

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public void setTime(String time) {
        this.time = time;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public String getFileName() {
        return fileName;
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

    public String getTime() {
        return time;
    }

    public long getDuration() {
        return duration;
    }

    private void convertDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
        time = dateFormat.format(new Date(duration));
    }
}
