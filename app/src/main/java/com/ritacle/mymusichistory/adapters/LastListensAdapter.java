package com.ritacle.mymusichistory.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ritacle.mymusichistory.R;
import com.ritacle.mymusichistory.model.LastListen;

import java.util.List;

public class LastListensAdapter extends RecyclerView.Adapter<LastListensAdapter.ViewHolder> {

    private List<LastListen> lastListens;

    public LastListensAdapter(Context context, List<LastListen> lastListens) {
        Context context1 = context;
        this.lastListens = lastListens;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView artistTextView;
        public TextView songTextView;
        public final View mView;

        public ViewHolder(View itemView) {

            super(itemView);
            mView = itemView;
            artistTextView = (TextView) itemView.findViewById(R.id.artistName);
            songTextView = (TextView) itemView.findViewById(R.id.songTitle);
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

    }


    @Override
    public int getItemCount() {
        return lastListens.size();
    }
}
