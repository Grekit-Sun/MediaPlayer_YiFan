package cn.weli.mediaplayer.module.detail;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.weli.mediaplayer.R;
import cn.weli.mediaplayer.base.BaseActivity;
import cn.weli.mediaplayer.constant.SongsConstant;
import cn.weli.mediaplayer.helper.MediaHelper;
import cn.weli.mediaplayer.manager.CallbackListener;
import cn.weli.mediaplayer.manager.MediaManager;
import cn.weli.mediaplayer.utils.ImageUtil;
import cn.weli.mediaplayer.utils.ThreadPoolUtil;


public class MusicDetailActivity extends BaseActivity<MusicDetailPresenter, IMusicDetailView> implements IMusicDetailView, CallbackListener, View.OnClickListener {

    private MediaManager mMediaManager;
    private boolean hasInitThread = false;
    private Animation animation;

    @BindView(R.id.detail_back)
    ImageView mBackImg;
    @BindView(R.id.detail_img)
    ImageView mDetailImg;
    @BindView(R.id.detail_song_name)
    TextView mSongName;
    @BindView(R.id.detail_singer)
    TextView mSinger;
    @BindView(R.id.detail_img_fav)
    ImageView mFavImg;
    @BindView(R.id.detail_lastsong)
    ImageView mLastImg;
    @BindView(R.id.detail_status_play)
    ImageView mPlayImg;
    @BindView(R.id.detail_status_pause)
    ImageView mPauseImg;
    @BindView(R.id.detail_nextsong)
    ImageView mNextImg;
    @BindView(R.id.detail_progress)
    ProgressBar mProgress;


    private Handler sHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123) {
                mProgress.setProgress((Integer) msg.obj);
            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindView();
        //初始化
        initMediaManager();
        initData();
        setListener();
        //初始化动画
        initAnim();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setData();
    }

    private void initData() {
        setData();
    }

    /**
     * 更新信息
     */
    private void initMediaManager() {
        mMediaManager = MediaManager.getInstance();
        mMediaManager.registCallbackListener(this);
    }


    /**
     * 动画初始化
     */
    private void initAnim() {
        animation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        LinearInterpolator interpolator = new LinearInterpolator();     //匀速插值器
        animation.setInterpolator(interpolator);
        if (animation != null) {
            mDetailImg.startAnimation(animation);
        }
    }


    private void setListener() {

        mBackImg.setOnClickListener(this);
        mFavImg.setOnClickListener(this);
        mLastImg.setOnClickListener(this);
        mPlayImg.setOnClickListener(this);
        mPauseImg.setOnClickListener(this);
        mNextImg.setOnClickListener(this);

    }

    /**
     * 点击事件
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.detail_back:      //返回
                finishActivity();
                break;
            case R.id.detail_img_fav:       //点击添加至收藏并且红心变实心
                mPresenter.dealFavItemClick();
                break;
            case R.id.detail_lastsong:
                mMediaManager.last();
                break;
            case R.id.detail_status_play:
                mMediaManager.play(MediaHelper.isPlaySongData);
                break;
            case R.id.detail_status_pause:
                mMediaManager.pause();
                break;
            case R.id.detail_nextsong:
                mMediaManager.next();
                break;
        }
    }

    /**
     * 设置正在播放音乐的数据
     */
    private void setData() {

        if (mMediaManager.mMediaPlayer.isPlaying()) {
            //状态图片
            mPlayImg.setVisibility(View.GONE);
            mPauseImg.setVisibility(View.VISIBLE);
        } else {
            //状态图片
            mPlayImg.setVisibility(View.VISIBLE);
            mPauseImg.setVisibility(View.GONE);
        }

        mProgress.setMax(MediaHelper.isPlaySongData.duration);

        if (MediaHelper.isPlaySongData.isFavorite == SongsConstant.IS_FAV) {
            mFavImg.setImageResource(R.drawable.hasfav);
        } else if (MediaHelper.isPlaySongData.isFavorite == SongsConstant.NOT_FAV) {
            mFavImg.setImageResource(R.drawable.wait_fav);
        }
        mSongName.setText(MediaHelper.isPlaySongData.songName);
        mSinger.setText(MediaHelper.isPlaySongData.singer);
        if (MediaHelper.isPlaySongData != null) {
            mDetailImg.setImageBitmap(ImageUtil.makeRoundCorner(MediaHelper.getSongAlbumBitmap(MediaHelper.isPlaySongData.songMediaId
                    , MediaHelper.isPlaySongData.albumId)));
        }
        //初始化进度线程
        initProgressThread();
    }

    /**
     * 设置红心状态
     */
    @Override
    public void setIsOrNotFav() {
        if (MediaHelper.isPlaySongData.isFavorite == SongsConstant.IS_FAV) {
            mFavImg.setImageResource(R.drawable.hasfav);
        } else if (MediaHelper.isPlaySongData.isFavorite == SongsConstant.NOT_FAV) {
            mFavImg.setImageResource(R.drawable.wait_fav);
        }
    }


    @Override
    public void play(int oldId, int nowId) {
        if (animation != null) {
            mDetailImg.startAnimation(animation);
        }

        setData();
    }

    @Override
    public void pause(int id) {
        if (animation != null) {
            mDetailImg.clearAnimation();
        }
        setData();
    }

    @Override
    public void stop(int id) {
        if (animation != null) {
            mDetailImg.clearAnimation();
        }
        setData();
    }

    /**
     * 获取当前音乐精度线程
     */
    private void initProgressThread() {

        if (!hasInitThread) {
            ThreadPoolUtil.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        int currentPosition = mMediaManager.mMediaPlayer.getCurrentPosition();
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Message message = new Message();
                        message.what = 0x123;
                        message.obj = currentPosition;
                        sHandler.sendMessage(message);
                    }
                }
            });
            hasInitThread = true;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int getLayout() {
        return R.layout.music_detail_layout;
    }

    @Override
    protected void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    protected Class<IMusicDetailView> getViewClass() {
        return IMusicDetailView.class;
    }

    @Override
    protected Class<MusicDetailPresenter> getPresenterClass() {
        return MusicDetailPresenter.class;
    }
}
