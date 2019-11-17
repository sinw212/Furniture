package com.example.webcrawling;

//아이템 클래스
public class ItemObject {
    private String title;
    private String img_url;
    private String link;
    private String price;


    public ItemObject(String title, String url, String price, String link) {
        this.title = title;
        this.price = price;
        this.img_url = url;
        this.link = link;
    }


    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getLink() {
        return link;
    }

}

