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

    private final List<VideoItem> items;

    public VideoAdapter(List<VideoItem> items) {
        this.items = items;
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

        String dur = it.durationText();
        h.txtTitle.setText(it.title);
        h.txtMeta.setText(it.views + " views • " + dur);
        h.txtDuration.setText(dur);

        Glide.with(h.itemView.getContext())
                .load(it.thumbnailUrl)
                .centerCrop()
                .into(h.imgThumb);

        h.itemView.setOnClickListener(v -> {
            String url = it.playableUrl();
            if (url.isEmpty()) {
                Toast.makeText(v.getContext(), "Video URL kosong. Cek API video_url.", Toast.LENGTH_SHORT).show();
                return;
            }
            PlayerActivity.open(v.getContext(), url, it.title, it.views + " views • " + dur);
        });

        h.btnMore.setOnClickListener(v -> {
            // nanti popup menu
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
