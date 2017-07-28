package cn.xiong.android_h5.http;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/7/13.
 */

public class AI {

    public static final MediaType Json = MediaType.parse("application/json; charset=utf-8");

    // 做网络请求使用的对象
    private static final OkHttpClient client = new OkHttpClient();

    public static void postData(String url,final HttpCallBack callBack,String json){
        RequestBody body = RequestBody.create(Json,json);
        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        new Thread(){
            public void run(){
                Response response = null;

                try {
                    response = client.newCall(request).execute();
                    if(response.isSuccessful()){

                        callBack.onSuccess(response.body().string());
                    }else{
                        String message = "错误：" + response;

                        callBack.onFail(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}
