package com.example.vrfa;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private final static int RESULT_PERMISSIONS = 100;

    private CameraPreview surfaceView;
    private static Camera mainCamera;
    private SurfaceHolder holder;
    public static MainActivity getInstance;

    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private String imageFilePath;
    private Uri photoUri;

    int i = 0;
    ImageView imageV1 = null;
    ImageView imageV2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);

        Button reuseButton = (Button) findViewById(R.id.reuseButton);
        Button renewButton = (Button) findViewById(R.id.renewButton);

        reuseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissionCamera();
                    }
                    else {
                        //카메라 실행
                        setInit();
                    }
                } else {
                    //카메라 실행
                    setInit();
                }
            }
        });

        renewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissionCamera();
                        }
                        else {
                            //카메라 실행
                            setInit();
                        }
                    } else {
                        //카메라 실행
                        setInit();
                    }
            }
        });
    }

    public void requestPermissionCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    RESULT_PERMISSIONS);
        }
        else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    RESULT_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (RESULT_PERMISSIONS == requestCode) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한 허가시
                setInit();
            } else {
                // 권한 거부시 어플 종료
                Toast.makeText(getApplicationContext(), "권한이 거부됨", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public static Camera getCamera() {
        return mainCamera;
    }

    private void setInit() {
        getInstance = this;
        // 카메라 객체를 R.layout.activity_main의 레이아웃에 선언한 SurfaceView에서 먼저 정의해야 함으로 setContentView 보다 먼저 정의한다.
        mainCamera = Camera.open();

        setContentView(R.layout.camera);

        ImageButton button_capture = (ImageButton)findViewById(R.id.button_capture);
        ImageButton button_switch = (ImageButton)findViewById(R.id.button_switch);
        imageV1 = (ImageView)findViewById(R.id.ImageV1);
        imageV2 = (ImageView)findViewById(R.id.ImageV2);

        imageV1.setVisibility(View.VISIBLE);
        imageV2.setVisibility(View.INVISIBLE);

        button_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bm = Bitmap.createBitmap(surfaceView.drawBitmap());

                if(bm != null) {
                    try {
                        String path = Environment.getExternalStorageDirectory().toString();
                        OutputStream fOut = null;
                        File file = new File(path + "/", "screentest.jpg");
                        fOut = new FileOutputStream(file);

                        bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                        fOut.flush();
                        fOut.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        button_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = 1 - i;

                if(i == 0) {
                    imageV1.setVisibility(View.VISIBLE);
                    imageV2.setVisibility(View.INVISIBLE);
                }
                else {
                    imageV1.setVisibility(View.INVISIBLE);
                    imageV2.setVisibility(View.VISIBLE);
                }
            }
        });

        // SurfaceView를 상속받은 레이아웃을 정의한다.
        surfaceView = (CameraPreview) findViewById(R.id.preview);

        // SurfaceView 정의 - holder와 Callback을 정의한다.
        holder = surfaceView.getHolder();
        holder.addCallback(surfaceView);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
}