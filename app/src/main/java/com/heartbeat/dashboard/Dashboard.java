package com.heartbeat.dashboard;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.heartbeat.HeartBeat;
import com.heartbeat.HeartBeatDao;
import com.heartbeat.HeartBeatImp;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.duckdns.berdosi.heartbeat.R;

import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

@RequiresApi(api = Build.VERSION_CODES.N)
public class Dashboard extends AppCompatActivity {
    LineGraphSeries<DataPoint> series;
    TextView highest_textview,lowest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_design);
        lowest=findViewById(R.id.showLowest);
        highest_textview=findViewById(R.id.showHighest);
        GraphView graph = findViewById(R.id.graph);
        series = new LineGraphSeries<>(getData());
        graph.addSeries(series);
    }

    private DataPoint[] getData() {
        HeartBeatDao heartBeatDao = HeartBeatImp.getDatabase(getApplicationContext()).heartbeatDao();
        List<HeartBeat> getAllResult = heartBeatDao.getSorted();
        int smallest = getAllResult.stream().mapToInt(small -> {
            String value;
            if(small.getHeartbeat().trim().substring(10).matches(".")){
              value=  small.getHeartbeat().substring(7,9).trim();
            }
            else{
               value= small.getHeartbeat().trim().substring(7,10).trim();
            }
             return Integer.parseInt(value);
        }).min().getAsInt();
        int highest = getAllResult.stream().mapToInt(big -> {
            String value;
            if(big.getHeartbeat().trim().substring(10).matches(".")){
                value=  big.getHeartbeat().substring(7,9).trim();
            }
            else{
                value= big.getHeartbeat().trim().substring(7,10).trim();
            }
            return Integer.parseInt(value);
        }).max().getAsInt();
        lowest.setText(getResources().getString(R.string.heart_rate_with_hp_integer,smallest));
        highest_textview.setText(getResources().getString(R.string.heart_rate_with_hp_integer,highest));
        Log.v("SmallestValue","val"+smallest);
        Log.v("SmallestValue","val"+highest);
        DataPoint[] db = new DataPoint[getAllResult.size()];
        for (int i = 0; i < getAllResult.size(); i++) {
            String value = getAllResult.get(i).getHeartbeat().substring(7, 8).trim();
            int newValue = Integer.parseInt(value.trim());
            db[i] = new DataPoint(getAllResult.get(i).getDay_of_month(), newValue);
        }
        return db;

    }

}