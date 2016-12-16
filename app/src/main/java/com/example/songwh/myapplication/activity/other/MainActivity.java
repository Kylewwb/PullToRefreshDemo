package com.example.songwh.myapplication.activity.other;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.songwh.myapplication.R;
import com.example.songwh.myapplication.db.MyDatabaseHelper;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.Request;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//测试demo。
public class MainActivity extends AppCompatActivity {
    private PullToRefreshListView ptrListView;
    private List<String> mList;
    private ArrayAdapter<String> adapter;
    private Button btn1,btn2;
    private Handler handler;
    private String password = "sdfsdf";
    private MyDatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);

        dbHelper = new MyDatabaseHelper(this,"BookStore.db",null,1);//创建对象

        ptrListView = (PullToRefreshListView) findViewById(R.id.ptrListView);
        handler = new Handler();
        //刷新布局个性化定制
        init_ptrListView_LoadingLayout();
        //加载数据
        initData();

        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putString("name","wwb");
        editor.putString("password",password);
        editor.putBoolean("married",false);
        editor.apply();
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
                String name = pref.getString("name","");
                String password = pref.getString("password","");
                Boolean married = pref.getBoolean("married",false);
                Log.i("MainActivity","name is"+name);
                Log.i("MainActivity","password is"+password);
                Log.i("MainActivity","married is"+married);

            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                btn2.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dbHelper.getWritableDatabase();
                    }
                });
            }
        }).start();

    }

    private void initData() {
        mList = new ArrayList<>();
        for (int i=0;i<20;i++){
            mList.add(String.format("第%03d条数据",i));
        }
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mList);
        ptrListView.setAdapter(adapter);
//设置刷新模式，上拉，下拉都行。
        ptrListView.setMode(PullToRefreshBase.Mode.BOTH);
//设置监听
        ptrListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
//          上下拉刷新?
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(2000);
                        for (int i = 0;i<5;i++){
                            mList.add(0,"新加的数据 "+i);//记得更改

                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //通知adapter集合数据已经修改
                                adapter.notifyDataSetChanged();
                                //停止刷新操作
                                ptrListView.onRefreshComplete();
                            }
                        });

                    }
                }).start();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(2000);
                        for (int i = 0;i<5;i++){
                            mList.add("新加的数据 "+i);//记得更改

                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //通知adapter集合数据已经修改
                                adapter.notifyDataSetChanged();
                                //停止刷新操作
                                ptrListView.onRefreshComplete();
                            }
                        });

                    }
                }).start();
            }
        });

    }

    private void init_ptrListView_LoadingLayout() {
        ILoadingLayout loadingLayoutProxy = ptrListView.getLoadingLayoutProxy();
        loadingLayoutProxy.setPullLabel("使劲拉。。");
        loadingLayoutProxy.setReleaseLabel("松手就刷新啦。。。");
        loadingLayoutProxy.setRefreshingLabel("拼命刷新中。。");
        String time = DateUtils.formatDateTime(this,System.currentTimeMillis(),DateUtils.FORMAT_SHOW_TIME|DateUtils.FORMAT_SHOW_DATE|DateUtils.FORMAT_ABBREV_MONTH);
        //显示刷新时间
        loadingLayoutProxy.setLastUpdatedLabel(time);

        Drawable drawable = getResources().getDrawable(R.drawable.frame_head);
        //设置加载时动画

        loadingLayoutProxy.setLoadingDrawable(drawable);
        ptrListView.setShowIndicator(false);//取消上下小箭头的标志
    }
}
