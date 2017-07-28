package cn.xiong.android_h5.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.xiong.android_h5.R;
import cn.xiong.android_h5.adapter.ListAdapter;
import cn.xiong.android_h5.entity.Person;

public class ListViewActivity extends AppCompatActivity {

    private ListAdapter adapter;
    private List<Person> list = new ArrayList<Person>();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        }else {
            initList();
        }
        listView = (ListView) findViewById(R.id.list_view);
        adapter = new ListAdapter(this,list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Person person = list.get(i);
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+person.getNumber()));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    initList();
                }else {
                    Toast.makeText(this,"你拒绝了读取联系人的权限",Toast.LENGTH_SHORT).show();
                    ListViewActivity.this.finish();
                }
                break;

            default:
        }
    }

    public void initList(){

        Cursor cursor = null;
        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI , null , null , null , null);
        try {
            if(cursor != null){
                while (cursor.moveToNext()){
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    Person person = new Person(name,number);
                    list.add(person);
                }
                adapter.notifyDataSetChanged();
            }
        }catch (Exception e){

        }finally {
            if(cursor != null){
                cursor.close();
            }
        }
    }


}
