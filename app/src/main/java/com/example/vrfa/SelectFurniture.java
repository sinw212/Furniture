package com.example.vrfa;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SelectFurniture extends Activity {
    Button btn_select, btn_gosite;
    TextView text_furniture, user_height, first_point, second_point, real_width, real_height;
    String UserHeight;
    int FirstPointX, SecondPointX, FirstPointY, SecondPointY, SampleHeight;
    int width, height;
    int int_UserHeight;
    String Furniture;
    final CharSequence[] furnitures = {"침대", "소파", "옷장", "화장대", "의자", "식탁", "서랍장", "책상", "수납장", "냉장고", "책장"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_furniture);

        btn_select = findViewById(R.id.SelectButton);
        text_furniture = findViewById(R.id.text_furniture);
        user_height = findViewById(R.id.UserHeight);
        first_point = findViewById(R.id.FirstPoint);
        second_point = findViewById(R.id.SecondPoint);
        real_width = findViewById(R.id.RealWidth);
        real_height = findViewById(R.id.RealHeight);
        btn_gosite = findViewById(R.id.GoSite_Button);

        //MainActivity의 인텐트를 받아서 text값을 저장
        Intent intent = getIntent();
        UserHeight = intent.getStringExtra("user_height");
        FirstPointX = intent.getIntExtra("firstPoint_X", 0);
        SecondPointX = intent.getIntExtra("secondPoint_X", 0);
        FirstPointY = intent.getIntExtra("firstPoint_Y", 0);
        SecondPointY = intent.getIntExtra("secondPoint_Y", 0);
        SampleHeight = intent.getIntExtra("sample_height", 0);

        Log.d("진입22userheight", UserHeight);
        Log.d("진입22firstpointX", String.valueOf(FirstPointX));
        Log.d("진입22secondpointX", String.valueOf(SecondPointX));
        Log.d("진입22firpointY", String.valueOf(FirstPointY));
        Log.d("진입22secondPointY", String.valueOf(SecondPointY));
        Log.d("진입22sampleheight", String.valueOf(SampleHeight));

        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SelectFurniture.this);
                builder.setTitle("가구 품목 선택")
                        .setNegativeButton("취소", null)
                        .setItems(furnitures,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(getApplicationContext(), furnitures[id] + "을 선택하였습니다.", Toast.LENGTH_SHORT).show();
                                text_furniture.setText(">>> " + furnitures[id]);
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        user_height.setText(UserHeight + "cm");
        first_point.setText("(" + FirstPointX + ", " + FirstPointY + ")");
        second_point.setText("(" + SecondPointX + ", " + SecondPointY + ")");

        Calc_ratio(FirstPointX, FirstPointY, SecondPointX, SecondPointY);
        int_UserHeight = Integer.parseInt(UserHeight);
        real_width.setText(((width*int_UserHeight)/SampleHeight) + "cm");
        real_height.setText(((height*int_UserHeight)/SampleHeight) + "cm");

        btn_gosite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Furniture = String.valueOf(text_furniture.getText());
                if (Furniture.equals("") || Furniture == null)
                    Toast.makeText(SelectFurniture.this, "구매하고자 하는 가구 품목을 선택해주세요.", Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent(getApplicationContext(), WebCrawling.class);
                    intent.putExtra("FurnitureName", Furniture);
                    startActivity(intent);
                }
            }
        });
    }

    public void Calc_ratio(int x1, int y1, int x2, int y2) {
        if (x1 < x2)
            width = x2 - x1;
        else
            width = x1 - x2;

        if (y1 < y2)
            height = y2 - y1;
        else
            height = y1 - y2;
    }
}