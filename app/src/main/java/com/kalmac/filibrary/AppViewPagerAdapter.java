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
        switch (position){
            case 0:
                return  new HomeFragment();
            case 1:
                return new LibraryFragment();
            case 2:
                return new SearchFragment();
                default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
