package com.ritacle.mymusichistory.fragments.topSongs;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ritacle.mymusichistory.R;
import com.ritacle.mymusichistory.adapters.TopSongsAdapter;
import com.ritacle.mymusichistory.adapters.TopSongsPagerAdapter;
import com.ritacle.mymusichistory.testutils.TopSongsStubService;


public class TopSongsMainFragment extends Fragment {


    private TopSongsStubService service = new TopSongsStubService();


    private TopSongsPagerAdapter pagerAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

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
        pagerAdapter.addFragment(new TopSongsFragment7Days(), "7 days");
        pagerAdapter.addFragment(new TopSongsFragment1Month(), "one month");
        pagerAdapter.addFragment(new TopSongsFragment3Months(), "3 months");
        pagerAdapter.addFragment(new TopSongsFragment6Months(), "6 months");
        pagerAdapter.addFragment(new TopSongsFragment12Months(), "12 months");

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


        return rootView;
    }





}