package dhht.myrecord.view;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import dhht.myrecord.R;

/**
 * Created by Administrator on 2017/5/19.
 */

public class DialogManager {
    private Dialog mDialog;
    private ImageView mIcon;
    private ImageView mVoice;
    private TextView textView;
    private Context context;

    public DialogManager( Context context) {

        this.context = context;
    }
    public void showRecordingDialog(){
        mDialog  = new Dialog(context, R.style.Theme_AudioDialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.dialog_recorder,null);
        mDialog.setContentView(view);
        mIcon = (ImageView) view.findViewById(R.id.id_recorder_dialog_icon);
        mVoice = (ImageView) view.findViewById(R.id.id_recorder_dialog_voice);
        textView = (TextView) view.findViewById(R.id.id_recorder_dialog_label);

        mIcon.setImageResource(R.drawable.recorder);
        textView.setText("手指上滑，取消发送");
        mDialog.show();
    }

    public void recording(){
        if(mDialog !=null && mDialog.isShowing()){
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
            mIcon.setImageResource(R.drawable.recorder);
            textView.setText("手指上滑，取消发送");
        }
    }

    public void wantToCancel(){
        if(mDialog !=null && mDialog.isShowing()){
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            mIcon.setImageResource(R.drawable.cancel);
            textView.setText("松开手指，取消发送");
        }
    }

    public void tooShort(){
        if(mDialog !=null && mDialog.isShowing()){
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            mIcon.setImageResource(R.drawable.voice_to_short);
            textView.setText("录音时间过短");
        }
    }

    public void dimissDialog(){
        if(mDialog !=null && mDialog.isShowing()){
            mDialog.dismiss();
            mDialog = null;
        }
    }

    public void updateVoiceLevel(int level){
        if(mDialog !=null && mDialog.isShowing()){
            int resId = context.getResources().getIdentifier("v" + level,"drawable",context.getPackageName());
            mVoice.setImageResource(resId);

        }
    }
}
