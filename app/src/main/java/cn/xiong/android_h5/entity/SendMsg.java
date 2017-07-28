package cn.xiong.android_h5.entity;

/**
 * Created by Administrator on 2017/7/13.
 */

public class SendMsg {

    private String key = "f652a49231a247498599bffc66476efa";

    private String info;

    private String userid="xiongshengjie";

    private String loc;

    public SendMsg(String info, String loc) {
        this.info = info;
        this.loc = loc;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
