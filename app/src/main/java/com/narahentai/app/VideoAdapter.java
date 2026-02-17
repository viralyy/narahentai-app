package com.narahentai.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VH> {

    private final List<VideoItem> items;
    private final ExecutorService imgPool = Executors.newFixedThreadPool(4);

    public VideoAdapter(List<VideoItem> items) {
        this.items = items;
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video_youtube, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        VideoItem it = items.get(position);

        h.title.setText(it.title);
        h.meta.setText(it.views + " views • " + it.durationText());
        h.badge.setText(it.durationMinutes + ":00"); // simple

        // load image sederhana (tanpa library)
        h.thumb.setImageDrawable(null);
        if (it.thumbnailUrl != null && !it.thumbnailUrl.isEmpty()) {
            imgPool.submit(() -> {
                try {
                    InputStream is = new URL(it.thumbnailUrl).openStream();
                    Bitmap bmp = BitmapFactory.decodeStream(is);
                    is.close();
                    h.thumb.post(() -> h.thumb.setImageBitmap(bmp));
                } catch (Exception ignored) {}
            });
        }

        h.itemView.setOnClickListener(v -> {
            Context c = v.getContext();
            Intent i = new Intent(c, PlayerActivity.class);
            i.putExtra("title", it.title);
            i.putExtra("url", it.videoUrl);
            c.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView thumb;
        TextView badge, title, meta;

        VH(@NonNull View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.thumb);
            badge = itemView.findViewById(R.id.badgeDuration);
            title = itemView.findViewById(R.id.title);
            meta = itemView.findViewById(R.id.meta);
        }
    }
}
