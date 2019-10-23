package com.example.vrfa;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    private final static int RESULT_PERMISSIONS = 100;
    public static Bitmap shareBitmap;

    private CameraPreview surfaceView;
    private static Camera mainCamera;
    private SurfaceHolder holder;
    public static MainActivity getInstance;

    int i = 0;
    ImageView imageV1 = null;
    ImageView imageV2 = null;
    LinearLayout shareLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);

        Button reuseButton = (Button) findViewById(R.id.reuseButton);
        Button renewButton = (Button) findViewById(R.id.renewButton);

        shareLayout = (LinearLayout)findViewById(R.id.shareLayout);

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

/*

                try{
                    File storage = getCacheDir();
                    File file = new File(storage,"test.png");
                    FileOutputStream fos = openFileOutput("test.png" , 0);
                    bm.compress(Bitmap.CompressFormat.PNG, 100 , fos);
                    fos.flush();
                    fos.close();

                    Toast.makeText(MainActivity.this, "file ok", Toast.LENGTH_SHORT).show();
                 } catch(Exception e) { Toast.makeText(MainActivity.this, "file error", Toast.LENGTH_SHORT).show();}
*/

                View viewLocat = new MyView(getApplicationContext());

                setContentView(viewLocat);

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

    protected class MyView extends View {
        Paint mPaint;
        Bitmap bm;

        public MyView(Context context) {
            super(context);
            mPaint = new Paint();
            capture();

           /* try {
                File storage = getCacheDir();
                bm = BitmapFactory.decodeFile(storage.toString());
                Toast.makeText(getApplicationContext(), "load ok", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "load error", Toast.LENGTH_SHORT).show();
            }*/

        }

        @Override
        public void onDraw(Canvas canvas) {
            //뷰의 배경 지정
            //canvas.drawBitmap(bm, 0,0, null);
            mPaint.setColor(Color.RED);
            mPaint.setStrokeWidth(30f);
        }


        @Override
        public boolean onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                float x = event.getX();
                float y = event.getY();

                String msg = "좌표 : " + x + " / " + y;

                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();

                return true;
            }

            return false;
        }



    }
    public void capture(){

        Bitmap overlay=Bitmap.createBitmap(shareBitmap.getWidth(),shareBitmap.getHeight(),shareBitmap.getConfig());
        Canvas canvas=new Canvas(overlay);
        /*canvas.drawBitmap(shareBitmap, 0,0, null);

        shareLayout.buildDrawingCache();
        Bitmap bm=shareLayout.getDrawingCache();
        canvas.drawBitmap(bm,0,0,null);*/

        try{
            File storage = getCacheDir();
            File file = new File(storage,"test.png");
            FileOutputStream fos = openFileOutput("test.png" , 0);
            overlay.compress(Bitmap.CompressFormat.PNG, 100 , fos);
            fos.flush();
            fos.close();

            Toast.makeText(MainActivity.this, "file ok", Toast.LENGTH_SHORT).show();
        } catch(Exception e) { Toast.makeText(MainActivity.this, "file error", Toast.LENGTH_SHORT).show();}


    }

}