# OctopusSDKDemo Android

[![release-image]][release-url]
[![minSdkVersion-image]][minSdkVersion-url]


## 简介
**版本历史**

| 版本         | 修订日期          | 说明  |
|:------------:|:------------:| :------: |
| v1.0.1        | 2019/6     |  初版  |

东润移动广告SDk(Android)是官方推出的移动广告SDK在Android平台上的版本（以下简称SDK）。

SDK的发行版本包括 AAR 包、Demo 示例、接入文档（您当前阅读的文档）。

:zap:Demo下的SDK为通用测试版，仅用于接入测试，不计费。上线前，请先联系合作方获取定制的发布版SDK。:zap:

## 运行环境

可运行于`Android 4.0(API level 14)`及以上版本。

## SDK包导入及权限配置
### 导入aar包
以AndroidStudio为例。将广告的aar包复制到您工程的 libs 目录下，在对应的 build.gradle 文件里面添加如下配置：
```gradle
repositories {
    flatDir {
        dirs 'libs'
    }
}

dependencies {
    implementation(name:'OctopusADSDK_v1.0.1_debug',ext:'aar')
}
```
点击 `Sync Now` 等待同步结束。

### 添加权限
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

## 广告接入
### 初始化
在调用SDK请求广告代码前，开发者需要在Application#onCreate()方法中调用以下代码来初始化sdk。
```java
public class App extends Application {
     @Override
    public void onCreate() {
         super.onCreate();
         /**
         * SDK初始化
         * public OctopusADSDK init(Context context, boolean isDebug)
         * isDebug为开启测试模式（不计费），开启SDK日志 正式上线前关闭!
         */
        OctopusADSDK.getInstance().init(this, true);
    }
}
```

### 开屏广告

当您的APP targetSdkVersion >= 23时，务必在广告请求前获取相应的权限。

**接入示例**
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
**主要API**
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

### Banner广告

当您的APP targetSdkVersion >= 23时，务必在广告请求前获取相应的权限。

**当界面关闭时务必调用`mOBanner.destroy()`释放资源。**

**接入示例**
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
**主要API**
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

### 信息流广告

当您的APP targetSdkVersion >= 23时，务必在广告请求前获取相应的权限。

**当界面关闭时务必调用`mOFeedList.destroy()`释放资源。**

每次请求返回一个广告，如需多个信息流广告，可多次调用请求。

**示例代码**
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

**主要API**
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

[release-image]:https://img.shields.io/badge/release-v1.0.1-brightgreen.svg
[release-url]:https://github.com/dorunad/OctopusSDKDemo/tree/master/app/libs

[minSdkVersion-image]:https://img.shields.io/badge/minSdkVersion-14-yellowgreen.svg
[minSdkVersion-url]:https://android-arsenal.com/api?level=14
