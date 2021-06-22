package musicplayer.group3.dev.musicplayer.common;

import android.content.Intent;

/**
 * Created by sev_user on 12/15/2016.
 */

public class Const {


    public static final int DEFAULT_VALUE_INT_0 = 0;
    public static final int DEFAULT_VALUE_INT_1 = 1;
    public static final int DEFAULT_VALUE_INT_2 = 2;
    //key of media player
    public static final int MEDIA_IDLE = 101;
    public static final int MEDIA_PLAYING = 102;
    public static final int MEDIA_PAUSE = 103;
    public static final int MEDIA_STOP = 104;
    public static final int MEDIA_STATE_LOOP_ALL = 105;
    public static final int MEDIA_STATE_LOOP_ONE = 106;
    public static final int MEDIA_STATE_NO_LOOP = 107;
    public static final boolean MEDIA_SHUFFLE_TRUE=true;
    public static final boolean MEDIA_SHUFFLE_FLASE=false;
    //Key
    public static final String KEY_ALBUM_ID = "KEY_ALBUM_ID";
    public static final String KEY_ALBUM_NAME = "KEY_ALBUM_NAME";
    public static final String KEY_ALBUM_ARTIST = "KEY_ALBUM_ARTIST";
    public static final String KEY_ID_ARTIST = "KEY_ID_ARTIST";
    public static final String KEY_NAME_ARTIST = "KEY_NAME_ARTIST";
    public static final String BLANK = "";
    public static final String KEY_SCORE = "KEY_SCORE";
    public static final String KEY_STATUS = "KEY_STATUS";
    public static final String FILE_SAVE_HIGHT_SCORE = "data_hight_score";
    public static final String KEY_HIGHT_SCORE = "KEY_HIGHT_SCORE";
    public static final String KEY_ACTION_SEARCH_SONG_NAME = "KEY_ACTION_SEARCH_SONG_NAME";
//    public static final String KEY_ACTION_SEARCH_ARTIST_NAME = "KEY_ACTION_SEARCH_ARTIST_NAME";

    public static final int KEY_OVER = 1;

    //SharePreferencesController
    public static final String KEY_IS_SHOW_BOTOM_MENU = "KEY_IS_SHOW_BOTOM_MENU";

    //Request code
    public static final int REQUEST_CODE_ACTION_SEARCH_MAIN = 22;
    public static final int REQUEST_CODE_ACTION_SEARCH_DETAIL_ALBUM = 11;
    public static final int REQUEST_CODE_ACTION_SEARCH_DETAIL_ARTIST = 33;
    public static final String ACTION_SEND_DATA = "ACTION_SEND_DATA";
    public static final String KEY_TITLE_SONG = "KEY_TITLE_SONG";

    public static final String ACTION_PREVIOUS = "ACTION_PREVIOUS";
    public static final String ACTION_NEXT = "ACTION_NEXT";
    public static final String ACTION_PAUSE_SONG = "ACTION_PAUSE_SONG";
    public static final String ACTION_STOP = "ACTION_STOP";
    public static final String KEY_SEND_PAUSE = "KEY_SEND_PAUSE";
    public static final String ACTION_STOP_ALL = "ACTION_STOP_ALL";
    public static final String ACTION_START_FOREGROUND = "ACTION_START_FOREGROUND";
    public static final int REQUEST_CODE_NOTIFICATION = 999;
    public static final int NOTIFICATION_ID = 1234;
    public static final String MEDIA_SHUFFLE = "MEDIA_SHUFFLE";
    public static final String MEDIA_CURRENT_STATE_LOOP = "MEDIA_CURRENT_STATE_LOOP";
}
