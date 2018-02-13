package com.shiyuesoft.util;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import com.shiyuesoft.adapter.BaseRecyclerViewAdapter;

/**
 */
public class PTRecycleView extends RecyclerView {

    private int lastVisibleItem = 0;
    private PTLoadMoreListener mLoadListener;
    private BaseRecyclerViewAdapter mAdapter;
    public PTRecycleView(Context context){
        super(context);
        initView();
    }

    public PTRecycleView(Context context, AttributeSet attrs){
        super(context, attrs);
        initView();
    }

    public interface PTLoadMoreListener{
        //加载更多
        void loadMore();
    }

    public void setLoadMoreListener(PTLoadMoreListener listener){
        mLoadListener = listener;
    }

    public void startLoadMore(){
        mLoadListener.loadMore();
    }

    public void setRecyclerAdapter(BaseRecyclerViewAdapter adapter){
        mAdapter = adapter;
        setAdapter(adapter);
    }

    //设置线性布局和分隔符
    public void init(Context context){
        this.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
//        this.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
    }



    private void initView(){
        this.setOnScrollListener(new OnScrollListener(){
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState){
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) getLayoutManager();
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == manager.getItemCount()){
                    if(mLoadListener!=null&&mAdapter.getLoadState()==BaseRecyclerViewAdapter.STATE_DEFAULT
                            &&mAdapter.getCanLoadMore()){
                        mLoadListener.loadMore();
                        mAdapter.setLoadState(BaseRecyclerViewAdapter.STATE_LOADING);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = ((LinearLayoutManager)getLayoutManager()).findLastVisibleItemPosition();
            }
        });
    }
}
