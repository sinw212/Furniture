package com.example.firstvision;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //renewButton을 눌렀을 때에 대한 이벤트 처리
        final EditText heightInput = (EditText) findViewById(R.id.heightInput);//EditText 객체 생성, 키 입력받은 값을 저장
        Button renewButton = (Button) findViewById(R.id.renewButton);//renewButton은 heightInput값을(키) activity_sub로 념겨준다.
        Button reuseButton = (Button) findViewById(R.id.reuseButton);//reuseButton은 이 전에 사용한 값을 activity_sub로 넘겨준다.

        renewButton.setOnClickListener(new View.OnClickListener(){//이벤트 처리
            @Override
            public void onClick(View v) {
                int height = Integer.parseInt(heightInput.getText().toString());
                Intent intent = new Intent(getApplicationContext(), SubActivity.class);//새로운 창을 열기 위해 사용되는 객체
                intent.putExtra("heightInput", height);//키를 보냄 왜 애러가 나는건지?
                startActivity(intent);
            }
        });

        //reuseButton을 눌렀을 때에 대한 이벤트 처리
        reuseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int height = Integer.parseInt(heightInput.getText().toString());
                Intent intent = new Intent(getApplicationContext(), SubActivity.class);//새로운 창을 열기 위해 사용되는 객체
                intent.putExtra("heightInput", height);//키를 보냄 비워둔 곳에 이전의 키 값 들어가면 됨./////////////////////////////바꿔야함!!!!!
                //뭐를 보내야 하지?
                startActivity(intent);
            }
        });

        //카메라
        /*Button cameraButton = null;//사진 찍는 버튼

        @Override
        protected void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sub);

            setup();
        }

        private void setup()
        {
            cameraButton = (Button)findViewById(R.id.cameraButton);

            //카메라를 실행시키기 위한 두 줄
            cameraButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivity(intent);
                }
            });
        }*/

    }
}
