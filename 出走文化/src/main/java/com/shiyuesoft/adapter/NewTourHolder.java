package com.shiyuesoft.adapter;

import android.view.View;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;
import com.shiyuesoft.R;
/**
 * Created by wangg on 2017/7/27.
 * 壮游 页面的holder组件
 */
public class NewTourHolder extends MyRVViewHolder{
    public SmartImageView imageView;
    public TextView tour_id_text;

    public NewTourHolder(View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.image);
        tour_id_text=itemView.findViewById(R.id.tour_id);
    }
    public int getHolderType(){
        return 0;
    }
}