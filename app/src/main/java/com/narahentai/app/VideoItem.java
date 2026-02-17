package com.narahentai.app;

public class VideoItem {
    public final String title;
    public final String thumbnailUrl;

    // dari API
    public final String videoUrl; // direct mp4 / m3u8
    public final int durationMinutes;
    public final int views;

    public VideoItem(String title, String thumbnailUrl, String videoUrl, int durationMinutes, int views) {
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.videoUrl = videoUrl;
        this.durationMinutes = durationMinutes;
        this.views = views;
    }

    public String durationText() {
        return durationMinutes + " menit";
    }

    public String playableUrl() {
        return (videoUrl == null) ? "" : videoUrl.trim();
    }
}
