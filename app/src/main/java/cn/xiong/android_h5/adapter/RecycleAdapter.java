package cn.xiong.android_h5.adapter;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.xiong.android_h5.R;
import cn.xiong.android_h5.entity.Item;
import cn.xiong.android_h5.entity.PicItem;

/**
 * Created by Administrator on 2017/7/12.
 */

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {

    private List<PicItem> list;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        ImageView picId;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.pic_name);
            picId = (ImageView) itemView.findViewById(R.id.pic_pic);
        }
    }

    public RecycleAdapter(List<PicItem> l){
        this.list = l;
    }
    @Override
    public RecycleAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pic_item,parent,false);

        final ViewHolder holder = new ViewHolder(view);

        holder.picId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                PicItem item = list.get(position);
                Toast.makeText(view.getContext(),String.valueOf(position),Toast.LENGTH_SHORT).show();
            }
        });

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                PicItem item = list.get(position);
                Toast.makeText(view.getContext(),item.getName(),Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecycleAdapter.ViewHolder holder, int position) {
        PicItem item = list.get(position);
        holder.name.setText(item.getName());
        holder.picId.setImageResource(item.getId());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
