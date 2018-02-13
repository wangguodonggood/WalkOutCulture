package com.shiyuesoft.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
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
import com.shiyuesoft.activity.TourDetailsActivity;
import com.shiyuesoft.adapter.BaseRecyclerViewAdapter;
import com.shiyuesoft.adapter.RecyclerViewAdapter;
import com.shiyuesoft.bean.TravelNotes;
import com.shiyuesoft.util.CustomProgress;
import com.shiyuesoft.util.MD5;
import com.shiyuesoft.util.OkManager;
import com.shiyuesoft.util.PTRecycleView;
import com.shiyuesoft.util.ShareSdkUtils;
import com.shiyuesoft.util.URLUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by wangg on 2017/7/20.
 * 校园游记 精华
 */
public class NotesChildFragment2 extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        PTRecycleView.PTLoadMoreListener,View.OnClickListener,RecyclerViewAdapter.CommonListener{
    private  View view;
    private PTRecycleView recyclerview;
    private RecyclerViewAdapter adapter;
    private String token;
    private String page="1";
    private String next_page="1";
    private String sum_page="1";
    private String url= URLUtil.URL+URLUtil.path+"?action=travel_record&elite=1";
    private List<TravelNotes.RecordBean> datas=new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private OkManager okManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CustomProgress customProgress;
    private LinearLayout net_btn;
    private LinearLayout text_empty;
    //可以直接跳转到下一个刷新出来的内容
    private int position;
    //username 和 pwd 之后可以登陆之后从SharedPreferences获取
    private String username = "admin";
    private String pwd = "sysesp";
    private String loginurl = URLUtil.LOGIN_URL;
    private SharedPreferences.Editor editor;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragmenrt_noteschild2,container,false);
        initView(view);
        return view;
    }
    private void initView(View view) {
        text_empty=view.findViewById(R.id.text_empty);
        net_btn=view.findViewById(R.id.net_btn);
        net_btn.setOnClickListener(this);
        sharedPreferences=getActivity().getSharedPreferences("tokenValue", getActivity().MODE_APPEND);
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "1");
        recyclerview=view.findViewById(R.id.recycleviewj);
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
        customProgress=new CustomProgress(getActivity());
        customProgress.show(getActivity(),"加载中",true,null);
        initNETDatas();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (customProgress!=null){
                    customProgress.todismiss();
                }
            }
        },4000);
    }
    //加载网络数据
    private void initNETDatas() {
        if ("1".equals(token)){
            checkLogin();
        }else {
            position=0;
            String URL=url+"&token="+token+"&page=1";
            datas=new ArrayList<>();
            getNetWork(URL);
            net_btn.setVisibility(View.GONE);
        }
    }
    public void setOnItemclicktoRecycle(){
        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnRVItemClickListener<TravelNotes.RecordBean>() {
            @Override
            public void onClick(View view, TravelNotes.RecordBean item, int position) {
                TextView textView=view.findViewById(R.id.moment_id);
                if (textView!=null) {
                    String moment_id = textView.getText().toString();
                    Intent intent = new Intent(getActivity(), NotesDetailsActivity.class);
                    intent.putExtra("note_id",moment_id);
                    startActivity(intent);
                }
            }
        });
    }
    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                //处理下拉刷新
                initNETDatas();
                swipeRefreshLayout.setRefreshing(false);  //下拉刷新结束
            }
        },2000);
    }
    @Override
    public void loadMore() {
        if (swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }
        new Handler().postDelayed(new Runnable() {
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
                    getNetWork(URL);
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
    public void getNetWork(String URL){
        okManager=new OkManager();
        okManager.asyncJsonStringByURL(URL, new OkManager.Func1() {
            @Override
            public void onResponse(String result) throws JSONException {
                customProgress.todismiss();
                Gson gson = new Gson();
                TravelNotes travelnotes = gson.fromJson(result, TravelNotes.class);
                if (travelnotes != null) {
                    page = travelnotes.getPage();
                    next_page = travelnotes.getNext_page();
                    sum_page = travelnotes.getTotal_page();
                    List<TravelNotes.RecordBean> list = travelnotes.getRecord();
                    if (list.size()==0){
                        text_empty.setVisibility(View.VISIBLE);
                        loadMore();
                    }else if (list.size()<2){
                        loadMore();
                        text_empty.setVisibility(View.GONE);
                    }
                    else {
                        text_empty.setVisibility(View.GONE);
                    }
                    datas.addAll(list);
                    adapter = new RecyclerViewAdapter(getActivity(), datas, R.layout.item_moment,NotesChildFragment2.this);
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
                swipeRefreshLayout.setRefreshing(false);  //下拉刷新结束
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
        Log.i("TAG", "dooIt: "+id);
        Intent intent=new Intent(new Intent(getActivity(), NotesDetailsActivity.class)) ;
        intent.putExtra("note_id",id);
        startActivity(intent);
    }
    @Override
    public void checkGood(String id,String ifGood) {
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
    public void checkColloct(String id,String ifColloct) {
        //http://10.0.0.137/travel/travel_app.php?action=travel_collect&token=xxx&rec_id=xx&collect_delete=1
        //http://10.0.0.137/travel/travel_app.php?action=travel_collect&token=xxx&rec_id=xx
        if (ifColloct.equals("colloct")){
            //收藏
            String url=URLUtil.URL+URLUtil.path+"?action=travel_collect&token="+token+"&rec_id="+id;
            putStatuToServer(url);
        }else if (ifColloct.equals("nocolloct")){
            //取消收藏
            String url=URLUtil.URL+URLUtil.path+"?action=travel_collect&token="+token+"&rec_id="+id+"&collect_delete=1";
            putStatuToServer(url);
        }
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
    public void putStatuToServer(String url){
        okManager=new OkManager();
        okManager.asyncJsonStringByURL(url, new OkManager.Func1() {
            @Override
            public void onResponse(String result) throws JSONException {
                Log.i("TAG", "onResponse: "+result);
            }
        }, new OkManager.Fail() {
            @Override
            public void onFiled() {
                Toast.makeText(getActivity(),"请检查网络状态",Toast.LENGTH_SHORT).show();
            }
        });
    }
    //检测是否登录的函数
    private void checkLogin() {
        okManager = new OkManager();
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", MD5.getMD5Str(pwd));
        Log.i("TAG", "checkLogin: " + loginurl);
        okManager.sendComplexForm(loginurl, params, new OkManager.Func4() {
            @Override
            public void onResponse(JSONObject jsonObject) throws JSONException {
                //请求成功
                if (jsonObject != null) {
                    String token1 = jsonObject.getString("token");
                    String userName = jsonObject.getString("userName");
                    String userImage = jsonObject.getString("userImg");
                    String userType = jsonObject.getString("userType");
                    if (token != "" && token != null) {
                        token=token1;
                        editor.putString("token", token);
                        editor.putString("userName", userName);
                        editor.putString("userImage", userImage);
                        editor.putString("userType", userType);
                        editor.commit();
                        page = "1";
                        String URL = url + "&page=" + page + "&token=" + token;
                        Log.i("TAG", "onResponse: "+URL+"ppppppppppppppppp");
                        Log.i("TAG", "initData: " + URL);
                        datas = new ArrayList<>();
                        getNetWork(URL);
                        net_btn.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(getActivity(), "你还没登录！", Toast.LENGTH_LONG).show();
                        //跳转回登录页面
                    }
                }
            }
        }, new OkManager.Fail() {
            @Override
            public void onFiled() {
                //请求失败
                Toast.makeText(getActivity(), "亲，请检查你的网络！", Toast.LENGTH_LONG).show();
            }
        });
    }
}
