package cn.xiong.android_h5.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import cn.xiong.android_h5.R;
import cn.xiong.android_h5.entity.News;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsContentFragment extends Fragment {

    private View view;
    private WebView webView;

    public NewsContentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news_content, container, false);
        return view;
    }

    public void refresh(News news){

        View news_layout = view.findViewById(R.id.news_layout);
        news_layout.setVisibility(View.VISIBLE);
        TextView title = (TextView) view.findViewById(R.id.news_title);
        webView = (WebView) view.findViewById(R.id.content_webview);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }

        });

        webView.loadUrl(news.getUrl());

        title.setText(news.getTitle());
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
