package cn.weli.mediaplayer.module.main.mine.recMusic;

import java.util.List;

import cn.weli.mediaplayer.base.IBaseView;
import cn.weli.mediaplayer.bean.SongData;

public interface IRecMusicView extends IBaseView {

    void initPlayList(List<SongData> list);

    void setData(List<SongData> list);
}
