package com.framgia.ytbsearch;

import android.content.Context;
import android.util.Log;

import com.framgia.adapter.VideoItem;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by doantrung on 11/19/15.
 */
public class YoutubeConnector {
    private YouTube youtube;
    private YouTube.Search.List query;

    public YoutubeConnector(Context context) {
        youtube = new YouTube.Builder(new NetHttpTransport(),
                new JacksonFactory(), new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest hr) throws IOException {
            }
        }).setApplicationName(context.getString(R.string.app_name)).build();

        try {
            query = youtube.search().list("id,snippet");
            query.setKey(MainActivity.KEY_DEVELOP);
            query.setType("video");
            query.setFields("items(id/videoId,snippet/title,snippet/channelTitle,snippet/publishedAt,snippet/thumbnails/default/url)");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<VideoItem> search(String keywords) {
        query.setQ(keywords);
        try {
            SearchListResponse response = query.execute();
            List<SearchResult> results = response.getItems();
            ArrayList<VideoItem> items = new ArrayList<VideoItem>();
            for (SearchResult result : results) {
                VideoItem item = new VideoItem(result);
                items.add(item);
            }
            return items;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}