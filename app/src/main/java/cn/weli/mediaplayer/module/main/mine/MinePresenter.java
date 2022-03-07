package cn.weli.mediaplayer.module.main.mine;

import cn.weli.mediaplayer.base.IPresenter;

public class MinePresenter implements IPresenter {

    private IMineView mView;

    public MinePresenter(IMineView view){
        mView = view;
    }

}
