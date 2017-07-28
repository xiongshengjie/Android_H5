package cn.xiong.android_h5.http;

/**
 * Created by Administrator on 2017/7/13.
 */

public interface HttpCallBack {

    void onSuccess(String data);

    void onFail(String message);
}
