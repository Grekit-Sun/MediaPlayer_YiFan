package cn.weli.mediaplayer.module.main.mine;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.weli.mediaplayer.R;
import cn.weli.mediaplayer.adpter.MyViewPageAdapter;
import cn.weli.mediaplayer.base.BaseFragment;
import cn.weli.mediaplayer.helper.MediaHelper;
import cn.weli.mediaplayer.manager.CallbackListener;
import cn.weli.mediaplayer.manager.MediaManager;
import cn.weli.mediaplayer.module.main.mine.allMusic.AllMusicFragment;
import cn.weli.mediaplayer.module.main.mine.favMusic.FavMusicFragment;
import cn.weli.mediaplayer.module.main.mine.recMusic.RecMusicFragment;

public class MineFragment extends BaseFragment<MinePresenter, IMineView> implements IMineView, CallbackListener, View.OnClickListener {

    private View mFragmentView;
    private AllMusicFragment mAllMusicFragment;
    private FavMusicFragment mFavMusicFragment;
    private RecMusicFragment mRecMusicFragment;

    @BindView(R.id.music_vp)
    ViewPager mViewPager;
    @BindView(R.id.image_album)
    ImageView mImage;
    @BindView(R.id.all_music_container)
    RelativeLayout mAllMusicContainer;
    @BindView(R.id.favorite_music_container)
    RelativeLayout mFavMusicContainer;
    @BindView(R.id.rec_music_container)
    RelativeLayout mRecMusicContainer;
    @BindView(R.id.all_music_txt)
    TextView mAllMusicTxt;
    @BindView(R.id.fav_music_txt)
    TextView mFavMusicTxt;
    @BindView(R.id.rec_music_txt)
    TextView mRecMusicTxt;

    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (mFragmentView == null) {
            mFragmentView = inflater.inflate(R.layout.mine_layout, container, false);
            mUnbinder = ButterKnife.bind(this, mFragmentView);
            initView();
            initFragment();
            initListener();
        } else {
            if (mFragmentView.getParent() != null) {
                ((ViewGroup) mFragmentView.getParent()).removeView(mFragmentView);
            }
        }
        return mFragmentView;

    }

    /**
     * 初始化
     */
    private void initView() {
        mAllMusicTxt.setTextColor(Color.RED);
        mFavMusicTxt.setTextColor(Color.BLACK);
        mRecMusicTxt.setTextColor(Color.BLACK);
    }

    private void initFragment() {
        mAllMusicFragment = new AllMusicFragment();
        mFavMusicFragment = new FavMusicFragment();
        mRecMusicFragment = new RecMusicFragment();
        List<Fragment> list = new ArrayList<>();
        list.add(mAllMusicFragment);
        list.add(mFavMusicFragment);
        list.add(mRecMusicFragment);

        //构造适配器
        MyViewPageAdapter adapter = new MyViewPageAdapter(getActivity().getSupportFragmentManager(), list);
        mViewPager.setAdapter(adapter);
        //注册监听
        registerListener();
        //设置照片
        if (MediaManager.getInstance().mMediaPlayer.isPlaying()) {     //正在播放
            mImage.setImageBitmap(MediaHelper.getSongAlbumBitmap(MediaHelper.isPlaySongData.songMediaId
                    , MediaHelper.isPlaySongData.albumId));
        } else if (MediaManager.getInstance().isPause()) {
            mImage.setImageBitmap(MediaHelper.getSongAlbumBitmap(MediaHelper.isPlaySongData.songMediaId
                    , MediaHelper.isPlaySongData.albumId));
        }
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        mAllMusicContainer.setOnClickListener(this);
        mFavMusicContainer.setOnClickListener(this);
        mRecMusicContainer.setOnClickListener(this);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mAllMusicTxt.setTextColor(Color.RED);
                    mFavMusicTxt.setTextColor(Color.BLACK);
                    mRecMusicTxt.setTextColor(Color.BLACK);
                } else if (position == 1) {
                    mAllMusicTxt.setTextColor(Color.BLACK);
                    mFavMusicTxt.setTextColor(Color.RED);
                    mRecMusicTxt.setTextColor(Color.BLACK);
                } else if (position == 2) {
                    mAllMusicTxt.setTextColor(Color.BLACK);
                    mFavMusicTxt.setTextColor(Color.BLACK);
                    mRecMusicTxt.setTextColor(Color.RED);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.all_music_container:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.favorite_music_container:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.rec_music_container:
                mViewPager.setCurrentItem(2);
                break;
        }
    }


    private void registerListener() {
        MediaManager.getInstance().registCallbackListener(this);
    }

    @Override
    protected Class<MinePresenter> getPresenterClass() {
        return MinePresenter.class;
    }

    @Override
    protected Class<IMineView> getViewClass() {
        return IMineView.class;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void play(int oldId, int nowId) {
        mImage.setImageBitmap(MediaHelper.getSongAlbumBitmap(MediaHelper.isPlaySongData.songMediaId
                , MediaHelper.isPlaySongData.albumId));
    }

    @Override
    public void pause(int id) {

    }

    @Override
    public void stop(int id) {

    }
}
