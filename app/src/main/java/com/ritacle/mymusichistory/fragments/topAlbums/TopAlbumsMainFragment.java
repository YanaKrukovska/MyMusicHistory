package com.ritacle.mymusichistory.fragments.topAlbums;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ritacle.mymusichistory.R;
import com.ritacle.mymusichistory.adapters.TopAlbumsPagerAdapter;

import java.util.Date;

import static android.content.Context.MODE_PRIVATE;
import static com.ritacle.mymusichistory.utils.DataUtils.addMonth;
import static com.ritacle.mymusichistory.utils.DataUtils.daysBetween;

public class TopAlbumsMainFragment extends Fragment {

    private TopAlbumsPagerAdapter pagerAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String MAIL;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login", MODE_PRIVATE);
        MAIL = sharedPreferences.getString("mail", "");
        View rootView = inflater.inflate(R.layout.top_albums_main, container, false);

        viewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        pagerAdapter = new TopAlbumsPagerAdapter(getChildFragmentManager());
        pagerAdapter.addFragment(new TopAlbumsFragment(-7, MAIL), "7 days");
        Date now = new Date();
        pagerAdapter.addFragment(new TopAlbumsFragment(daysBetween(now, addMonth(now, -1)), MAIL), "one month");
        pagerAdapter.addFragment(new TopAlbumsFragment(daysBetween(now, addMonth(now, -3)), MAIL), "3 months");
        pagerAdapter.addFragment(new TopAlbumsFragment(daysBetween(now, addMonth(now, -6)), MAIL), "6 months");
        pagerAdapter.addFragment(new TopAlbumsFragment(daysBetween(now, addMonth(now, -12)), MAIL), "12 months");
        pagerAdapter.addFragment(new TopAlbumsFragment(daysBetween(now, addMonth(now, -120)), MAIL), "overall");

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return rootView;
    }

}