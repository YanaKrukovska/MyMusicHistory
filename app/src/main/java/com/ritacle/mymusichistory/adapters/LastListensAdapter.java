package com.ritacle.mymusichistory.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ritacle.mymusichistory.R;
import com.ritacle.mymusichistory.model.LastListen;
import com.ritacle.mymusichistory.model.ResponseMMH;
import com.ritacle.mymusichistory.model.scrobbler_model.Album;
import com.ritacle.mymusichistory.model.scrobbler_model.Artist;
import com.ritacle.mymusichistory.model.scrobbler_model.Scrobble;
import com.ritacle.mymusichistory.model.scrobbler_model.Song;
import com.ritacle.mymusichistory.model.scrobbler_model.User;
import com.ritacle.mymusichistory.network.StatisticRestService;
import com.ritacle.mymusichistory.utils.DataUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

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
        public LastListen listen;
        public AlertDialog alertDialog;

        private static final String BASE_URL = "https://my-music-history.herokuapp.com/";
        private final StatisticRestService mmhRestAPI;
        private final String TAG = "LastListenAdapter";
        private EditText editSong;
        private EditText editAlbum;
        private EditText editArtist;

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
                            warnAboutListenDeletion();
                            break;
                        case R.id.last_listen_edit_item:
                            Log.d(TAG, "Editing listen id = " + listenId);
                            openEditListenDialog();
                            break;
                    }
                    return false;
                });
                popupMenu.show();

            });
        }

        private void performListenDeletion() {
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
        }

        private void warnAboutListenDeletion() {
            if (alertDialog != null) {
                alertDialog.dismiss();
            }

            alertDialog =
                    new AlertDialog.Builder(context)
                            .setTitle("Are you sure?")
                            .setMessage("You can't bring the listen back once it's deleted")
                            .setNegativeButton(
                                    android.R.string.cancel,
                                    (dialogInterface, i) -> Log.d(TAG, "Listen deletion cancelled"))
                            .setPositiveButton(
                                    android.R.string.ok,
                                    (dialogInterface, i) -> performListenDeletion())
                            .show();
        }

        private void openEditListenDialog() {
            AlertDialog.Builder editDialogBuilder = new AlertDialog.Builder(context);
            LayoutInflater mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View customLayout = mLayoutInflater.inflate(R.layout.edit_listen_dialog, null);

            editDialogBuilder.setView(customLayout);
            editSong = customLayout.findViewById(R.id.editSongTitle);
            editSong.setText(listen.getTitle());

            editAlbum = customLayout.findViewById(R.id.editAlbumTitle);
            editAlbum.setText(listen.getAlbum());

            editArtist = customLayout.findViewById(R.id.editArtistName);
            editArtist.setText(listen.getArtist());

            editDialogBuilder.setPositiveButton("EDIT", (dialog, which) -> sendCallForEditing());
            AlertDialog dialog = editDialogBuilder.create();
            dialog.show();
        }

        private void sendCallForEditing() {
            Log.d(TAG, "Editing listen id = " + listenId);
            User user = new User();
            SharedPreferences sharedPreferences = context.getSharedPreferences("login", MODE_PRIVATE);
            user.setMail(sharedPreferences.getString("mail", ""));
            user.setId(sharedPreferences.getLong("user_id", -1));

            Call<ResponseMMH<Scrobble>> call = mmhRestAPI.editListen(new Scrobble(user, new Song(editSong.getText().toString(), new Album(editAlbum.getText().toString(), new Artist(editArtist.getText().toString()))), listen.getDate(), listen.getId()));
            call.enqueue(new Callback<ResponseMMH<Scrobble>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseMMH<Scrobble>> call, @NonNull Response<ResponseMMH<Scrobble>> response) {
                    Log.d(TAG, "Successfully edited listen with id = " + listen.getId());
                    Toast.makeText(context, "Edited", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(@NonNull Call<ResponseMMH<Scrobble>> call, @NonNull Throwable t) {
                    Log.d(TAG, "Failed to edit listen");
                    Toast.makeText(context, "Failed to edit", Toast.LENGTH_SHORT).show();
                }
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
        holder.listen = lastListen;
    }

    @Override
    public int getItemCount() {
        return lastListens.size();
    }
}
