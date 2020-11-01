package com.heartbeat.dashboard;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.heartbeat.HeartBeat;
import com.heartbeat.HeartBeatDao;
import com.heartbeat.HeartBeatImp;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.duckdns.berdosi.heartbeat.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

@RequiresApi(api = Build.VERSION_CODES.N)
public class Dashboard extends AppCompatActivity implements OnChartGestureListener {
    LineGraphSeries<DataPoint> series;
    TextView highest_textview, lowest;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_design);
        toolbar = findViewById(R.id.toolbar_dashboard);
        setSupportActionBar(toolbar);
        //back arrow navigation
        toolbar.setNavigationOnClickListener(v -> finish());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        lowest = findViewById(R.id.showLowest);
        highest_textview = findViewById(R.id.showHighest);
        LineChart graph = findViewById(R.id.graph);
        graph.setOnChartGestureListener(this);
        graph.setDragEnabled(true);
        graph.setScaleEnabled(true);
        LineDataSet graphData = new LineDataSet(getData(), "HeartBeat : BP");

        //Customizing graph
        graphData.setFillAlpha(110);
        graphData.setColor(Color.RED);
        graphData.setLineWidth(3f);
        graphData.setValueTextSize(10f);

        //disabling right axis
        graph.getAxisRight().setEnabled(false);

        ArrayList<ILineDataSet> dataSetArrayList = new ArrayList<>();
        dataSetArrayList.add(graphData);
        LineData lineData = new LineData(dataSetArrayList);
        graph.setData(lineData);

    }

    private ArrayList<Entry> getData() {
        HeartBeatDao heartBeatDao = HeartBeatImp.getDatabase(getApplicationContext()).heartbeatDao();
        ArrayList<Entry> dataset = new ArrayList<>();
        List<HeartBeat> getAllResult = heartBeatDao.getMonthSorted();
        Log.v(Dashboard.class.getName(),"Size"+getAllResult.size());
        int smallest = getAllResult.stream().mapToInt(small -> {
            String value;
            if (small.getHeartbeat().trim().substring(10).matches(".")) {
                value = small.getHeartbeat().substring(7, 9).trim();
            } else {
                value = small.getHeartbeat().trim().substring(7, 10).trim();
            }
            return Integer.parseInt(value);
        }).min().getAsInt();
        int highest = getAllResult.stream().mapToInt(big -> {
            String value;
            if (big.getHeartbeat().trim().substring(10).matches(".")) {
                value = big.getHeartbeat().substring(7, 9).trim();
            } else {
                value = big.getHeartbeat().trim().substring(7, 10).trim();
            }
            return Integer.parseInt(value);
        }).max().getAsInt();
        lowest.setText(getResources().getString(R.string.heart_rate_with_hp_integer, smallest));
        highest_textview.setText(getResources().getString(R.string.heart_rate_with_hp_integer, highest));
        for (int i = 0; i < getAllResult.size(); i++) {
            String value = getAllResult.get(i).getHeartbeat().substring(7, 9).trim();
            int newValue = Integer.parseInt(value.trim());
            dataset.add(new Entry(getAllResult.get(i).getDay_of_month(), newValue));
            Log.v("ValueCheck", "" + dataset.get(i).getX());
            Log.v("ValueCheck", "" + newValue);

        }
        return dataset;

    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }
}