package cn.weli.mediaplayer.module.main.mine.favMusic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.weli.mediaplayer.R;
import cn.weli.mediaplayer.adpter.MusicRecyleViewAdapter;
import cn.weli.mediaplayer.base.BaseFragment;
import cn.weli.mediaplayer.bean.SongData;
import cn.weli.mediaplayer.constant.SongsConstant;
import cn.weli.mediaplayer.helper.BroadcastHelper;

public class FavMusicFragment extends BaseFragment<FavMusicPresenter, IFavMusicView> implements IFavMusicView {


    private View mFragmentView;
    private boolean isFragmentInit = false;
    private MusicRecyleViewAdapter adapter;
    private BroadcastReceiver mBroadcastReceiver;

    @BindView(R.id.recy_music_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.load_txt)
    TextView mLoadTxt;

    @Override
    protected Class<FavMusicPresenter> getPresenterClass() {
        return FavMusicPresenter.class;
    }

    @Override
    protected Class<IFavMusicView> getViewClass() {
        return IFavMusicView.class;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mFragmentView == null) {
            mFragmentView = inflater.inflate(R.layout.music_layout, container, false);
            ButterKnife.bind(this, mFragmentView);
            initView();
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

        //设置RecyclerView保持固定的大小，这样可以优化RecyclerView的性能
        mRecyclerView.setHasFixedSize(true);
        //线性布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        //设置RecyclerView滚动方向
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        //为RecyclerView设置布局管理器
        mRecyclerView.setLayoutManager(layoutManager);
        mPresenter.obtainFavSongs();
        initBroadcastReceiver();
    }

    private void initBroadcastReceiver() {

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (SongsConstant.ACTION_UPDATE_FAV_MUSIC.equals(intent.getAction())) {
                    mPresenter.dealReceiver();
                }
            }
        };

        BroadcastHelper.registerLocalBroadcast(mBroadcastReceiver, SongsConstant.ACTION_UPDATE_FAV_MUSIC);

    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        if (isVisibleToUser) {    //用户可见
//            if (isFragmentInit) {
//                mPresenter.obtainFavSongs();
//            }
//        }
//    }

    @Override
    public void initPlayList(List<SongData> list) {

        if (list.size() > 0) {
            mLoadTxt.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mLoadTxt.setText("暂无收藏音乐...");
        }
        if(!isFragmentInit ) {
            adapter = new MusicRecyleViewAdapter(list, SongsConstant.TYPE_FAV);
            mRecyclerView.setAdapter(adapter);
            //点击监听
            adapter.setOnItemClickListener(new MusicRecyleViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(SongData songData, int position) {
                    mPresenter.dealItemClick(songData, position);
                }

                @Override
                public void onItemLongClick(SongData songData) {

                }

                @Override
                public void hasFav(SongData songData) {
                }
            });
            isFragmentInit = true;
        }else {
            adapter.setMusicList(list);
        }

    }

    @Override
    public void setData(List<SongData> list) {
        adapter.setMusicList(list);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BroadcastHelper.unRegisterLocalBroadcast(mBroadcastReceiver);
    }
}
