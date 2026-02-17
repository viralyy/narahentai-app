package com.narahentai.app;

public class VideoItem {
    public final String title;
    public final String thumbnailUrl;
    public final String videoUrl;
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
        // lu bisa bikin lebih detail nanti (mm:ss)
        return durationMinutes + " menit";
    }
}
