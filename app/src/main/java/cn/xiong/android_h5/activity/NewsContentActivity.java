package cn.xiong.android_h5.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.xiong.android_h5.R;
import cn.xiong.android_h5.entity.News;
import cn.xiong.android_h5.fragment.NewsContentFragment;


public class NewsContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);

        News news = (News) getIntent().getSerializableExtra("news");
        NewsContentFragment newsContentFragment = (NewsContentFragment) getSupportFragmentManager().findFragmentById(R.id.news_content_fragment);
        newsContentFragment.refresh(news);
    }

    public static void actionStart(Context context, News news){
        Intent intent = new Intent(context,NewsContentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("news",news);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
