package org.duckdns.berdosi.heartbeat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ImageView;

public class ResultView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_view);
        ImageView img1;
        ImageView img2;
        img1= (ImageView)findViewById(R.id.img1);
        img2= (ImageView)findViewById(R.id.img2);
    }
}