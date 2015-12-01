package com.framgia.adapter;

import com.google.api.services.youtube.model.SearchResult;

/**
 * Created by doantrung on 11/19/15.
 */
public class VideoItem {
    private String title;
    private String publishedAt;
    private String thumbnailURL;
    private String id;
    private String channelTitle;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getChannelTitle() {
        return channelTitle;
    }


    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public VideoItem(SearchResult result) {
        this.title = result.getSnippet().getTitle();
        this.channelTitle = result.getSnippet().getChannelTitle();
        this.publishedAt = result.getSnippet().getPublishedAt().toString().substring(0, 10);
        this.thumbnailURL = result.getSnippet().getThumbnails().getDefault().getUrl();
        this.id = result.getId().getVideoId();
    }
}
