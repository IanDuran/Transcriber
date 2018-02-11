package com.duran.ian.transcriber;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private final int REQUEST_CAMERA_PERMISSION = 1001;
    private TextView textDisplay;
    private SurfaceView cameraView;
    private CameraSource cameraSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.cameraView = (SurfaceView) findViewById(R.id.camera_view);
        this.textDisplay = (TextView) findViewById(R.id.text_display);

        TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if (!recognizer.isOperational()){
            Log.w("MainActivity", "Detector dependencies are not available");
        }
        else{
            this.cameraSource = new CameraSource.Builder(getApplicationContext(), recognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();

            this.cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[] {Manifest.permission.CAMERA},
                                    REQUEST_CAMERA_PERMISSION);
                            return;
                        }
                        cameraSource.start(cameraView.getHolder());
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    cameraSource.stop();
                }
            });

            recognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if(items.size() > 0){
                        textDisplay.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder builder = new StringBuilder();
                                for (int i = 0; i < items.size(); i++) {
                                    builder.append(items.valueAt(i).getValue());
                                    builder.append("\n");
                                }
                                textDisplay.setText(builder.toString());
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_CAMERA_PERMISSION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                        return;

                    try{
                        cameraSource.start(cameraView.getHolder());
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }
            break;
        }
    }
}
