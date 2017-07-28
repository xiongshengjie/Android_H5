package cn.xiong.android_h5.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.xiong.android_h5.R;
import cn.xiong.android_h5.entity.Item;
import cn.xiong.android_h5.entity.Person;

/**
 * Created by Administrator on 2017/7/12.
 */

public class ListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Person> list;

    public ListAdapter(Context context, List<Person> list) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent) {

        Viewholder viewholder;
        if (convertview == null){
            convertview = inflater.inflate(R.layout.list_item,null);
            viewholder = new Viewholder(convertview);
            convertview.setTag(viewholder);
        }else {
            viewholder = (Viewholder) convertview.getTag();
        }

        Person person = list.get(position);
        viewholder.name.setText(person.getName());
        viewholder.content.setText(person.getNumber());
        return convertview;
    }

    class Viewholder{
        TextView name;
        TextView content;
        TextView date;

        public Viewholder(View view){
            name = (TextView) view.findViewById(R.id.item_name);
            content = (TextView) view.findViewById(R.id.item_content);
            date = (TextView) view.findViewById(R.id.item_date);
        }
    }
}
