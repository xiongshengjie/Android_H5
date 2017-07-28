package cn.xiong.android_h5.entity;

/**
 * Created by Administrator on 2017/7/12.
 */

public class PicItem {
    String name;
    int id;

    public PicItem() {
    }

    public PicItem(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
