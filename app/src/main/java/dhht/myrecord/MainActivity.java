package dhht.myrecord;

import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import dhht.myrecord.view.AudioRecorderButton;
import dhht.myrecord.view.MediaManager;
import dhht.myrecord.view.Recorder;
import dhht.myrecord.view.RecorderAdapter;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private RecorderAdapter mAdapter;
    private List<Recorder> mDatas = new ArrayList<>();
    private AudioRecorderButton mAudioRecorderButton;
    private View mAnimView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.id_listview);
        mAudioRecorderButton = (AudioRecorderButton) findViewById(R.id.id_recorder_button);
        mAudioRecorderButton.setOnAudioFinishRecorderListener(new AudioRecorderButton.OnAudioFinishRecorderListener() {
            @Override
            public void onFinish(float seconds, String path) {
                Recorder recorder = new Recorder(seconds,path);
                mDatas.add(recorder);
                mAdapter.notifyDataSetChanged();
                mListView.setSelection(mDatas.size()-1);
            }
        });
        mAdapter = new RecorderAdapter(this,mDatas);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mAnimView != null){
                    mAnimView.setBackgroundResource(R.drawable.adj);
                    mAnimView = null;
                }
                mAnimView = view.findViewById(R.id.id_recorder_anim);
                mAnimView.setBackgroundResource(R.drawable.play_anim);
                AnimationDrawable anim = (AnimationDrawable) mAnimView.getBackground();
                anim.start();
                MediaManager.playSound(mDatas.get(position).getFilePath(), new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mAnimView.setBackgroundResource(R.drawable.adj);
                    }
                });
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaManager.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MediaManager.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaManager.release();
    }
}
