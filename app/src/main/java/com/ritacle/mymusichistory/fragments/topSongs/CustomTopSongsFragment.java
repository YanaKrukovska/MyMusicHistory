package com.ritacle.mymusichistory.fragments.topSongs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
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
import com.ritacle.mymusichistory.utils.EditTextDatePicker;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomTopSongsFragment extends Fragment implements View.OnClickListener {

    private EditText startEditText;
    private EditText endEditText;
    private String accountName;
    private String startDate;
    private String endDate;
    private SwipeRefreshLayout swipeToRefresh;
    private TopSongsAdapter adapter;
    private RecyclerView rvTopSongs;
    private boolean readyToUpdate = false;
    private View rootView;
    private EditTextDatePicker startDatePicker;
    private EditTextDatePicker endDatePicker;
    private ImageButton searchButton;

    public CustomTopSongsFragment(String accountName) {
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
        rootView = inflater.inflate(R.layout.custom_top_songs, container, false);

        // startEditText = (EditText) rootView.findViewById(R.id.startDate);
        // endEditText = (EditText) rootView.findViewById(R.id.endDate);

        startDatePicker = new EditTextDatePicker(rootView, getContext(), R.id.startDate);
        endDatePicker = new EditTextDatePicker(rootView, getContext(), R.id.endDate);
        searchButton = rootView.findViewById(R.id.button);
        searchButton.setOnClickListener(this);


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
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        Call<List<ListenAmount>> call = service.getUserTopListens(accountName,
                startDatePicker.returnDate(),
                endDatePicker.returnDate());
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


 /*   @Override
    public void onResume() {
        super.onResume();

        startDatePicker = new EditTextDatePicker(rootView, getContext(), R.id.startDate);
        endDatePicker = new EditTextDatePicker(rootView, getContext(), R.id.endDate);
        if (startDatePicker.isDateChosen() && endDatePicker.isDateChosen()) {
            rvTopSongsCustom = rootView.findViewById(R.id.rvTopSongsCustom);
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

        }
    }*/


    public void updateCustomFragment() {
        // do something to update the fragment
    }

    @Override
    public void onClick(View view) {
        addTopSongs();
    }
}