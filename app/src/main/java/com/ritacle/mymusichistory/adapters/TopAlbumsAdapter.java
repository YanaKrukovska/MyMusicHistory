package com.ritacle.mymusichistory.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ritacle.mymusichistory.R;
import com.ritacle.mymusichistory.model.TopAlbum;

import java.util.List;

public class TopAlbumsAdapter extends RecyclerView.Adapter<TopAlbumsAdapter.ViewHolder> {

    private List<TopAlbum> topAlbums;

    public TopAlbumsAdapter(Context context, List<TopAlbum> topAlbums) {
        this.topAlbums = topAlbums;
    }

    public void clear() {
        topAlbums.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<TopAlbum> list) {
        topAlbums.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView albumTextView;
        public TextView artistTextView;
        public TextView listenCountView;
        public final View mView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            albumTextView = (TextView) itemView.findViewById(R.id.albumTitle);
            artistTextView = (TextView) itemView.findViewById(R.id.artistName);
            listenCountView = (TextView) itemView.findViewById(R.id.listensCount);
        }
    }

    @NonNull
    @Override
    public TopAlbumsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.top_album_item, parent, false);
        return new TopAlbumsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopAlbumsAdapter.ViewHolder holder, int position) {
        TopAlbum topAlbum = this.topAlbums.get(position);
        holder.artistTextView.setText(topAlbum.getArtist());
        holder.albumTextView.setText(topAlbum.getAlbum());
        holder.listenCountView.setText(topAlbum.getListenCount() + " listens");
    }

    @Override
    public int getItemCount() {
        //TODO: find the root cause of NPE
        return topAlbums == null ? null : topAlbums.size();
    }
}