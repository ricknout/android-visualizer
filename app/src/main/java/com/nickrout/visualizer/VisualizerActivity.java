package com.nickrout.visualizer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.audiofx.Visualizer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.nickrout.visualizer.util.PermissionUtil;

public class VisualizerActivity extends AppCompatActivity {

    private VisualizerView mVisualizerView;
    private Visualizer mVisualizer;
    private Button mPermissionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizer);
        mVisualizerView = (VisualizerView) findViewById(R.id.view_visualizer);
        mPermissionButton = (Button) findViewById(R.id.button_permission);
        if (PermissionUtil.hasPermission(this, Manifest.permission.RECORD_AUDIO)) {
            mPermissionButton.setVisibility(View.GONE);
            initAudio();
        } else {
            mPermissionButton.setVisibility(View.VISIBLE);
            mPermissionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PermissionUtil.requestPermission(
                            VisualizerActivity.this, Manifest.permission.RECORD_AUDIO,
                            PermissionUtil.PERMISSION_RESULT_RECORD_AUDIO);
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionUtil.PERMISSION_RESULT_RECORD_AUDIO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mPermissionButton.setVisibility(View.GONE);
                    initAudio();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVisualizer == null) {
            return;
        }
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
