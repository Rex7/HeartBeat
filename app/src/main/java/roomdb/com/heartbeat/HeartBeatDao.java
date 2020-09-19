package com.heartbeat;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface HeartBeatDao {
    @Insert
    public void insert(HeartBeat ... heartBeat);
    @Query("select * from heartbeat")
    public List<HeartBeat> getAllResult();
}
