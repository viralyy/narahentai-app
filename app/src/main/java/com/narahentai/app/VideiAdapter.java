package com.narahentai.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VH> {

    public interface OnVideoClick {
        void onClick(VideoItem item);
    }

    private final List<VideoItem> items;
    private OnVideoClick onVideoClick;

    public VideoAdapter(List<VideoItem> items) {
        this.items = items;
    }

    public void setOnVideoClick(OnVideoClick cb) {
        this.onVideoClick = cb;
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
        VideoItem it = items.get(position);

        h.txtTitle.setText(it.title);
        h.txtMeta.setText(it.views + " views");
        h.txtDuration.setText(it.durationText());

        Glide.with(h.itemView.getContext())
                .load(it.thumbnailUrl)
                .centerCrop()
                .into(h.imgThumb);

        h.itemView.setOnClickListener(v -> {
            if (onVideoClick != null) onVideoClick.onClick(it);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView imgThumb;
        TextView txtDuration;
        TextView txtTitle;
        TextView txtMeta;

        VH(@NonNull View itemView) {
            super(itemView);
            imgThumb = itemView.findViewById(R.id.imgThumb);
            txtDuration = itemView.findViewById(R.id.txtDuration);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtMeta = itemView.findViewById(R.id.txtMeta);
        }
    }
}
