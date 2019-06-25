package com.dorunad.lxad;

import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;

import com.dorunad.lxad.base.BaseActivity;
import com.dorunad.lxad.databinding.ActivityFeedListBinding;
import com.dorunad.octopus.open.OAdException;
import com.dorunad.octopus.open.ad.feed.OFeedList;

public class FeedListActivity extends BaseActivity<ActivityFeedListBinding> {
    private OFeedList mOFeedList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_feed_list;
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
        fetchFeedListAD();
    }

    @Override
    protected void initListener() {

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_refresh:
                fetchFeedListAD();
                break;
        }
    }

    private void fetchFeedListAD() {
        showLoading();
        mOFeedList = new OFeedList(this, mViewBinding.flContainer);

        mOFeedList.load();
        mOFeedList.setOInteractionListener(new OFeedList.OInteractionListener() {
            @Override
            public void onAdLoad() {
                hideLoading();
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
                showToast("Error：" + e.getErrorCode() + " " + e.getMsg());
            }
        });
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
        if (mOFeedList != null) {
            mOFeedList.destroy();
        }
    }
}
