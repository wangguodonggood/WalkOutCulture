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
 * 在MineFragment中显示游记状态的适配器 holder组件
 */
public class MineFragmentHolder extends MyRVViewHolder{
    public CircleImageView moments_ImageView;
    public TextView moments_identity;
    public TextView moments_time;
    public TextView moments_title,moments_status;
    public TextView moments_content;
    public NineGridTestLayout layout;
    public TextView moments_address;
    public TextView moments_name,moment_mine_id,if_good,if_colloct;
    public RadioButton r1,r2,r3,r4,r5;
    public ImageView imageView;
    public MineFragmentHolder(View itemView) {
        super(itemView);
        moments_ImageView=itemView.findViewById(R.id.moments_ImageView);
        moments_identity=itemView.findViewById(R.id.moments_identity);
        moments_time=itemView.findViewById(R.id.moments_time);
        moments_title=itemView.findViewById(R.id.moments_title);
        moments_status=itemView.findViewById(R.id.moments_status);
        moments_content=itemView.findViewById(R.id.moments_content);
        moments_name=itemView.findViewById(R.id.moments_name);
        moments_address=itemView.findViewById(R.id.moments_address);
        moment_mine_id=itemView.findViewById(R.id.moment_mine_id);
        layout = itemView.findViewById(R.id.layout_nine_grid);
        imageView=itemView.findViewById(R.id.jingflag);
        if_good=itemView.findViewById(R.id.if_good);
        if_colloct=itemView.findViewById(R.id.if_colloct);
        //分享
        r1=itemView.findViewById(R.id.m_r1);
        //删除
        r2=itemView.findViewById(R.id.m_r2);
        //收藏
        r3=itemView.findViewById(R.id.m_r3);
        //点赞
        r4=itemView.findViewById(R.id.m_r4);
        //评论
        r5=itemView.findViewById(R.id.m_r5);
    }
    public int getHolderType(){
        return 0;
    }
}
