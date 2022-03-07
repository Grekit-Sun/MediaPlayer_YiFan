package cn.weli.mediaplayer.module.main.mine.allMusic;

import android.util.Log;

import java.util.List;

import cn.weli.mediaplayer.base.IPresenter;
import cn.weli.mediaplayer.bean.SongData;
import cn.weli.mediaplayer.constant.SongsConstant;
import cn.weli.mediaplayer.helper.BroadcastHelper;
import cn.weli.mediaplayer.helper.MediaHelper;
import cn.weli.mediaplayer.manager.CallbackListener;
import cn.weli.mediaplayer.manager.MediaManager;

import static android.support.constraint.Constraints.TAG;

public class AllMusicPresenter implements IPresenter, CallbackListener {

    private IAllMusicView mView;
    private AllMusicModel mAllMusicModel;
    private MediaManager mMediaManager;

    private List<SongData> list;    //所有音乐清单
    private boolean hasSetListener = false;
    public static final String TAG = "PLAY";

    /**
     * 设置list
     *
     * @param list
     */
    public void setList(List<SongData> list) {
        this.list = list;

    }


    public AllMusicPresenter(IAllMusicView view) {
        mView = view;
        mAllMusicModel = new AllMusicModel();
        mMediaManager = MediaManager.getInstance();
        mMediaManager.registCallbackListener(this);    //注册回调
    }

    /**
     * 获取所有音乐
     */
    public void obtainAllSongs() {

        mAllMusicModel.querySongs();     //查询

        /**
         * 设置数据库操作监听
         */
        if (!hasSetListener) {
            mAllMusicModel.setOnQueryEndListener(new AllMusicModel.OnQueryEndListener() {
                @Override
                public void queryAllSongs(List<SongData> list) {
                    setList(list);
                    mView.initPlayList(list);
                }
            });
            hasSetListener = true;
        }
    }

    /**
     * 处理点击事件
     *
     * @param songData
     */
    public void dealItemClick(SongData songData, int position) {

        MediaHelper.isPlayList = list;     //正在播放的list

        if (songData.status == SongsConstant.MUSIC_STATUS_STOP) {     //音乐停止->播放
            mMediaManager.play(songData);
        } else if (songData.status == SongsConstant.MUSIC_STATUS_PLAY) {       //音乐播放->暂停
            mMediaManager.pause();
        } else if (songData.status == SongsConstant.MUSIC_STATUS_PAUSE) {      //音乐暂停->播放
            mMediaManager.play(songData);
        }

    }

    /**
     * 处理长按事件(收藏)
     *
     * @param songData
     */
    public void dealItemLongClick(SongData songData) {

        if (songData.isFavorite == SongsConstant.IS_FAV) {    //是收藏
            //不变
        } else {
            songData.isFavorite = SongsConstant.IS_FAV;
            //更新数据库
            mAllMusicModel.setDbFavById(songData.id, SongsConstant.IS_FAV);
            //发送更新收藏的广播
            BroadcastHelper.senLocalBroadcast(SongsConstant.ACTION_UPDATE_FAV_MUSIC);
        }

    }


    @Override
    public void play(int oldId, int nowId) {

        mAllMusicModel.upSongDbStatus(oldId, SongsConstant.MUSIC_STATUS_STOP);
        mAllMusicModel.upSongDbStatus(nowId, SongsConstant.MUSIC_STATUS_PLAY);
        //更新新位置
        int nowPosition = -1;
        for (SongData songData : list) {
            if (songData.id == nowId) {
                nowPosition = list.indexOf(songData);
                songData.status = SongsConstant.MUSIC_STATUS_PLAY;
                list.set(nowPosition, songData);
                Log.e(TAG, "play------------back: ---------------nowPosition" + nowPosition);
                break;
            }
        }
        if (oldId < 0) {
            mView.setData(list);
            return;
        } else if (oldId == nowId) {
            mView.setData(list);
            return;
        } else {

            int oldPosition = -1;
            for (SongData songData : list) {
                if (songData.id == oldId) {
                    oldPosition = list.indexOf(songData);
                    songData.status = SongsConstant.MUSIC_STATUS_STOP;
                    list.set(oldPosition, songData);
                    Log.e(TAG, "play------------back: ---------------oldPosition" + oldPosition);
                    break;
                }
            }
            mView.setData(list);
        }

    }

    @Override
    public void pause(int id) {

        mAllMusicModel.upSongDbStatus(id, SongsConstant.MUSIC_STATUS_PAUSE);
        int position = -1;
        for (SongData songData : list) {
            if (songData.id == id) {
                position = list.indexOf(songData);
                songData.status = SongsConstant.MUSIC_STATUS_PAUSE;
                list.set(position, songData);
                Log.e(TAG, "pause------------back: ---------------position" + position);
                break;
            }
        }
        mView.setData(list);

    }

    @Override
    public void stop(int id) {

        Log.e(TAG, "stop: ***********************");
        mAllMusicModel.upSongDbStatus(id, SongsConstant.MUSIC_STATUS_STOP);
        int position = -1;
        for (SongData songData : list) {
            if (songData.id == id) {
                position = list.indexOf(songData);
                songData.status = SongsConstant.MUSIC_STATUS_STOP;
                list.set(position, songData);
                Log.e(TAG, "stop------------back: ---------------position" + position);
                break;
            }
        }
        mView.setData(list);

    }
}
