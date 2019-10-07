package com.example.firstvision;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SubActivity extends AppCompatActivity {

    //@Override
    protected void onCreatge(Bundle bundle) {
        super.onCreate(bundle);//onCreate가 정상적으로 작동할 수 있게 해줌
        setContentView(R.layout.activity_sub);//레이아웃은 activity_sub
        TextView heightInput = (TextView) findViewById(R.id.heightInput);//입력받은 키를(heightInput) 가져옴
        Intent intent = getIntent();//넘어온 값을(heightInput)
    }
}
