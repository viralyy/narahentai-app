package com.narahentai.app;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {

    private static final String API = "https://narahentai.pages.dev/api/posts";

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView rv = view.findViewById(R.id.recycler);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

        // padding bawah auto ngikut gesture/nav bar
        ViewCompat.setOnApplyWindowInsetsListener(rv, (v, insets) -> {
            Insets sys = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(
                    v.getPaddingLeft(),
                    v.getPaddingTop(),
                    v.getPaddingRight(),
                    sys.bottom + dp(8) // aman + rapi
            );
            return insets;
        });

        List<VideoItem> list = new ArrayList<>();
        VideoAdapter adapter = new VideoAdapter(list);
        rv.setAdapter(adapter);

        ExecutorService pool = Executors.newSingleThreadExecutor();
        pool.submit(() -> {
            try {
                List<VideoItem> items = ApiClient.fetchPosts(API);
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    list.clear();
                    list.addAll(items);
                    adapter.notifyDataSetChanged();
                });
            } catch (Exception ignored) {}
        });
    }

    private int dp(int v) {
        float d = requireContext().getResources().getDisplayMetrics().density;
        return (int) (v * d);
    }
}
