package com.ritacle.mymusichistory.db;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;

@Dao
public interface PendingListenDao {

    @Query("SELECT * FROM PENDING_LISTENS ORDER BY ID")
    List<PendingListenEntity> loadAllListens();

    @Insert
    void insertListen(PendingListenEntity person);

    @Update
    void updateListen(PendingListenEntity person);

    @Delete
    void delete(PendingListenEntity person);

    @Query("SELECT * FROM PENDING_LISTENS WHERE id = :id")
    PendingListenEntity loadListenById(int id);
}
