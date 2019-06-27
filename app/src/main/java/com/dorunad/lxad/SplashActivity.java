package com.dorunad.lxad;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.dorunad.lxad.base.BaseActivity;
import com.dorunad.lxad.databinding.ActivitySplashBinding;
import com.dorunad.octopus.open.OAdException;
import com.dorunad.octopus.open.ad.splash.OSplash;
import com.dorunad.octopus.open.ad.splash.OSplashListener;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends BaseActivity<ActivitySplashBinding> {
    private OSplash mOSplash;
    private boolean isAdClicked;

    public static final int REQUEST_PERMISSION_CODE = 1024;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        // 如果targetSDKVersion >= 23，就要申请好权限。如果您的App没有适配到Android6.0（即targetSDKVersion < 23），那么只需要在这里直接调用fetchSplashAD接口。
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkAndRequestPermission(this)) {
                fetchSplashAD();
            }
        } else {
            // 如果是Android6.0以下的机器，默认在安装时获得了所有权限，可以直接调用SDK
            fetchSplashAD();
        }
    }

    @Override
    protected void initListener() {

    }

    /**
     * ----------非常重要----------
     * <p>
     * Android6.0以上的权限适配简单示例：
     * <p>
     * 如果targetSDKVersion >= 23，那么必须要申请到所需要的权限，再调用SDK，否则SDK不会工作，可能会抛异常。
     * <p>
     * Demo代码里是一个基本的权限申请示例，请开发者根据自己的场景合理地编写这部分代码来实现权限申请。
     * 注意：下面的`checkSelfPermission`和`requestPermissions`方法都是在Android6.0的SDK中增加的API，如果您的App还没有适配到Android6.0以上，则不需要调用这些方法，直接调用SDK即可。
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static boolean checkAndRequestPermission(Activity activity) {
        List<String> lackedPermission = new ArrayList<String>();
        if (!(activity.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (!(activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!(activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        // 权限都已经有了，那么直接调用SDK
        if (lackedPermission.size() == 0) {
            return true;
        } else {
            // 请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限，如果获得权限就可以调用SDK，否则不要调用SDK。
            String[] requestPermissions = new String[lackedPermission.size()];
            lackedPermission.toArray(requestPermissions);
            activity.requestPermissions(requestPermissions, REQUEST_PERMISSION_CODE);
        }
        return false;
    }

    public static boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (hasAllPermissionsGranted(grantResults)) {
                fetchSplashAD();
            } else {
                // 如果用户没有授权，那么应该说明意图，引导用户去设置里面授权。
                Toast.makeText(this, "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }
        }
    }

    /**
     * 获取开屏广告
     */
    private void fetchSplashAD() {
        //1.实例化开屏广告
        mOSplash = new OSplash(this, mViewBinding.content);
        //2.设置加载监听
        mOSplash.setOSplashListener(new OSplashListener() {
            /**
             * 广告加载
             */
            @Override
            public void onAdLoad() {
                showToast("广告加载");
            }

            /**
             * 广告跳过：包括倒计时结束跳过和手动跳过
             */
            @Override
            public void onAdSkip() {
                gotoMainActivity();
            }

            /**
             * 广告异常
             * @param e 异常类
             */
            @Override
            public void onNoAd(OAdException e) {
                gotoMainActivity();

                showToast("Error：" + e.getErrorCode() + " " + e.getMsg());
            }

            /**
             * 广告展示
             */
            @Override
            public void onAdShow() {
                showToast("广告展示");
            }

            /**
             * 广告被点击
             */
            @Override
            public void onAdClicked() {
                isAdClicked = true;
                showToast("广告被点击");
            }
        });
        //3.加载广告
        mOSplash.load();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isAdClicked) {
            gotoMainActivity();
        }
    }


    private void gotoMainActivity() {
        startActivity(MainActivity.class);
        finish();
    }
}
