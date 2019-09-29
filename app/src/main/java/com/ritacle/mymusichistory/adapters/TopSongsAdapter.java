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

import java.util.List;

public class TopSongsAdapter extends RecyclerView.Adapter<TopSongsAdapter.ViewHolder> {

    private List<ListenAmount> topSongs;

    public TopSongsAdapter(Context context, List<ListenAmount> topSongs) {
        Context context1 = context;
        this.topSongs = topSongs;
    }

    public void clear() {
        topSongs.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<ListenAmount> list) {
        topSongs.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView artistTextView;
        public TextView songTextView;
        public TextView listenCountView;
        public final View mView;

        public ViewHolder(View itemView) {

            super(itemView);
            mView = itemView;
            artistTextView = (TextView) itemView.findViewById(R.id.artistName);
            songTextView = (TextView) itemView.findViewById(R.id.songTitle);
            listenCountView = (TextView) itemView.findViewById(R.id.listensCount);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.top_song_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ListenAmount topSongs = this.topSongs.get(position);

        holder.artistTextView.setText(topSongs.getArtist());
        holder.songTextView.setText(topSongs.getTitle());
        holder.listenCountView.setText(topSongs.getListenCount() + " listens");

    }


    @Override
    public int getItemCount() {
        //TODO: find the root cause of NPE
        return topSongs == null?null:topSongs.size();
    }
}
