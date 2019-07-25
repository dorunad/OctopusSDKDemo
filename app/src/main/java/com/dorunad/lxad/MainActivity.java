package com.dorunad.lxad;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.dorunad.lxad.base.BaseActivity;
import com.dorunad.lxad.databinding.ActivityMainBinding;


public class MainActivity extends BaseActivity<ActivityMainBinding> {
    private boolean havePermissions;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (SplashActivity.checkAndRequestPermission(this)) {
                havePermissions = true;
                mViewBinding.btnPermission.setText(getString(R.string.permissions_on));
            }
        } else {
            // 如果是Android6.0以下的机器，默认在安装时获得了所有权限，可以直接调用SDK
            havePermissions = true;
            mViewBinding.btnPermission.setText(getString(R.string.permissions_on));
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_permission:
                if (!havePermissions) {
                    SplashActivity.checkAndRequestPermission(this);
                }
                break;
            case R.id.btn_splash:
                startActivity(SplashActivity.class);
                break;
            case R.id.btn_banner:
                startActivity(BannerActivity.class);
                break;
            case R.id.btn_feed:
                startActivity(FeedListActivity.class);
                break;
            case R.id.btn_reward_video:
                startActivity(RewardVideoActivity.class);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SplashActivity.REQUEST_PERMISSION_CODE) {
            if (SplashActivity.hasAllPermissionsGranted(grantResults)) {
                havePermissions = true;
                mViewBinding.btnPermission.setText(getString(R.string.permissions_on));
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

}
