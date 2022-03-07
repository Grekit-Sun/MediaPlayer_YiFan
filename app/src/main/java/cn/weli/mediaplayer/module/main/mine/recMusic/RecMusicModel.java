package cn.weli.mediaplayer.module.main.mine.recMusic;

import java.util.List;

import cn.weli.mediaplayer.bean.SongData;
import cn.weli.mediaplayer.db.SongDataDbHelper;
import cn.weli.mediaplayer.helper.DbOperateHelper;
import io.reactivex.disposables.Disposable;

public class RecMusicModel {

    /**
     * 查询所有最近音乐
     */
    public void queryRecSongs(){
        new DbOperateHelper<List<SongData>>() {
            @Override
            protected List<SongData> doInBackground() {
                return  SongDataDbHelper.getInstance().loadRecSong();
            }

            @Override
            protected void dealResult(List<SongData> result) {
                mOnQueryEndListener.queryRecSongs(result);
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


    private OnQueryEndListener mOnQueryEndListener;

    public interface OnQueryEndListener {
        void queryRecSongs(List<SongData> list);
    }

    public void setOnQueryEndListener(OnQueryEndListener mOnQueryEndListener) {
        this.mOnQueryEndListener = mOnQueryEndListener;
    }
}
