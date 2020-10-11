package com.heartbeat;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface HeartBeatDao {
    @Insert
     void insert(HeartBeat ... heartBeat);
    @Query("select * from heartbeat")
     List<HeartBeat> getAllResult();
    @Query("select count(id) as rowcount from  HeartBeat")
     int getRowCount();
    @Query("SELECT * from  HeartBeat order by day_of_month ASC")
     List<HeartBeat> getSorted();
}
