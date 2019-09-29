package com.ritacle.mymusichistory.fragments;

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
import com.ritacle.mymusichistory.adapters.LastListensAdapter;
import com.ritacle.mymusichistory.common.ui.decorators.SimpleDividerItemDecoration;
import com.ritacle.mymusichistory.model.LastListen;
import com.ritacle.mymusichistory.network.GetDataService;
import com.ritacle.mymusichistory.network.RetrofitClientInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListensFragment extends Fragment {

    private SwipeRefreshLayout swipeToRefresh;
    private LastListensAdapter adapter;
    private ProgressDialog progressDialog;
    private RecyclerView rvListens;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.listens_main, container, false);
        rvListens = (RecyclerView) rootView.findViewById(R.id.rvListens);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Wait luv....");
        progressDialog.show();

        swipeToRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeToRefresh);
        swipeToRefresh.setColorSchemeResources(R.color.colorAccent);


        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                addListens();
                swipeToRefresh.setRefreshing(false);
            }
        });

        addListens();


        return rootView;
    }

    private void addListens() {
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        //TODO: add getting user
        Call<List<LastListen>> call = service.getSongReport("jana.krua@gmail.com");
        call.enqueue(new Callback<List<LastListen>>() {
            @Override
            public void onResponse(Call<List<LastListen>> call, Response<List<LastListen>> response) {
                progressDialog.dismiss();
                adapter = new LastListensAdapter(getContext(), response.body());
                rvListens.setAdapter(adapter);
                rvListens.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
                rvListens.setLayoutManager(new LinearLayoutManager(getActivity()));

            }

            @Override
            public void onFailure(Call<List<LastListen>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
