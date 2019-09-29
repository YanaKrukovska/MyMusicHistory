package com.ritacle.mymusichistory.fragments.topSongs;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ritacle.mymusichistory.R;
import com.ritacle.mymusichistory.adapters.TopSongsAdapter;
import com.ritacle.mymusichistory.common.ui.decorators.SimpleDividerItemDecoration;
import com.ritacle.mymusichistory.model.ListenAmount;
import com.ritacle.mymusichistory.network.GetDataService;
import com.ritacle.mymusichistory.network.RetrofitClientInstance;
import com.ritacle.mymusichistory.testutils.TopSongsStubService;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ritacle.mymusichistory.utils.DataUtils.*;

public class TopSongsFragment1Month extends Fragment {


    private SwipeRefreshLayout swipeToRefresh;
    private TopSongsAdapter adapter;
    private RecyclerView rvTopSongs;
    private TopSongsStubService serviceStub = new TopSongsStubService();



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.top_songs_1month, container, false);

        rvTopSongs = (RecyclerView) rootView.findViewById(R.id.rvTopSongs);


        swipeToRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeToRefresh);
        swipeToRefresh.setColorSchemeResources(R.color.colorAccent);

        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                addTopSongs();
                swipeToRefresh.setRefreshing(false);
            }
        });

        addTopSongs();

        return rootView;
    }

    private void addTopSongs() {
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        Date now = new Date();

        //TODO: add getting user
        Call<List<ListenAmount>> call = service.getUserTopListens("jana.krua@gmail.com",
                convertToString(addMonth(now, -1)),
                convertToString(now));
        call.enqueue(new Callback<List<ListenAmount>>() {
            @Override
            public void onResponse(Call<List<ListenAmount>> call, Response<List<ListenAmount>> response) {
                adapter = new TopSongsAdapter(getContext(), response.body());
                rvTopSongs.setAdapter(adapter);
                rvTopSongs.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
                rvTopSongs.setLayoutManager(new LinearLayoutManager(getActivity()));

            }

            @Override
            public void onFailure(Call<List<ListenAmount>> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}