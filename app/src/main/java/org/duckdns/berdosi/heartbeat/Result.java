package org.duckdns.berdosi.heartbeat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class Result extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ImageView img1;
        ImageView img2;
        img1=findViewById(R.id.img1);
        img2=findViewById(R.id.img2);
    }
}