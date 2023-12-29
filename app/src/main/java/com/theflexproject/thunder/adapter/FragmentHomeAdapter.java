package com.theflexproject.thunder.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.theflexproject.thunder.fragments.FilesLibraryFragment;
import com.theflexproject.thunder.fragments.HomeFragment;
import com.theflexproject.thunder.fragments.MovieLibraryFragment;
import com.theflexproject.thunder.fragments.SeriesFragment;
import com.theflexproject.thunder.fragments.TvShowsLibraryFragment;

public class FragmentHomeAdapter extends FragmentStateAdapter {

    public FragmentHomeAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position==0){
            return new HomeFragment();
        }else if(position==1){
            return new SeriesFragment();
        }else {
            return new HomeFragment();
        }
    }



    @Override
    public int getItemCount() {
        return 2;
    }
}
