package cn.weli.mediaplayer.module.splash;

import java.util.List;

import cn.weli.mediaplayer.bean.SongData;
import cn.weli.mediaplayer.db.SongDataDbHelper;
import cn.weli.mediaplayer.helper.MediaHelper;

public class SplashModel {

    /**
     * 获取所有音乐并插入数据库
     */
    public void insertAllMusic(){
        MediaHelper mediaHelper = new MediaHelper();
        SongDataDbHelper dbHelper = SongDataDbHelper.getInstance();
        List<SongData> list = mediaHelper.obtainLoalSongs();
        for(SongData songData :list){
            dbHelper.insert(songData);
        }
    }

}
