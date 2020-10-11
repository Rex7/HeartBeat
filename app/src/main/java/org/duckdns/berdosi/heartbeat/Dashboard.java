package org.duckdns.berdosi.heartbeat;

import android.os.Bundle;

import com.heartbeat.HeartBeat;
import com.heartbeat.HeartBeatDao;
import com.heartbeat.HeartBeatImp;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class Dashboard extends AppCompatActivity {
    LineGraphSeries<DataPoint> series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        GraphView graph = findViewById(R.id.graph);
        series = new LineGraphSeries<>(getData());
        graph.addSeries(series);
    }

    private DataPoint[] getData() {
        HeartBeatDao heartBeatDao = HeartBeatImp.getDatabase(getApplicationContext()).heartbeatDao();
        List<HeartBeat> getAllResult = heartBeatDao.getSorted();
        DataPoint[] db = new DataPoint[getAllResult.size()];
        for (int i = 0; i < getAllResult.size(); i++) {
            String value = getAllResult.get(i).getHeartbeat().substring(7, 8).trim();
            int newValue = Integer.parseInt(value.trim());
            db[i] = new DataPoint(getAllResult.get(i).getDay_of_month(), newValue);
        }

        return db;


    }

}