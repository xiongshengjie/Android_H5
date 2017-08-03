package cn.xiong.android_h5.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cn.xiong.android_h5.R;
import cn.xiong.android_h5.activity.NewsActivity;
import cn.xiong.android_h5.entity.News;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsContentFragment extends Fragment {

    private View view;
    private WebView webView;

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView imageView;
    public NewsContentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news_content, container, false);

        webView = (WebView) view.findViewById(R.id.content_webview);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }

        });

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_bar);
        imageView = (ImageView) view.findViewById(R.id.head_toolbar);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        return view;
    }

    public void refresh(News news){

        webView.loadUrl(news.getUrl());
        collapsingToolbarLayout.setTitle(news.getCategory());
        Glide.with(getActivity())
                .load(news.getThumbnail_pic_s02())
                .into(imageView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        webView.stopLoading();
        webView.removeAllViews();
        webView.destroy();
        webView = null;

    }

}
