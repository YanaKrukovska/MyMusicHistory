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

    public List<Scrobble> convertAllPendingListensToScrobbles() {
        List<PendingListenEntity> listensEntities = sInstance.pendingListenDao().loadAllListens();
        List<Scrobble> listensEntitiesConverted = new LinkedList<>();
        SharedPreferences sharedPreferences = appContext.getSharedPreferences("login", MODE_PRIVATE);

        for (int i = 0; i < listensEntities.size(); i++) {
            PendingListenEntity entity = listensEntities.get(i);
            Scrobble scrobble = new Scrobble();
            scrobble.setSyncId(entity.getSyncId());
            scrobble.setSong(new Song(entity.getSong(), new Album(entity.getAlbum(), new Artist(entity.artist))));
            scrobble.setListenDate(DataUtils.convertFromStringWithTime(entity.getListenDate()));
            scrobble.setSyncId(entity.getSyncId());

            User user = new User();
            user.setMail(sharedPreferences.getString("mail", ""));
            user.setId(sharedPreferences.getLong("user_id", -1));

            scrobble.setUser(user);
            listensEntitiesConverted.add(scrobble);
        }

        return listensEntitiesConverted;
    }


    public List<PendingListenEntity> convertScrobbleToPendingListenEntity(List<Scrobble> scrobbles) {
        List<PendingListenEntity> result = new LinkedList<>();
        for (int i = 0; i < scrobbles.size(); i++) {
            result.add(convertScrobbleToPendingListenEntity(scrobbles.get(i)));
        }
        return result;
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