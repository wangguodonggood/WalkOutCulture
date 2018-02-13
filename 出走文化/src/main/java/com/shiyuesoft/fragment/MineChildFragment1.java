package com.shiyuesoft.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shiyuesoft.R;
import com.shiyuesoft.activity.NotesDetailsActivity;
import com.shiyuesoft.adapter.BaseRecyclerViewAdapter;
import com.shiyuesoft.adapter.MineRecyclerViewAdapter;
import com.shiyuesoft.adapter.MinefragmentAdapter;
import com.shiyuesoft.adapter.RecyclerViewAdapter;
import com.shiyuesoft.bean.TravelNotes;
import com.shiyuesoft.util.CustomProgress;
import com.shiyuesoft.util.OkManager;
import com.shiyuesoft.util.PTRecycleView;
import com.shiyuesoft.util.ShareSdkUtils;
import com.shiyuesoft.util.SourceUtil;
import com.shiyuesoft.util.URLUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by wangg on 2017/7/20.
 * 我的游记 全部
 */
public class MineChildFragment1 extends Fragment implements SwipeRefreshLayout.OnRefreshListener,PTRecycleView.PTLoadMoreListener,View.OnClickListener,MineRecyclerViewAdapter.mineCommonListener {
    //当前布局
    private  View view;
    //展示信息的recyclerview的列表
    private PTRecycleView recyclerview;
    private MineRecyclerViewAdapter adapter;
    //下拉刷新的布局
    private SwipeRefreshLayout swipeRefreshLayout;
    private OkManager okManager;
    //获取token值
    private SharedPreferences sharedPreferences;
    private String token;
    //判断是否有下一页
    private String page="1";
    private String next_page="1";
    private String sum_page="1";
    //重新刷新的按钮
    private LinearLayout net_btn;
    private String url= URLUtil.URL+URLUtil.path+"?action=travel_self";
    //获取评论数据详情
    private List<TravelNotes.RecordBean> datas=new ArrayList<>();
    //判断数据是否为空 如果为空就显示出来
    private LinearLayout text_empty;
    private CustomProgress customProgress;
    private int position;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragmenrt_minechild1,container,false);
        initView(view);
        return view;
    }
    /*
    * 初始化组件
    * */

    private void initView(View view) {
        net_btn=view.findViewById(R.id.net_btn);
        net_btn.setOnClickListener(this);
        text_empty=view.findViewById(R.id.text_empty);
        sharedPreferences=getActivity().getSharedPreferences("tokenValue", getActivity().MODE_APPEND);
        token = sharedPreferences.getString("token", "1");
        recyclerview=view.findViewById(R.id.recycle_mine1);
        recyclerview.init(getActivity());
        recyclerview.setLoadMoreListener(this);
        //下拉刷新
        swipeRefreshLayout = view.findViewById(R.id.SwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        position=0;
        //开始加载网络数据
        customProgress=new CustomProgress(getActivity());
        customProgress.show(getActivity(),"加载中",true,null);
        initNETDatas();
        //当4秒进度条还在转则让它消失
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (customProgress!=null){
                    customProgress.todismiss();
                }
            }
        },4000);
    }
////开始加载网络数据
    private void initNETDatas() {
        position=0;
        String URL=url+"&token="+token+"&page=1";
        datas=new ArrayList<>();
        getNetData(URL);
        net_btn.setVisibility(View.GONE);
    }
    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                //处理下拉刷新
                initNETDatas();
            }
        },2000);
    }
    //上拉加载触发的回调方法
    @Override
    public void loadMore() {
        if (swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int p=1;
                int s=1;
                if (page != null && sum_page != null) {
                    p=Integer.parseInt(page);
                    s=Integer.parseInt(sum_page);
                }
                //是说明是最后一页
                if (p==s){
                    //没有更多数据了
                    if (adapter!=null){
                        adapter.finishLoad(BaseRecyclerViewAdapter.STATE_FINISH);
                    }
                }else if (p<s){
                    String URL=url + "&page=" + next_page + "&token=" + token;
                    getNetData(URL);
                    if (adapter!=null){
                        adapter.finishLoad(BaseRecyclerViewAdapter.STATE_SUCCESS);
                    }
                }else {
                    //加载失败
                    if (adapter!=null){
                        adapter.finishLoad(BaseRecyclerViewAdapter.STATE_FAILURE);
                    }
                }
            }
        },2000);
    }
    //设置点击事件
    public void setOnItemclicktoRecycle(){
        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnRVItemClickListener<TravelNotes.RecordBean>() {
            @Override
            public void onClick(View view, TravelNotes.RecordBean item, int position) {
                TextView textView=view.findViewById(R.id.moment_mine_id);
                if (textView!=null) {
                    String moment_id = textView.getText().toString();
                    Intent intent = new Intent(getActivity(), NotesDetailsActivity.class);
                    intent.putExtra("note_id",moment_id);
                    startActivity(intent);
                }
            }
        });
    }
    //重新加载网络数据
    public void getNetData(String URL){
        okManager=new OkManager();
        okManager.asyncJsonStringByURL(URL, new OkManager.Func1() {
            @Override
            public void onResponse(String result) throws JSONException {
                Log.i("TAG", "onResponse: "+result+"-------------");
                customProgress.todismiss();
                Gson gson = new Gson();
                TravelNotes travelnotes = gson.fromJson(result, TravelNotes.class);
                if (travelnotes != null) {
                    text_empty.setVisibility(View.GONE);
                    page = travelnotes.getPage();
                    next_page = travelnotes.getNext_page();
                    sum_page = travelnotes.getTotal_page();
                    List<TravelNotes.RecordBean> list = travelnotes.getRecord();
                    if (list.size()==0){
                        text_empty.setVisibility(View.VISIBLE);
                        loadMore();
                    }else if(list.size()<2){
                        //如果不满一屏幕 则调用上拉加载的逻辑
                        loadMore();
                        text_empty.setVisibility(View.GONE);
                    }
                    else {
                        text_empty.setVisibility(View.GONE);
                    }
                    datas.addAll(list);
                    adapter = new MineRecyclerViewAdapter(getActivity(), datas, R.layout.item_moment_mine, true,MineChildFragment1.this);
                    setOnItemclicktoRecycle();
                    adapter.setCanLoadMore(true);
                    adapter.setPTRecyclerView(recyclerview);  //Adapter绑定RecyclerView
                    recyclerview.setRecyclerAdapter(adapter);
                    recyclerview.scrollToPosition(position);
                    swipeRefreshLayout.setRefreshing(false);  //下拉刷新结束
                    position=datas.size();
                }
            }
        }, new OkManager.Fail() {
            @Override
            public void onFiled() {
                if (customProgress!=null){
                    customProgress.todismiss();
                }
                if (net_btn!=null){
                    net_btn.setVisibility(View.VISIBLE);
                }
                if (getActivity()!=null){
                    Toast.makeText(getActivity(),"请检查网络",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.net_btn){
            customProgress=new CustomProgress(getActivity());
            customProgress.show(getActivity(),"加载中",true,null);
            initNETDatas();
        }
    }
    @Override
    public void dooIt(String id) {
        Intent intent = new Intent(getActivity(), NotesDetailsActivity.class);
        intent.putExtra("note_id",id);
        startActivity(intent);
    }
    @Override
    public void checkGood(String id, String ifGood) {
        //http://10.0.0.137/travel/travel_app.php?action=travel_good&token=xxx&rec_id=xx
        //http://10.0.0.137/travel/travel_app.php?action=travel_good&token=xxx&rec_id=xx&good_delete=1
        if (ifGood.equals("good")){
            //点赞
            String url=URLUtil.URL+URLUtil.path+"?action=travel_good&token="+token+"&rec_id="+id;
            putStatuToServer(url);
        }else if(ifGood.equals("false")){
            //取消点赞
            String url=URLUtil.URL+URLUtil.path+"?action=travel_good&token="+token+"&rec_id="+id+"&good_delete=1";
            putStatuToServer(url);
        }
    }
    @Override
    public void checkColloct(String id) {
        //http://10.0.0.137/travel/travel_app.php?action=travel_collect&token=xxx&rec_id=xx&collect_delete=1
        //取消收藏
     /*   String url=URLUtil.URL+URLUtil.path+"?action=travel_collect&token="+token+"&rec_id="+id+"&collect_delete=1";
        putStatuToServer(url);*/
    }
    @Override
    public void deleteNotes(final String id) {
         //http://10.0.0.137/travel/travel_app.php?action=travel_delete&token=xxx&rec_id=xx
        new AlertDialog.Builder(getActivity()).setTitle("删除游记！")//设置对话框标题

                .setMessage("您确定要删除该条游记吗？")//设置显示的内容

                .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        customProgress=new CustomProgress(getActivity());
                        customProgress.show(getActivity(),"请稍等！",true,null);
                        //取消收藏
                        String url=URLUtil.URL+URLUtil.path+"?action=travel_delete&token="+token+"&rec_id="+id;
                        putStatuToServer(url);
                    }
                }).setNegativeButton("取消",null).show();//在按键响应事件中显示此对话框

    }

    @Override
    public void ShareMsg(String id,String title,String content) {
        final String url=URLUtil.URL+"/travel/transpond.php?action=transpond&rec_id="+id;
        ShareSdkUtils.showShare(id, getActivity(), new ShareSdkUtils.WeixinCallBack() {
            @Override
            public void OnComplete() {
                Toast.makeText(getActivity(),"成功了！",Toast.LENGTH_SHORT).show();
                putStatuToServer(url);
            }
            @Override
            public void onError() {
                Toast.makeText(getActivity(),"失敗了！",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancel() {
                Toast.makeText(getActivity(),"取消了！",Toast.LENGTH_SHORT).show();
            }
        },title,content);
    }

    //处理 点赞 和收藏的逻辑
    public void putStatuToServer(String url){
        okManager=new OkManager();
        okManager.asyncJsonStringByURL(url, new OkManager.Func1() {
            @Override
            public void onResponse(String result) throws JSONException {
                Log.i("TAG", "onResponse: "+result);
                JSONObject object=new JSONObject(result);
                if (object.getString("delete")!=null){
                    initNETDatas();
                }
            }
        }, new OkManager.Fail() {
            @Override
            public void onFiled() {
                Toast.makeText(getActivity(),"请检查网络状态",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
