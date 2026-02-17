package com.narahentai.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    // 🔥 ganti sesuai domain lu
    private static final String API_URL = "https://narahentai.pages.dev/api/posts";

    private RecyclerView recycler;
    private VideoAdapter adapter;
    private final ArrayList<VideoItem> items = new ArrayList<>();

    private MaterialToolbar toolbar;
    private ImageButton btnSearch;
    private ImageButton btnRefresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        toolbar = view.findViewById(R.id.topToolbar);
        btnSearch = view.findViewById(R.id.btnSearch);
        btnRefresh = view.findViewById(R.id.btnRefresh);

        recycler = view.findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new VideoAdapter(items, requireContext(), item -> {
            // klik = fullscreen player (bukan mini)
            PlayerActivity.open(requireContext(), item.videoUrl, item.title, item.views, item.durationText);
        });
        recycler.setAdapter(adapter);

        btnSearch.setOnClickListener(v ->
                Toast.makeText(requireContext(), "Search belum dibuat", Toast.LENGTH_SHORT).show()
        );

        btnRefresh.setOnClickListener(v -> loadPosts());

        loadPosts();
    }

    private void loadPosts() {
        new Thread(() -> {
            try {
                URL url = new URL(API_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(15000);

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) sb.append(line);
                br.close();

                JSONObject root = new JSONObject(sb.toString());
                JSONArray arr = root.getJSONArray("items");

                items.clear();

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.getJSONObject(i);

                    String title = o.optString("title");
                    String thumb = o.optString("thumbnail_url");
                    String slug = o.optString("slug");
                    int durationMin = o.optInt("duration_minutes");
                    int views = o.optInt("views");

                    // ✅ video URL dari slug (sesuai pola lu)
                    String videoUrl = "https://cdn.videy.co/v/?id=" + slug + ".mp4";

                    items.add(new VideoItem(title, thumb, videoUrl, durationMin + " menit", views));
                }

                requireActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());

            } catch (Exception e) {
                e.printStackTrace();
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(requireContext(), "Gagal load /api/posts", Toast.LENGTH_SHORT).show()
                    );
                }
            }
        }).start();
    }
}
