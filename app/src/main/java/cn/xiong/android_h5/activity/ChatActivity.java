package cn.xiong.android_h5.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.model.LatLng;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.xiong.android_h5.MainActivity;
import cn.xiong.android_h5.R;
import cn.xiong.android_h5.adapter.MsgAdapter;
import cn.xiong.android_h5.constant.ServerUrl;
import cn.xiong.android_h5.entity.Msg;
import cn.xiong.android_h5.entity.SendMsg;
import cn.xiong.android_h5.http.AI;
import cn.xiong.android_h5.http.HttpCallBack;

public class ChatActivity extends AppCompatActivity implements AMapLocationListener {

    private List<Msg> list = new ArrayList<Msg>();
    private AMapLocationClient mLocationClient = null;//定位发起端
    private AMapLocationClientOption mLocationOption = null;//定位参数
    private LocationSource.OnLocationChangedListener mListener = null;//定位监听器
    private String place = "武汉市洪山区武汉理工大学升升公寓C409";

    private EditText inputText;
    private Button send;
    private RecyclerView recyclerView;
    private MsgAdapter msgAdapter;
    private Gson gson = new Gson();

    private NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initLoc();

        initMsg();
        initView();
    }

    private void initView(){
        inputText = (EditText) findViewById(R.id.input_text);
        send = (Button) findViewById(R.id.btn_send);
        recyclerView = (RecyclerView) findViewById(R.id.msg_recycle_view);
        msgAdapter = new MsgAdapter(list);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(msgAdapter);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = inputText.getText().toString();
                if(!"".equals(content)){
                    Msg msg = new Msg(content,Msg.TYPE_SEND);
                    list.add(msg);

                    SendMsg sendMsg = new SendMsg(content,place);
                    String json = gson.toJson(sendMsg);
                    AI.postData(ServerUrl.RebortUrl,new AiCallBack(),json);
                    msgAdapter.notifyItemChanged(list.size()-1);
                    recyclerView.scrollToPosition(list.size()-1);
                    inputText.setText("");
                }
            }
        });
    }

    private void initMsg(){
        Msg msg1 = new Msg("你好，我是DCG",Msg.TYPE_RECEIVED);
        list.add(msg1);
        Msg msg4 = new Msg(" 在我们每个人的成长过程中，相信许多人都有这样的同感，就是很多我们看起来很一般的同学、同事和朋友却在若干年后在大家不经意间变得令人刮目相看，做出了令人意想不到的成就。其实，他们自有过人之处，但这“过人之处”在很大程度上是指他们具有很强的“梯子意识”，善于借梯给力，会找并善找梯子，具有主动搭接梯子的良好情商。这种“梯子意识”引申开来，就是指良好的人际关系意识、敏锐的观察思考意识、超前的创新意识、捕捉稍纵即逝的机会意识等等。",Msg.TYPE_RECEIVED);
        list.add(msg4);
        Msg msg5 = new Msg("卡夫卡说过“除了文学，我一无是处”，然而他追求爱情，但不渴望婚姻，正是几次不算完美的爱情成就了他的几部经典名著《判决》》、《变形记》、《城堡》、《地洞》。而唐朝诗人李商隐更是留下无数爱情佳句-“何当共剪西窗烛，却话巴山夜雨时”、“相见时难别亦难，东风无力百花残”等等，诸如此类的文人不胜枚举",Msg.TYPE_RECEIVED);
        list.add(msg5);
        Msg msg2 = new Msg("你好，我是熊胜杰",Msg.TYPE_SEND);
        list.add(msg2);
        Msg msg3 = new Msg("很高兴认识你",Msg.TYPE_RECEIVED);
        list.add(msg3);
    }

    class AiCallBack implements HttpCallBack{

        @Override
        public void onSuccess(String data) {
            final Msg msg = gson.fromJson(data,Msg.class);
            msg.setType(Msg.TYPE_RECEIVED);
            if(msg.getUrl()!=null){
                msg.setText(msg.getText()+"\n"+msg.getUrl());
            }
            list.add(msg);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    msgAdapter.notifyItemChanged(list.size()-1);
                    recyclerView.scrollToPosition(list.size()-1);

                    Intent intent = new Intent(ChatActivity.this, MainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(ChatActivity.this,0,intent,0);
                    Notification notification = new NotificationCompat.Builder(ChatActivity.this)
                            .setContentTitle("小小熊发来消息")
                            .setWhen(System.currentTimeMillis())
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                            .setAutoCancel(true)
                            .setContentText(msg.getText())
                            .setContentIntent(pendingIntent)
                            .setLights(Color.GREEN,1000,1000)
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(msg.getText()))
                            .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(),R.drawable.notification)))
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .build();
                    notificationManager.notify(1,notification);
                }
            });
        }

        @Override
        public void onFail(String message) {

        }
    }

    //定位
    private void initLoc() {
        //初始化定位
        mLocationClient = new AMapLocationClient(this);
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }



    @Override
    public void onLocationChanged(AMapLocation amapLocation) {

        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
                amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                amapLocation.getCountry();//国家信息
                amapLocation.getProvince();//省信息
                amapLocation.getCity();//城市信息
                amapLocation.getDistrict();//城区信息
                amapLocation.getStreet();//街道信息
                amapLocation.getStreetNum();//街道门牌号信息
                amapLocation.getCityCode();//城市编码
                amapLocation.getAdCode();//地区编码
                StringBuffer buffer = new StringBuffer();
                buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() + "" + amapLocation.getProvince() + "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());

                place = buffer.toString();
                mLocationClient.stopLocation();

            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());

                Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
            }
        }
    }
}
