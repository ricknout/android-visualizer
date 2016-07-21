package com.nickrout.visualizer;

import android.media.AudioManager;
import android.media.audiofx.Visualizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class VisualizerActivity extends AppCompatActivity {

    VisualizerView mVisualizerView;
    private Visualizer mVisualizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizer);
        mVisualizerView = (VisualizerView) findViewById(R.id.view_visualizer);
        initAudio();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVisualizer.setEnabled(false);
        mVisualizer.release();
    }

    private void initAudio() {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setupVisualizerFxAndUi();
        mVisualizer.setEnabled(true);
    }

    private void setupVisualizerFxAndUi() {
        // Create the Visualizer object and attach it to our media player.
        mVisualizer = new Visualizer(0); // Using system audio session ID
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        mVisualizer.setDataCaptureListener(
                new Visualizer.OnDataCaptureListener() {
                    public void onWaveFormDataCapture(
                            Visualizer visualizer,
                            byte[] bytes,
                            int samplingRate) {
                        mVisualizerView.updateVisualizer(bytes);
                    }

                    public void onFftDataCapture(
                            Visualizer visualizer,
                            byte[] bytes,
                            int samplingRate) {
                        // Do nothing for now
                    }
                }, Visualizer.getMaxCaptureRate() / 2, true, false);
    }
}
