package com.narahentai.app;

public class VideoItem {
    public final String title;
    public final String slug;
    public final String thumbnailUrl;
    public final int durationMinutes;
    public final int views;
    public final String publishedAt;

    public VideoItem(String title, String slug, String thumbnailUrl, int durationMinutes, int views, String publishedAt) {
        this.title = title;
        this.slug = slug;
        this.thumbnailUrl = thumbnailUrl;
        this.durationMinutes = durationMinutes;
        this.views = views;
        this.publishedAt = publishedAt;
    }

    public String durationText() {
        return durationMinutes + " menit";
    }
}
