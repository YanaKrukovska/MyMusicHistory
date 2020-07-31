package com.ritacle.mymusichistory.fragments.topAlbums;

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
import com.ritacle.mymusichistory.adapters.TopAlbumsAdapter;
import com.ritacle.mymusichistory.common.ui.decorators.SimpleDividerItemDecoration;
import com.ritacle.mymusichistory.model.TopAlbum;
import com.ritacle.mymusichistory.network.GetDataService;
import com.ritacle.mymusichistory.network.RetrofitClientInstance;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ritacle.mymusichistory.utils.DataUtils.addDays;
import static com.ritacle.mymusichistory.utils.DataUtils.convertToString;

public class TopAlbumsFragment extends Fragment {

    private int timeShift;
    private String accountName;
    private SwipeRefreshLayout swipeToRefresh;
    private TopAlbumsAdapter adapter;
    private RecyclerView rvTopAlbums;

    public TopAlbumsFragment(int timeShift, String accountName) {
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
        View rootView = inflater.inflate(R.layout.top_albums, container, false);

        rvTopAlbums = rootView.findViewById(R.id.rvTopAlbums);
        swipeToRefresh = rootView.findViewById(R.id.swipeToRefresh);
        swipeToRefresh.setColorSchemeResources(R.color.colorAccent);

        swipeToRefresh.setOnRefreshListener(() -> {
            addTopAlbums();
            swipeToRefresh.setRefreshing(false);
        });

        addTopAlbums();
        return rootView;
    }

    private void addTopAlbums() {

        final GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Date now = new Date();

        Call<List<TopAlbum>> call = service.getUserTopAlbums(accountName,
                convertToString(addDays(now, timeShift)),
                convertToString(now));
        call.enqueue(new Callback<List<TopAlbum>>() {
            @Override
            public void onResponse(@NonNull Call<List<TopAlbum>> call, @NonNull Response<List<TopAlbum>> response) {
                adapter = new TopAlbumsAdapter(getContext(), response.body());
                rvTopAlbums.setAdapter(adapter);
                rvTopAlbums.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
                rvTopAlbums.setLayoutManager(new LinearLayoutManager(getActivity()));
            }

            @Override
            public void onFailure(@NonNull Call<List<TopAlbum>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}