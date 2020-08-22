package com.ritacle.mymusichistory.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ritacle.mymusichistory.R;
import com.ritacle.mymusichistory.model.LastListen;
import com.ritacle.mymusichistory.model.ResponseMMH;
import com.ritacle.mymusichistory.model.scrobbler_model.Scrobble;
import com.ritacle.mymusichistory.network.StatisticRestService;
import com.ritacle.mymusichistory.utils.DataUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LastListensAdapter extends RecyclerView.Adapter<LastListensAdapter.ViewHolder> {

    private List<LastListen> lastListens;
    public Context context;

    public LastListensAdapter(Context context, List<LastListen> lastListens) {
        this.context = context;
        this.lastListens = lastListens;
    }

    public void clear() {
        lastListens.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<LastListen> list) {
        lastListens.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView artistTextView;
        public TextView songTextView;
        public TextView timeTextView;
        public final View mView;
        public ImageView threeDotsMenu;
        public Long listenId;

        private static final String BASE_URL = "https://my-music-history.herokuapp.com/";
        private final StatisticRestService mmhRestAPI;
        private final String TAG = "LastListenAdapter";

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            artistTextView = itemView.findViewById(R.id.artistName);
            songTextView = itemView.findViewById(R.id.songTitle);
            timeTextView = itemView.findViewById(R.id.listenDate);

            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            mmhRestAPI = retrofit.create(StatisticRestService.class);

            threeDotsMenu = itemView.findViewById(R.id.listenThreeDotsMenu);
            threeDotsMenu.setOnClickListener((View view) -> {
                PopupMenu popupMenu = new PopupMenu(context, threeDotsMenu);
                popupMenu.getMenuInflater().inflate(R.menu.last_listen_menu_popup, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener((MenuItem item) -> {
                    switch (item.getItemId()) {
                        case R.id.last_listen_delete_item:
                            Log.d(TAG, "Deleting listen id = " + listenId);

                            Call<ResponseMMH<Scrobble>> call = mmhRestAPI.deleteListen(listenId);
                            call.enqueue(new Callback<ResponseMMH<Scrobble>>() {
                                @Override
                                public void onResponse(@NonNull Call<ResponseMMH<Scrobble>> call, @NonNull Response<ResponseMMH<Scrobble>> response) {
                                    Log.d(TAG, "Successfully deleted listen with id = " + listenId);
                                    artistTextView.setTextColor(ContextCompat.getColor(context, R.color.deletedListenArtistName));
                                    timeTextView.setTextColor(ContextCompat.getColor(context, R.color.deletedListenArtistName));
                                    songTextView.setTextColor(ContextCompat.getColor(context, R.color.deletedListenSongTitle));
                                }

                                @Override
                                public void onFailure(@NonNull Call<ResponseMMH<Scrobble>> call, @NonNull Throwable t) {
                                    Log.d(TAG, "Failed to delete listen");
                                }
                            });
                            break;

                        case R.id.last_listen_edit_item:
                            Log.d(TAG, "Editing listen id = " + listenId);

                            break;
                    }
                    return false;
                });
                popupMenu.show();

            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.listen_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LastListen lastListen = this.lastListens.get(position);
        holder.artistTextView.setText(lastListen.getArtist());
        holder.songTextView.setText(lastListen.getTitle());
        holder.timeTextView.setText(DataUtils.convertToTimeLabel(lastListen.getDate()));
        holder.listenId = lastListen.getId();
    }

    @Override
    public int getItemCount() {
        return lastListens.size();
    }
}
