package com.shiyuesoft.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.shiyuesoft.R;
import com.shiyuesoft.bean.TravelNotes;
import com.shiyuesoft.util.ButtonClickUtil;
import com.shiyuesoft.util.ImageLoaderUtil;
import com.shiyuesoft.util.URLUtil;

import java.util.List;

/**
 * Created by wangg on 2017/7/31.
 */

public class MineRecyclerViewAdapter extends BaseRecyclerViewAdapter<TravelNotes.RecordBean,MyRVViewHolder> {
    /**
     * @param context
     * @param list    the datas to attach the adapter
     * @param viewId
     */
    private Context context;
    private boolean flag;
    private mineCommonListener listener;
    public MineRecyclerViewAdapter(Context context, List<TravelNotes.RecordBean> list, int viewId,boolean flag,mineCommonListener listener) {
        super(context, list, viewId);
        this.listener=listener;
        this.context=context;
        this.flag=flag;
    }
    public CharSequence getTitleText(String html){
        CharSequence text= Html.fromHtml(html, new Html.ImageGetter() {

            public Drawable getDrawable(String source) {
                //根据图片资源ID获取图片
                Log.d("source", source);
                if(source.equals("‘strawberry’")){
                    Drawable draw=context.getResources().getDrawable(R.drawable.jing);
                    draw.setBounds(0, 0, draw.getIntrinsicWidth(), draw.getIntrinsicHeight());
                    return draw;
                }
                return null;
            }
        }, null);
        return text;
    }
    @Override
    protected void bindDataToItemView(MyRVViewHolder myRVViewHolder, final TravelNotes.RecordBean item) {
        if (myRVViewHolder instanceof MineFragmentHolder){
            final MineFragmentHolder holder= (MineFragmentHolder) myRVViewHolder;
            ImageLoaderUtil.getImageLoader(context).displayImage(item.getUser_img(),
                    holder.moments_ImageView, ImageLoaderUtil.getPhotoImageOption());
            holder.moments_name.setText(item.getName());
            holder.moments_identity.setText(URLUtil.getType(item.getUser_type()));
            holder.layout.setIsShowAll(false);
            if (item.getElite().equals("1")){
                //代表是精华游记 则显示精华图片
                String html="<font>"+item.getTitle()+"</font>&nbsp;&nbsp;&nbsp;&nbsp;<img src=‘strawberry’>";
                CharSequence text=getTitleText(html);
                holder.moments_title.setText(text);
            }else if (item.getState().equals("0")){
                String html="<font>"+item.getTitle()+"</font>&nbsp;&nbsp;&nbsp;&nbsp;"+"<font color='#C9132C'>(审核中...)</font>";
                CharSequence text=getTitleText(html);
                holder.moments_title.setText(text);
                holder.r1.setFocusable(false);
                holder.r1.setClickable(false);

                holder.r2.setFocusable(false);
                holder.r2.setClickable(false);

                holder.r3.setFocusable(false);
                holder.r3.setClickable(false);

                holder.r4.setFocusable(false);
                holder.r4.setClickable(false);
            }else {
                holder.moments_title.setText(item.getTitle());
            }
            holder.layout.setUrlList(item.getImg());
            holder.moments_time.setText(item.getPublish_date());
            holder.moments_content.setText(item.getContent());
            holder.moments_address.setText(item.getPlace());
            holder.moment_mine_id.setText(item.getRec_id());
            //转发按钮-----------------------------------------------
            holder.r1.setText(item.getTranspond());
            holder.r1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  listener.ShareMsg(item.getRec_id().toString(),
                          holder.moments_title.getText().toString(),
                              holder.moments_content.getText().toString());
                }
            });
            //删除按钮-----------收藏-----------------------------------------------------
            if (flag==true){
                holder.r2.setVisibility(View.VISIBLE);
            }else {
                holder.r3.setVisibility(View.VISIBLE);
                holder.r3.setChecked(true);
            }
            holder.r2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 listener.deleteNotes(item.getRec_id().toString());
                }
            });
            holder.r3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!ButtonClickUtil.isFastDoubleClick(view.getId())){
                        holder.r3.setChecked(false);
                        listener.checkColloct(item.getRec_id().toString());
                    }else {
                        Toast.makeText(context,"请不要连续点击",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            //点赞----------------------------------------------------------
            holder.r4.setText(item.getGood());
            if (item.getIf_good().equals("1")){
                  holder.r4.setChecked(true);
                  holder.if_good.setText("good");
            }else {
                  holder.r5.setChecked(true);
                  holder.if_colloct.setText("false");
            }
            holder.r4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!ButtonClickUtil.isFastDoubleClick(view.getId())){
                        int num=Integer.parseInt(holder.r4.getText().toString());
                        if (holder.if_good.getText().toString().equals("good")){
                            holder.r4.setChecked(false);
                            holder.if_good.setText("false");
                            if (num>0){
                                holder.r4.setText(--num+"");
                            }
                        }else {
                            holder.r4.setChecked(true);
                            holder.if_good.setText("good");
                            holder.r4.setText(++num+"");
                        }
                        listener.checkGood(item.getRec_id().toString(), holder.if_good.getText().toString());
                    }else {
                        Toast.makeText(context,"请不要连续点击",Toast.LENGTH_SHORT).show();
                    }
                }
            });
           //评论按钮----------------------------------------------------------------------------
            holder.r5.setText(item.getReview_num());
            if (item.getElite().equals("1")){
                holder.imageView.setVisibility(View.VISIBLE);
            }
            holder.r5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   listener.dooIt(item.getRec_id());
                }
            });
        }
    }
    /*通过接口将事件分配到页面中去*/
    public interface mineCommonListener{
        void dooIt(String id);
        //修改点赞状态
        void checkGood(String id,String ifGood);
        //修改收藏状态
        void checkColloct(String id);
        void deleteNotes(String id);
        void ShareMsg(String id,String title,String content);
    }

}
