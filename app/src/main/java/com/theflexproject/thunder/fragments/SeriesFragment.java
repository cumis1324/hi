package com.theflexproject.thunder.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.theflexproject.thunder.R;
import com.theflexproject.thunder.adapter.BannerRecyclerAdapter;
import com.theflexproject.thunder.adapter.DrakorBannerAdapter;
import com.theflexproject.thunder.adapter.MediaAdapter;
import com.theflexproject.thunder.adapter.ScaleCenterItemLayoutManager;
import com.theflexproject.thunder.database.DatabaseClient;
import com.theflexproject.thunder.model.Movie;
import com.theflexproject.thunder.model.MyMedia;
import com.theflexproject.thunder.model.TVShowInfo.TVShow;

import java.util.List;

public class SeriesFragment extends BaseFragment{
    BannerRecyclerAdapter recentlyAddedRecyclerAdapter;
    MediaAdapter recentlyReleasedRecyclerViewAdapter;
    BannerRecyclerAdapter topRatedMoviesRecyclerViewAdapter;
    MediaAdapter trendingMoviesRecyclerAdapter;
    MediaAdapter lastPlayedMoviesRecyclerViewAdapter;
    MediaAdapter watchlistRecyclerViewAdapter;

    MediaAdapter topRatedShowsRecyclerAdapter;
    MediaAdapter newSeasonRecyclerAdapter;



    TextView recentlyAddedRecyclerViewTitle;
    RecyclerView recentlyAddedRecyclerView;

    TextView recentlyReleasedRecyclerViewTitle;
    RecyclerView recentlyReleasedRecyclerView;

    TextView topRatedMoviesRecyclerViewTitle;
    TextView trendingTitle;
    TextView filmIndoTitle;
    RecyclerView filmIndoView;
    MediaAdapter filmIndoAdapter;
    MediaAdapter.OnItemClickListener filmIndoListener;
    TextView drakorTitle;
    RecyclerView drakorView;
    DrakorBannerAdapter drakorAdapter;
    DrakorBannerAdapter.OnItemClickListener drakorListener;
    RecyclerView topRatedMoviesRecyclerView;
    RecyclerView trendingRecyclerView;

    TextView lastPlayedMoviesRecyclerViewTitle;
    RecyclerView lastPlayedMoviesRecyclerView;

    TextView watchlistRecyclerViewTitle;
    RecyclerView watchlistRecyclerView;

    TextView topRatedShowsRecyclerViewTitle;
    RecyclerView topRatedShowsRecyclerView;

    TextView newSeasonRecyclerViewTitle;
    RecyclerView newSeasonRecyclerView;

    List<Movie> recentlyAddedMovies;
    List<Movie> recentlyReleasedMovies;
    List<Movie> topRatedMovies;
    List<Movie> trending;
    List<Movie> lastPlayedList;
    List<Movie> fav;
    List<Movie> played;
    List<Movie> ogMovies;
    List<Movie> topOld;
    List<Movie> filmIndo;
    List<TVShow> drakor;
    List<MyMedia> ogtop;
    List<MyMedia> someRecom;
    List<TVShow> newSeason;
    List<TVShow> topRatedShows;

    BannerRecyclerAdapter.OnItemClickListener recentlyAddedListener;
    MediaAdapter.OnItemClickListener recentlyReleasedListener;
    BannerRecyclerAdapter.OnItemClickListener topRatedMoviesListener;
    MediaAdapter.OnItemClickListener trendingListener;
    MediaAdapter.OnItemClickListener lastPlayedListener;
    MediaAdapter.OnItemClickListener watchlistListener;
    MediaAdapter.OnItemClickListener topRatedShowsListener;
    MediaAdapter.OnItemClickListener newSeasonListener;
    FrameLayout floatingActionButton;
    Button scanButton;



    private SwipeRefreshLayout swipeRefreshLayout;

//    List<PairMovies> pairMoviesList;
//    List<PairTvShows> pairTvShowsList;

    public SeriesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_series_home, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout2);
        loadNewSeason();
        setOnClickListner();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your refresh logic here
                refreshData();

            }
        });
    }
    private void refreshData() {
        // Implement your refresh logic here
        // For example, you can re-fetch the data or perform any necessary updates
        // Once the refresh is complete, call setRefreshing(false) on the SwipeRefreshLayout
        // to indicate that the refresh has finished.
        loadNewSeason();

        loadDrakor();


        swipeRefreshLayout.setRefreshing(false);

    }

    private void loadDrakor()  {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                drakor = DatabaseClient
                        .getInstance(mActivity)
                        .getAppDatabase()
                        .tvShowDao()
                        .getDrakor();
                if(drakor!=null && drakor.size()>0){
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ScaleCenterItemLayoutManager linearLayoutManager2 = new ScaleCenterItemLayoutManager(getContext() , LinearLayoutManager.HORIZONTAL , false);
                            drakorTitle = mActivity.findViewById(R.id.drakor);
                            drakorTitle.setVisibility(View.VISIBLE);
                            drakorView = mActivity.findViewById(R.id.drakorRecycler);
                            drakorView.setLayoutManager(linearLayoutManager2);
                            drakorView.setHasFixedSize(true);
                            drakorAdapter = new DrakorBannerAdapter(getContext(), drakor , drakorListener);
                            drakorView.setAdapter(drakorAdapter);
                        }
                    });
                }
            }});
        thread.start();
    }
    private void loadNewSeason(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                newSeason = DatabaseClient
                        .getInstance(mActivity)
                        .getAppDatabase()
                        .tvShowDao()
                        .getNewShows();
                if(newSeason!=null && newSeason.size()>0){
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ScaleCenterItemLayoutManager linearLayoutManager3 = new ScaleCenterItemLayoutManager(getContext() , LinearLayoutManager.HORIZONTAL , false);
                            newSeasonRecyclerViewTitle = mActivity.findViewById(R.id.newSeason);
                            newSeasonRecyclerViewTitle.setVisibility(View.VISIBLE);
                            newSeasonRecyclerView = mActivity.findViewById(R.id.newSeasonRecycler);
                            newSeasonRecyclerView.setVisibility(View.VISIBLE);
                            newSeasonRecyclerView = mActivity.findViewById(R.id.newSeasonRecycler);
                            newSeasonRecyclerView.setLayoutManager(linearLayoutManager3);
                            newSeasonRecyclerView.setHasFixedSize(true);
                            newSeasonRecyclerAdapter = new MediaAdapter(getContext() ,(List<MyMedia>)(List<?>) newSeason , newSeasonListener);
                            newSeasonRecyclerView.setAdapter(newSeasonRecyclerAdapter);
                        }
                    });
                }
            }});
        thread.start();
    }
    private void setOnClickListner() {

        drakorListener = new DrakorBannerAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                TvShowDetailsFragment tvShowDetailsFragment = new TvShowDetailsFragment(drakor.get(position).getId());
                mActivity.getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fade_in,R.anim.fade_out,R.anim.fade_in,R.anim.fade_out)
                        .add(R.id.container,tvShowDetailsFragment).addToBackStack(null).commit();
            }
        };


        newSeasonListener = new MediaAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                TvShowDetailsFragment tvShowDetailsFragment = new TvShowDetailsFragment(newSeason.get(position).getId());
                mActivity.getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fade_in,R.anim.fade_out)
                        .add(R.id.container,tvShowDetailsFragment).addToBackStack(null).commit();
            }
        };
        topRatedShowsListener = new MediaAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                TvShowDetailsFragment tvShowDetailsFragment = new TvShowDetailsFragment(topRatedShows.get(position).getId());
                mActivity.getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fade_in,R.anim.fade_out)
                        .add(R.id.container,tvShowDetailsFragment).addToBackStack(null).commit();
            }
        };
    }
}
