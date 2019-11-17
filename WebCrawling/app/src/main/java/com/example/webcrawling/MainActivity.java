package com.example.webcrawling;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<ItemObject> list = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        //AsyncTask 작동시킴(파싱)
        new Description().execute();
    }


    private class Description extends AsyncTask<Void, Void, Void> {

        //진행바표시
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //진행다일로그 시작
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("잠시 기다려 주세요.");
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                //선택된 가구 품목의 구매 사이트 연동
                Document doc = Jsoup.connect("https://search.shopping.naver.com/search/all.nhn?query=%EC%86%8C%ED%8C%8C&frm=NVSCVUI").get();
                Elements mElementData1 = doc.select("div[class=img_area]");
                int mElementData1Size = mElementData1.size();
                String imgUrl[] = new String[mElementData1Size];
                int i = 0;


                for(Element elem : mElementData1){
                    String f_imgUrl = elem.select("img[class=_productLazyImg]").attr("data-original"); //가구 이미지

                    imgUrl[i] = f_imgUrl;
                    i++;

                }

                Elements mElementData2 = doc.select("div[class=info]");
                //int mElementData2Size = mElementData2.size();
                i = 0;

                for(Element elem : mElementData2){
                    String f_title = elem.select("div[class=tit] a").text(); //사이트 제목
                    String f_price = elem.select("span[class=price] em").text(); //가구 가격
                    String f_link = elem.select("div[class=tit] a").attr("href"); //실 구매 사이트 URL

                    list.add(new ItemObject(f_title, imgUrl[i], f_price, f_link));
                    i++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //ArraList를 인자로 해서 어답터와 연결한다.
            MyAdapter myAdapter = new MyAdapter(list);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(myAdapter);

            progressDialog.dismiss();
        }
    }


}






