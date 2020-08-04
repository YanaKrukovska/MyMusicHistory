package com.ritacle.mymusichistory.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ritacle.mymusichistory.R;
import com.ritacle.mymusichistory.model.LastListen;
import com.ritacle.mymusichistory.model.discogs_model.DiscogsResponse;
import com.ritacle.mymusichistory.network.GetDataService;
import com.ritacle.mymusichistory.network.RetrofitDiscogsClientInstance;
import com.ritacle.mymusichistory.utils.DataUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LastListensAdapter extends RecyclerView.Adapter<LastListensAdapter.ViewHolder> {

    private List<LastListen> lastListens;
    private static final String TAG = "Last Listens Adapter";
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
        public ImageView imageView;
        public final View mView;

        public ViewHolder(View itemView) {

            super(itemView);
            mView = itemView;
            artistTextView = itemView.findViewById(R.id.artistName);
            songTextView = itemView.findViewById(R.id.songTitle);
            timeTextView = itemView.findViewById(R.id.listenDate);
            imageView = itemView.findViewById(R.id.imageIcon);
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

        GetDataService service = RetrofitDiscogsClientInstance.getRetrofitInstance().create(GetDataService.class);
        String album = lastListen.getAlbum().replaceAll("'", "").replaceAll("(Deluxe)", "").replaceAll("(Deluxe Edition)", "");

        Call<DiscogsResponse> callUser = service.getSongArtwork(lastListen.getArtist(), album);
        callUser.enqueue(new Callback<DiscogsResponse>() {
            @Override
            public void onResponse(@NonNull Call<DiscogsResponse> call, @NonNull Response<DiscogsResponse> response) {
                if (response.body() != null && response.body().getResults() != null && response.body().getResults().length != 0) {
                    if (response.body().getResults()[0].getCover_image() != null && !response.body().getResults()[0].getCover_image().isEmpty()) {
                        Picasso.with(context).load(response.body().getResults()[0].getCover_image()).fit().into(holder.imageView);
                    }
                } else {
                    Log.d(TAG, "Artwork doesn't exist");
                }
            }

            @Override
            public void onFailure(@NonNull Call<DiscogsResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "failed to process");
            }
        });

    }

    @Override
    public int getItemCount() {
        return lastListens.size();
    }
}
