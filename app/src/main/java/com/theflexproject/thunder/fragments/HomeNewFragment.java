package com.theflexproject.thunder.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.theflexproject.thunder.R;
import com.theflexproject.thunder.adapter.FragmentHomeAdapter;
import com.theflexproject.thunder.adapter.FragmentViewPagerAdapter;

public class HomeNewFragment extends BaseFragment {

    TabLayout tabLayout ;
    TabItem moviesTab;
    TabItem tvTab;
    TabItem filesTab;
    ViewPager2 viewPagerLibrary;
    FragmentHomeAdapter fragmentViewPagerAdapter;

    public HomeNewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_new, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initWidgets();
        tabLayout = mActivity.findViewById(R.id.tabLayout2);
        moviesTab = mActivity.findViewById(R.id.movieTab2);
        tvTab = mActivity.findViewById(R.id.seriesTab);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerLibrary.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPagerLibrary.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

    }

    private void initWidgets() {



        tabLayout = mActivity.findViewById(R.id.tabLayout2);
        viewPagerLibrary = mActivity.findViewById(R.id.homePagerLibrary);
        fragmentViewPagerAdapter = new FragmentHomeAdapter(this);
        viewPagerLibrary.setSaveEnabled(false);
        viewPagerLibrary.setAdapter(fragmentViewPagerAdapter);


    }
}
