package com.shiyuesoft.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import com.shiyuesoft.activity.MainActivity;
import com.shiyuesoft.activity.TourDetailsActivity;
import com.shiyuesoft.adapter.BaseRecyclerViewAdapter;
import com.shiyuesoft.adapter.MyRVViewHolder;
import com.shiyuesoft.adapter.NewTourHolder;
import com.shiyuesoft.bean.NewTour;
import com.shiyuesoft.util.CustomProgress;
import com.shiyuesoft.util.MD5;
import com.shiyuesoft.util.NetUtil;
import com.shiyuesoft.util.OkManager;
import com.shiyuesoft.util.PTRecycleView;
import com.shiyuesoft.util.URLUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangg on 2017/7/19.
 * 壮游片段
 */
public class NewTourFragment extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener, PTRecycleView.PTLoadMoreListener {
    private View view;
    private PTRecycleView recyclerview;
    private LinearLayoutManager mlayoutManager;
    private RecyclerViewAdapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CustomProgress customProgress;
    private Handler handler = new Handler();
    //代表图的URL集合
    private List<NewTour.StrongBean> datas = new ArrayList<>();
    private String url = URLUtil.URL + URLUtil.path + "?action=travel_strong";
    private OkManager okManager;
    private String token;
    private String page = "1";
    private String next_page = "1";
    private String sum_page = "1";
    private SharedPreferences sharedPreferences;
    private String TAG = NewTourFragment.class.getSimpleName();
    //重新加载网络
    private LinearLayout net_btn;
    //数据为空时展示
    private LinearLayout text_empty;
    //username 和 pwd 之后可以登陆之后从SharedPreferences获取
    private String username = "admin";
    private String pwd = "sysesp";
    private String loginurl = URLUtil.LOGIN_URL;
    private SharedPreferences.Editor editor;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmenrt_newtour, container, false);
        initView(view);
        return view;
    }

    //初始化控件
    private void initView(View view) {
        text_empty = view.findViewById(R.id.text_empty);
        net_btn = view.findViewById(R.id.net_btn);
        net_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customProgress = new CustomProgress(getActivity());
                customProgress.show(getActivity(), "加载中", true, null);
                //重新加载网络数据
                initDatas();
            }
        });
        sharedPreferences = getActivity().getSharedPreferences("tokenValue", getActivity().MODE_APPEND);
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "1");
        recyclerview = view.findViewById(R.id.recyclerView);
        recyclerview.init(getActivity());
        recyclerview.setLoadMoreListener(this);
        //下拉刷新
        swipeRefreshLayout = view.findViewById(R.id.SwipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.blue_bac));
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        customProgress = new CustomProgress(getActivity());
        customProgress.show(getActivity(), "加载中", true, null);
        initDatas();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (customProgress != null) {
                    customProgress.todismiss();
                }
            }
        }, 4000);
    }

    private void initDatas() {
//        if (NetUtil.checkNet(getActivity())){
        //如果没有拿到token值则重新请求
        if ("1".equals(token)) {
            Log.i("TAG", "initDatas:" + "------------------------");
            checkLogin();
        }else {
        page = "1";
        String URL = url + "&page=" + page + "&token=" + token;
        Log.i("TAG", "initData: " + URL);
        datas = new ArrayList<>();
        getNetData(URL);
        net_btn.setVisibility(View.GONE);
        }
    /*    }else {
            customProgress.todismiss();
            net_btn.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(),"请检查网络",Toast.LENGTH_SHORT).show();
        }*/
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //处理下拉刷新
                initDatas();
            }
        }, 2000);
    }

    @Override
    public void loadMore() {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int p = Integer.parseInt(page);
                int s = Integer.parseInt(sum_page);
                //是说明是最后一页
                if (p == s) {
                    //没有更多数据了
                    if (mAdapter != null) {
                        mAdapter.finishLoad(BaseRecyclerViewAdapter.STATE_FINISH);
                    }
                } else if (p < s) {
                    String URL = url + "&page=" + next_page + "&token=" + token;
                    getNetData(URL);
                    if (mAdapter != null) {
                        mAdapter.finishLoad(BaseRecyclerViewAdapter.STATE_SUCCESS);
                    }
                } else {
                    //加载失败
                    if (mAdapter != null) {
                        mAdapter.finishLoad(BaseRecyclerViewAdapter.STATE_FAILURE);
                    }
                }
            }
        }, 2000);
    }

    private class RecyclerViewAdapter extends BaseRecyclerViewAdapter<NewTour.StrongBean, MyRVViewHolder> {
        /**
         * @param context
         * @param viewId
         */
        public RecyclerViewAdapter(Context context, List<NewTour.StrongBean> list, int viewId) {
            super(context, list, viewId);
        }

        @Override
        protected void bindDataToItemView(MyRVViewHolder myRVViewHolder, NewTour.StrongBean item) {
            if (myRVViewHolder instanceof NewTourHolder) {
                NewTourHolder holder = (NewTourHolder) myRVViewHolder;
                holder.imageView.setImageUrl(item.getCover_img());
                holder.tour_id_text.setText(item.getId().toString());
            }
        }

    }

    private void setOnClickToRecyler() {
        mAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnRVItemClickListener<NewTour.StrongBean>() {
            @Override
            public void onClick(View view, NewTour.StrongBean item, int position) {
                TextView t = view.findViewById(R.id.tour_id);
                if (t != null) {
                    String id = t.getText().toString();
                    Intent intent = new Intent(getActivity(), TourDetailsActivity.class);
                    intent.putExtra("tour_id", id);
                    startActivity(intent);
                }
            }
        });
    }

    private void getNetData(String URL) {
        okManager = new OkManager();
        okManager.asyncJsonStringByURL(URL, new OkManager.Func1() {
            @Override
            public void onResponse(String result) throws JSONException {
                if (result != null && result != "") {
                    JSONObject json = new JSONObject(result);
                    page = json.getString("page");
                    next_page = json.getString("next_page");
                    sum_page = json.getString("total_page");
                }
                Gson gson = new Gson();
                NewTour newTour = gson.fromJson(result, NewTour.class);
                List<NewTour.StrongBean> list = newTour.getStrong();
                if (list.size() == 0) {
                    text_empty.setVisibility(View.VISIBLE);
                    loadMore();
                }
                if (list.size() <= 2) {
                    loadMore();
                    text_empty.setVisibility(View.GONE);
                }
                datas.addAll(list);
                mAdapter = new RecyclerViewAdapter(getActivity(), datas, R.layout.item_newtour);
                mAdapter.setCanLoadMore(true);
                mAdapter.setPTRecyclerView(recyclerview);  //Adapter绑定RecyclerView
                recyclerview.setRecyclerAdapter(mAdapter);
                setOnClickToRecyler();
                swipeRefreshLayout.setRefreshing(false);  //下拉刷新结束
                customProgress.todismiss();
            }
        }, new OkManager.Fail() {
            @Override
            public void onFiled() {
                if (customProgress != null) {
                    customProgress.todismiss();
                }
                if (net_btn != null) {
                    net_btn.setVisibility(View.VISIBLE);
                }
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), "请检查网络", Toast.LENGTH_SHORT).show();
                }
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
                Log.i("TAG", "onResponse: " + jsonObject.toString());
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
                        String URL = url + "&page=" + page + "&token=" + token1;
                        Log.i("TAG", "initData: " + URL);
                        datas = new ArrayList<>();
                        getNetData(URL);
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
