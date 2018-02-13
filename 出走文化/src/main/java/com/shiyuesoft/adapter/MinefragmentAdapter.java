package com.shiyuesoft.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.shiyuesoft.R;
import com.shiyuesoft.bean.Moments;
import com.shiyuesoft.util.CircleImageView;
import com.shiyuesoft.util.NineGridTestLayout;

import java.util.List;

/**
 * Created by wangg on 2017/7/19.
 * 自定义在游记页面的两个片段的RecyclerView的适配器
 */
public class MinefragmentAdapter extends RecyclerView.Adapter<MinefragmentAdapter.ViewHolder> implements View.OnClickListener{
    private LayoutInflater mInflater;
    private List<Moments> datas;
    private OnRecyclerItemClickListener onRecyclerItemClickListener;
    private Context context;
    private boolean flag;
    public MinefragmentAdapter(Context context, List<Moments> datas) {
        this.mInflater=LayoutInflater.from(context);
        this.datas=datas;
        this.context=context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.item_moment_mine,parent,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onRecyclerItemClickListener!=null){
                    onRecyclerItemClickListener.onItemClick(view,(int)view.getTag());
                }
            }
        });
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
         holder.itemView.setTag(position);
         Moments moments=datas.get(position);
         holder.moments_ImageView.setImageResource(moments.getTitleimageurl());
         holder.moments_name.setText(moments.getMoments_name());
         holder.moments_identity.setText(moments.getMoments_identity());
         holder.layout.setIsShowAll(moments.isShowAll());
         holder.layout.setUrlList(moments.getUrlList());
         holder.moments_time.setText(moments.getMoments_time());
         holder.moments_title.setText(moments.getMoments_title());
         holder.moments_content.setText(moments.getMoments_content());
         holder.moments_address.setText(moments.getMoments_address());
         holder.r1.setText(moments.getShards()+"");
         holder.r2.setVisibility(View.VISIBLE);
         if (moments.isFlag()==true){
            holder.r5.setChecked(true);
             flag=true;
         }
//         holder.r3.setText(moments.getZans()+"");
//         holder.r4.setText(moments.getComments()+"");
      /*   holder.r1.setOnClickListener(this);
         holder.r2.setOnClickListener(this);
         holder.r3.setOnClickListener(this);
         holder.r4.setOnClickListener(this);*/
    }
    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        if(id==R.id.m_r1){

        }else if(id==R.id.m_r2){
            RadioButton r2=(RadioButton)view;
            if (flag==true){
                r2.setChecked(false);
                flag=false;
            }else if (flag==false){
                r2.setChecked(true);
                flag=true;
            }
        }
    }
    //自定义Viewholder ，持有每个Item的所有界面元素
    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView moments_ImageView;
        private TextView moments_identity;
        private TextView moments_time;
        private TextView moments_title;
        private TextView moments_content;
        private NineGridTestLayout layout;
        private TextView moments_address;
        private TextView moments_name;
        private RadioButton r1,r2,r3,r4,r5;

        public ViewHolder(View itemView) {
            super(itemView);
            moments_ImageView=itemView.findViewById(R.id.moments_ImageView);
            moments_identity=itemView.findViewById(R.id.moments_identity);
            moments_time=itemView.findViewById(R.id.moments_time);
            moments_title=itemView.findViewById(R.id.moments_title);
            moments_content=itemView.findViewById(R.id.moments_content);
            moments_name=itemView.findViewById(R.id.moments_name);
            moments_address=itemView.findViewById(R.id.moments_address);
            layout = itemView.findViewById(R.id.layout_nine_grid);
            r1=itemView.findViewById(R.id.m_r1);
            r2=itemView.findViewById(R.id.m_r2);
            r3=itemView.findViewById(R.id.m_r3);
            r4=itemView.findViewById(R.id.m_r4);
            r5=itemView.findViewById(R.id.m_r5);
        }
    }
    /**
     * 自定义Recyclerview中 itemview的点击回调函数s
     */
    public interface OnRecyclerItemClickListener{
        void onItemClick(View view, int position);
    }
    public  void addOnItemClickListener(OnRecyclerItemClickListener listener){
        this.onRecyclerItemClickListener=listener;
    }
}
