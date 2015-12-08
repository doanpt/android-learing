package com.framgia.adapter;

import com.google.api.services.youtube.model.Playlist;

/**
 * Created by doantrung on 12/8/15.
 */
public class ItemPlayListPage {
    private String title;
    private String publishedAt;
    private String thumbnailURL;
    private String id;
    private String channelTitle;
    private String numberVideo;

    public String getNumberVideo() {
        return numberVideo;
    }

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

    public ItemPlayListPage(Playlist result) {
        this.title = result.getSnippet().getTitle();
        this.channelTitle = result.getSnippet().getChannelTitle();
        this.publishedAt = result.getSnippet().getPublishedAt().toString().substring(0, 10);
        this.thumbnailURL = result.getSnippet().getThumbnails().getHigh().getUrl();
        this.id = result.getId();
        this.numberVideo = result.getContentDetails().getItemCount().toString();
    }
}