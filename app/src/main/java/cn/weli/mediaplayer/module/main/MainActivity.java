package cn.weli.mediaplayer.module.main;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.weli.mediaplayer.R;
import cn.weli.mediaplayer.adpter.MyViewPageAdapter;
import cn.weli.mediaplayer.base.BaseActivity;
import cn.weli.mediaplayer.bean.SongData;
import cn.weli.mediaplayer.constant.SongsConstant;
import cn.weli.mediaplayer.helper.MediaHelper;
import cn.weli.mediaplayer.manager.CallbackListener;
import cn.weli.mediaplayer.manager.MediaManager;
import cn.weli.mediaplayer.module.detail.MusicDetailActivity;
import cn.weli.mediaplayer.utils.ThreadPoolUtil;

public class MainActivity extends BaseActivity<MainPresenter, IMainView> implements IMainView, CallbackListener, View.OnClickListener {

    private boolean hasInitThread = false;
    private MediaManager mMediaManager;

    @BindView(R.id.main_vp)
    ViewPager mViewPager;
    @BindView(R.id.name_txt)
    TextView mNameTxt;
    @BindView(R.id.author_txt)
    TextView mAuthor;
    @BindView(R.id.icon_img)
    ImageView mImgIcon;
    @BindView(R.id.my_txt)
    TextView mMyTxt;
    @BindView(R.id.find_txt)
    TextView mFindTxt;
    @BindView(R.id.progress)
    ProgressBar mProgress;
    @BindView(R.id.container_enterDetail)
    LinearLayout mEnterDetail;
    @BindView(R.id.progress_container)
    LinearLayout mProgressContainer;
    @BindView(R.id.bottom_container)
    LinearLayout mBottomContainer;
    @BindView(R.id.music_status)
    ImageView mMusicStatusImg;

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
        initView();
        initData();
        setViewPagerChangeListener();
        setListener();
    }


    /**
     * 初始化控件
     */
    private void initView() {
        mMediaManager = MediaManager.getInstance();
        mMediaManager.registCallbackListener(this);
        mPresenter.initFragment();  //初始化fragment

        mMyTxt.setTextColor(Color.RED);
        mFindTxt.setTextColor(Color.BLACK);

        if (mMediaManager.mMediaPlayer.isPlaying()) {
            mProgressContainer.setVisibility(View.VISIBLE);
            mBottomContainer.setVisibility(View.VISIBLE);
        } else {
            mProgressContainer.setVisibility(View.GONE);
            mBottomContainer.setVisibility(View.GONE);
        }

    }

    /**
     * 初始化数据
     */
    private void initData() {

        if (mMediaManager.mMediaPlayer.isPlaying()) {     //正在播放
            setData(SongsConstant.MUSIC_STATUS_PLAY);
        } else if (mMediaManager.isPause()) {
            setData(SongsConstant.MUSIC_STATUS_PAUSE);
        }
        if (!MediaManager.getInstance().mMediaPlayer.isPlaying() && !MediaManager.getInstance().isPause()) {
            mPresenter.stopPlay();
        }
    }


    /**
     * viewpager的监听
     */
    private void setViewPagerChangeListener() {
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {

                    if (mMediaManager.mMediaPlayer.isPlaying()) {
                        mProgressContainer.setVisibility(View.VISIBLE);
                        mBottomContainer.setVisibility(View.VISIBLE);
                    } else {
                        mProgressContainer.setVisibility(View.GONE);
                        mBottomContainer.setVisibility(View.GONE);
                    }

                    mMyTxt.setTextColor(Color.RED);
                    mFindTxt.setTextColor(Color.BLACK);

                } else if (position == 1) {
                    if (mMediaManager.mMediaPlayer.isPlaying()) {
                        mProgressContainer.setVisibility(View.VISIBLE);
                        mBottomContainer.setVisibility(View.VISIBLE);
                    } else {
                        mProgressContainer.setVisibility(View.GONE);
                        mBottomContainer.setVisibility(View.GONE);
                    }

                    mMyTxt.setTextColor(Color.BLACK);
                    mFindTxt.setTextColor(Color.RED);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setListener() {
        mMyTxt.setOnClickListener(this);
        mFindTxt.setOnClickListener(this);
        mMusicStatusImg.setOnClickListener(this);
        mEnterDetail.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_txt:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.find_txt:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.music_status:     //音乐状态
                if (MediaHelper.isPlaySongData.status == SongsConstant.MUSIC_STATUS_PLAY) {    //正在播放
                    mMediaManager.pause();
                } else if (MediaHelper.isPlaySongData.status == SongsConstant.MUSIC_STATUS_PAUSE) {    //暂停
                    int position = MediaHelper.isPlayList.indexOf(MediaHelper.isPlaySongData);
                    mMediaManager.play(MediaHelper.isPlaySongData);
                }
                break;
            case R.id.container_enterDetail:
                Intent intent = new Intent(MainActivity.this, MusicDetailActivity.class);
                startActivity(intent);  //进入音乐详情
                break;
        }
    }


    /**
     * 设置适配器
     *
     * @param list
     */
    @Override
    public void addFragmet(List<Fragment> list) {
        //构造适配器
        MyViewPageAdapter adapter = new MyViewPageAdapter(getSupportFragmentManager(), list);
        mViewPager.setAdapter(adapter);
    }

    @Override
    public void play(int oldId, int nowId) {
        setData(SongsConstant.MUSIC_STATUS_PLAY);
    }

    @Override
    public void pause(int id) {
        setData(SongsConstant.MUSIC_STATUS_PAUSE);
    }

    @Override
    public void stop(int id) {
        setData(SongsConstant.MUSIC_STATUS_STOP);
    }


    /**
     * 设置数据
     */
    public void setData(int status) {
        SongData songData = MediaHelper.isPlaySongData;
        mProgress.setMax(songData.duration);
        mNameTxt.setText(songData.songName);
        mAuthor.setText(songData.singer);
        if (MediaHelper.isPlaySongData != null) {
            mImgIcon.setImageBitmap(MediaHelper.getSongAlbumBitmap(MediaHelper.isPlaySongData.songMediaId
                    , MediaHelper.isPlaySongData.albumId));
        }
        if (status == SongsConstant.MUSIC_STATUS_STOP) {
            mMusicStatusImg.setImageResource(R.drawable.waitplay);
        } else if (status == SongsConstant.MUSIC_STATUS_PLAY) {
            mMusicStatusImg.setImageResource(R.drawable.isplay);
        } else if (status == SongsConstant.MUSIC_STATUS_PAUSE) {
            mMusicStatusImg.setImageResource(R.drawable.waitplay);
        }

        if (mMediaManager.mMediaPlayer.isPlaying() || mMediaManager.isPause()) {
            mProgressContainer.setVisibility(View.VISIBLE);
            mBottomContainer.setVisibility(View.VISIBLE);
        } else {
            mProgressContainer.setVisibility(View.GONE);
            mBottomContainer.setVisibility(View.GONE);
        }

        initProgressThread();
    }

    /**
     * 初始化进度线程
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
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    protected Class<IMainView> getViewClass() {
        return IMainView.class;
    }

    @Override
    protected Class<MainPresenter> getPresenterClass() {
        return MainPresenter.class;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaHelper.saveIsPlaySongId(MediaHelper.isPlaySongData.id);
    }

}
