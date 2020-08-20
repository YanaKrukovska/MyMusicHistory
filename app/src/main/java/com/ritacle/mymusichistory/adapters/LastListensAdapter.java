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
import androidx.recyclerview.widget.RecyclerView;

import com.ritacle.mymusichistory.R;
import com.ritacle.mymusichistory.model.LastListen;
import com.ritacle.mymusichistory.utils.DataUtils;

import java.util.List;

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

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            artistTextView = (TextView) itemView.findViewById(R.id.artistName);
            songTextView = (TextView) itemView.findViewById(R.id.songTitle);
            timeTextView = (TextView) itemView.findViewById(R.id.listenDate);
            threeDotsMenu = (ImageView) itemView.findViewById(R.id.listenThreeDotsMenu);
            threeDotsMenu.setOnClickListener((View view) -> {
                PopupMenu popupMenu = new PopupMenu(context, threeDotsMenu);
                popupMenu.getMenuInflater().inflate(R.menu.last_listen_menu_popup, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener((MenuItem item) -> {
                    if (item.getItemId() == R.id.last_listen_delete_item) {
                        Log.d("LastListenAdapter", "want to de lete listen id = " + listenId);
                        return true;
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
