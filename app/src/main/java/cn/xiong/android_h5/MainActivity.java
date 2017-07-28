package cn.xiong.android_h5;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import cn.xiong.android_h5.activity.AboutActivity;
import cn.xiong.android_h5.activity.HelloActivity;
import cn.xiong.android_h5.activity.ListViewActivity;
import cn.xiong.android_h5.activity.MapActivity;
import cn.xiong.android_h5.activity.RccycleViewActivity;

public class MainActivity extends Activity {

    private WebView webView;
    private LinearLayout root;
    private EditText et_user;
    private IntentFilter intentFilter;
    private NetWorkChangeReceiver netWorkChangeReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        root = (LinearLayout) findViewById(R.id.root);
        et_user = (EditText) findViewById(R.id.et_user);

        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        netWorkChangeReceiver = new NetWorkChangeReceiver();
        registerReceiver(netWorkChangeReceiver,intentFilter);

        initView();
    }

    private void initView(){

        webView = new WebView(getApplication());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

        webView.setLayoutParams(params);
        root.addView(webView);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });

        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);

        webView.loadUrl("file:///android_asset/H5.html");

        webView.addJavascriptInterface(new JSInterface(),"Android");
    }

    public void click(View view){

        webView.loadUrl(et_user.getText().toString());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        root.removeView(webView);
        webView.stopLoading();
        webView.removeAllViews();
        webView.destroy();
        webView = null;
        unregisterReceiver(netWorkChangeReceiver);
    }

    private class JSInterface {
        //JS需要调用的方法
        @JavascriptInterface
        public void showToast(String arg){
            Toast.makeText(MainActivity.this,arg, Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public void changeActivity(String arg){
            String classpath = "cn.xiong.android_h5.activity."+arg;
            Intent intent = new Intent();
            try {
                intent.setClass(MainActivity.this,Class.forName(classpath));
                startActivity(intent);
            } catch (ClassNotFoundException e) {

            }
        }
    }

    class NetWorkChangeReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if(action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isAvailable()) {
                    Toast.makeText(context, "网络可用:"+networkInfo.getTypeName(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()){
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
