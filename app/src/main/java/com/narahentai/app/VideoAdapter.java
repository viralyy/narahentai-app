package com.narahentai.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
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

    private final List<VideoItem> data;
    private final ExecutorService imgPool = Executors.newFixedThreadPool(4);
    private final Handler main = new Handler(Looper.getMainLooper());

    public VideoAdapter(List<VideoItem> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        VideoItem item = data.get(position);

        h.title.setText(item.title != null ? item.title : "");
        String meta = (item.views + " views • " + item.duration_minutes + " menit");
        h.meta.setText(meta);

        // load thumbnail simple
        h.thumb.setImageDrawable(null);
        if (item.thumbnail_url != null && item.thumbnail_url.startsWith("http")) {
            imgPool.submit(() -> {
                try {
                    InputStream in = new URL(item.thumbnail_url).openStream();
                    Bitmap bmp = BitmapFactory.decodeStream(in);
                    main.post(() -> h.thumb.setImageBitmap(bmp));
                } catch (Exception ignored) {}
            });
        }

        h.itemView.setOnClickListener(v -> {
            Context c = v.getContext();
            Intent it = new Intent(c, PlayerActivity.class);
            it.putExtra(PlayerActivity.EXTRA_URL, item.video_url);
            it.putExtra(PlayerActivity.EXTRA_TITLE, item.title);
            c.startActivity(it);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView thumb;
        TextView title;
        TextView meta;

        VH(@NonNull View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.thumb);
            title = itemView.findViewById(R.id.title);
            meta = itemView.findViewById(R.id.meta);
        }
    }
}
