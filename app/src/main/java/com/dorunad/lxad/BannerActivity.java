package com.dorunad.lxad;

import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;

import com.dorunad.lxad.base.BaseActivity;
import com.dorunad.lxad.databinding.ActivityBannerBinding;
import com.dorunad.octopus.open.OAdException;
import com.dorunad.octopus.open.ad.banner.OBanner;
import com.dorunad.octopus.open.ad.banner.OBannerListener;

public class BannerActivity extends BaseActivity<ActivityBannerBinding> {
    private OBanner mOBanner;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_banner;
    }

    @Override
    protected void initView() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.banner_ad));
        }
    }

    @Override
    protected void initData() {
        fetchBannerAD();
    }

    @Override
    protected void initListener() {

    }

    public void onClick(View view) {
        if (mOBanner != null) {
            //3.加载广告
            showLoading();
            mOBanner.load();
        }
    }

    /**
     * 获取Banner广告
     */
    private void fetchBannerAD() {
        showLoading();
        //1.实例化Banner广告
        mOBanner = new OBanner(this, mViewBinding.bannerContainer);
        //2.设置加载监听
        mOBanner.setOBannerListener(new OBannerListener() {
            /**
             * 广告加载
             */
            @Override
            public void onAdLoad() {
                hideLoading();
                showToast("广告加载");
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
                showToast("广告被点击");
            }

            /**
             * 广告异常
             * @param e 异常类
             */
            @Override
            public void onNoAd(OAdException e) {
                hideLoading();
                showToast("Error：" + e.getErrorCode() + " " + e.getMsg());
            }

            /**
             * 广告关闭：dislike
             */
            @Override
            public void onAdClose() {
                showToast("广告关闭");
            }
        });
        //3.加载广告
        mOBanner.load();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**
         * 务必销毁
         */
        if (mOBanner != null) {
            //4.销毁。释放资源
            mOBanner.destroy();
        }
    }
}
