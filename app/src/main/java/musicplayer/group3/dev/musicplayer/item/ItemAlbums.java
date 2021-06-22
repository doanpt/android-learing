package musicplayer.group3.dev.musicplayer.item;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by sev_user on 12/16/2016.
 */

public class ItemAlbums {

    private int albumID;
    private String albumName;
    private String albumArtist;
    private String albumArt;
    private int numOfSong;

    public ItemAlbums(int albumID, String albumName, String albumArtist, String albumArt, int numOfSong) {
        this.albumID = albumID;
        this.albumName = albumName;
        this.albumArtist = albumArtist;
        this.albumArt = albumArt;
        this.numOfSong = numOfSong;
    }

    public Bitmap getImageSong(Context context) {
        Uri sArtworkUri = Uri
                .parse("content://media/external/audio/albumart");
        Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, Long.parseLong(albumID + ""));

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(
                    context.getContentResolver(), albumArtUri);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public int getAlbumID() {
        return albumID;
    }

    public void setAlbumID(int albumID) {
        this.albumID = albumID;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumArtist() {
        return albumArtist;
    }

    public void setAlbumArtist(String albumArtist) {
        this.albumArtist = albumArtist;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }

    public int getNumOfSong() {
        return numOfSong;
    }

    public void setNumOfSong(int numOfSong) {
        this.numOfSong = numOfSong;
    }
}
