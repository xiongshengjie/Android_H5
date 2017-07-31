package cn.xiong.android_h5.activity;

/**
 * Created by Administrator on 2017/7/31 0031.
 */

public interface DownloadListener {

    void onProgress(int progress);

    void onSuccess();

    void onFailed();

    void onPaused();

    void onCanceled();

}
