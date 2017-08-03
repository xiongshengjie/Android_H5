package cn.xiong.android_h5.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import cn.xiong.android_h5.R;
import cn.xiong.android_h5.fragment.NewsTitleFragment;

public class NewsActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBar actionBar;
    private NavigationView navigationView;
    private NewsTitleFragment newsTitleFragment;
    private FloatingActionButton floatingButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initView();
    }

    private void initView(){
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBar = getSupportActionBar();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        newsTitleFragment = (NewsTitleFragment) getSupportFragmentManager().findFragmentById(R.id.news_title_fragment);
        floatingButton = (FloatingActionButton) findViewById(R.id.floating_button);

        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view,"点击撤销",Snackbar.LENGTH_SHORT)
                        .setAction("撤销", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(NewsActivity.this,"撤销了操作",Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });

        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.icon_menu);
        }

        navigationView.setCheckedItem(R.id.top);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.top:
                        newsTitleFragment.setType("top");
                        break;
                    case R.id.sociaty:
                        newsTitleFragment.setType("shehui");
                        break;
                    case R.id.in:
                        newsTitleFragment.setType("guonei");
                        break;
                    case R.id.out:
                        newsTitleFragment.setType("guoji");
                        break;
                    case R.id.fun:
                        newsTitleFragment.setType("yule");
                        break;
                    case R.id.sport:
                        newsTitleFragment.setType("tiyu");
                        break;
                    case R.id.army:
                        newsTitleFragment.setType("junshi");
                        break;
                    case R.id.science:
                        newsTitleFragment.setType("keji");
                        break;
                    case R.id.money:
                        newsTitleFragment.setType("caijing");
                        break;
                    case R.id.fation:
                        newsTitleFragment.setType("shishang");
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true;
    }
}
