package com.ritacle.mymusichistory.fragments.topArtists;

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
import com.ritacle.mymusichistory.adapters.TopArtistsAdapter;
import com.ritacle.mymusichistory.common.ui.decorators.SimpleDividerItemDecoration;
import com.ritacle.mymusichistory.model.TopArtist;
import com.ritacle.mymusichistory.network.ReportRestService;
import com.ritacle.mymusichistory.network.RetrofitClientInstance;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ritacle.mymusichistory.utils.DataUtils.addDays;
import static com.ritacle.mymusichistory.utils.DataUtils.convertToString;

public class TopArtistsFragment extends Fragment {

    private int timeShift;
    private String accountName;
    private SwipeRefreshLayout swipeToRefresh;
    private TopArtistsAdapter adapter;
    private RecyclerView rvTopArtists;

    public TopArtistsFragment(int timeShift, String accountName) {
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
        View rootView = inflater.inflate(R.layout.top_artists, container, false);

        rvTopArtists = rootView.findViewById(R.id.rvTopArtists);
        swipeToRefresh = rootView.findViewById(R.id.swipeToRefresh);
        swipeToRefresh.setColorSchemeResources(R.color.colorAccent);

        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                addTopArtists();
                swipeToRefresh.setRefreshing(false);
            }
        });

        addTopArtists();
        return rootView;
    }

    private void addTopArtists() {

        final ReportRestService service = RetrofitClientInstance.getRetrofitInstance().create(ReportRestService.class);
        Date now = new Date();

        Call<List<TopArtist>> call = service.getUserTopArtists(accountName,
                convertToString(addDays(now, timeShift)),
                convertToString(now));
        call.enqueue(new Callback<List<TopArtist>>() {
            @Override
            public void onResponse(@NonNull Call<List<TopArtist>> call, @NonNull Response<List<TopArtist>> response) {
                adapter = new TopArtistsAdapter(getContext(), response.body());
                rvTopArtists.setAdapter(adapter);
                rvTopArtists.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
                rvTopArtists.setLayoutManager(new LinearLayoutManager(getActivity()));
            }

            @Override
            public void onFailure(@NonNull Call<List<TopArtist>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}