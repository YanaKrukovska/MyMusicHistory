package com.ritacle.mymusichistory.fragments.topSongs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ritacle.mymusichistory.R;
import com.ritacle.mymusichistory.adapters.TopSongsAdapter;
import com.ritacle.mymusichistory.common.ui.decorators.SimpleDividerItemDecoration;
import com.ritacle.mymusichistory.model.ListenAmount;
import com.ritacle.mymusichistory.network.ReportRestService;
import com.ritacle.mymusichistory.network.RetrofitClientInstance;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ritacle.mymusichistory.utils.DataUtils.addDays;
import static com.ritacle.mymusichistory.utils.DataUtils.convertToString;

public class TopSongsFragment extends Fragment {

    private int timeShift;
    private String accountName;
    private SwipeRefreshLayout swipeToRefresh;
    private TopSongsAdapter adapter;
    private RecyclerView rvTopSongs;

    public TopSongsFragment(int timeShift, String accountName) {
        this.timeShift = timeShift;
        this.accountName = accountName;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.top_songs, container, false);


        rvTopSongs = rootView.findViewById(R.id.rvTopSongs);
        swipeToRefresh = rootView.findViewById(R.id.swipeToRefresh);
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
        ReportRestService service = RetrofitClientInstance.getRetrofitInstance().create(ReportRestService.class);

        Date now = new Date();

        Call<List<ListenAmount>> call = service.getUserTopListens(accountName,
                convertToString(addDays(now, timeShift)),
                convertToString(now));
        call.enqueue(new Callback<List<ListenAmount>>() {
            @Override
            public void onResponse(@NonNull Call<List<ListenAmount>> call, @NonNull Response<List<ListenAmount>> response) {
                adapter = new TopSongsAdapter(getContext(), response.body());
                rvTopSongs.setAdapter(adapter);
                rvTopSongs.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
                rvTopSongs.setLayoutManager(new LinearLayoutManager(getActivity()));

            }

            @Override
            public void onFailure(@NonNull Call<List<ListenAmount>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}