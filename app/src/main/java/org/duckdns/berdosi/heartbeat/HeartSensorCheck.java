package org.duckdns.berdosi.heartbeat;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class HeartSensorCheck {
    private Context ctx;

    public HeartSensorCheck(Context ctx) {
        this.ctx = ctx;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean checkHeartSensor() {
        SensorManager sensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        return sensorManager.getDefaultSensor(Sensor.TYPE_HEART_BEAT) != null;
    }
}
