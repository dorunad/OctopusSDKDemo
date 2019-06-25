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
        fetchBannerAD();
    }

    private void fetchBannerAD() {
        showLoading();
        mOBanner = new OBanner(this,  mViewBinding.bannerContainer);
        mOBanner.setOBannerListener(new OBannerListener() {
            @Override
            public void onAdLoad() {
                hideLoading();
                showToast("广告加载");
            }
            @Override
            public void onAdShow() {
                showToast("广告展示");
            }

            @Override
            public void onAdClicked() {
                showToast("广告被点击");
            }

            @Override
            public void onNoAd(OAdException e) {
                hideLoading();
                showToast("Error：" + e.getErrorCode()+" "+e.getMsg());
            }
        });
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
        if (mOBanner != null) {
            mOBanner.destroy();
        }
    }
}
