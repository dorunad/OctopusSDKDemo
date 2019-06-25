package com.dorunad.lxad.base;

import android.app.Application;

import com.dorunad.octopus.open.OctopusADSDK;

public class App extends Application {
    protected static App instance;

    static {
        instance = null;
    }


    public static App me() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        initOctopusSDK();
    }

    private void initOctopusSDK() {
        // SDK初始化 isDebug开启SDK日志 正式上线前关闭
        OctopusADSDK.getInstance().init(this, true);
    }
}
