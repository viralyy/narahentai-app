package com.narahentai.app;

public class VideoItem {
    public final String title;
    public final String thumbnailUrl;

    // optional fields
    public final String videoUrl;   // direct mp4 kalau API nyediain
    public final String videoId;    // kalau lu cuma simpan id videy
    public final String slug;

    public final int durationMinutes;
    public final int views;

    public VideoItem(
            String title,
            String thumbnailUrl,
            String videoUrl,
            String videoId,
            String slug,
            int durationMinutes,
            int views
    ) {
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.videoUrl = videoUrl;
        this.videoId = videoId;
        this.slug = slug;
        this.durationMinutes = durationMinutes;
        this.views = views;
    }

    public String durationText() {
        return durationMinutes + " menit";
    }

    /** URL yang dipakai player */
    public String playableUrl() {
        // 1) paling prioritas: video_url dari API
        if (videoUrl != null && !videoUrl.trim().isEmpty()) return videoUrl.trim();

        // 2) kalau API cuma nyimpen video_id (videy id)
        if (videoId != null && !videoId.trim().isEmpty()) {
            return "https://cdn.videy.co/v/=?id=" + videoId.trim() + ".mp4";
        }

        // 3) fallback terakhir (kalau slug lu emang id videy)
        if (slug != null && !slug.trim().isEmpty()) {
            return "https://cdn.videy.co/v/=?id=" + slug.trim() + ".mp4";
        }

        return "";
    }

    public boolean hasPlayableUrl() {
        return playableUrl() != null && !playableUrl().isEmpty();
    }
}
