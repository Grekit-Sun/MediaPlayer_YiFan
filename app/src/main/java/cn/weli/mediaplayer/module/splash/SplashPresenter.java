package cn.weli.mediaplayer.module.splash;

import cn.weli.mediaplayer.base.IPresenter;
import cn.weli.mediaplayer.helper.DbOperateHelper;
import io.reactivex.disposables.Disposable;

public class SplashPresenter implements IPresenter {

    private ISplashView mView;
    private SplashModel mSplashModel;


    public SplashPresenter(ISplashView view) {
        mView = view;
        mSplashModel = new SplashModel();
        mView.startToMainNow();
    }


    /**
     * 获取本地音乐并且插入(异步)
     */
    public void initMusic() {

        new DbOperateHelper<Void>() {
            @Override
            protected Void doInBackground() {
                mSplashModel.insertAllMusic();       //获取本地音乐并且插入数据库
                return null;
            }

            @Override
            protected void dealResult(Void result) {

            }

            @Override
            protected void postException(Throwable t) {

            }

            @Override
            protected void onComplete() {

            }

            @Override
            protected void acceptDisposable(Disposable disposable) {

            }
        };

    }


}
