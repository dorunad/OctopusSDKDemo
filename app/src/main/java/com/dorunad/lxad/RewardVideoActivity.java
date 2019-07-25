package com.dorunad.lxad;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.dorunad.lxad.base.BaseActivity;
import com.dorunad.lxad.databinding.ActivityBannerBinding;
import com.dorunad.lxad.databinding.ActivityRewardVideoBinding;
import com.dorunad.octopus.open.OAdException;
import com.dorunad.octopus.open.ad.OAdSlot;
import com.dorunad.octopus.open.ad.rewardvideo.ORewardVideo;

public class RewardVideoActivity extends BaseActivity<ActivityRewardVideoBinding> {
    private ORewardVideo mORewardVideo;

    private boolean mDownloadActive = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_reward_video;
    }

    @Override
    protected void initView() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.rewardvideo_ad));
        }
    }

    @Override
    protected void initData() {
        fetchRewardVideoAD();
    }

    @Override
    protected void initListener() {

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_refresh:
                if (mORewardVideo != null) {
                    //3. 加载广告
                    mORewardVideo.load();
                }
                break;
        }
    }

    /**
     * 获取激励视频广告
     */
    public void fetchRewardVideoAD() {
        //1.实例化激励视频广告
        mORewardVideo = new ORewardVideo(this);
//        mORewardVideo = new ORewardVideo(this, new OAdSlot.Builder()
//                //用户标识。可为空
//                .setUserID("")
//                //奖励名称
//                .setRewardName("金币")
//                //奖励数量
//                .setRewardAmount(100)
//                //附加参数，可选
//                .setMediaExtra("")
//                .build());
        //2.设置激励视频广告加载监听
        mORewardVideo.setORewardVideoListener(new ORewardVideo.ORewardVideoListener() {
            /**
             * 广告加载
             */
            @Override
            public void onAdLoad() {
                showToast("广告加载");
            }

            /**
             * 广告展示
             */
            @Override
            public void onAdShow() {
                showToast("广告展示");
            }

            @Override
            public void onAdClicked() {
                showToast("广告被点击");
            }

            /**
             * 广告点击
             */
            @Override
            public void onAdClose() {
                showToast("广告被关闭");
            }

            /**
             * 广告异常
             * @param e
             */
            @Override
            public void onNoAd(OAdException e) {
                showToast("Error：" + e.getErrorCode() + " " + e.getMsg());
            }

            /**
             * 视频播放结束
             */
            @Override
            public void onVideoComplete() {
                showToast("视频播放结束");
            }

            /**
             * 视频广告播完验证奖励有效性回调
             * @param rewardVerify 是否有效
             * @param rewardAmount 奖励数量
             * @param rewardName 奖励名称
             */
            @Override
            public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName) {
                showToast("是否有效：" + rewardVerify + " 奖励数量：" + rewardAmount + " 奖励名称：" + rewardName);
            }
        });
        //3. 加载广告
        mORewardVideo.load();
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
}
