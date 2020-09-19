package com.heartbeat;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class HeartBeat {
    @PrimaryKey(autoGenerate = true)
    public int Id;
    @ColumnInfo(name = "pulseRate")
    public String heartbeat;
    @ColumnInfo
    public  int month;
    @ColumnInfo
    public int day_of_month;

    public String getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(String heartbeat) {
        this.heartbeat = heartbeat;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay_of_month() {
        return day_of_month;
    }

    public void setDay_of_month(int day_of_month) {
        this.day_of_month = day_of_month;
    }
}
