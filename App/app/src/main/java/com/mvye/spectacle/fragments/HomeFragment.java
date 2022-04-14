package com.mvye.spectacle.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mvye.spectacle.R;
import com.mvye.spectacle.adapters.ShowAdapter;
import com.mvye.spectacle.models.Show;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    Toolbar toolbar;
    SearchView searchView;
    RecyclerView recyclerViewFollowingShows;
    RecyclerView recyclerViewRecommendedShows;
    List<Show> followingShows;
    List<Show> recommendedShows;
    ShowAdapter followingShowsAdapter;
    ShowAdapter recommendedShowsAdapter;

    public HomeFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerViews(view);
        setupToolbar(view);
        queryFollowingShows();
        queryRecommendedShows();
    }

    private void setupRecyclerViews(View view) {
        recyclerViewFollowingShows = view.findViewById(R.id.recyclerViewFollowingShows);
        recyclerViewRecommendedShows = view.findViewById(R.id.recyclerViewRecommendedShows);
        followingShows = new ArrayList<>();
        recommendedShows = new ArrayList<>();
        ShowAdapter.OnPosterImageClickListener onPosterImageClickListener = new ShowAdapter.OnPosterImageClickListener() {
            @Override
            public void OnPosterImageClickListener(int postion) {
                // TODO: Open ShowDetailsFragment using the show at position
            }
        };
        followingShowsAdapter = new ShowAdapter(getContext(), followingShows, onPosterImageClickListener);
        recommendedShowsAdapter = new ShowAdapter(getContext(), recommendedShows, onPosterImageClickListener);
        recyclerViewFollowingShows.setAdapter(followingShowsAdapter);
        recyclerViewRecommendedShows.setAdapter(recommendedShowsAdapter);
        recyclerViewFollowingShows.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        recyclerViewRecommendedShows.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
    }

    private void setupToolbar(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        searchView = (SearchView) toolbar.getMenu().findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO: Do something searchy
                return false;
            }
        });
    }

    private void queryFollowingShows() {
        ParseQuery<ParseObject> query = ParseUser.getCurrentUser().getRelation("following").getQuery();
        query.include("Show");
        List<Show> shows = new ArrayList<>();
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> showList, ParseException e) {
                for (ParseObject show : showList) {
                    shows.add((Show) show);
                }
                Log.i("HomeFragment", "Size is: " + shows.size());
                followingShowsAdapter.clear();
                followingShowsAdapter.addAll(shows);
            }
        });
    }

    private void queryRecommendedShows() {
        ParseQuery<Show> query = ParseQuery.getQuery(Show.class);
        query.findInBackground(new FindCallback<Show>() {
            @Override
            public void done(List<Show> showList, ParseException e) {
                recommendedShowsAdapter.clear();
                recommendedShowsAdapter.addAll(showList);
            }
        });
    }
}