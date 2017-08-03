package cn.xiong.android_h5.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/14.
 */

public class News implements Serializable{

    private String title;
    private String date;
    private String url;
    private String category;
    private String thumbnail_pic_s;
    private String thumbnail_pic_s02;

    public News() {
    }

    public News(String title, String date, String url, String category, String thumbnail_pic_s,String thumbnail_pic_s02) {
        this.title = title;
        this.date = date;
        this.url = url;
        this.category = category;
        this.thumbnail_pic_s = thumbnail_pic_s;
        this.thumbnail_pic_s02 = thumbnail_pic_s02;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getThumbnail_pic_s() {
        return thumbnail_pic_s;
    }

    public void setThumbnail_pic_s(String thumbnail_pic_s) {
        this.thumbnail_pic_s = thumbnail_pic_s;
    }

    public String getThumbnail_pic_s02() {
        return thumbnail_pic_s02;
    }

    public void setThumbnail_pic_s02(String thumbnail_pic_s02) {
        this.thumbnail_pic_s02 = thumbnail_pic_s02;
    }
}
