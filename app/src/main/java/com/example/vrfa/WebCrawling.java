/*package com.example.vrfa;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class WebCrawling extends Activity {
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
            // progressDialog = new ProgressDialog(MainActivity);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("잠시 기다려 주세요.");
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                //선택된 가구 품목의 구매 사이트 연동
                Document doc = Jsoup.connect("https://search.shopping.naver.com/search/all.nhn?query=%EC%86%8C%ED%8C%8C&frm=NVSCVUI").get();
                Elements mElementDataSize = doc.select("div[class=search_list thumb]").select("ul");
                int mElementSize = mElementDataSize.size();

                for(Element elem : mElementDataSize){
                    String f_title = elem.select("ul div[class=tit] a").text(); //사이트 제목
                    String f_price = elem.select("ul div[class=num_price_reload]").text(); //가구 가격
                    String f_imgUrl = elem.select("ul div[class=tit] a img").attr("data-original"); //가구 이미지
                    String f_link = elem.select("ul div[class=tit] a").attr("href"); //실 구매 사이트 URL

                    list.add(new ItemObject(f_title, f_imgUrl, f_price, f_link));
                }



            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //ArraList를 인자로 해서 어답터와 연결한다.
            ItemObject.MyAdapter myAdapter = new ItemObject.MyAdapter(list);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(myAdapter);

            progressDialog.dismiss();
        }
    }
}


public class ItemObject {
    private String title;
    private String img_url;
    private String detail_link;
    private String release;
    private String director;


    public ItemObject(String title,  String link, String price, String url){
        this.title = title;
        this.img_url = url;
        this.detail_link = link;
        this.release = release;
        this.director = director;
    }


    public String getTitle() {
        return title;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getDetail_link() {
        return detail_link;
    }

    public String getRelease() {
        return release;
    }

    public String getDirector() {
        return director;
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        //데이터 배열 선언
        private ArrayList<ItemObject> mList;

        public  class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView imageView_img;
            private TextView textView_title, textView_release, texView_director;

            public ViewHolder(View itemView) {
                super(itemView);

                imageView_img = (ImageView) itemView.findViewById(R.id.imageView_img);
                textView_title = (TextView) itemView.findViewById(R.id.textView_title);
                textView_release = (TextView) itemView.findViewById(R.id.textView_release);
                texView_director = (TextView) itemView.findViewById(R.id.textView_director);
            }
        }

        //생성자
        public MyAdapter(ArrayList<ItemObject> list) {
            this.mList = list;
        }

        @NonNull
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_data, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {

            holder.textView_title.setText(String.valueOf(mList.get(position).getTitle()));
            holder.textView_release.setText(String.valueOf(mList.get(position).getRelease()));
            holder.texView_director.setText(String.valueOf(mList.get(position).getDirector()));
            //다 해줬는데도 GlideApp 에러가 나면 rebuild project를 해주자.
            GlideApp.with(holder.itemView).load(mList.get(position).getImg_url())
                    .override(300,400)
                    .into(holder.imageView_img);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }
}*/