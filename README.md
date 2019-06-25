<h3>OctopusSDKDemo Android</h3>

[![release-image]][release-url]
[![minSdkVersion-image]][minSdkVersion-url]

## 简介
**版本历史**

| 版本         | 修订日期          | 说明  |
|:------------:|:------------:| :------: |
| v1.0.1        | 2019/6     |  初版  |

东润移动广告SDk(Android)是官方推出的移动广告SDK在Android平台上的版本（以下简称SDK）。

SDK的发行版本包括 AAR 包、[Demo 示例][demo-url]、接入文档（您当前阅读的文档）。

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
在调用SDK代码前，调用初始化方法。


[release-image]:https://img.shields.io/badge/release-v1.0.1-brightgreen.svg
[release-url]:https://github.com/dorunad/OctopusSDKDemo/tree/master/app/libs

[minSdkVersion-image]:https://img.shields.io/badge/minSdkVersion-14-yellowgreen.svg
[minSdkVersion-url]:https://android-arsenal.com/api?level=14

[demo-url]:https://github.com/dorunad/OctopusSDKDemo
