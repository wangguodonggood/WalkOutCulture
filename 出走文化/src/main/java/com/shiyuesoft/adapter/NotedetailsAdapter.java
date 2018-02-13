package com.shiyuesoft.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shiyuesoft.R;
import com.shiyuesoft.bean.NoteDetails;
import com.shiyuesoft.bean.TourDetails;
import com.shiyuesoft.util.CircleImageView;
import com.shiyuesoft.util.ImageLoaderUtil;
import com.shiyuesoft.util.URLUtil;

import java.util.List;

/**
 * Created by wangg on 2017/7/19.
 * 负责动态展示的adapter
 * 游记的详情信息
 */

public class NotedetailsAdapter extends BaseAdapter{

    private Context context;
    private List<NoteDetails.ReviewBean> datas;
    private ViewHolder holder;

    public NotedetailsAdapter(Context context, List<NoteDetails.ReviewBean> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view=null;
        NoteDetails.ReviewBean reviewbean=null;
        if (convertView==null){
        view=View.inflate(context, R.layout.item_comment,null);
        holder=new ViewHolder();
        holder.image= (CircleImageView) view.findViewById(R.id.roundImageView);
        holder.comment_name= (TextView) view.findViewById(R.id.comment_name);
        holder.identity= (TextView) view.findViewById(R.id.identity);
        holder.comment_time= (TextView) view.findViewById(R.id.comment_time);
        holder.comment_content= (TextView) view.findViewById(R.id.comment_content);
      /*  Typeface face = Typeface.createFromAsset (context.getAssets() ,"fonts/kk.ttf" );
        holder.comment_content.setTypeface(face);*/
        view.setTag(holder);
       }else {
        view=convertView;
        holder= (ViewHolder) view.getTag();
      }
        reviewbean=datas.get(i);
        ImageLoaderUtil.getImageLoader(context).displayImage(reviewbean.getUser_img(),
                holder.image, ImageLoaderUtil.getPhotoImageOption());
        holder.comment_name.setText(reviewbean.getUser_name());
        holder.identity.setText(URLUtil.getType(reviewbean.getUser_type()));
        holder.comment_time.setText(reviewbean.getReview_date());
        holder.comment_content.setText(reviewbean.getReview_content());
        return view;
    }
    public class ViewHolder{
        CircleImageView image;
        TextView comment_name;
        TextView identity;
        TextView comment_time;
        TextView comment_content;
    }
}
