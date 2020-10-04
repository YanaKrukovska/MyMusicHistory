package com.ritacle.mymusichistory.db;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.ritacle.mymusichistory.model.scrobbler_model.Album;
import com.ritacle.mymusichistory.model.scrobbler_model.Artist;
import com.ritacle.mymusichistory.model.scrobbler_model.Scrobble;
import com.ritacle.mymusichistory.model.scrobbler_model.Song;
import com.ritacle.mymusichistory.model.scrobbler_model.User;
import com.ritacle.mymusichistory.utils.DataUtils;

import java.util.LinkedList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

@Database(entities = {PendingListenEntity.class}, version = 1, exportSchema = false)
public abstract class PendingListensDB extends RoomDatabase {
    private static final String TAG = PendingListensDB.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "pendinglistenslist";
    private static PendingListensDB sInstance;
    private static Context appContext;

    public static PendingListensDB getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        PendingListensDB.class, PendingListensDB.DATABASE_NAME)
                        .build();
            }
            appContext = context;
        }
        Log.d(TAG, "Getting the database instance");
        return sInstance;
    }

    public List<Scrobble> convertAllPendingListenEntitiesToScrobbles(List<PendingListenEntity> pendingListenEntities) {
        List<Scrobble> listensEntitiesConverted = new LinkedList<>();

        for (int i = 0; i < pendingListenEntities.size(); i++) {
            listensEntitiesConverted.add(convertPendingListenEntityToScrobble(pendingListenEntities.get(i)));
        }

        return listensEntitiesConverted;
    }

    public Scrobble convertPendingListenEntityToScrobble(PendingListenEntity pendingListenEntity) {

        Scrobble scrobble = new Scrobble();
        scrobble.setSyncId(pendingListenEntity.getSyncId());
        scrobble.setSong(new Song(pendingListenEntity.getSong(), new Album(pendingListenEntity.getAlbum(), new Artist(pendingListenEntity.artist))));
        scrobble.setListenDate(DataUtils.convertFromStringWithTime(pendingListenEntity.getListenDate()));
        scrobble.setSyncId(pendingListenEntity.getSyncId());

        User user = new User();
        SharedPreferences sharedPreferences = appContext.getSharedPreferences("login", MODE_PRIVATE);
        user.setMail(sharedPreferences.getString("mail", ""));
        user.setId(sharedPreferences.getLong("user_id", -1));

        scrobble.setUser(user);
        return scrobble;
    }

    public PendingListenEntity convertScrobbleToPendingListenEntity(Scrobble scrobble) {
        PendingListenEntity pendingListenEntity = new PendingListenEntity();
        pendingListenEntity.setAlbum(scrobble.getSong().getAlbum().getTitle());
        pendingListenEntity.setArtist(scrobble.getSong().getAlbum().getArtist().getName());
        pendingListenEntity.setSong(scrobble.getSong().getTitle());
        pendingListenEntity.setUser(scrobble.getUser().getMail());
        pendingListenEntity.setListenDate(DataUtils.convertToStringWithTime(scrobble.getListenDate()));
        pendingListenEntity.setSyncId(scrobble.getSyncId());

        return pendingListenEntity;
    }

    public abstract PendingListenDao pendingListenDao();
}