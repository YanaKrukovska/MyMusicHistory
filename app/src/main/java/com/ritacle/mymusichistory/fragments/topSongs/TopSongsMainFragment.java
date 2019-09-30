package com.ritacle.mymusichistory.fragments.topSongs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ritacle.mymusichistory.R;
import com.ritacle.mymusichistory.adapters.TopSongsPagerAdapter;
import com.ritacle.mymusichistory.testutils.TopSongsStubService;
import com.ritacle.mymusichistory.utils.DataUtils;

import java.util.Date;

import static com.ritacle.mymusichistory.utils.DataUtils.*;


public class TopSongsMainFragment extends Fragment {


    private TopSongsStubService service = new TopSongsStubService();


    private TopSongsPagerAdapter pagerAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String MAIL = "jana.krua@gmail.com";

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.top_songs_main, container, false);

        //((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        // Toolbar toolbar = rootView.findViewById(R.id.toolbar_tabs);
        //  ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);


        viewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        pagerAdapter = new TopSongsPagerAdapter(getChildFragmentManager());
        pagerAdapter.addFragment(new TopSongsFragment(-7, MAIL), "7 days");
        Date now = new Date();
        pagerAdapter.addFragment(new TopSongsFragment(daysBetween(now, addMonth(now, -1)), MAIL), "one month");
        pagerAdapter.addFragment(new TopSongsFragment(daysBetween(now, addMonth(now, -3)), MAIL), "3 months");
        pagerAdapter.addFragment(new TopSongsFragment(daysBetween(now, addMonth(now, -6)), MAIL), "6 months");
        pagerAdapter.addFragment(new TopSongsFragment(daysBetween(now, addMonth(now, -12)), MAIL), "12 months");
        pagerAdapter.addFragment(new TopSongsFragment(daysBetween(now, addMonth(now, -120)), MAIL), "overall");

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


        return rootView;
    }


}