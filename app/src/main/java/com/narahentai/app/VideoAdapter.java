package com.narahentai.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VH> {

    public interface OnClick {
        void onClick(VideoItem item);
    }

    private final ArrayList<VideoItem> items;
    private final Context ctx;
    private final OnClick onClick;

    public VideoAdapter(ArrayList<VideoItem> items, Context ctx, OnClick onClick) {
        this.items = items;
        this.ctx = ctx;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video_youtube, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        VideoItem item = items.get(position);

        h.title.setText(item.title);
        h.meta.setText(formatViews(item.views) + " • " + item.durationText);

        Glide.with(ctx)
                .load(item.thumbnailUrl)
                .centerCrop()
                .into(h.thumb);

        h.itemView.setOnClickListener(v -> onClick.onClick(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView thumb;
        TextView title, meta;

        VH(@NonNull View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.thumb);
            title = itemView.findViewById(R.id.title);
            meta = itemView.findViewById(R.id.meta);
        }
    }

    private String formatViews(int v) {
        if (v >= 1_000_000) return String.format("%.1fM views", v / 1_000_000f);
        if (v >= 1_000) return String.format("%.1fK views", v / 1_000f);
        return v + " views";
    }
}
