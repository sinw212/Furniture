package com.example.vrfa;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class WebCrawling extends Activity {
    private RecyclerView recyclerView;
    private ArrayList<CrawlingData> list = new ArrayList();

    String FurnitureName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_crawling);

        Intent intent = getIntent();
        FurnitureName = intent.getStringExtra("FurnitureName");

        recyclerView = findViewById(R.id.recyclerview);

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
                Log.d("진입사이트주소", "https://search.shopping.naver.com/search/all.nhn?query="+FurnitureName+"&frm=NVSCVUI");
                Document doc = Jsoup.connect("https://search.shopping.naver.com/search/all.nhn?query="+FurnitureName+"&frm=NVSCVUI").get();
//                Elements mElementData1 = doc.select("div[class=thumbnail_thumb_wrap__1pEkS _wrapper]");
                Elements mElementData1 = doc.select("div.thumbnail_thumb_wrap__1pEkS _wrapper img");
                int mElementData1Size = mElementData1.size();
                String imgUrl[] = new String[mElementData1Size];
                int i = 0;

                for(Element elem : mElementData1){
//                    <ul class="ah_1" data-list''
//                    <li clas="ah_item".. >
//                    <a href = "hts;dlkfjl;">
//
//                    doc.select("ul.ah_l li.ah_item a");
//                    url = e.attr("href")
//                    num = e.select("span.ah_r").text();
//                    String f_imgUrl = elem.select("img[class=_productLazyImg]").attr("data-original"); //가구 이미지
                    String f_imgUrl = elem.attr("src"); //가구 이미지

                    imgUrl[i] = f_imgUrl;
                    i++;
                }

//                Elements mElementData2 = doc.select("div[class=basicList_title__3P9Q7]");
                Elements mElementData2 = doc.select("div.basicList_info_area__17Xyo");
                //int mElementData2Size = mElementData2.size();
                i = 0;

                for(Element elem : mElementData2){
                    String f_title = elem.select("div.basicList_title__3P9Q7 a").attr("title");
//                    String f_title = elem.select("div[class=tit] a").text(); //사이트 제목
                    String f_price = elem.select("div.basicList_price_area__1UXXR strong.basicList_price__2r23_ span.price_num__2WUXn").text();
//                    String f_price = elem.select("span[class=price] em").text(); //가구 가격
                    String f_link = elem.select("div.basicList_title__3P9Q7 a").attr("href");
//                    String f_link = elem.select("div[class=tit] a").attr("href"); //실 구매 사이트 URL

                    list.add(new CrawlingData(f_title, imgUrl[i], f_price, f_link));
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
            CrawlingAdapter crawlingAdapter = new CrawlingAdapter(list);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(crawlingAdapter);

            progressDialog.dismiss();
        }
    }
}