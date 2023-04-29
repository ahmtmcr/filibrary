package com.kalmac.filibrary;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.kalmac.filibrary.fragments.FilmFragment;
import com.kalmac.filibrary.fragments.HomeFragment;
import com.kalmac.filibrary.fragments.LibraryFragment;
import com.kalmac.filibrary.fragments.SearchFragment;

public class AppViewPagerAdapter extends FragmentStateAdapter {
    public AppViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        HomeFragment homeFragment = new HomeFragment();
        LibraryFragment libraryFragment = new LibraryFragment();
        SearchFragment searchFragment = new SearchFragment();
        switch (position){
            case 0:
                return homeFragment;
            case 1:
                return libraryFragment;
            case 2:
                return searchFragment;
                default:
                return homeFragment;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
