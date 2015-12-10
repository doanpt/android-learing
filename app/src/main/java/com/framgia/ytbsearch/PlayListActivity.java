package com.framgia.ytbsearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.Toast;

import com.framgia.adapter.ItemPlayListPage;
import com.framgia.adapter.ListPlayListPageAdapter;

import java.util.ArrayList;

/**
 * Created by doantrung on 12/8/15.
 */
public class PlayListActivity extends Activity {
    private YoutubeConnector youtubeConnector;
    private ListPlayListPageAdapter listPlayListAdapter;
    private ViewPager lvPlayList;
    private ArrayList<ItemPlayListPage> listPlayListResults;
    private Handler handler;
    private static String channelID = "UCBkNpeyvBO2TdPGVC_PsPUA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        initView();
    }

    private void initView() {
        handler = new Handler();
        lvPlayList = (ViewPager) findViewById(R.id.vp_playlist);
        getPlayList();
    }

    public void getPlayList() {
        new Thread() {
            public void run() {
                youtubeConnector = new YoutubeConnector(PlayListActivity.this);
                listPlayListResults = youtubeConnector.getAllPlayList(channelID);
                handler.post(new Runnable() {
                    public void run() {
                        updateVideosFound();
                    }
                });
            }
        }.start();
    }

    private void updateVideosFound() {
        if (listPlayListResults.size() < 0) {
            Toast.makeText(this, "No playlist to display!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            listPlayListAdapter = new ListPlayListPageAdapter(PlayListActivity.this, listPlayListResults);
            lvPlayList.setAdapter(listPlayListAdapter);
        }
    }
}