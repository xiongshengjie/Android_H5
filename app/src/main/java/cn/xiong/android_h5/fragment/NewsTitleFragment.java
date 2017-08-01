package cn.xiong.android_h5.fragment;


import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.touch.OnItemMoveListener;
import com.yanzhenjie.recyclerview.swipe.touch.OnItemStateChangedListener;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.xiong.android_h5.R;
import cn.xiong.android_h5.activity.NewsContentActivity;
import cn.xiong.android_h5.constant.ServerUrl;
import cn.xiong.android_h5.entity.News;
import cn.xiong.android_h5.entity.Reason;
import cn.xiong.android_h5.entity.RequestNews;
import cn.xiong.android_h5.http.AI;
import cn.xiong.android_h5.http.HttpCallBack;
import cn.xiong.android_h5.listener.onItemClickListener;
import cn.xiong.android_h5.utils.GlideCircleTransform;
import cn.xiong.android_h5.utils.GlideRoundTransform;
import cn.xiong.android_h5.utils.RecycleViewDivider;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsTitleFragment extends Fragment {

    private boolean isTwoPane;
    private Gson gson = new Gson();
    private List<News> list = new ArrayList<News>();
    private NewsAdapter newsAdapter;
    private SwipeMenuRecyclerView newsTitleLyout;
    private boolean sIsScrolling;
    private String type;


    public NewsTitleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_title, container, false);
        newsTitleLyout = (SwipeMenuRecyclerView) view.findViewById(R.id.news_title_layout);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        newsTitleLyout.setLayoutManager(linearLayoutManager);
        newsTitleLyout.addItemDecoration(new RecycleViewDivider(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL,2, ContextCompat.getColor(getActivity(),R.color.gray)));
        getNews();
        newsAdapter = new NewsAdapter(list);
        newsAdapter.setmOnItemClickListener(new onItemClickListener(){

            @Override
            public void onItemClick(int position) {
                News news = list.get(position);
                if(isTwoPane){
                    NewsContentFragment newsContentFragment = (NewsContentFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.news_content_fragment);
                    newsContentFragment.refresh(news);
                }else{
                    NewsContentActivity.actionStart(getActivity(),news);
                }
            }
        });
        newsTitleLyout.setAdapter(newsAdapter);

        newsTitleLyout.setLongPressDragEnabled(true);
        newsTitleLyout.setItemViewSwipeEnabled(false);

        newsTitleLyout.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                int width = getResources().getDimensionPixelSize(R.dimen.item_height);

                // MATCH_PARENT 自适应高度，保持和内容一样高；也可以指定菜单具体高度，也可以用WRAP_CONTENT。
                int height = ViewGroup.LayoutParams.MATCH_PARENT;
                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity())
                        .setBackgroundColor(R.color.black)
                        .setText("删除")
                        .setTextSize(20)
                        .setHeight(height)
                        .setWidth(width);
                SwipeMenuItem topItem = new SwipeMenuItem(getActivity())
                        .setBackgroundColor(R.color.white)
                        .setText("置顶")
                        .setTextSize(20)
                        .setHeight(height)
                        .setWidth(width);
                swipeRightMenu.addMenuItem(topItem);
                swipeRightMenu.addMenuItem(deleteItem);
            }
        });

        newsTitleLyout.setSwipeMenuItemClickListener(new OnSwipeMenuItemClickListener() {
            @Override
            public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {

                if(menuPosition == 0){
                    News news = list.get(adapterPosition);
                    list.remove(adapterPosition);
                    list.add(0,news);
                    newsAdapter.notifyDataSetChanged();
                }else {
                    list.remove(adapterPosition);
                    newsAdapter.notifyItemRemoved(adapterPosition);
                }
                closeable.smoothCloseMenu();
            }
        });

        newsTitleLyout.setOnItemMoveListener(new OnItemMoveListener() {
            @Override
            public boolean onItemMove(int fromPosition, int toPosition) {
                Collections.swap(list,fromPosition,toPosition);
                newsAdapter.notifyItemMoved(fromPosition,toPosition);
                return true;
            }

            @Override
            public void onItemDismiss(int position) {
                list.remove(position);
                newsAdapter.notifyItemRemoved(position);
            }
        });

        newsTitleLyout.setOnItemStateChangedListener(new OnItemStateChangedListener() {
            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if(actionState==OnItemStateChangedListener.ACTION_STATE_DRAG){
                    viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white_pressed));
                }else if(actionState == OnItemStateChangedListener.ACTION_STATE_IDLE){
                    ViewCompat.setBackground(viewHolder.itemView, ContextCompat.getDrawable(getActivity(), R.drawable.select_white));
                }
            }
        });

        newsTitleLyout.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    sIsScrolling = true;
                    Glide.with(getActivity()).pauseRequests();
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (sIsScrolling == true) {
                        Glide.with(getActivity()).resumeRequests();

                    }
                    sIsScrolling = false;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        return view;
    }

    public void setType(String type) {
        this.type = type;
        getNews();
        newsAdapter.notifyDataSetChanged();
        newsTitleLyout.scrollToPosition(0);
    }

    private void getNews(){
        RequestNews news = new RequestNews();
        if(type != null){
            news.setType(type);
        }
        String json = gson.toJson(news);
        AI.postData(ServerUrl.NewsUrl+"?key="+news.getKey()+"&type="+news.getType(),new NewsCallBack(),json);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getActivity().findViewById(R.id.news_layout)!=null){
            isTwoPane = true;
        }else{
            isTwoPane = false;
        }
    }



    class NewsAdapter extends SwipeMenuAdapter<NewsAdapter.ViewHolder>{

        private List<News> list;

        private onItemClickListener mOnItemClickListener;

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            onItemClickListener vOnItemClickListener;
            TextView newsItemTitle;
            ImageView newsPic;
            TextView newsDate;
            TextView newsCategory;

            public ViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                newsItemTitle = (TextView) itemView.findViewById(R.id.news_item_title);
                newsDate = (TextView) itemView.findViewById(R.id.news_date);
                newsPic = (ImageView) itemView.findViewById(R.id.news_pic);
                newsCategory = (TextView) itemView.findViewById(R.id.news_item_category);
            }

            @Override
            public void onClick(View v) {
                if(vOnItemClickListener != null){
                    vOnItemClickListener.onItemClick(getAdapterPosition());
                }
            }
        }

        public void setList(List<News> list) {
            this.list = list;
            newsAdapter.notifyDataSetChanged();
        }

        public void setmOnItemClickListener(onItemClickListener mOnItemClickListener) {
            this.mOnItemClickListener = mOnItemClickListener;
        }

        public NewsAdapter(List<News> list) {
            this.list = list;
        }



        @Override
        public View onCreateContentView(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item,parent,false);

            return view;
        }

        @Override
        public NewsAdapter.ViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
            ViewHolder viewHolder = new ViewHolder(realContentView);
            viewHolder.vOnItemClickListener = mOnItemClickListener;
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(NewsAdapter.ViewHolder holder, int position) {
            News news = list.get(position);
            holder.newsItemTitle.setText(news.getTitle());
            holder.newsDate.setText(news.getDate().substring(6));
            holder.newsCategory.setText(news.getCategory());
            Glide
                    .with(getActivity())
                    .load(news.getThumbnail_pic_s())
                    .centerCrop()
                    .error(R.mipmap.ic_launcher)
                    .animate(R.anim.little_to_large)
                    .into(holder.newsPic);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    class NewsCallBack implements HttpCallBack{

        @Override
        public void onSuccess(String data) {
            Reason reason = gson.fromJson(data,Reason.class);
            list = reason.getResult().getNewses();

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    newsAdapter.setList(list);
                }
            });
        }

        @Override
        public void onFail(String message) {

        }
    }
}
