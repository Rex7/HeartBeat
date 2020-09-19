package com.heartbeat;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
@Database(entities = {HeartBeat.class},version = 2,exportSchema = false)
public abstract  class HeartBeatImp extends RoomDatabase {
    private static volatile HeartBeatImp heartBeatImp;
public abstract HeartBeatDao heartbeatDao();
 public static  HeartBeatImp getDatabase(final Context con){
     if(heartBeatImp==null){
         synchronized (HeartBeatImp.class){
             if(heartBeatImp==null){
                 heartBeatImp= Room.databaseBuilder(con.getApplicationContext(),HeartBeatImp.class,"heart_database")
                         .allowMainThreadQueries()
                         .fallbackToDestructiveMigration()
                         .build();

             }
         }

     }
     return heartBeatImp;

 }
}
