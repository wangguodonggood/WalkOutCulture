package com.shiyuesoft.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;
import com.shiyuesoft.R;
import com.shiyuesoft.util.CircleProgress;
import com.shiyuesoft.util.PTRecycleView;

import java.util.List;

public abstract class BaseRecyclerViewAdapter<T, VH extends MyRVViewHolder> extends RecyclerView.Adapter<VH> {
    /**
     * click listener
     */
    protected OnRVItemClickListener<T> mOnItemClickListener;
    /**
     * long click listener
     */
    protected OnRVItemLongClickListener<T> mOnItemLongClickListener;
    /**
     * data
     */
    protected List<T> mList;
    protected Context mContext;
    protected int mViewId;
    private FooterHolder mFooterHolder;

    private  int mLoadState = 0;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    public static final int STATE_DEFAULT = 0;   //默认情况
    public static final int STATE_LOADING = 1;     //正在加载
    public static final int STATE_FAILURE = 2;     //加载失败
    public static final int STATE_SUCCESS = 3;     //加载失败
    public static final int STATE_FINISH = 4;      //没有更多数据了

    /**
     * 得到加载的状态
     * @return
     */
    public int getLoadState(){
        return mLoadState;
    }

    public void setLoadState(int state){
        mLoadState = state;
    }

    public void finishLoad(int state){
        switch (state){
            case BaseRecyclerViewAdapter.STATE_SUCCESS:  //加载成功
                mLoadState = STATE_DEFAULT;
                break;
            case BaseRecyclerViewAdapter.STATE_FAILURE:  //加载失败
                mLoadState = STATE_FAILURE;
                setLoadFailView();
                break;
            case BaseRecyclerViewAdapter.STATE_FINISH:   //没有更多数据了
                mLoadState = STATE_FINISH;
                setLoadFinishView();
                break;

        }
    }
    private void setLoadFinishView(){
        if (mFooterHolder!=null){
            mFooterHolder.setProgressBarVisible(View.GONE);
            mFooterHolder.setMessage("没有更多数据了o(╯□╰)o");
        }
    //    mFooterHolder.setmFooterTextView(View.GONE);
        // Toast.makeText(mContext,"没有更多数据了o(╯□╰)o",Toast.LENGTH_SHORT).show();
    }

    private void setLoadFailView(){
        if (mFooterHolder!=null){
            mFooterHolder.setProgressBarVisible(View.GONE);
            mFooterHolder.setMessage("加载失败，点击重试");
        }
    }

    /**
     * @param list the datas to attach the adapter
     */
    public  BaseRecyclerViewAdapter(Context context, List<T> list , int viewId){
        mList = list;
        mContext = context;
        mViewId = viewId;
    }
    /**
     * get a item by index
     * @param position
     * @return
     */
    protected T getItem(int position){
        if(position==mList.size()&&mCanLoadMore)
            return null;
        else
            return mList.get(position);
    }

    private boolean mCanLoadMore = false;   //说明是否可以上拉加载
    public void setCanLoadMore(boolean loadMore){
        mCanLoadMore = loadMore;
    }

    public boolean getCanLoadMore(){
        return mCanLoadMore;
    }

    @Override
    public int getItemCount(){
        if(mCanLoadMore)
            return mList.size()+1;
        else
            return mList.size();
    }

    @Override
    public int getItemViewType(int position){
        if (position + 1 == getItemCount()&&mCanLoadMore){
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }
    /**
     * set a long click listener
     *
     * @param onItemLongClickListener
     */
    public void setOnItemLongClickListener(OnRVItemLongClickListener<T> onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }


    public void setOnItemClickListener(OnRVItemClickListener<T> onItemClickListener){
        mOnItemClickListener = onItemClickListener;
    }
    protected View inflateItemView(ViewGroup viewGroup, int layoutId){
        return inflateItemView(viewGroup, layoutId, false);
    }

    protected View inflateItemView(ViewGroup viewGroup, int layoutId, boolean attach) {
        return LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, attach);
    }
    @Override
    public final void onBindViewHolder(VH vh, int position){
        final T item = getItem(position);
        bindDataToItemView(vh, item);
        bindItemViewClickListener(vh, item);
    }
    protected abstract void bindDataToItemView(VH vh, T item);

    protected final void bindItemViewClickListener(final VH vh, final T item) {
        if (mOnItemClickListener != null) {
            vh.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onClick(view, item,vh.getLayoutPosition());
                }
            });
        }
        if (mOnItemLongClickListener != null){
            vh.itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v){
                    mOnItemLongClickListener.onLongClick(v, item,vh.getLayoutPosition());
                    return true;
                }
            });
        }
    }
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType){
        if(mCanLoadMore&&viewType==TYPE_FOOTER) {    //
            View v = LayoutInflater.from(mContext).inflate(R.layout.footer_layout, parent, false);
            FooterHolder holder = new FooterHolder(v);
            mFooterHolder = holder;
            mFooterHolder.bindAdapter(this);
            return (VH) holder;
        }else{
             View v = LayoutInflater.from(mContext).inflate(mViewId,parent,false);
             if (mViewId==R.layout.item_newtour){
                 NewTourHolder holder = new NewTourHolder(v);
                 return (VH)holder;
             }else if (mViewId==R.layout.item_moment){
                 TravelNotesHolder holder=new TravelNotesHolder(v);
                 return (VH)holder;
             }else if (mViewId== R.layout.item_moment_mine){
                 MineFragmentHolder holder=new MineFragmentHolder(v);
                 return (VH)holder;
             }
             return null;
        }
    }
    public  void reload() {
        mRecyclerView.startLoadMore();
    }

    private PTRecycleView mRecyclerView;
    public void setPTRecyclerView(PTRecycleView recyclerView){
        mRecyclerView = recyclerView;
    }

    public interface OnRVItemClickListener<T> {
        void onClick(View view, T item, int position);
    }

    public interface OnRVItemLongClickListener<T> {
        void onLongClick(View view, T item, int position);
    }
}
class FooterHolder extends MyRVViewHolder{
    private BaseRecyclerViewAdapter mAdapter;
    private TextView mFooterTextView;
    private CircleProgress mProgressBar;
    public FooterHolder(View itemView){
        super(itemView);
        mFooterTextView = (TextView) itemView.findViewById(R.id.tv_footer);
        mProgressBar = (CircleProgress) itemView.findViewById(R.id.footer_progressbar);
        mFooterTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(mAdapter!=null&&mAdapter.getLoadState()==BaseRecyclerViewAdapter.STATE_FAILURE){
                    mFooterTextView.setText("加载中···");
                    mProgressBar.setVisibility(View.VISIBLE);
                    mAdapter.reload();
                    mAdapter.setLoadState(BaseRecyclerViewAdapter.STATE_LOADING);
                }
            }
        });
    }
    public void setMessage(String msg){
        mFooterTextView.setText(msg);
    }

    public void setProgressBarVisible(int visible){
        mProgressBar.setVisibility(visible);
    }

    public void bindAdapter(BaseRecyclerViewAdapter adapter){
        mAdapter = adapter;
    }

    public void setmFooterTextView(int visible){
        mFooterTextView.setVisibility(visible);
    }

    public int getHolderType(){
        return 1;
    }
}
