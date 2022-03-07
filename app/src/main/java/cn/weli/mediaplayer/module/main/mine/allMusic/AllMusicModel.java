package cn.weli.mediaplayer.module.main.mine.allMusic;

import java.util.List;

import cn.weli.mediaplayer.bean.SongData;
import cn.weli.mediaplayer.db.SongDataDbHelper;
import cn.weli.mediaplayer.helper.DbOperateHelper;
import io.reactivex.disposables.Disposable;

public class AllMusicModel {


    public AllMusicModel() {
    }

    /**
     * 获取所有音乐
     *
     * @return
     */
    public void querySongs() {

        new DbOperateHelper<List<SongData>>() {
            @Override
            protected List<SongData> doInBackground() {
                return SongDataDbHelper.getInstance().getAllSongs();
            }

            @Override
            protected void dealResult(List<SongData> list) {
                mOnQueryEndListener.queryAllSongs(list);
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

    /**
     * 获取所有音乐
     *
     * @return
     */
    public void querySongById(final int id) {
        new DbOperateHelper<SongData>() {
            @Override
            protected SongData doInBackground() {
                return SongDataDbHelper.getInstance().getDataById(id);
            }

            @Override
            protected void dealResult(SongData result) {

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


    /**
     * 更新音乐状态
     *
     * @param id
     * @param status
     */
    public void upSongDbStatus(final int id, final int status) {

        new DbOperateHelper<Void>() {
            @Override
            protected Void doInBackground() {
                SongDataDbHelper.getInstance().updateStatusById(id, status);
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

    /**
     * 更新音乐类型
     *
     * @param id
     * @param type
     */
    public void upSongDbTypeById(final int id, final int type) {
        new DbOperateHelper<Void>() {
            @Override
            protected Void doInBackground() {
                SongDataDbHelper.getInstance().updateTypeypeById(id, type);
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

    /**
     * 通过id进行收藏
     */
    public void setDbFavById(final int id, final int isFav) {
        new DbOperateHelper<Void>() {
            @Override
            protected Void doInBackground() {
                SongDataDbHelper.getInstance().setFavOrNotById(id, isFav);
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


    private OnQueryEndListener mOnQueryEndListener;

    public interface OnQueryEndListener {
        void queryAllSongs(List<SongData> list);
    }

    public void setOnQueryEndListener(OnQueryEndListener mOnQueryEndListener) {
        this.mOnQueryEndListener = mOnQueryEndListener;
    }

}
