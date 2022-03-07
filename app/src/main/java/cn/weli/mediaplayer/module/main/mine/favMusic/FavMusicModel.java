package cn.weli.mediaplayer.module.main.mine.favMusic;

import java.util.List;

import cn.weli.mediaplayer.bean.SongData;
import cn.weli.mediaplayer.db.SongDataDbHelper;
import cn.weli.mediaplayer.helper.DbOperateHelper;
import io.reactivex.disposables.Disposable;

public class FavMusicModel {

    /**
     * 查询所有收藏音乐
     */
    public void queryFavSongs(){

        new DbOperateHelper<List<SongData>>() {
            @Override
            protected List<SongData> doInBackground() {
                return  SongDataDbHelper.getInstance().loadFavSong();
            }

            @Override
            protected void dealResult(List<SongData> result) {
                mOnQueryEndListener.queryFavSongs(result);
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
        void queryFavSongs(List<SongData> list);
    }

    public void setOnQueryEndListener(OnQueryEndListener mOnQueryEndListener) {
        this.mOnQueryEndListener = mOnQueryEndListener;
    }
}
