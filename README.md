# MediaPlayer_YiFan

### 这是我在公司Android入门练手的一个项目——“简单的音乐播放器”

#### 项目简介

- 功能：包括两部分

  - 我的：此部分为音乐播放模块，里面包含三个部分，全部音乐、收藏音乐、最近播放音乐；
  - 发现：此部分为新闻资讯，因为找的是免费的源，所以5-30分钟更新一次；

- 所需掌握技术：

  - MVP：项目是用的mvp搭建，在base文件夹中是Activity和Fragment的基类；	
  - 我的界面布局:CoordinatorLayout + AppBarLayout;
  
  - ViewPager + Fragment;
  - RecyclerView + Fragment;
  
  - Notification;
  - 数据库操作；
  
  - sp操作；
  - 本地广播；
  
  - 动画；
  - 回调；
  
  - Activity启动模式；
  - RxJava实现线程的切换；
  
  ...
  
- 依赖框架

  - `Butterknife`
  - `AndPermission`
  - `gson`
  - `smartTabLayout`
  - `SmartRefreshLayout`
  - `statusbarutil`
  - `retrofit2 + rxjava2`
  - `Room`
  - `glide`




- 业务流程图



1. 我的

![Image text](https://github.com/Grekit-Sun-Coder/MediaPlayer_YiFan/tree/master/img_floder /逸帆音乐业务流程图.png)



- 所遇问题：

  - 音乐播放时会出现问题必须调用`onError`监听并且返回true

    ```
     mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    return true;    //必须返回true
                }
            });
    ```

  - 数据库操作为耗时操作，需要异步处理；