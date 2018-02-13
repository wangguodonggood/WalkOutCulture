package com.shiyuesoft.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by wangg on 2017/7/27.
 * 抽象holder组件 主要供继承
 */

public class MyRVViewHolder extends RecyclerView.ViewHolder{
    public MyRVViewHolder(View itemView) {
        super(itemView);
    }
    public int getHolderType(){
        return 0;
    }
}
