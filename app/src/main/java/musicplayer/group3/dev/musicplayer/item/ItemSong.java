package musicplayer.group3.dev.musicplayer.item;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.Serializable;

public class ItemSong implements Serializable {
    private String dataPath, title, displayName, album, albumID, artist;
    private int duration;
    private boolean isSelected;

    public ItemSong(String dataPath, String title, String displayName, String album, String albumID, String artist, int duration, boolean isSelected) {
        this.dataPath = dataPath;
        this.title = title;
        this.displayName = displayName;
        this.album = album;
        this.albumID = albumID;
        this.artist = artist;
        this.duration = duration;
        this.isSelected = isSelected;
    }

    public String getDataPath() {
        return dataPath;
    }

    public String getTitle() {
        return title;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public int getDuration() {
        return duration;
    }

    public Bitmap getImageSong(Context context) {
        Uri sArtworkUri = Uri
                .parse("content://media/external/audio/albumart");
        Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, Long.parseLong(albumID));
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(
                    context.getContentResolver(), albumArtUri);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getAlbumID() {
        return albumID;
    }
}
