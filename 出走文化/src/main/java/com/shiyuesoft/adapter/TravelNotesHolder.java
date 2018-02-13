package com.shiyuesoft.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import com.shiyuesoft.R;
import com.shiyuesoft.util.CircleImageView;
import com.shiyuesoft.util.NineGridTestLayout;
/**
 * Created by wangg on 2017/7/31.
 */
public class TravelNotesHolder extends MyRVViewHolder{
    public CircleImageView moments_ImageView;
    public TextView moments_identity;
    public TextView moments_time;
    public TextView moments_title;
    public TextView moments_content;
    public NineGridTestLayout layout;
    public ImageView jingflag;
    public TextView moments_address;
    public TextView moments_name;
    public RadioButton r1,r2,r3,r4;
    public TextView moment_id,if_colloct,if_good,good_num;
    public TravelNotesHolder(View itemView) {
        super(itemView);
        moments_ImageView=itemView.findViewById(R.id.moments_ImageView);
        moments_identity=itemView.findViewById(R.id.moments_identity);
        moments_time=itemView.findViewById(R.id.moments_time);
        moments_title=itemView.findViewById(R.id.moments_title);
        moments_content=itemView.findViewById(R.id.moments_content);
        moments_name=itemView.findViewById(R.id.moments_name);
        moments_address=itemView.findViewById(R.id.moments_address);
        jingflag=itemView.findViewById(R.id.jingflag);
        layout = itemView.findViewById(R.id.layout_nine_grid);
        moment_id=itemView.findViewById(R.id.moment_id);
        if_colloct=itemView.findViewById(R.id.if_colloct);
        if_good=itemView.findViewById(R.id.if_good);
        r1=itemView.findViewById(R.id.m_r1);
        r2=itemView.findViewById(R.id.m_r2);
        r3=itemView.findViewById(R.id.m_r3);
        r4=itemView.findViewById(R.id.m_r4);
    }
    public int getHolderType(){
        return 0;
    }
}
