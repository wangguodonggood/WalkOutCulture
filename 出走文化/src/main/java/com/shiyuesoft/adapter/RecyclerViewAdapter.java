package com.shiyuesoft.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.shiyuesoft.R;
import com.shiyuesoft.activity.NotesDetailsActivity;
import com.shiyuesoft.activity.TourDetailsActivity;
import com.shiyuesoft.bean.TravelNotes;
import com.shiyuesoft.util.ButtonClickUtil;
import com.shiyuesoft.util.ImageLoaderUtil;
import com.shiyuesoft.util.URLUtil;

import java.util.List;

/**
 * Created by wangg on 2017/7/31.
 * 实现对游记信息的展示以及添加点击事件
 */

public class RecyclerViewAdapter extends BaseRecyclerViewAdapter<TravelNotes.RecordBean,MyRVViewHolder>{
    /**
     * @param context
     * @param list    the datas to attach the adapter
     * @param viewId
     */
    private Context context;
    private CommonListener commListener;
    public RecyclerViewAdapter(Context context, List<TravelNotes.RecordBean> list, int viewId,CommonListener commListener) {
        super(context, list, viewId);
        this.context=context;
        this.commListener=commListener;
    }
    @Override
    protected void bindDataToItemView(MyRVViewHolder myRVViewHolder, final TravelNotes.RecordBean item) {
        if (myRVViewHolder instanceof TravelNotesHolder){
            final TravelNotesHolder  holder= (TravelNotesHolder) myRVViewHolder;
            ImageLoaderUtil.getImageLoader(context).displayImage(item.getUser_img(),
                    holder.moments_ImageView, ImageLoaderUtil.getPhotoImageOption());
            holder.moments_name.setText(item.getName());
            holder.moments_identity.setText(URLUtil.getType(item.getUser_type()));
            holder.layout.setIsShowAll(false);
            holder.layout.setUrlList(item.getImg());
            holder.moments_time.setText(item.getPublish_date());
            holder.moments_title.setText(item.getTitle());
            holder.moments_content.setText(item.getContent());
            holder.moments_address.setText(item.getPlace());
            holder.moment_id.setText(item.getRec_id());
            holder.r1.setText(item.getTranspond());
            holder.r3.setText(item.getGood());
            holder.r4.setText(item.getReview_num());
            if (item.getIf_good().equals("1")){
                holder.r3.setChecked(true);
                holder.if_good.setText("good");
            }else {
                holder.r3.setChecked(false);
                holder.if_good.setText("false");
            }
            if (item.getIf_collect().equals("1")){
                holder.r2.setChecked(true);
                holder.if_colloct.setText("colloct");
            }else {
                holder.r2.setChecked(false);
                holder.if_colloct.setText("nocolloct");
            }
            if (item.getElite().equals("1")){
                holder.jingflag.setVisibility(View.VISIBLE);
            }
            holder.r1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  commListener.ShareMsg(item.getRec_id().toString()
                          ,holder.moments_title.getText().toString()
                                       ,holder.moments_content.getText().toString());
                }
            });
            //收藏按钮--点击事件-----------------------------------------
            holder.r2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!ButtonClickUtil.isFastDoubleClick(view.getId())){
                        if (holder.if_colloct.getText().toString().equals("colloct")){
                            holder.r2.setChecked(false);
                            holder.if_colloct.setText("nocolloct");
                        }else {
                            holder.r2.setChecked(true);
                            holder.if_colloct.setText("colloct");
                        }
                        commListener.checkColloct(item.getRec_id().toString(),holder.if_colloct.getText().toString());
                    }else {
                        Toast.makeText(context,"请不要连续点击",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            //点赞按钮 的点击事件
            holder.r3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!ButtonClickUtil.isFastDoubleClick(view.getId())){
                        int num=Integer.parseInt(holder.r3.getText().toString());
                        if (holder.if_good.getText().toString().equals("good")){
                            holder.r3.setChecked(false);
                            holder.if_good.setText("false");
                            if (num>0){
                                holder.r3.setText(--num+"");
                            }
                        }else {
                            holder.r3.setChecked(true);
                            holder.if_good.setText("good");
                            holder.r3.setText(++num+"");
                        }
                        commListener.checkGood(item.getRec_id().toString(), holder.if_good.getText().toString());
                    }else {
                        Toast.makeText(context,"请不要连续点击",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            //评论按钮
            holder.r4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    commListener.dooIt(item.getRec_id().toString());
                }
            });
        }
    }
    public interface CommonListener{
        void dooIt(String id);
        //修改点赞状态
        void checkGood(String id,String ifGood);
        //修改收藏状态
        void checkColloct(String id,String ifColloct);

        void ShareMsg(String id,String title,String content);
    }
}
