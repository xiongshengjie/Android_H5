package cn.xiong.android_h5.entity;

/**
 * Created by Administrator on 2017/7/13.
 */

public class Msg {
    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SEND = 1;

    private String text;
    private int type;
    private String code;
    private String url;

    public Msg(String content, int type) {
        this.text = content;
        this.type = type;
    }

    public static int getTypeReceived() {
        return TYPE_RECEIVED;
    }

    public static int getTypeSend() {
        return TYPE_SEND;
    }

    public String getText() {
        return text;
    }

    public void setText(String content) {
        this.text = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
