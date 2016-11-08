package com.dvt.samsung.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.IBinder;
import android.provider.MediaStore;

import com.dvt.samsung.model.Song;
import com.dvt.samsung.service.MyMusicService;

import java.util.ArrayList;

/**
 * Created by Android on 11/4/2016.
 */

public class CommonMethod {
    private static CommonMethod commonMethod;

    public static CommonMethod getInstance() {
        if (commonMethod == null) {
            commonMethod = new CommonMethod();
        }
        return commonMethod;
    }

}

