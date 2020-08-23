package org.duckdns.berdosi.heartbeat;

import android.Manifest;
import android.graphics.SurfaceTexture;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;


import android.view.MotionEvent;
import android.view.TextureView;
import android.view.Surface;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import com.gigamole.library.PulseView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import me.itangqi.waveloadingview.WaveLoadingView;

public class MainActivity extends AppCompatActivity {
    private final CameraService cameraService = new CameraService(this);
    private OutputAnalyzer analyzer;
    private Toolbar toolbar;
    private TextView start;
    private boolean isSpeakButtonLongPressed = false;
    WaveLoadingView waveLoadingView;


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
                    analyzer.measurePulse(cameraTextureView, cameraService);
//                    pulseView.startPulse();
                    waveLoadingView.startAnimation();

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                MY_PERMISSIONS_REQUEST_READ_CONTACTS);
    }

}