package com.example.vrfa;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CrawlingAdapter extends RecyclerView.Adapter<CrawlingAdapter.ViewHolder> {

    //데이터 배열 선언
    private ArrayList<CrawlingData> mList;

    public  class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView_img;
        private TextView textView_title, textView_price, texView_link;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView_img = (ImageView) itemView.findViewById(R.id.imageView_img);
            textView_title = (TextView) itemView.findViewById(R.id.textView_title);
            textView_price = (TextView) itemView.findViewById(R.id.textView_price);
            texView_link = (TextView) itemView.findViewById(R.id.textView_link);
        }
    }

    //생성자
    public CrawlingAdapter(ArrayList<CrawlingData> list) {
        this.mList = list;
    }

    @NonNull
    @Override
    public CrawlingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CrawlingAdapter.ViewHolder holder, int position) {

        holder.textView_title.setText(String.valueOf(mList.get(position).getTitle()));
        holder.textView_price.setText(String.valueOf(mList.get(position).getPrice()));
        holder.texView_link.setText(String.valueOf(mList.get(position).getLink()));
        //다 해줬는데도 GlideApp 에러가 나면 rebuild project를 해주자.
        Glide.with(holder.itemView).load(mList.get(position).getImg_url())
                .override(300,400)
                .into(holder.imageView_img);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
