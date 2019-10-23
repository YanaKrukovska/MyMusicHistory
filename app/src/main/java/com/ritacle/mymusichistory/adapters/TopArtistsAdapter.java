package com.ritacle.mymusichistory.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ritacle.mymusichistory.R;
import com.ritacle.mymusichistory.model.ListenAmount;
import com.ritacle.mymusichistory.model.TopArtist;

import java.util.List;

public class TopArtistsAdapter extends RecyclerView.Adapter<TopArtistsAdapter.ViewHolder> {

    private List<TopArtist> topArtists;

    public TopArtistsAdapter(Context context, List<TopArtist> topArtists) {
        Context context1 = context;
        this.topArtists = topArtists;
    }

    public void clear() {
        topArtists.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<TopArtist> list) {
        topArtists.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView artistTextView;
        public TextView listenCountView;
        public final View mView;

        public ViewHolder(View itemView) {

            super(itemView);
            mView = itemView;
            artistTextView = (TextView) itemView.findViewById(R.id.artistName);
            listenCountView = (TextView) itemView.findViewById(R.id.listensCount);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.top_artist_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TopArtist topArtists = this.topArtists.get(position);

        holder.artistTextView.setText(topArtists.getArtist());
        holder.listenCountView.setText(topArtists.getListenCount() + " listens");

    }


    @Override
    public int getItemCount() {
        //TODO: find the root cause of NPE
        return topArtists == null?null: topArtists.size();
    }
}
