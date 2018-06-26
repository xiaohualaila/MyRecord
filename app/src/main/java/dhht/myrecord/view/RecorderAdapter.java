package dhht.myrecord.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import dhht.myrecord.R;

/**
 * Created by Administrator on 2017/5/23.
 */

public class RecorderAdapter extends ArrayAdapter<Recorder> {



    private int mMinItemWidth;
    private int mMaxItemWidth;
    private LayoutInflater layoutInflater;
    public RecorderAdapter(Context context, List<Recorder> dates) {
        super(context,-1, dates);
        layoutInflater =LayoutInflater.from(context);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        mMaxItemWidth = (int) (displayMetrics.widthPixels*0.7f);
        mMinItemWidth = (int) (displayMetrics.widthPixels*0.15f);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_recorder,parent,false);
            holder = new ViewHolder();
            holder.seconds = (TextView) convertView.findViewById(R.id.id_recorder_time);
            holder.length = convertView.findViewById(R.id.id_recorder_length);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.seconds.setText(Math.round(getItem(position).time)+"\"");
        ViewGroup.LayoutParams lp = holder.length.getLayoutParams();
        lp.width = (int)(mMinItemWidth+(mMaxItemWidth/60f * getItem(position).time));
        return convertView;
    }

    private class  ViewHolder{
        TextView seconds;
        View length;
    }
}
