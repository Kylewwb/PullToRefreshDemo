package com.example.songwh.myapplication.activity.other;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.songwh.myapplication.R;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.Request;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private PullToRefreshListView ptrListView;
    private List<String> mList;
    private ArrayAdapter<String> adapter;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ptrListView = (PullToRefreshListView) findViewById(R.id.ptrListView);
        handler = new Handler();
        //刷新布局个性化定制
        init_ptrListView_LoadingLayout();
        //加载数据
        initData();
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
                            mList.add(1,"xinshuj "+i);//记得更改

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
                            mList.add("xinshuj "+i);//记得更改

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

    }
}
