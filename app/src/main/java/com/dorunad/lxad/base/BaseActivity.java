package com.dorunad.lxad.base;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dorunad.lxad.dialog.LoadingUtil;
import com.dorunad.lxad.utils.AppManager;

public abstract class BaseActivity<B extends ViewDataBinding> extends AppCompatActivity {
    protected String TAG = getClass().getSimpleName();
    protected B mViewBinding;
    protected View rootView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, " onCreate()");
        int layoutId = this.getLayoutId();

        if (layoutId != 0) {
            rootView = getLayoutInflater().inflate(layoutId, null, false);
        }
        if (rootView != null) {
            mViewBinding = DataBindingUtil.bind(rootView);
        }
        this.setContentView(rootView);

        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            getBundleExtras(extras);
        } else {
            getBundleExtras(new Bundle());
        }

        // 把actvity放到application栈中管理
        AppManager.getAppManager().addActivity(this);

        initView();
        initData();
        initListener();
    }

    /**
     * 获取资源文件布局
     *
     * @return 资源布局文件layout
     */
    protected abstract int getLayoutId();

    /**
     * 获取传递的bundle数据
     *
     * @param extras
     */
    protected void getBundleExtras(Bundle extras) {
    }

    /**
     * 初始化布局
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化监听事件
     */
    protected abstract void initListener();

    public void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    public void showToast(@StringRes int str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    public void showLoading() {
        LoadingUtil.showMaterLoading(this, "加载中..");
    }

    public void hideLoading() {
        LoadingUtil.hideMaterLoading();
    }

    /**
     * [页面跳转]
     *
     * @param clz
     */
    public void startActivity(Class<?> clz) {
        startActivity(new Intent(this, clz));
    }

    /**
     * 携带数据页面跳转
     *
     * @param clz
     * @param bundle
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mViewBinding != null) {
            mViewBinding.unbind();
        }
        AppManager.getAppManager().finishActivity(this);
        hideLoading();
    }
}
