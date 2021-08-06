package com.example.vrfa;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomPopup extends Activity {
    ImageView img_dialog;
    Button btn_cancel, btn_previous, btn_next;
    TextView txt_pagenum;

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_popup);

        img_dialog = findViewById(R.id.img_dialog);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_previous = findViewById(R.id.btn_previous);
        btn_next = findViewById(R.id.btn_next);
        txt_pagenum = findViewById(R.id.txt_pagenum);

        ChangePopup(0);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i--;
                ChangePopup(i);
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i != 5) {
                    i++;
                    ChangePopup(i);
                } else {
                    finish();
                }
            }
        });
    }

    public void ChangePopup(int num) {
        switch(num) {
            case 0:
                img_dialog.setImageResource(R.drawable.popup0);
                btn_previous.setVisibility(View.INVISIBLE);
                txt_pagenum.setText("");
                break;
            case 1:
                img_dialog.setImageResource(R.drawable.popup1);
                btn_previous.setVisibility(View.VISIBLE);
                txt_pagenum.setText("1/5");
                break;
            case 2:
                img_dialog.setImageResource(R.drawable.popup2);
                txt_pagenum.setText("2/5");
                break;
            case 3:
                img_dialog.setImageResource(R.drawable.popup3);
                txt_pagenum.setText("3/5");
                break;
            case 4:
                img_dialog.setImageResource(R.drawable.popup4);
                txt_pagenum.setText("4/5");
                btn_next.setText("다음");
                break;
            case 5:
                img_dialog.setImageResource(R.drawable.popup5);
                txt_pagenum.setText("5/5");
                btn_next.setText("확인");
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}