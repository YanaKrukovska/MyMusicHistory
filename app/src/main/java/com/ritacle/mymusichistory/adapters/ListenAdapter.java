package com.ritacle.mymusichistory.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ritacle.mymusichistory.R;
import com.ritacle.mymusichistory.model.Listen;

import java.util.List;

public class ListenAdapter extends RecyclerView.Adapter<ListenAdapter.ViewHolder> {


    private List<Listen> listens;

    public ListenAdapter(List<Listen> listens) {
        this.listens = listens;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View listenView = inflater.inflate(R.layout.listen_item, parent, false);

        return new ViewHolder(listenView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListenAdapter.ViewHolder holder, int position) {
        Listen listen = listens.get(position);

        holder.artistTextView.setText(listen.getArtist());
        holder.songTextView.setText(listen.getTitle());
    }

    @Override
    public int getItemCount() {
        return listens.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView artistTextView;
        public TextView songTextView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            artistTextView = (TextView) itemView.findViewById(R.id.artistName);
            songTextView = (TextView) itemView.findViewById(R.id.songTitle);
        }
    }


}
