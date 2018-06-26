package dhht.myrecord.view;

import android.content.Context;
import android.media.AudioRecord;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import dhht.myrecord.R;


/**
 * Created by Administrator on 2017/5/19.
 */

public class AudioRecorderButton  extends Button implements AudioManager.AudioStateListenter {
    private static final int DISTANCE_Y_CANCEL = 50;
    private static final int STATE_NORMAL =1;
    private static final int STATE_RECORDING =2;
    private static final int STATE_WANT_TO_CANCEL =3;

    private int mCurState = STATE_NORMAL;

    private boolean isRecoding = false;
    private DialogManager manager;
    private AudioManager mAudioManager;

    private static final int MSG_AUDIO_PREPARED = 0X001;
    private static final int MSG_VOICE_CHANGED = 0X002;
    private static final int MSG_DIALOG_DIMISS = 0X003;
    private float mTime;
    private boolean mReady;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case  MSG_AUDIO_PREPARED:
                    manager.showRecordingDialog();
                    isRecoding = true;
                    new Thread(mGetVoiceLevelRunnable).start();
                    break;
                case  MSG_VOICE_CHANGED:
                    manager.updateVoiceLevel(mAudioManager.getVoiceLevel(7));

                    break;
                case  MSG_DIALOG_DIMISS:
                    manager.dimissDialog();

                    break;
            }

        }
    };
    Runnable mGetVoiceLevelRunnable = new Runnable() {
        @Override
        public void run() {
          while (isRecoding){
              try{
                  Thread.sleep(100);
                  mTime += 0.1f;
                  handler.sendEmptyMessage(MSG_VOICE_CHANGED);
              }catch (Exception e){

              }
          }
        }
    };




    //录音结束后的回调
    public interface OnAudioFinishRecorderListener{
        void onFinish(float seconds,String path);
    }

    private OnAudioFinishRecorderListener mOnAudioFinishRecorderListener;

    public void setOnAudioFinishRecorderListener(OnAudioFinishRecorderListener mOnAudioFinishRecorderListener){
        this.mOnAudioFinishRecorderListener = mOnAudioFinishRecorderListener;
    }

    public AudioRecorderButton(Context context) {
        this(context,null);

    }

    public AudioRecorderButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        manager = new DialogManager(getContext());
        String dir = Environment.getExternalStorageDirectory() + "/imooc_recorder_audios";
        mAudioManager = AudioManager.getmInstance(dir);
        mAudioManager.setAudioStateListenter(this);
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mReady =true;
                mAudioManager.prepareAudio();
                return false;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
          int  action = event.getAction();
          int x = (int) event.getX();
          int y = (int) event.getY();
         switch (action){
             case MotionEvent.ACTION_DOWN:
                 changeStage(STATE_RECORDING);
                 break;
             case MotionEvent.ACTION_MOVE:

                 if(isRecoding){
                     if(wantToCancel(x,y)){
                         changeStage(STATE_WANT_TO_CANCEL);
                     }else {
                         changeStage(STATE_RECORDING);
                     }
                 }
                 break;
             case MotionEvent.ACTION_UP:
                 if(!mReady){
                      reset();
                     return super.onTouchEvent(event);
                 }
                 if(!isRecoding||mTime < 0.6f){
                     manager.tooShort();
                     mAudioManager.cancel();
                     handler.sendEmptyMessageDelayed(MSG_DIALOG_DIMISS,1300);
                 }else if(mCurState == STATE_RECORDING){
                     manager.dimissDialog();
                     mAudioManager.release();
                     if(mOnAudioFinishRecorderListener != null){
                         mOnAudioFinishRecorderListener.onFinish(mTime, mAudioManager.getCurrentFilePath());
                     }

                }else if(mCurState == STATE_WANT_TO_CANCEL){
                    manager.dimissDialog();
                    mAudioManager.cancel();
                }
                 reset();
                 break;

         }
         return super.onTouchEvent(event);
    }
    //恢复标志位
    private void reset() {
         isRecoding = false;
        mReady = false;
        changeStage(STATE_NORMAL);
        mTime = 0;
    }

    private boolean wantToCancel(int x, int y) {
        if(x < 0||x > getWidth()){
            return true;
        }
        if(y < -DISTANCE_Y_CANCEL||y > getHeight() + DISTANCE_Y_CANCEL){
            return true;
        }
         return false;
    }

    private void changeStage(int stateRecording) {
        if(mCurState !=stateRecording ){
            mCurState = stateRecording;
            switch (stateRecording){
                case STATE_NORMAL:
                    setBackgroundResource(R.drawable.btn_recorder_normal);
                    setText(R.string.str_recorder_normal);
                  //  manager.recording();
                    break;
                case STATE_RECORDING:
                    setBackgroundResource(R.drawable.btn_recording);
                    setText(R.string.str_recorder_recording);
                    if(isRecoding){
                        manager.recording();
                    }
                    break;
                case STATE_WANT_TO_CANCEL:
                    setBackgroundResource(R.drawable.btn_recording);
                    setText(R.string.str_recorder_want_cancel);
                    manager.wantToCancel();
                    break;
            }
        }

    }

    @Override
    public void wellPrepared() {
        handler.sendEmptyMessage(MSG_AUDIO_PREPARED);
    }
}
