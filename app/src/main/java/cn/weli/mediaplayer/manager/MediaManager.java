package cn.weli.mediaplayer.manager;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.weli.mediaplayer.bean.SongData;
import cn.weli.mediaplayer.helper.MediaHelper;
import cn.weli.mediaplayer.helper.NotificationHelper;

import static cn.weli.mediaplayer.MediaApplication.appCtx;

/**
 * 媒体管理类
 */
public class MediaManager {

    public MediaPlayer mMediaPlayer;
    private List<CallbackListener> mList = new ArrayList<>();
    private boolean isPause = false;
    private int oldPlayId = -1;

    /**
     * 单例
     */
    private static MediaManager mMediaManager;

    public static MediaManager getInstance() {
        if (mMediaManager == null) {
            synchronized (MediaManager.class) {
                if (mMediaManager == null) {
                    mMediaManager = new MediaManager();
                }
            }
        }
        return mMediaManager;
    }

    private MediaManager() {
        mMediaPlayer = new MediaPlayer();

        /**
         * 播放完监听
         */
        playCompletionListener();

        /**
         * 播放错误监听
         */
        playErrorListener();

    }


    /**
     * 播放音乐
     *
     * @param songData
     */
    public void play(SongData songData) {
        isPause = false;
        //当前播放的songData
        MediaHelper.isPlaySongData = songData;
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);      // 指定参数为音频文件
            mMediaPlayer.setDataSource(songData.path);   //为多媒体对象设置播放路径
            mMediaPlayer.prepare();  //准备播放
            mMediaPlayer.start();

//            saveIsPlayMusicAlbum();     //存储专辑图片
            for (CallbackListener callback : mList) {
                callback.play(oldPlayId, songData.id);
            }
            oldPlayId = songData.id;
            sendNotification(songData);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 暂停播放
     */
    public void pause() {
        isPause =true;
        if (MediaHelper.isPlaySongData == null || MediaHelper.isPlayList == null) {
            return;
        }

        if (mMediaPlayer != null) {     //暂停
            mMediaPlayer.pause();
        }

        for (CallbackListener callback : mList) {
            callback.pause(MediaHelper.isPlaySongData.id);
        }

        sendNotification(MediaHelper.isPlaySongData);
    }

    /**
     * 下一首
     */
    public void next() {

        if (MediaHelper.isPlaySongData == null || MediaHelper.isPlayList == null) {
            return;
        }
        int position = -1;
        for (SongData sd : MediaHelper.isPlayList) {
            if (sd.id == MediaHelper.isPlaySongData.id) {
                position = MediaHelper.isPlayList.indexOf(sd);
            }
        }
        if (position == MediaHelper.isPlayList.size() - 1) {
            position = -1;
        }
        position = position + 1;

        play(MediaHelper.isPlayList.get(position));

    }

    /**
     * 上一首
     */
    public void last() {

        if (MediaHelper.isPlaySongData == null || MediaHelper.isPlayList == null) {
            return;
        }

        int position = -1;
        for (SongData sd : MediaHelper.isPlayList) {
            if (sd.id == MediaHelper.isPlaySongData.id) {
                position = MediaHelper.isPlayList.indexOf(sd);
            }
        }
        if (position == 0) {
            position = MediaHelper.isPlayList.size();
        }
        position = position - 1;

        play(MediaHelper.isPlayList.get(position));
    }

    /**
     * 停止播放
     */
    public void stop() {
        isPause = false;
        if (MediaHelper.isPlaySongData == null || MediaHelper.isPlayList == null) {
            return;
        }

        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

        for (CallbackListener callback : mList) {
            callback.stop(MediaHelper.isPlaySongData.id);
        }


        sendNotification(MediaHelper.isPlaySongData);
    }


    /**
     * @return
     */
    public boolean isPause() {
        return isPause;
    }


    /**
     * 发送广播
     *
     * @param songData
     */
    private void sendNotification(SongData songData) {
        if (NotificationHelper.isNotificationEnable(appCtx)) {
            NotificationHelper helper = new NotificationHelper();
            helper.sendNotification(0x123, songData);
        }
    }

    /**
     * 播放错误监听
     */
    private void playErrorListener() {
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                return true;    //必须返回true
            }
        });
    }

    /**
     * 下一首监听
     */
    private void playCompletionListener() {
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                next();
            }
        });
    }


    /**
     * 注册监听
     *
     * @param callbackListener
     */
    public void registCallbackListener(CallbackListener callbackListener) {
        mList.add(callbackListener);
    }


}
