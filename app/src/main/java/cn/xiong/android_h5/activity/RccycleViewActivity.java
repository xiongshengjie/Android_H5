package cn.xiong.android_h5.activity;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.xiong.android_h5.R;
import cn.xiong.android_h5.adapter.RecycleAdapter;
import cn.xiong.android_h5.entity.Item;
import cn.xiong.android_h5.entity.PicItem;
import cn.xiong.android_h5.utils.RecycleViewDivider;

public class RccycleViewActivity extends AppCompatActivity {

    private List<PicItem> list = new ArrayList<PicItem>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rccycle_view);

        initList();
        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        StaggeredGridLayoutManager linearLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //recyclerView.addItemDecoration(new RecycleViewDivider(this.getApplicationContext(), LinearLayoutManager.HORIZONTAL,2, ContextCompat.getColor(getActivity(),R.color.gray)));
        RecycleAdapter adapter = new RecycleAdapter(list);
        recyclerView.setAdapter(adapter);
    }

    public void initList(){

        for (int i = 0;i<20;i++){
            PicItem item = new PicItem();
            item.setName(getRandomName());
            item.setId(R.mipmap.ic_launcher);
            list.add(item);
        }
    }

    private String getRandomName(){
        Random random = new Random();
        int length = random.nextInt(20)+1;
        StringBuilder builder = new StringBuilder();
        for (int i = 0;i<length;i++){
            builder.append("你好");
        }
        return builder.toString();
    }
}
