package cn.xiong.android_h5.task;

import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.xiong.android_h5.activity.DownloadListener;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/7/31 0031.
 */

public class DownloadTask extends AsyncTask<String, Integer, Integer> {

    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAILED = 1;
    public static final int TYPE_PAUSED = 2;
    public static final int TYPE_CANCELED = 3;

    private DownloadListener listener;
    private String fileName;

    private boolean isCanceled =  false;
    private boolean isPaused = false;

    private int lastProgress;

    private final static OkHttpClient client = new OkHttpClient();

    public DownloadTask(DownloadListener listener) {
        this.listener = listener;
    }

    @Override
    protected Integer doInBackground(String... params) {

        InputStream is = null;
        RandomAccessFile savedFile = null;
        File file = null;

        try {
            long downloadedLength = 0;
            String downloadUrl = params[0];
            fileName = getFilename(downloadUrl);
            if(TextUtils.isEmpty(fileName)){
                return TYPE_FAILED;
            }

            String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();

            file = new File(directory + "/" + fileName);

            if(file.exists()){
                downloadedLength = file.length();
            }

            long contentLength = getContentLength(downloadUrl);
            if(0 == contentLength){
                return TYPE_FAILED;
            }else if(contentLength == downloadedLength){
                return TYPE_SUCCESS;
            }

            Request request = new Request.Builder()
                    .addHeader("RANGE","bytes="+downloadedLength+"-")
                    .url(downloadUrl)
                    .build();

            Response response = client.newCall(request).execute();
            if(response!=null){
                is = response.body().byteStream();
                savedFile = new RandomAccessFile(file,"rw");
                savedFile.seek(downloadedLength);
                byte[] b = new byte[1024];
                int total = 0;
                int len;
                while ((len = is.read(b)) != -1){
                    if(isCanceled){
                        return TYPE_CANCELED;
                    }else if(isPaused){
                        return TYPE_PAUSED;
                    }else {
                        total += len;
                        savedFile.write(b,0,len);
                        int progress = (int)((total+downloadedLength)*100/contentLength);

                        publishProgress(progress);
                    }
                }
                response.body().close();
                return TYPE_SUCCESS;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(is!=null){
                        is.close();
                }
                if(savedFile != null){
                    savedFile.close();
                }
                if(isCanceled&&file!=null){
                    file.delete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return TYPE_FAILED;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        if(progress > lastProgress){
            listener.onProgress(progress);
            lastProgress = progress;
        }
    }

    @Override
    protected void onPostExecute(Integer status) {
        switch (status){
            case TYPE_SUCCESS:
                listener.onSuccess();
                break;
            case TYPE_FAILED:
                listener.onFailed();
                break;
            case TYPE_PAUSED:
                listener.onPaused();
                break;
            case TYPE_CANCELED:
                listener.onCanceled(fileName);
                break;
            default:
                break;
        }
    }

    public void pauseDownload(){
        isPaused = true;
    }

    public void cancelDownload(){
        isCanceled = true;
    }

    private long getContentLength(String downloadUrl) throws IOException {
        Request request = new Request.Builder()
                .url(downloadUrl)
                .build();
        Response response = client.newCall(request).execute();
        if(response!=null && response.isSuccessful()){
            long contentLength = response.body().contentLength();
            response.close();
            return contentLength;
        }
        return 0;
    }

    public static String getFilename(String downloadUrl) throws IOException {
        URL url = new URL(downloadUrl);
        URLConnection conn = url.openConnection();
        String filename = null;
        Map<String, List<String>> hf = conn.getHeaderFields();
        if (hf == null) {
            return null;
        }
        Set<String> key = hf.keySet();
        if (key == null) {
            return null;
        }

        for (String skey : key) {
            List<String> values = hf.get(skey);
            for (String value : values) {
                String result;
                result = new String(value.getBytes("ISO-8859-1"), "GBK");
                int location = result.indexOf("filename");
                if (location >= 0) {
                    result = result.substring(location
                            + "filename".length());
                    filename = result
                            .substring(result.indexOf("=") + 1);
                }
            }
        }
        return filename;
    }
}
