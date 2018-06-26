package dhht.myrecord.view;

import android.media.MediaRecorder;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Administrator on 2017/5/22.
 */

public class AudioManager {
    private MediaRecorder mMediaRecorder;
    private String mDir;
    private String mCurrentFilePath;
    private static AudioManager mInstance;
     private boolean isPrepare =false;
    private AudioManager(String dir){
        this.mDir  = dir ;
    }

    public interface AudioStateListenter{
         void wellPrepared();
    }

    public AudioStateListenter mAudioStateListenter;

    public void setAudioStateListenter(AudioStateListenter listenter){
        this.mAudioStateListenter = listenter;
    }
    public static AudioManager getmInstance(String dir){
        if(mInstance == null){
         synchronized (AudioManager.class){
            if(mInstance == null){
                mInstance = new AudioManager(dir);
            }
         }
        }
        return mInstance;
    }

    public String getCurrentFilePath() {
        return mCurrentFilePath;
    }
    public void prepareAudio(){
        try {
            isPrepare =false;
            File dir = new File(mDir);
            if(!dir.exists()){
                dir.mkdirs();
            }
             String fileName = generateFileName();
            File file = new File(dir,fileName);
            mCurrentFilePath =file.getAbsolutePath();
            mMediaRecorder = new MediaRecorder();
            mMediaRecorder.setOutputFile(file.getAbsolutePath());
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);//设置音频源
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);//设置音频的格式
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);//设置音频的编码
            mMediaRecorder.prepare();
            mMediaRecorder.start();
            isPrepare = true;
        if(mAudioStateListenter != null){
            mAudioStateListenter.wellPrepared();
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //随机生成文件
    private String generateFileName() {

        return UUID.randomUUID().toString()+".amr";
    }

    public int getVoiceLevel(int maxLevel){
        if(isPrepare){
            try{
                return maxLevel * mMediaRecorder.getMaxAmplitude() / 32768 + 1;
        } catch (Exception e) {

        }
        }
        return 1;
    }

    public void release(){
        mMediaRecorder.stop();
        mMediaRecorder.release();
        mMediaRecorder =null;
    }

    public void cancel(){
        release();
        if(mCurrentFilePath != null){
            File file = new File(mCurrentFilePath);
            file.delete();
            mCurrentFilePath = null;
        }
    }
}
