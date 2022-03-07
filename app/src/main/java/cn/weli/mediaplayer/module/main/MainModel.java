package cn.weli.mediaplayer.module.main;

import cn.weli.mediaplayer.constant.SongsConstant;
import cn.weli.mediaplayer.db.SongDataDbHelper;
import cn.weli.mediaplayer.helper.DbOperateHelper;
import cn.weli.mediaplayer.helper.MediaHelper;
import io.reactivex.disposables.Disposable;

public class MainModel {

    /**
     * 将之前推出app时播放的歌曲置为停止
     */
    public void stopMusicById(){
        new DbOperateHelper<Void>() {
            @Override
            protected Void doInBackground() {
                int songId = MediaHelper.readIsPlaySongId();
                if(songId > 0) {
                    SongDataDbHelper.getInstance().updateStatusById(songId, SongsConstant.MUSIC_STATUS_STOP);
                }
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
