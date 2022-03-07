package cn.weli.mediaplayer.module.detail;

import cn.weli.mediaplayer.base.IPresenter;
import cn.weli.mediaplayer.constant.SongsConstant;
import cn.weli.mediaplayer.helper.BroadcastHelper;
import cn.weli.mediaplayer.helper.MediaHelper;
import cn.weli.mediaplayer.module.main.mine.allMusic.AllMusicModel;

public class MusicDetailPresenter implements IPresenter {

    private IMusicDetailView mView;
    private AllMusicModel mAllMusicModel;

    public MusicDetailPresenter(IMusicDetailView view) {
        mView = view;
        mAllMusicModel = new AllMusicModel();
    }


    public void dealFavItemClick() {
        if (MediaHelper.isPlaySongData.isFavorite == SongsConstant.IS_FAV) {
            MediaHelper.isPlaySongData.isFavorite = SongsConstant.NOT_FAV;
            //更新数据库
            mAllMusicModel.setDbFavById(MediaHelper.isPlaySongData.id, SongsConstant.NOT_FAV);
        } else if (MediaHelper.isPlaySongData.isFavorite == SongsConstant.NOT_FAV) {
            MediaHelper.isPlaySongData.isFavorite = SongsConstant.IS_FAV;
            //更新数据库
            mAllMusicModel.setDbFavById(MediaHelper.isPlaySongData.id, SongsConstant.IS_FAV);
        }
        BroadcastHelper.senLocalBroadcast(SongsConstant.ACTION_UPDATE_FAV_MUSIC);
        mView.setIsOrNotFav();
    }
}
