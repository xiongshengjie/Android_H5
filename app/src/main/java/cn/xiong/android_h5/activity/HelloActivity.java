package cn.xiong.android_h5.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.javia.arity.Symbols;
import org.javia.arity.SyntaxException;

import cn.xiong.android_h5.R;

public class HelloActivity extends AppCompatActivity {

    private TextView input;
    private TextView result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        input = (TextView)findViewById(R.id.text_input);
        result = (TextView)findViewById(R.id.text_result);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.expanded_menu_about:
            {
                Intent intent = new Intent(HelloActivity.this,AboutActivity.class);
                startActivity(intent);
            }
            break;
            case android.R.id.home:
            {
                finish();
            }
            break;
        }

        return  true;
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_zero:
            case R.id.btn_one:
            case R.id.btn_two:
            case R.id.btn_three:
            case R.id.btn_four:
            case R.id.btn_five:
            case R.id.btn_six:
            case R.id.btn_seven:
            case R.id.btn_eight:
            case R.id.btn_nine:
            case R.id.btn_digital:
            case R.id.btn_add:
            case R.id.btn_sub:
            case R.id.btn_multi:
            case R.id.btn_div:
            {
                Button btn = (Button)view;
                String strAdd = btn.getText().toString();
                input.append(strAdd);
            }
            break;
            case R.id.btn_equal:
            {
                String in = input.getText().toString();
                if ("".equals(in)){
                    return;
                }
                Symbols s = new Symbols();
                try {
                    double res= s.eval(in);

                    result.setText(String.valueOf(res));

                } catch (SyntaxException e) {
                    Toast.makeText(HelloActivity.this,"错误："+e.toString(),Toast.LENGTH_SHORT).show();
                }

                Animation fadein = AnimationUtils.loadAnimation(HelloActivity.this,R.anim.fade_in);
                result.startAnimation(fadein);


                Animation fadeout = AnimationUtils.loadAnimation(HelloActivity.this,R.anim.fade_out);
                input.startAnimation(fadeout);
                fadeout.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        input.setText("");
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
            break;

            case R.id.btn_clear:
            {
                input.setText("");
                result.setText("");
            }
            break;
            case R.id.btn_clear_last:
            {
                String in = input.getText().toString();
                if(!in.equals("")){
                    in = in.substring(0,in.length()-1);
                    input.setText(in);
                }

            }
            break;
        }
    }
}
