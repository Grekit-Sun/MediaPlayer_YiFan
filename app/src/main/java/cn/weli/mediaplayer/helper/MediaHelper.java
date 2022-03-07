package cn.weli.mediaplayer.helper;

import android.content.ContentUris;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import cn.weli.mediaplayer.R;
import cn.weli.mediaplayer.bean.SongData;

import static cn.weli.mediaplayer.MediaApplication.appCtx;

/**
 * @author Grekit
 * @description 媒体帮助类
 * @date 2019/12/30
 */
public class MediaHelper {

    public static SongData isPlaySongData;
    public static List<SongData> isPlayList;

    /**
     * 获取音乐专辑图片()
     *
     * @param songMeidaId
     * @param albumId
     * @return 音乐专辑图片
     */
    public static Bitmap getSongAlbumBitmap(long songMeidaId, long albumId) {
        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        Bitmap bm = null;
        // 专辑id和歌曲id小于0说明没有专辑、歌曲，并抛出异常
        if (albumId < 0 && songMeidaId < 0) {
            throw new IllegalArgumentException(
                    "Must specify an album or a song id");
        }
        try {
            if (albumId < 0) {
                Uri uri = Uri.parse("content://media/external/audio/media/" + songMeidaId + "/albumart");
                ParcelFileDescriptor pfd = null;

                pfd = appCtx.getContentResolver().openFileDescriptor(uri, "r");

                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    bm = BitmapFactory.decodeFileDescriptor(fd);
                }
            } else {
                Uri uri = ContentUris.withAppendedId(sArtworkUri, albumId);
                ParcelFileDescriptor pfd = appCtx.getContentResolver()
                        .openFileDescriptor(uri, "r");
                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    bm = BitmapFactory.decodeFileDescriptor(fd);


                } else {
                    return null;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //如果获取的bitmap为空，则返回一个默认的bitmap
        if (bm == null) {
            Resources resources = appCtx.getResources();
            Drawable drawable = resources.getDrawable(R.drawable.icon_music);
            //Drawable 转 Bitmap
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            bm = bitmapDrawable.getBitmap();

        }
        return Bitmap.createScaledBitmap(bm, 150, 150, true);
    }

    /**
     * 获取本地音乐
     *
     * @return 本地音乐
     */
    public List<SongData> obtainLoalSongs() {
        List<SongData> list = new ArrayList<SongData>();
        // 媒体库查询语句
        Cursor cursor = appCtx.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                null, MediaStore.Audio.AudioColumns.IS_MUSIC);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    SongData song = new SongData();
                    song.songMediaId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                    song.songName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                    song.singer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                    song.path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                    song.duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                    song.size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                    song.albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));

                    if (song.songName == null) {
                        continue;
                    }

                    if (song.size > 1000 * 800) {
                        // 注释部分是切割标题，分离出歌曲名和歌手 （本地媒体库读取的歌曲信息不规范）
                        if (song.songName.contains("-")) {
                            String[] str = song.songName.split("-");
                            song.singer = str[0];
                            song.songName = str[1];
                        }
                        list.add(song);
                    }
                } while (cursor.moveToNext());
            }
            // 释放资源
            cursor.close();
        }
        return list;
    }

    /**
     * 存储上一首歌的id
     *
     * @param id
     */
    public static void saveIsPlaySongId(int id) {
        SharedPreferences.Editor editor = appCtx.getSharedPreferences("music_sp", appCtx.MODE_PRIVATE).edit();
        editor.putInt("isPlaySongId", id);
        editor.commit();
    }

    /**
     * 读取正在播放Id
     *
     * @return
     */
    public static Integer readIsPlaySongId() {
        SharedPreferences read = appCtx.getSharedPreferences("music_sp", appCtx.MODE_PRIVATE);
        return read.getInt("isPlaySongId", -1);
    }
}
