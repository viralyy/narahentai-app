package com.narahentai.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

        String dur = it.durationText(); // ✅ method
        h.txtTitle.setText(it.title);
        h.txtMeta.setText(it.views + " views • " + dur);
        h.txtDuration.setText(dur);

        Glide.with(h.itemView.getContext())
                .load(it.thumbnailUrl)
                .centerCrop()
                .into(h.imgThumb);

        h.itemView.setOnClickListener(v -> {
            // ✅ pastiin URL playable ada
            String url = it.playableUrl();
            if (url == null || url.trim().isEmpty()) {
                Toast.makeText(v.getContext(), "Video URL kosong (cek API: video_url / video_id)", Toast.LENGTH_SHORT).show();
                return;
            }

            if (onVideoClick != null) {
                onVideoClick.onClick(it);
            } else {
                // fallback: langsung buka player kalau lu belum set callback di fragment
                PlayerActivity.open(
                        v.getContext(),
                        url,
                        it.title,
                        it.views + " views • " + dur
                );
            }
        });

        h.btnMore.setOnClickListener(v -> {
            // nanti bisa popup menu
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
        ImageButton btnMore;

        VH(@NonNull View itemView) {
            super(itemView);
            imgThumb = itemView.findViewById(R.id.imgThumb);
            txtDuration = itemView.findViewById(R.id.txtDuration);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtMeta = itemView.findViewById(R.id.txtMeta);
            btnMore = itemView.findViewById(R.id.btnMore);
        }
    }
}
