package com.example.vrfa;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private CameraPreview surfaceView;
    private static Camera mainCamera;
    private SurfaceHolder holder;
    public static MainActivity getInstance;

    Button OkButton;
    EditText edit_height;
    String user_height;
    int sample_height;

    int i = -1;
    ImageView imageV1 = null;
    ImageView imageV2 = null;

    int x, y;
    int pointX[] = new int[2];
    int pointY[] = new int[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit_height = findViewById(R.id.edit_height);
        OkButton = findViewById(R.id.OkButton);

        //팝업(액티비티) 호출
        Intent intent = new Intent(this, CustomPopup.class);
        startActivity(intent);

        OkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_height = String.valueOf(edit_height.getText());
                if (user_height.equals("") || user_height == null)
                    Toast.makeText(MainActivity.this, "키를 입력해주세요.", Toast.LENGTH_SHORT).show();
                else {
                    Log.d("진입user_height", user_height);
                    setInit();
                }
            }
        });
    }

    public static Camera getCamera() {
        return mainCamera;
    }

    private void setInit() {
        getInstance = this;
        // 카메라 객체를 R.layout.activity_main의 레이아웃에 선언한 SurfaceView에서 먼저 정의해야 함으로 setContentView 보다 먼저 정의한다.
        mainCamera = Camera.open();

        setContentView(R.layout.camera);

        ImageButton button_switch = (ImageButton) findViewById(R.id.button_switch);
        imageV1 = (ImageView) findViewById(R.id.ImageV1);
        imageV2 = (ImageView) findViewById(R.id.ImageV2);

        imageV1.setVisibility(View.VISIBLE);
        imageV2.setVisibility(View.INVISIBLE);
        imageV1.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        sample_height = imageV1.getMeasuredWidth();

        // SurfaceView를 상속받은 레이아웃을 정의한다.
        surfaceView = (CameraPreview) findViewById(R.id.preview);

        // SurfaceView 정의 - holder와 Callback을 정의한다.
        holder = surfaceView.getHolder();
        holder.addCallback(surfaceView);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        //사람 모형 바꾸는 버튼
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean ret = false;

        switch(event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                x = (int) event.getX();
                y = (int) event.getY();
                ret = true;
                break;
            case MotionEvent.ACTION_UP:
                i++;
                performClick(x, y, i);
                ret = true;
                break;
            case MotionEvent.ACTION_MOVE:
                ret = true;
                break;
        }
        return ret;
    }

    private void performClick(int x, int y, int count) {
        String pointer_msg = (count + 1) + "번째 값 : " + x + " / " + y;
        Toast.makeText(MainActivity.this, pointer_msg, Toast.LENGTH_SHORT).show();

        pointX[count] = x;
        pointY[count] = y;

        if(count==1) {
            Dialog();
        }
    }

    private void Dialog() {
        //다이얼로그 바디
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("알림").setMessage("두 점을 모두 선택하셨습니다.").setIcon(R.drawable.icon);

        //확인버튼
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Log.d("진입user_height", user_height);
                Log.d("진입firstpointX", String.valueOf(pointX[0]));
                Log.d("진입secondpointX", String.valueOf(pointX[1]));
                Log.d("진입firpointY", String.valueOf(pointY[0]));
                Log.d("진입secondPointY", String.valueOf(pointY[1]));
                Log.d("진입sample_height", String.valueOf(sample_height));

                Intent intent = new Intent(getApplicationContext(), SelectFurniture.class);
                intent.putExtra("user_height", user_height);
                intent.putExtra("firstPoint_X", pointX[0]);
                intent.putExtra("secondPoint_X", pointX[1]);
                intent.putExtra("firstPoint_Y", pointY[0]);
                intent.putExtra("secondPoint_Y", pointY[1]);
                intent.putExtra("sample_height", sample_height);
                startActivity(intent);
            }
        });

        //다시찍기버튼
        builder.setNegativeButton("다시찍기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "'다시찍기' 버튼을 눌렀습니다.", Toast.LENGTH_SHORT).show();
                //점 다시 찍도록 리셋시키기
                i = -1;
            }
        });
        //메인 다이얼로그 생성
        AlertDialog alertDialog = builder.create();
        //다이얼로그 보기
        alertDialog.show();
    }
}