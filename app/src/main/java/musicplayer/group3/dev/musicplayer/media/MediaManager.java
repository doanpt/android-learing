package musicplayer.group3.dev.musicplayer.media;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import musicplayer.group3.dev.musicplayer.common.Const;
import musicplayer.group3.dev.musicplayer.common.SharePreferencesController;
import musicplayer.group3.dev.musicplayer.item.ItemAlbums;
import musicplayer.group3.dev.musicplayer.item.ItemArtist;
import musicplayer.group3.dev.musicplayer.item.ItemSong;


public class MediaManager {
    private static final int PERMISION_ALL = 265;
    private static final String TAG = "MediaManager";
    private static MediaManager mediaManager;
    private Context context;
    //media player object
    private MediaPlayer mPlayer;
    private ArrayList<ItemSong> arrItemSongPlay = new ArrayList<>();
    //this is current song play.
    private int currentIndex = 0;
    //this is state of mediaplayer
    private int mediaState = Const.MEDIA_IDLE;
    private int loop = Const.MEDIA_STATE_LOOP_ALL;
    private Random random = new Random();
    private boolean shuffle = Const.MEDIA_SHUFFLE_TRUE;

    private MediaManager(Context context) {
        this.context = context;
        initPlayer();
    }

    public static MediaManager getInstance(Context context) {
        if (mediaManager == null) {
            mediaManager = new MediaManager(context);
        }
        return mediaManager;
    }

    private void initPlayer() {
        mPlayer = new MediaPlayer();
        loop = SharePreferencesController.getInstance(context).getInt(Const.MEDIA_CURRENT_STATE_LOOP, Const.MEDIA_STATE_LOOP_ALL);
        shuffle = SharePreferencesController.getInstance(context).getBoolean(Const.MEDIA_SHUFFLE, Const.MEDIA_SHUFFLE_TRUE);
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                next();
            }
        });
    }

    public void play(boolean isPlayAgain) {
        if (mediaState == Const.MEDIA_IDLE || mediaState == Const.MEDIA_STOP || isPlayAgain) {
            try {
                mPlayer.reset();
                ItemSong song = arrItemSongPlay.get(currentIndex);
                mPlayer.setDataSource(song.getDataPath());
                mPlayer.prepare();
                mPlayer.start();
                Intent intent = new Intent(Const.ACTION_SEND_DATA);
                intent.putExtra(Const.KEY_TITLE_SONG, song.getDisplayName());
                intent.putExtra(Const.KEY_NAME_ARTIST, song.getArtist());
                context.sendBroadcast(intent);
                mediaState = Const.MEDIA_PLAYING;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (mediaState == Const.MEDIA_PAUSE) {
            mPlayer.start();
            mediaState = Const.MEDIA_PLAYING;
        } else if (mediaState == Const.MEDIA_PLAYING) {
            mPlayer.pause();
            mediaState = Const.MEDIA_PAUSE;
        }
    }

    public void next() {
        SharePreferencesController sharePreference = SharePreferencesController.getInstance(context);
        loop=sharePreference.getInt(Const.MEDIA_CURRENT_STATE_LOOP,Const.MEDIA_STATE_NO_LOOP);
        shuffle=sharePreference.getBoolean(Const.MEDIA_SHUFFLE,Const.MEDIA_SHUFFLE_TRUE);
        if (loop != Const.MEDIA_STATE_LOOP_ONE) {
            if (shuffle) {
                currentIndex = random.nextInt(arrItemSongPlay.size());
            } else {
                if (currentIndex > arrItemSongPlay.size() - 2) {
                    currentIndex = 0;
                } else {
                    currentIndex++;
                }
            }
        }
        play(true);
    }

    public void previous() {
        SharePreferencesController sharePreference = SharePreferencesController.getInstance(context);
        loop=sharePreference.getInt(Const.MEDIA_CURRENT_STATE_LOOP,Const.MEDIA_STATE_NO_LOOP);
        shuffle=sharePreference.getBoolean(Const.MEDIA_SHUFFLE,Const.MEDIA_SHUFFLE_TRUE);        if (loop != Const.MEDIA_STATE_LOOP_ONE) {
            if (shuffle) {
                currentIndex = random.nextInt(arrItemSongPlay.size());
            } else {
                if (currentIndex <= 0) {
                    currentIndex = arrItemSongPlay.size() - 1;
                } else {
                    currentIndex--;
                }
            }
        }
        play(true);
    }

    public void stop() {
        if (mediaState == Const.MEDIA_IDLE) {
            return;
        }
        mPlayer.stop();
        mediaState = Const.MEDIA_STOP;
    }
    public ArrayList<ItemSong> getArrItemSong() {
        return arrItemSongPlay;
    }

    public void setArrItemSong(ArrayList<ItemSong> arrItemSong) {
        this.arrItemSongPlay = arrItemSong;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public boolean isShuffle() {
        return shuffle;
    }

    public int getLoop() {
        return loop;
    }

    public MediaPlayer getmPlayer() {
        return mPlayer;
    }

    public ArrayList<ItemSong> getSongList(String filed, String value[]) {
        ArrayList<ItemSong> arrItemSong = new ArrayList<>();
        String columsName[] = {MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.ARTIST
        };
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            String[] PERMISSION = {Manifest.permission.READ_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions((Activity) context, PERMISSION, PERMISION_ALL);
        } else {
            Cursor cursor = context.getContentResolver()
                    .query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                            columsName, filed, value, null, null);
            if (cursor == null) {
                return null;
            }
            int indexData = cursor.getColumnIndex(columsName[0]);
            int indexTitle = cursor.getColumnIndex(columsName[1]);
            int indexDisPlay = cursor.getColumnIndex(columsName[2]);
            int indexDuration = cursor.getColumnIndex(columsName[3]);
            int indexAlbum = cursor.getColumnIndex(columsName[4]);
            int indexAlbumId = cursor.getColumnIndex(columsName[5]);
            int indexArtist = cursor.getColumnIndex(columsName[6]);
            String data, title, display, album, albumID, artist;
            int duration;
            cursor.moveToFirst();
            arrItemSong.clear();
            while (!cursor.isAfterLast()) {
                data = cursor.getString(indexData);
                title = cursor.getString(indexTitle);
                display = cursor.getString(indexDisPlay);
                album = cursor.getString(indexAlbum);
                albumID = cursor.getString(indexAlbumId);
                artist = cursor.getString(indexArtist);
                duration = cursor.getInt(indexDuration);
                String extension = display.substring(display.lastIndexOf("."));
                if (extension.equalsIgnoreCase(".mp3")) {
                    arrItemSong.add(new ItemSong(data, title, display, album, albumID, artist, duration, false));
                }
                Log.i(TAG, "extension:= " + extension);
                cursor.moveToNext();
            }
            cursor.close();

        }
        return arrItemSong;
    }

    public ArrayList<ItemAlbums> getAllAlbums(String filed, String value[]) {
        ArrayList<ItemAlbums> arrItemAlbums = new ArrayList<>();
        String columsName[] = {
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.ALBUM_ART,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS
        };
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            String[] PERMISSION = {Manifest.permission.READ_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions((Activity) context, PERMISSION, PERMISION_ALL);
        } else {
            Cursor cursor = context.getContentResolver()
                    .query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                            columsName, filed, value, null);

            if (cursor == null) {
                return null;
            }
            int indexAlbumID = cursor.getColumnIndex(columsName[0]);
            int indexAlbum = cursor.getColumnIndex(columsName[1]);
            int indexAlbumArtist = cursor.getColumnIndex(columsName[2]);
            int indexAlbumArt = cursor.getColumnIndex(columsName[3]);
            int indexNumofSongs = cursor.getColumnIndex(columsName[4]);
            String albumName, albumArtist, albumArt;
            int numofSongs, albumID;
            cursor.moveToFirst();
            arrItemAlbums.clear();
            while (!cursor.isAfterLast()) {
                albumID = cursor.getInt(indexAlbumID);
                albumName = cursor.getString(indexAlbum);
                albumArtist = cursor.getString(indexAlbumArtist);
                albumArt = cursor.getString(indexAlbumArt);
                numofSongs = cursor.getInt(indexNumofSongs);
                arrItemAlbums.add(new ItemAlbums(albumID, albumName,
                        albumArtist, albumArt, numofSongs));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return arrItemAlbums;
    }

    public ArrayList<ItemArtist> getAllArtist() {
        ArrayList<ItemArtist> arrItemArtist = new ArrayList<>();
        String columsName[] = {
                MediaStore.Audio.Artists._ID,
                MediaStore.Audio.Artists.ARTIST,
                MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
                MediaStore.Audio.Artists.NUMBER_OF_TRACKS
        };
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            String[] PERMISSION = {Manifest.permission.READ_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions((Activity) context, PERMISSION, PERMISION_ALL);
        } else {
            Cursor cursor = context.getContentResolver()
                    .query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                            columsName, null, null, null);

            if (cursor == null) {
                return null;
            }
            int indexArtistID = cursor.getColumnIndex(columsName[0]);
            int indexArtist = cursor.getColumnIndex(columsName[1]);
            int indexNumOfAlbums = cursor.getColumnIndex(columsName[2]);
            int indexNumOfTracks = cursor.getColumnIndex(columsName[3]);
            String artistName;
            int idArtist, numOfAlbums, numofTracks;
            cursor.moveToFirst();
            arrItemArtist.clear();
            while (!cursor.isAfterLast()) {
                idArtist = cursor.getInt(indexArtistID);
                artistName = cursor.getString(indexArtist);
                numOfAlbums = cursor.getInt(indexNumOfAlbums);
                numofTracks = cursor.getInt(indexNumOfTracks);
                arrItemArtist.add(new ItemArtist(idArtist, artistName, numOfAlbums, numofTracks));
                cursor.moveToNext();
            }
            cursor.close();

        }
        return arrItemArtist;
    }
}
