package com.example.songwh.myapplication.engine;

import android.app.Application;

import com.yanzhenjie.nohttp.OkHttpNetworkExecutor;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.cache.DBCacheStore;
import com.yolanda.nohttp.cookie.DBCookieStore;

/**
 * Created by songwh on 2016/12/12.
 */
public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        NoHttp.initialize(this); // NoHttp默认初始化。
        NoHttp.initialize(this, new NoHttp.Config()
                .setConnectTimeout(30 * 1000) // 全局连接超时时间，单位毫秒。
                .setReadTimeout(30 * 1000) // 全局服务器响应超时时间，单位毫秒。
                .setCacheStore(
                        new DBCacheStore(this) // 配置缓存到数据库。
                                .setEnable(true) // true启用缓存，fasle禁用缓存。
                )
                 .setCookieStore(
                          new DBCookieStore(this)
                                .setEnable(true) // true启用自动维护Cookie，fasle禁用自动维护Cookie。

                )
                .setNetworkExecutor(new OkHttpNetworkExecutor())  // 使用OkHttp做网络层。
        );
        Logger.setDebug(true); // 开启NoHttp调试模式。
        Logger.setTag("NoHttpSample"); // 设置NoHttp打印Log的TAG。
    }

}
