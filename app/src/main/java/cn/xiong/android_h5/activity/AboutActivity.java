package cn.xiong.android_h5.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.mock.MockPackageManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cn.xiong.android_h5.MainActivity;
import cn.xiong.android_h5.R;

public class AboutActivity extends AppCompatActivity {

    private TextView ver,connect;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        PackageManager packageManager = getPackageManager();
        PackageInfo info = null;



        try {
            info = packageManager.getPackageInfo(getPackageName(),0);
            String version = info==null?"unknow":info.versionName;

            String msg = String.format("版本号: %s\n设计者: 熊胜杰",version);
            ver = (TextView) findViewById(R.id.version);
            connect = (TextView) findViewById(R.id.connect);
            ver.setText(msg);
            connect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(ContextCompat.checkSelfPermission(AboutActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(AboutActivity.this,new String[]{Manifest.permission.SEND_SMS},1);
                    }else {
                        sendToMe();
                    }
                }
            });
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void sendToMe(){
        intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:15927201152"));
        intent.putExtra("sms_body","你好，我是……，我有……");
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0&& grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    sendToMe();
                }else {
                    Toast.makeText(AboutActivity.this,"你拒绝了发送短信的权限",Toast.LENGTH_SHORT);
                }
                break;
            default:
        }
    }
}
