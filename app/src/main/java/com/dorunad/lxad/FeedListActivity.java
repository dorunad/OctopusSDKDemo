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
                showLoading();
                //3.加载广告
                mOFeedList.load();
                break;
        }
    }

    /**
     * 获取信息流广告
     */
    private void fetchFeedListAD() {
        showLoading();
        //1.实例化信息流广告
        mOFeedList = new OFeedList(this, mViewBinding.flContainer);
        //2.设置信息流广告创意监听
        mOFeedList.setOInteractionListener(new OFeedList.OInteractionListener() {
            /**
             * 广告加载
             */
            @Override
            public void onAdLoad() {
                hideLoading();
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
             * @param e
             */
            @Override
            public void onNoAd(OAdException e) {
                hideLoading();
                showToast("Error：" + e.getErrorCode() + " " + e.getMsg());
            }
        });
        //3.加载广告
        mOFeedList.load();
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
        if (mOFeedList != null) {
            //4.销毁。释放资源
            mOFeedList.destroy();
        }
    }
}
