package cn.weli.mediaplayer.module.main;


import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import cn.weli.mediaplayer.base.IPresenter;
import cn.weli.mediaplayer.module.main.find.FindFragment;
import cn.weli.mediaplayer.module.main.mine.MineFragment;

public class MainPresenter implements IPresenter {

    private IMainView mView;
    private MainModel mMainModel;

    public MainPresenter(IMainView view) {
        mView = view;
        mMainModel = new MainModel();
    }

    /**
     * 添加fragment
     */
    private List<Fragment> addFragment() {
        List<Fragment> list = new ArrayList<>();
        list.add(new MineFragment());
        list.add(new FindFragment());
        return list;
    }

    /**
     * 添加Fragment
     */
    public void initFragment(){
        mView.addFragmet(addFragment());
    }

    /**
     * 数据库置为停止
     */
    public void stopPlay(){
        mMainModel.stopMusicById();
    }

}
