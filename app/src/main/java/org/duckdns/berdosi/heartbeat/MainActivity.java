package org.duckdns.berdosi.heartbeat;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;


import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.Surface;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;


import com.google.android.material.navigation.NavigationView;
import com.heartbeat.history.HistoryActivity;
import com.heartbeat.suggestion.ParentAdapter;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.itangqi.waveloadingview.WaveLoadingView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private final CameraService cameraService = new CameraService(this);
    private OutputAnalyzer analyzer;
    private Toolbar toolbar;
    private TextView start;
    private boolean isSpeakButtonLongPressed = false;
    WaveLoadingView waveLoadingView;
    RecyclerView parentRecycler;
    ParentAdapter parentAdapter;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout =  findViewById(R.id.drawer);
        navigationView=findViewById(R.id.navView);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_navigation, R.string.close_navigation);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        navigationView.setNavigationItemSelectedListener(this);
        parentRecycler=findViewById(R.id.main_recyclerview);
        parentRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        parentRecycler.setHasFixedSize(true);



        int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                MY_PERMISSIONS_REQUEST_READ_CONTACTS);
    }
    @Override
    protected void onResume() {
        super.onResume();

        analyzer = new OutputAnalyzer(this);

        start= findViewById(R.id.start);
        TextureView cameraTextureView = findViewById(R.id.textureView2);
       waveLoadingView=findViewById(R.id.waveloadingview);

        SurfaceTexture previewSurfaceTexture = cameraTextureView.getSurfaceTexture();

        start.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View pView) {
                if (previewSurfaceTexture != null) {
                    // this first appears when we close the application and switch back - TextureView isn't quite ready at the first onResume.
                    Surface previewSurface = new Surface(previewSurfaceTexture);


                    cameraService.start(previewSurface);
                    Bitmap currentBitmap = cameraTextureView.getBitmap();
                    int pixelCount = cameraTextureView.getWidth() * cameraTextureView.getHeight();
                    int measurement = 0;
                    int[] pixels = new int[pixelCount];
                    currentBitmap.getPixels(pixels, 0, cameraTextureView.getWidth(), 0, 0, cameraTextureView.getWidth(), cameraTextureView.getHeight());

                    // extract the red component
                    // https://developer.android.com/reference/android/graphics/Color.html#decoding
                    for (int pixelIndex = 0; pixelIndex < pixelCount; pixelIndex++) {
                        measurement += (pixels[pixelIndex] >> 16) & 0xff;
                    }

                    if (measurement>200) {
                        start.setAlpha(0.0f);
                        analyzer.measurePulse(cameraTextureView, cameraService);
//                    pulseView.startPulse();
                        waveLoadingView.startAnimation();
                    }

                }
                isSpeakButtonLongPressed = true;
                return false;
            }


        });
        start.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View pView, MotionEvent pEvent) {
                pView.onTouchEvent(pEvent);
                if (pEvent.getAction() == MotionEvent.ACTION_UP) {
                    // We're only interested in anything if our speak button is currently pressed.
                    if (isSpeakButtonLongPressed) {
                        // Do something when the button is released.
                        SurfaceTexture previewSurfaceTexture = cameraTextureView.getSurfaceTexture();
                        previewSurfaceTexture.releaseTexImage();
                        start.setAlpha(1.0f);
                        onPause();
                        isSpeakButtonLongPressed = false;
                    }
                }
                return true;
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();

        cameraService.stop();
        if (analyzer != null) analyzer.stop();
        analyzer = new OutputAnalyzer(this);


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