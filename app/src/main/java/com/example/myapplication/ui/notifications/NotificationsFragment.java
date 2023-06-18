package com.example.myapplication.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentNotificationsBinding;
import com.example.myapplication.ui.feed.FeedAdapter;
import com.example.myapplication.ui.feed.FeedItem;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private RecyclerView recyclerViewFeed;
    private LinearLayout emptyStateLayout;
    private FeedAdapter feedAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerViewFeed = root.findViewById(R.id.recycler_view_feed);
        emptyStateLayout = root.findViewById(R.id.empty_state_layout);

        // Initialize the RecyclerView and adapter
        recyclerViewFeed.setLayoutManager(new LinearLayoutManager(getContext()));
        feedAdapter = new FeedAdapter();
        recyclerViewFeed.setAdapter(feedAdapter);

        // Load the feed items from a data source (e.g., API, database)
        List<FeedItem> feedItems = loadFeedItems();

        // Update the UI based on the feed items
        if (feedItems.isEmpty()) {
            showEmptyState();
        } else {
            showFeedItems(feedItems);
        }

        return root;
    }

    private List<FeedItem> loadFeedItems() {
        // Implement your logic to load the feed items from a data source
        // For demonstration purposes, let's assume we have some dummy data
        List<FeedItem> feedItems = new ArrayList<>();

        feedItems.add(new FeedItem(""));



        return feedItems;
    }

    private void showEmptyState() {
        emptyStateLayout.setVisibility(View.VISIBLE);
        recyclerViewFeed.setVisibility(View.GONE);
    }

    private void showFeedItems(List<FeedItem> feedItems) {
        emptyStateLayout.setVisibility(View.GONE);
        recyclerViewFeed.setVisibility(View.VISIBLE);

        // Update the feed items in the adapter
        feedAdapter.setFeedItems(feedItems);
        feedAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
