package com.ritacle.mymusichistory.fragments.topSongs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ritacle.mymusichistory.R;
import com.ritacle.mymusichistory.adapters.TopSongsPagerAdapter;

import java.util.Date;

import static com.ritacle.mymusichistory.utils.DataUtils.addMonth;
import static com.ritacle.mymusichistory.utils.DataUtils.daysBetween;


public class TopSongsMainFragment extends FragmentStatePagerAdapter {

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.top_songs_main, container, false);


        ViewPager viewPager = rootView.findViewById(R.id.view_pager);
        TabLayout tabLayout = rootView.findViewById(R.id.tab_layout);

        TopSongsPagerAdapter pagerAdapter = new TopSongsPagerAdapter(getChildFragmentManager());
        if (getArguments() == null || getArguments().getString("accountName") == null) {
            throw new IllegalStateException("User name not provided");
        }
        String accountName = getArguments().getString("accountName");

        pagerAdapter.addFragment(new TopSongsFragment(-7, accountName), "7 days");
        Date now = new Date();

        pagerAdapter.addFragment(new TopSongsFragment(daysBetween(now, addMonth(now, -1)), accountName), "one month");
        pagerAdapter.addFragment(new TopSongsFragment(daysBetween(now, addMonth(now, -3)), accountName), "3 months");
        pagerAdapter.addFragment(new TopSongsFragment(daysBetween(now, addMonth(now, -6)), accountName), "6 months");
        pagerAdapter.addFragment(new TopSongsFragment(daysBetween(now, addMonth(now, -12)), accountName), "12 months");
        pagerAdapter.addFragment(new TopSongsFragment(daysBetween(now, addMonth(now, -120)), accountName), "overall");

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


        return rootView;
    }


    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }
}