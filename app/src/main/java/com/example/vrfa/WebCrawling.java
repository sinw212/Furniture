package com.example.vrfa;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

public class WebCrawling extends Activity {
    private RecyclerView recyclerView;
    private ArrayList<CrawlingData> list = new ArrayList();

    TextView txt_furnitureName;
    String FurnitureName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_crawling);

        Intent intent = getIntent();
        recyclerView = findViewById(R.id.recyclerview);
        txt_furnitureName = findViewById(R.id.txt_furnitureName);

        FurnitureName = intent.getStringExtra("FurnitureName");
        txt_furnitureName.setText("\"" + FurnitureName + "\" 구매 사이트 목록");

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
            progressDialog = new ProgressDialog(WebCrawling.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("잠시 기다려 주세요.");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                //선택된 가구 품목의 구매 사이트 연동
                Document doc = Jsoup.connect("https://search.shopping.naver.com/search/all?query="+FurnitureName+"&frm=NVSCVUI").get();
//                Document doc = Jsoup.connect("https://search.shopping.naver.com/search/all?query=%EC%86%8C%ED%8C%8C&frm=NVSCVUI").get();
                Elements mElementData1 = doc.select("div[class=basicList_inner__eY_mq]");

                for(Element elem : mElementData1){
                    String f_title = elem.select("div[class=basicList_info_area__17Xyo] a").attr("title");
                    String f_imgUrl = elem.select("div[class=basicList_img_area__a3NRA] a img").attr("src");
                    String f_price = elem.select("div[class=basicList_info_area__17Xyo] span[class=price_num__2WUXn]").text();
                    String f_link = elem.select("div[class=basicList_info_area__17Xyo] a").attr("href");

                    Log.d("진입f_title", f_title);
                    Log.d("진입f_imgUrl", f_imgUrl);
                    Log.d("진입f_price", f_price);
                    Log.d("진입f_link", f_link);
                    list.add(new CrawlingData(f_title, f_imgUrl, f_price, f_link));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //ArraList를 인자로 해서 어답터와 연결한다.
            CrawlingAdapter crawlingAdapter = new CrawlingAdapter(list);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(crawlingAdapter);

            progressDialog.dismiss();
        }
    }
}