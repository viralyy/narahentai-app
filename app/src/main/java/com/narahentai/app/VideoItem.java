package com.narahentai.app;

public class VideoItem {
    public final String title;
    public final String thumbnailUrl;
    public final String videoUrl;
    public final String durationText;
    public final int views;

    public VideoItem(String title, String thumbnailUrl, String videoUrl, String durationText, int views) {
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.videoUrl = videoUrl;
        this.durationText = durationText;
        this.views = views;
    }
}
