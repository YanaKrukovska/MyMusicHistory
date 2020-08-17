package com.ritacle.mymusichistory.fragments.topArtists;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.ritacle.mymusichistory.utils.EditTextDatePicker;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomTopArtistsFragment extends Fragment implements View.OnClickListener {

    private String accountName;
    private SwipeRefreshLayout swipeToRefresh;
    private TopArtistsAdapter adapter;
    private RecyclerView rvTopArtists;
    private EditTextDatePicker startDatePicker;
    private EditTextDatePicker endDatePicker;

    public CustomTopArtistsFragment(String accountName) {
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
        View rootView = inflater.inflate(R.layout.custom_top_artists, container, false);

        startDatePicker = new EditTextDatePicker(rootView, getContext(), R.id.startDate);
        endDatePicker = new EditTextDatePicker(rootView, getContext(), R.id.endDate);
        ImageButton searchButton = rootView.findViewById(R.id.button);
        searchButton.setOnClickListener(this);

        rvTopArtists = rootView.findViewById(R.id.rvTopArtists);
        swipeToRefresh = rootView.findViewById(R.id.swipeToRefresh);
        swipeToRefresh.setColorSchemeResources(R.color.colorAccent);

        swipeToRefresh.setOnRefreshListener(() -> {
            addTopArtists();
            swipeToRefresh.setRefreshing(false);
        });

        addTopArtists();

        return rootView;
    }

    private void addTopArtists() {
        ReportRestService service = RetrofitClientInstance.getRetrofitInstance().create(ReportRestService.class);
        Call<List<TopArtist>> call = service.getUserTopArtists(accountName,
                startDatePicker.returnDate(),
                endDatePicker.returnDate());
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

    @Override
    public void onClick(View view) {
        addTopArtists();
    }
}