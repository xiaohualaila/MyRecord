package dhht.myrecord.view;

import android.media.*;
import android.media.AudioManager;

import java.io.IOException;

/**
 * Created by Administrator on 2017/5/23.
 */

public class MediaManager {

    private static MediaPlayer mMediaPlayer;
    private static  boolean isPause;
    public static void playSound(String filePath, MediaPlayer.OnCompletionListener onCompletionListener) {
        try {
            if(mMediaPlayer ==null){
               mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        mMediaPlayer.reset();
                        return false;
                    }
                });
            }else {
                mMediaPlayer.reset();
            }
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnCompletionListener(onCompletionListener);
            mMediaPlayer.setDataSource(filePath);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void pause(){
        if(mMediaPlayer.isPlaying()||mMediaPlayer !=null){
            mMediaPlayer.pause();
            isPause =true;
        }
    }

    public static void resume(){
        if(mMediaPlayer != null && isPause){
            mMediaPlayer.start();
            isPause =false;
        }
    }

    public static void release(){
        if(mMediaPlayer != null){
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
