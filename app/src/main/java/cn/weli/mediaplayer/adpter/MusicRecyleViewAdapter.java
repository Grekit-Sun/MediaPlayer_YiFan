package cn.weli.mediaplayer.adpter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.weli.mediaplayer.R;
import cn.weli.mediaplayer.bean.SongData;
import cn.weli.mediaplayer.constant.SongsConstant;

public class MusicRecyleViewAdapter extends RecyclerView.Adapter {

    private List<SongData> list;
    private int initType;

    public MusicRecyleViewAdapter(List<SongData> list, int initType) {
        this.list = list;
        this.initType = initType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayout(), parent, false);
        return new MyBaseViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final SongData songData = list.get(position);
        MyBaseViewHolder myHolder = (MyBaseViewHolder) holder;
        if (initType == SongsConstant.TYPE_ALL) {
            myHolder.mCountMusic.setText(songData.id + "");
        } else {
            myHolder.mCountMusic.setText(" ");
        }
        myHolder.mSongName.setText(songData.songName);
        myHolder.mSongAuthor.setText(songData.singer);
        if (songData.status == SongsConstant.MUSIC_STATUS_PLAY) {     //播放
            isPlayBackground(myHolder);
        } else if (songData.status == SongsConstant.MUSIC_STATUS_PAUSE) {      //暂停
            isPauseBackGround(myHolder);
        } else if (songData.status == SongsConstant.MUSIC_STATUS_STOP) {       //停止
            notPlayBackground(myHolder);
        }

        myHolder.mSongContainer.setOnClickListener(new View.OnClickListener() {  //点击事件
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClick(songData, position);
            }
        });

        myHolder.mSongContainer.setOnLongClickListener(new View.OnLongClickListener() {    //长按事件
            @Override
            public boolean onLongClick(View view) {
                if (songData.isFavorite == SongsConstant.NOT_FAV) {       //未收藏，则长按收藏
                    mOnItemClickListener.onItemLongClick(songData);
                } else {     //已收藏提示
                    mOnItemClickListener.hasFav(songData);
                }
                return false;
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    protected int getLayout() {
        return R.layout.recy_music_layout;
    }

    protected class MyBaseViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recy_count_music)
        TextView mCountMusic;
        @BindView(R.id.recy_music_name)
        TextView mSongName;
        @BindView(R.id.recy_music_author)
        TextView mSongAuthor;
        @BindView(R.id.is_play_img)
        ImageView mIsPlayImg;
        @BindView(R.id.rec_recy_container)
        LinearLayout mSongContainer;

        private MyBaseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 设置音乐清单
     */
    public void setMusicList(List<SongData> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    protected <T> T $(int res, View parent) {
        return (T) parent.findViewById(res);
    }

    /**
     * 暂停音乐的背景
     *
     * @param holder
     */
    private void isPauseBackGround(MyBaseViewHolder holder) {
        holder.mCountMusic.setTextColor(Color.RED);
        holder.mSongName.setTextColor(Color.RED);
        holder.mSongAuthor.setTextColor(Color.RED);
        holder.mIsPlayImg.setImageResource(R.drawable.waitplay);
    }

    /**
     * 播放音乐的背景
     *
     * @param holder
     */
    private void isPlayBackground(MyBaseViewHolder holder) {
        holder.mCountMusic.setTextColor(Color.RED);
        holder.mSongName.setTextColor(Color.RED);
        holder.mSongAuthor.setTextColor(Color.RED);
        holder.mIsPlayImg.setImageResource(R.drawable.isplay);
    }

    /**
     * 待播放音乐的背景
     *
     * @param holder
     */
    private void notPlayBackground(MyBaseViewHolder holder) {
        holder.mCountMusic.setTextColor(Color.BLACK);
        holder.mSongName.setTextColor(Color.BLACK);
        holder.mSongAuthor.setTextColor(Color.BLACK);
        holder.mIsPlayImg.setImageResource(R.drawable.waitplay);
    }


    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(SongData songData, int position);

        void onItemLongClick(SongData songData);

        void hasFav(SongData songData);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
