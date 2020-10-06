package com.heartbeat.history;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.heartbeat.HeartBeat;
import com.heartbeat.HeartBeatImp;
import com.heartbeat.SortMonth;

import org.duckdns.berdosi.heartbeat.MainActivity;
import org.duckdns.berdosi.heartbeat.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView recyclerView;
    HistoryAdapter historyAdapter;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        recyclerView=findViewById(R.id.recyclerview_history);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout =  findViewById(R.id.drawer);
        navigationView=findViewById(R.id.navView);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_navigation, R.string.close_navigation);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        navigationView.setNavigationItemSelectedListener(this);
        HeartBeatImp heartBeatImp=HeartBeatImp.getDatabase(getApplicationContext());
        List<HeartBeat> heartBeats=heartBeatImp.heartbeatDao().getAllResult();
        Collections.sort(heartBeats, new SortMonth());
        ArrayList<HeartBeat> heartBeatArrayList= new ArrayList<>( heartBeats);
        historyAdapter=new HistoryAdapter(getApplicationContext(),heartBeatArrayList);
        recyclerView.setAdapter(historyAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator( new DefaultItemAnimator());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.history:
                startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
                break;
            case R.id.home:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
        }
        return true;
    }
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
}
