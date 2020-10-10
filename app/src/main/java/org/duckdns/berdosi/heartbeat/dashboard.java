package org.duckdns.berdosi.heartbeat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.heartbeat.HeartBeat;
import com.heartbeat.HeartBeatDao;
import com.heartbeat.HeartBeatImp;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

import static android.media.CamcorderProfile.get;
import static java.lang.Integer.bitCount;
import static java.lang.Integer.parseInt;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
public class dashboard extends AppCompatActivity {
    LineGraphSeries<DataPoint> series;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dashboard);
        GraphView graph = (GraphView) findViewById(R.id.graph);


        series=new LineGraphSeries<DataPoint>(getData());
        graph.addSeries(series);

    }

private DataPoint[] getData(){

    HeartBeatDao heartBeatDao= HeartBeatImp.getDatabase(getApplicationContext()).heartbeatDao();
    List<HeartBeat> getAllResult = heartBeatDao.getAllResult();
    //Log.v("Babe","Data"+getAllResult.get(2).heartbeat);

    DataPoint[] db=new DataPoint[getAllResult.get];





}

}