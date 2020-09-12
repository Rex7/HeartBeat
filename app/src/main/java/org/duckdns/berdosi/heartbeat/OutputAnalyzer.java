package org.duckdns.berdosi.heartbeat;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

import me.itangqi.waveloadingview.WaveLoadingView;

class OutputAnalyzer {
    private final Activity activity;
       private float pulse;
    private MeasureStore store;

    private final int measurementInterval = 45;
    private final int measurementLength = 15000; // ensure the number of data points is the power of two
    private final int clipLength = 3500;
    private String currentValue;
    private int detectedValleys = 0;
    private int ticksPassed = 0;
    private int calc;
    private final CopyOnWriteArrayList<Long> valleys = new CopyOnWriteArrayList<>();

    private CountDownTimer timer;


    OutputAnalyzer(Activity activity) {
        this.activity = activity;


    }

    private boolean detectValley() {
        final int valleyDetectionWindowSize = 13;
        CopyOnWriteArrayList<Measurement<Integer>> subList = store.getLastStdValues(valleyDetectionWindowSize);
        if (subList.size() < valleyDetectionWindowSize) {
            return false;
        } else {
            Integer referenceValue = subList.get((int) Math.ceil(valleyDetectionWindowSize / 2)).measurement;

            for (Measurement<Integer> measurement : subList) {
                if (measurement.measurement < referenceValue) return false;
            }

            // filter out consecutive measurements due to too high measurement rate
            return (!subList.get((int) Math.ceil(valleyDetectionWindowSize / 2)).measurement.equals(
                    subList.get((int) Math.ceil(valleyDetectionWindowSize / 2) - 1).measurement));
        }
    }

    void measurePulse(TextureView textureView, CameraService cameraService) {

        // 20 times a second, get the amount of red on the picture.
        // detect local minimums, calculate pulse.

        store = new MeasureStore();

        detectedValleys = 0;

        timer = new CountDownTimer(measurementLength, measurementInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                // skip the first measurements, which are broken by exposure metering
                if (clipLength > (++ticksPassed * measurementInterval)) return;

                Thread thread = new Thread(() -> {
                    Bitmap currentBitmap = textureView.getBitmap();
                    int pixelCount = textureView.getWidth() * textureView.getHeight();
                    int measurement = 0;

                    int[] pixels = new int[pixelCount];

                    currentBitmap.getPixels(pixels, 0, textureView.getWidth(), 0, 0, textureView.getWidth(), textureView.getHeight());

                    // extract the red component
                    // https://developer.android.com/reference/android/graphics/Color.html#decoding
                    for (int pixelIndex = 0; pixelIndex < pixelCount; pixelIndex++) {
                        measurement += (pixels[pixelIndex] >> 16) & 0xff;
                    }
                    // max int is 2^31 (2147483647) , so width and height can be at most 2^11,
                    // as 2^8 * 2^11 * 2^11 = 2^30, just below the limit



                    store.add(measurement);

                    if (detectValley()) {
                        detectedValleys = detectedValleys + 1;
                        valleys.add(store.getLastTimestamp().getTime());
                        // in 13 seconds (13000 milliseconds), I expect 15 valleys. that would be a pulse of 15 / 130000 * 60 * 1000 = 69

                        currentValue = String.format(
                                Locale.getDefault(),
                                activity.getResources().getQuantityString(R.plurals.measurement_output_template, detectedValleys),
                                (valleys.size() == 1)
                                        ? (60f * (detectedValleys) / (Math.max(1, (measurementLength - millisUntilFinished - clipLength) / 1000f)))
                                        : (60f * (detectedValleys - 1) / (Math.max(1, (valleys.get(valleys.size() - 1) - valleys.get(0)) / 1000f))),
                                detectedValleys,
                                1f * (measurementLength - millisUntilFinished - clipLength) / 1000f);

                        pulse=60f * (detectedValleys - 1) / (Math.max(1, (valleys.get(valleys.size() - 1) - valleys.get(0)) / 1000f));
                        ((TextView) activity.findViewById(R.id.textView)).setText(currentValue);
                        activity.runOnUiThread(() -> {
                            //Your code

                            ((WaveLoadingView) activity.findViewById(R.id.waveloadingview)).setProgressValue((int) pulse);
                        ((WaveLoadingView) activity.findViewById(R.id.waveloadingview)).setCenterTitle(String.valueOf(pulse));

                        });


                    }

//
                    // draw the chart on a separate thread.
//                    Thread chartDrawerThread = new Thread(() -> chartDrawer.draw(store.getStdValues()));
//                    chartDrawerThread.start();
                });
                thread.start();
            }

            @Override
            public void onFinish() {
                CopyOnWriteArrayList<Measurement<Float>> stdValues = store.getStdValues();

                // clip the interval to the first till the last one - on this interval, there were detectedValleys - 1 periods
                String currentValue = String.format(
                        Locale.getDefault(),
                        activity.getResources().getQuantityString(R.plurals.measurement_output_template, detectedValleys - 1),
                        60f * (detectedValleys - 1) / (Math.max(1, (valleys.get(valleys.size() - 1) - valleys.get(0)) / 1000f)),
                        detectedValleys - 1,
                        1f * (valleys.get(valleys.size() - 1) - valleys.get(0)) / 1000f);
                pulse=60f * (detectedValleys - 1) / (Math.max(1, (valleys.get(valleys.size() - 1) - valleys.get(0)) / 1000f));

                ((TextView) activity.findViewById(R.id.textView)).setText(currentValue);
                activity.runOnUiThread(() -> {
                    //Your code
                    ((WaveLoadingView) activity.findViewById(R.id.waveloadingview)).setProgressValue((int) pulse);
                        ((WaveLoadingView) activity.findViewById(R.id.waveloadingview)).setCenterTitle(String.valueOf(pulse));

                });

                StringBuilder returnValueSb = new StringBuilder();
                returnValueSb.append(currentValue);
                returnValueSb.append(activity.getString(R.string.row_separator));
                returnValueSb.append(activity.getString(R.string.raw_values));
                returnValueSb.append(activity.getString(R.string.row_separator));


                for (int stdValueIdx = 0; stdValueIdx < stdValues.size(); stdValueIdx++) {
                    // stdValues.forEach((value) -> { // would require API level 24 instead of 21.
                    Measurement<Float> value = stdValues.get(stdValueIdx);
                    returnValueSb.append(value.timestamp.getTime());
                    returnValueSb.append(activity.getString(R.string.separator));
                    returnValueSb.append(value.measurement);
                    returnValueSb.append(activity.getString(R.string.row_separator));
                }

                // add detected valleys location
                for (long tick : valleys) {
                    returnValueSb.append(tick);
                    returnValueSb.append(activity.getString(R.string.row_separator));
                }



                cameraService.stop();
                ( activity.findViewById(R.id.start)).setVisibility(View.INVISIBLE);
                (activity.findViewById(R.id.waveloadingview)).setVisibility(View.INVISIBLE);
                (activity.findViewById(R.id.showResult)).setVisibility(View.VISIBLE);
                ((TextView) activity.findViewById(R.id.textView)).setText(currentValue);


            }
        };

        timer.start();
    }

    void stop() {
        if (timer != null) {
            timer.cancel();
        }
    }
}
