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
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private final static int RESULT_PERMISSIONS = 100;

    private CameraPreview surfaceView;
    private static Camera mainCamera;
    private SurfaceHolder holder;
    public static MainActivity getInstance;
    public static Bitmap bm;
    public SimpleDateFormat mformat = new SimpleDateFormat("yyyyMMddHHmmss");
    String filenamePath;

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

        ImageButton button_capture = (ImageButton) findViewById(R.id.button_capture);
        ImageButton button_switch = (ImageButton) findViewById(R.id.button_switch);
        imageV1 = (ImageView) findViewById(R.id.ImageV1);
        imageV2 = (ImageView) findViewById(R.id.ImageV2);

        imageV1.setVisibility(View.VISIBLE);
        imageV2.setVisibility(View.INVISIBLE);

        // SurfaceView를 상속받은 레이아웃을 정의한다.
        surfaceView = (CameraPreview) findViewById(R.id.preview);

        // SurfaceView 정의 - holder와 Callback을 정의한다.
        holder = surfaceView.getHolder();
        holder.addCallback(surfaceView);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        button_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View rootView = getWindow().getDecorView();

                File screenShot = ScreenShot(rootView);
                if(screenShot != null) {
                    //갤러리추가
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(screenShot)));
                }
//                surfaceView.createBitmap(bm);
//
//                filenamePath = mformat.format(System.currentTimeMillis());
//                String fileName = filenamePath;
//
//                //서버로부터 받아온 bitmap을 screentest이름의 jpg로 변환하여 캐시에 저장
//                saveBitmapToJpeg(bm, fileName); //비트맵을 파일로 변환 후 캐시에 저장하는 함수
//
//                ArrayList<String> filenamePath = new ArrayList<>();
//
//                File file = new File(getCacheDir().toString());
//                File files[] = file.listFiles();
//
//                for (File tempFile : files) {
//                    Log.d("MyTag", tempFile.getName());
//                }
//                Log.e("MyTag", "size : " + filenamePath.size());
//
//                if (filenamePath.size() > 0) {
//                    int randomPosition = new Random().nextInt(filenamePath.size());
//
//                    //filenamePath 배열에 있는 파일 경로 중 하나를 랜덤으로 불러오기
//                    String path = getCacheDir() + "/" + filenamePath.get(randomPosition);
//
//                    //파일 경로로부터 비트맵 생성
//                    bm = BitmapFactory.decodeFile(path);
//                }
//                surfaceView.createBitmap(bm);
            }
        });

//                if(bm != null) {
//                    try {
//                        String path = Environment.getExternalStorageDirectory().toString();
//                        OutputStream fOut = null;
//                        File file = new File(path + "/", "screentest.jpg");
//                        fOut = new FileOutputStream(file);
//                        bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
//                        fOut.flush();
//                        fOut.close();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent,0);
//
//                View viewLocat = new MyView(getApplicationContext());

//                setContentView(viewLocat);
//            }
//        });

        button_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = 1 - i;

                if (i == 0) {
                    imageV1.setVisibility(View.VISIBLE);
                    imageV2.setVisibility(View.INVISIBLE);
                } else {
                    imageV1.setVisibility(View.INVISIBLE);
                    imageV2.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private File ScreenShot(View view) {
        view.setDrawingCacheEnabled(true); //화면에 뿌릴때 캐시를 사용하게 한다

        bm = view.getDrawingCache(); //캐시를 비트맵으로 변환

        String filename = "screenshot.png";
        //Pictures폴더 screenshot.png 파일
        File file = new File(Environment.getExternalStorageDirectory()+"/Pictures", filename);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 90, fos); //비트맵을 PNG 파일로 변환
            fos.close();
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }

        view.setDrawingCacheEnabled(false);
        return file;

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        Bitmap bm = (Bitmap)data.getExtras().get("data");
//        imageV1.setImageBitmap(bm);
//    }

//    protected class MyView extends View {
//        Paint mPaint;
//
//        public MyView(Context context) {
//            super(context);
//            mPaint = new Paint();
//        }
//
//        protected void onDraw(Canvas canvas) {
//            //뷰의 배경 지정
//            //canvas.drawBitmap(bm, 0,0,mPaint);
//            surfaceView.drawBitmap();
//            mPaint.setColor(Color.RED);
//            mPaint.setStrokeWidth(3);
//        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);

            float x[] = new float[2];
            float y[] = new float[2];

            for (int i = 0; i < 2; i++) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    x[i] = event.getX();
                    y[i] = event.getY();

                    String msg = (i + 1) + "번째 값을 입력받음 : " + x[i] + " / " + y[i];

                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();

                    return true;
                }
            }
            Dialog();

            return false;
        }

        private void Dialog() {
            //다이얼로그 바디
            AlertDialog.Builder alertdialog = new AlertDialog.Builder(getApplicationContext());
            //다이얼로그 메세지
            alertdialog.setMessage("두 점을 모두 선택하셨습니다.");
            //확인버튼
            alertdialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    Toast.makeText(activity, "'확인'버튼을 눌렀습니다.", Toast.LENGTH_SHORT).show();
                    //Calculate.java 로 넘기기
                    Intent intent = new Intent(getApplicationContext(), Calculate.class);
                    intent.putExtra("data", "put data");
                    startActivityForResult(intent,1);
                }
            });
            //다시찍기버튼
            alertdialog.setNegativeButton("다시찍기", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    Toast.makeText(activity, "'다시찍기' 버튼을 눌렀습니다.", Toast.LENGTH_SHORT).show();
                    //점 다시 찍도록 리셋시키기
                }
            });
            //메인 다이얼로그 생성
            AlertDialog alert = alertdialog.create();
            //아이콘 설정
            alert.setIcon(R.drawable.icon);
            //타이틀
            alert.setTitle("경고");
            //다이얼로그 보기
            alert.show();
        }


    private void saveBitmapToJpeg (Bitmap bm, String name) {
        //내부저장소 캐시 경로 받아오기
        File storage = getCacheDir();

        //저장할 파일 이름
        String fileName = name + ".jpg";

        //storage에 파일 인스턴스 생성
        File tempFile = new File(storage, fileName);

        try {
            //자동으로 빈 파일 생성
            tempFile.createNewFile();
            //파일을 쓸 수 있는 스트림 준비
            FileOutputStream fout = new FileOutputStream(tempFile);
            //compress함수 이용하여 스트림에 비트맵 저장
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fout);
            //스트림 사용 후 닫아주기
            fout.close();
        } catch (FileNotFoundException e) {
            Log.e("MyTag", "FileNotFoundException : " + e.getMessage());
        } catch (IOException e) {
            Log.e("MyTag", "IOException : " + e.getMessage());
        }
    }
}