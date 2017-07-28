package cn.xiong.android_h5.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/7/14.
 */

public class Result {

    private String stat;
    private List<News> data;

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public List<News> getNewses() {
        return data;
    }

    public void setNewses(List<News> newses) {
        this.data = newses;
    }
}
