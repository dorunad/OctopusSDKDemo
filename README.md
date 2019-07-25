# OctopusSDKDemo Android

[![release-image]][release-url] [![minSdkVersion-image]][minSdkVersion-url]


- **目录**
  - [1. 简介](#1-简介)
  - [2. 运行环境](#2-运行环境)
  - [3. SDK包导入及权限配置](#3-sdk包导入及权限配置)
    - [3.1 导入aar包](#31-导入aar包)
    - [3.2 添加权限](#32-添加权限)
  - [4. 广告接入](#4-广告接入)
    - [4.1 初始化](#41-初始化)
    - [4.2 开屏广告(OSplash)](#42-开屏广告osplash)
    - [4.3 Banner广告(OBanner)](#43-banner广告obanner)
    - [4.4 信息流广告(OFeedList)](#44-信息流广告ofeedlist)
    - [4.5 激励视频广告(ORewardVideo)](#45-激励视频广告orewardvideo)


## 1. 简介
**版本历史**

| 版本         | 修订日期          | 说明  |
|:------------:|:------------:| :------: |
| v1.0.1        | 2019/6     |  初版  |
| v1.0.2        | 2019/6     |  增加onAdClose()回调  |
| v1.0.3        | 2019/7     |  增加激励视频广告

东润移动广告SDk(Android)是官方推出的移动广告SDK在Android平台上的版本（以下简称SDK）。

SDK的发行版本包括 AAR 包、Demo 示例、接入文档（您当前阅读的文档）。

:zap:Demo下的SDK为通用测试版，仅用于接入测试，不计费。上线前，请先联系合作方获取定制的发布版SDK。:zap:

## 2. 运行环境

可运行于`Android 4.0(API level 14)`及以上版本。

## 3. SDK包导入及权限配置
### 3.1 导入aar包
以AndroidStudio为例。将广告的aar包复制到您工程的 libs 目录下，在对应的 build.gradle 文件里面添加如下配置：
```gradle
repositories {
    flatDir {
        dirs 'libs'
    }
}

dependencies {
    implementation(name:'OctopusADSDK_v1.0.3_debug',ext:'aar')
}
```
点击 `Sync Now` 等待同步结束。

### 3.2 添加权限
如果您打包 App 时的 targetSdkVersion >= 23：请先获取到 SDK 要求的所有权限，然后再调用 SDK 的广告接口。
否则 SDK 将无法工作，我们建议您在 App 启动时就去获取 SDK 需要的权限，Demo 工程中的 SplashActivity 也提供了基本的权限处理示例代码供开发者参考。
```xml
<manifest>
    <!--必须要有的权限-->
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.AUTHENTICATE_ACCOUNTS" />
    <uses-permission android:name="android.permission.WRITE_SYNC_SETTINGS" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.BLUETOOTH" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <!-- 精确定位 -->
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <!-- targetSdkVersion >= 26 时需要配置此权限，否则无法进行安装app的动作 -->
    <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />
    <uses-permission android:name="android.permission.GET_TASKS" />
    <!-- 如果有视频相关的广告且使用textureView播放，请务必添加，否则黑屏 -->
    <uses-permission android:name="android.permission.WAKE_LOCK" />
</manifest>
```

## 4. 广告接入
### 4.1 初始化
在调用SDK请求广告代码前，开发者需要在Application#onCreate()方法中调用以下代码来初始化sdk。
```java
public class App extends Application {
     @Override
    public void onCreate() {
         super.onCreate();
         /**
         * SDK初始化
         * public OctopusADSDK init(Context context, boolean isDebug)
         * isDebug为开启测试模式（不计费），开启SDK日志 正式上线前关闭!!!
         */
        OctopusADSDK.getInstance().init(this, true);
    }
}
```
详细代码参考Demo中的base/App。

### 4.2 开屏广告(OSplash)

当您的APP targetSdkVersion >= 23时，务必在广告请求前获取相应的权限。

**4.2.1 接入示例**
```java
public class SplashActivity extends BaseActivity<ActivitySplashBinding> {
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
}
```
详细代码参考Demo中的SplashActivity。

**4.2.2 OSplash主要API**
```java
public class OSplash{
    /**
     * 开屏广告初始化
     * @param activity 开屏广告展示的Activity
     * @param viewGroup 承载开屏广告的父容器
     */
    public OSplash(@NonNull Activity activity, @NonNull ViewGroup viewGroup){ }
    
    /**
     * 设置开屏广告加载监听
     * @param oSplashListener 监听器
     */
    public OSplash setOSplashListener(OSplashListener oSplashListener) { }
    
    /**
     * 广告加载
     */
    public final void load() { }
}
```

### 4.3 Banner广告(OBanner)

当您的APP targetSdkVersion >= 23时，务必在广告请求前获取相应的权限。

**当界面关闭时务必调用`mOBanner.destroy()`释放资源。**

**4.3.1 接入示例**
```java
public class BannerActivity extends BaseActivity<ActivityBannerBinding> {
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
    protected void onDestroy() {
        super.onDestroy();
        if (mOBanner != null) {
            //4.销毁。释放资源
            mOBanner.destroy();
        }
    }
}
```
详细代码参考Demo中的BannerActivity。

**4.3.2 OBanner主要API**
```java
public class OBanner{
    /**
     * 构造方法
     * 
     * @param activity 展示Banner广告的Activity
     * @param viewGroup 承载Banner广告的父容器
     */
    public OBanner(Activity activity, ViewGroup viewGroup) {}
    
    /**
     * 设置Banner广告加载监听器
     *
     * @param OBannerListener
     */
    public void setOBannerListener(OBannerListener OBannerListener) {}
    
     /**
     * 广告加载
     */
    public final void load() { }
    
     /**
     * 广告销毁。释放资源
     */
    public final void destroy() { }
}
```

### 4.4 信息流广告(OFeedList)

当您的APP targetSdkVersion >= 23时，务必在广告请求前获取相应的权限。

**当界面关闭时务必调用`mOFeedList.destroy()`释放资源。**

每次请求返回一个广告，如需多个信息流广告，可多次调用请求。

**4.4.1 示例代码**
```java
public class FeedListActivity extends BaseActivity<ActivityFeedListBinding> {
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
            
            /**
             * 广告关闭：dislike
             */
            @Override
            public void onAdClose() {
                showToast("广告关闭");
            }
        });
        //3.加载广告
        mOFeedList.load();
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
```
详细代码参考Demo中的FeedListActivity。

**4.4.2 OFeedList主要API**
```java
public class OFeedList{
    /**
     * 构造方法
     *
     * @param activity   展示信息流的Activity
     * @param mViewGroup 承载信息流的容器
     */
    public OFeedList(Activity activity, ViewGroup mViewGroup) {}
    
    /**
     * 设置广告监听
     * @param OInteractionListener
     */
    public void setOInteractionListener(OInteractionListener OInteractionListener) {}
    
    /**
     * 广告加载
     */
    public final void load() { }
    
     /**
     * 广告销毁。释放资源
     */
    public final void destroy() { }
}
```
### 4.5 激励视频广告(ORewardVideo)
当您的APP targetSdkVersion >= 23时，务必在广告请求前获取相应的权限。

该广告的效果为观看完毕视频广告，发放奖励给用户。可配置服务器回调让您判定是否提供奖励给观看广告的用户。
支持的广告尺寸： 全屏竖屏播放和横屏，默认竖屏（观看效果更佳）。

正式接入前需要提供：奖励名称、奖励数量。

**4.5.1 示例代码**
```java
public class RewardVideoActivity extends BaseActivity<ActivityRewardVideoBinding> {
    /**
     * 获取激励视频广告
     */
    public void fetchRewardVideoAD() {
        //1.实例化激励视频广告
        mORewardVideo = new ORewardVideo(this);
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
}
```
详细代码参考Demo中的RewardVideoActivity。

**4.5.2 ORewardVideo主要API**
```java
public class ORewardVideo{
     /**
     * 激励视频广告构造方法（不需要服务器判定）
     *
     * @param activity 激励视频广告展示的Activity
     */
    public ORewardVideo(@NonNull Activity activity) {}

    /**
     * 激励视频广告构造方法（需要服务器判定）
     * @param activity
     * @param oAdSlot 数据槽。提供校验的奖励名称、奖励数量、userId（可为空）
     */
    public ORewardVideo(@NonNull Activity activity, @NonNull OAdSlot oAdSlot) {}

    /**
     * 设置激励视频广告监听
     * @param ORewardVideoListener
     */
    public void setORewardVideoListener(ORewardVideoListener ORewardVideoListener) {}

    /**
     * 设置下载类广告下载状态监听
     * @param ODownloadListener
     */
    public void setODownloadListener(ODownloadListener ODownloadListener) {}
}
```

**4.5.3 服务器回调**

服务器回调让您判定是否提供奖励给观看广告的用户。当用户成功看完广告时，以通知您用户完成了操作。需要提供给合作方回调地址。

**SDK广告初始化**

初始化时需要传入OAdSlot（如果服务器不需要根据userID判断，也不需要附加参数，可用`ORewardVideo(@NonNull Activity activity)`创建）。
```
 mORewardVideo = new ORewardVideo(this, new OAdSlot.Builder()
               //用户标识。可为空
               .setUserID("9527")
               //奖励名称
               .setRewardName("金币")
               //奖励数量
               .setRewardAmount(100)
               //附加参数，可选
               .setMediaExtra("")
               .build());
```

**回调方式**

服务器会以 GET 方式请求第三方服务的回调链接，并拼接以下参数回传：

user_id=%s&trans_id=%s&reward_name=%s&reward_amount=%d&extra=%s&sign=%s

|字段名称|字段定义|字段类型|备注|
|----|---|---|---|
|user_id|用户id|String|调用SDK透传，应用对用户的唯一标识|
|trans_id|交易id|String|完成观看的唯一交易ID|
|reward_name|奖励名称|String|媒体平台配置或调用SDK传入|
|reward_amount|奖励数量|int|媒体平台配置或调用SDK传入|
|extra|附加参数（可选）|String|调用SDK传入并透传，如无需要则为空|
|sign|签名|String|SecurityKey:transId拼接的SHA256加密后的签名|

**签名生成方式**
+ app_security_key: 媒体平台新建奖励视频代码位获取到的密钥 SecurityKey
+ transId：交易id 
+ sign = sha256(SecurityKey:transId)

python示例：
```python
import hashlib

if __name__ == "__main__":
    trans_id = "6FEB23ACB0374985A2A52D282EDD5361u6643"
    app_security_key = "7ca31ab0a59d69a42dd8abc7cf2d8fbd"
    check_sign_raw = "%s:%s" % (app_security_key, trans_id)
    sign = hashlib.sha256(check_sign_raw).hexdigest()
```

**返回约定**

返回 json 数据，字段如下：

|字段名称|字段定义|字段类型|备注|
|----|---|---|---|
|isValid|校验结果|bool|判定结果，是否发放奖励|
示例：
```json
{
    "isValid": true
}
```


[release-image]:https://img.shields.io/badge/release-v1.0.3-brightgreen.svg
[release-url]:https://github.com/dorunad/OctopusSDKDemo/tree/master/app/libs

[minSdkVersion-image]:https://img.shields.io/badge/minSdkVersion-14-yellowgreen.svg
[minSdkVersion-url]:https://android-arsenal.com/api?level=14
