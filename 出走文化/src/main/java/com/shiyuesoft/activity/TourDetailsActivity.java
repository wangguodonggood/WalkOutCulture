package com.shiyuesoft.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.image.SmartImage;
import com.loopj.android.image.SmartImageView;
import com.shiyuesoft.R;
import com.shiyuesoft.adapter.TourdetailsAdapter;
import com.shiyuesoft.bean.NewTour;
import com.shiyuesoft.bean.TourDetails;
import com.shiyuesoft.util.ButtonClickUtil;
import com.shiyuesoft.util.CustomProgress;
import com.shiyuesoft.util.MD5;
import com.shiyuesoft.util.OkManager;
import com.shiyuesoft.util.SourceUtil;
import com.shiyuesoft.util.URLUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 壮游页面点击进来的详情页
 */
public class TourDetailsActivity extends AppCompatActivity implements View.OnClickListener,View.OnTouchListener{
    private LinearLayout back,comment_linear,all_linear;
    private ListView listView;
    private TourdetailsAdapter adapter;
    private LinearLayout layout_edit;
    private String tour_id;
    private Intent intent;
    private String url= URLUtil.URL+URLUtil.path+"?action=travel_strong_info";
    private SharedPreferences sharedpreference;
    //登录成功就会有Token返回值
    private String token;
    private OkManager manager;
    //详情标题
    private TextView tour_title;
    //详情展示的图片
    private SmartImageView imageView;
    //详情content
    private WebView content;
    private CustomProgress customProgress,customProgress1;
    private Button send;
    //评论的内容
    private EditText edit_content;
    //评论的集合
    private List<TourDetails.ReviewBean> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_details);
        initView();
    }
    private void initView() {
        edit_content= (EditText) findViewById(R.id.review_content);
        send= (Button) findViewById(R.id.send);
        send.setOnClickListener(this);
        customProgress=new CustomProgress(this);
        customProgress.show(this,"加载中",true,null);
        tour_title= (TextView) findViewById(R.id.title);
        imageView= (SmartImageView) findViewById(R.id.tour_image);
        content= (WebView) findViewById(R.id.content);
        sharedpreference=getSharedPreferences("tokenValue",this.MODE_APPEND);
        token=sharedpreference.getString("token","1");
        intent=getIntent();
        tour_id=intent.getExtras().getString("tour_id");
        layout_edit= (LinearLayout) findViewById(R.id.layout_edit);
        layout_edit.setOnClickListener(this);
        back= (LinearLayout) findViewById(R.id.back);
        comment_linear= (LinearLayout) findViewById(R.id.comment_linear);
        all_linear= (LinearLayout) findViewById(R.id.all_linear);
        comment_linear.setFocusable(true);
        comment_linear.setFocusableInTouchMode(true);
        comment_linear.requestFocus();
        comment_linear.requestFocusFromTouch();
        all_linear.setOnTouchListener(this);
        listView= (ListView) findViewById(R.id.listview);
        listView.setOnTouchListener(this);
        back.setOnClickListener(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("TAG", "run: "+customProgress.isShowing());
                if (customProgress!=null){
                    customProgress.todismiss();
                }
            }
        },4000);
        initNetData();
    }
  //加载网络数据
    private void initNetData() {
        manager=new OkManager();
        String URL=url+"&token="+token+"&rec_id="+tour_id;
        Log.i("TAG", "initNetData: "+URL);
        manager.asyncJsonStringByURL(URL, new OkManager.Func1() {
            @Override
            public void onResponse(String result) throws JSONException {
                Gson gson = new Gson();
                TourDetails tourdetails = gson.fromJson(result, TourDetails.class);
                TourDetails.StrongBean strongbean = tourdetails.getStrong();
                Log.i("TAG", "onResponse: "+strongbean.getContent());
                if (strongbean != null) {
                    tour_title.setText(strongbean.getTitle());
                    imageView.setImageUrl(strongbean.getCover_img());
                    content.loadDataWithBaseURL(null,strongbean.getContent(),"text/html","utf-8",null);
                }
                //评论数据
                data = tourdetails.getReview();
                adapter = new TourdetailsAdapter(TourDetailsActivity.this, data);
                listView.setAdapter(adapter);
                if (customProgress != null) {
                    customProgress.todismiss();
                }
            }
        }, new OkManager.Fail() {
            @Override
            public void onFiled() {
                if (customProgress != null) {
                    customProgress.todismiss();
                }
                Toast.makeText(TourDetailsActivity.this,"请检查网络！",Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onClick(View view) {
        int id=view.getId();
        if (id==R.id.back){
            finish();
        }else if (id==R.id.layout_edit){
            startActivity(new Intent(TourDetailsActivity.this,SendNotesActivity.class));
        }else if (id==R.id.send){
            if ("".equals(edit_content.getText().toString())){
                Toast.makeText(TourDetailsActivity.this,"请填写评论内容",Toast.LENGTH_SHORT).show();
            }else {
                if (!ButtonClickUtil.isFastDoubleClick(view.getId())){
                    customProgress1=new CustomProgress(this);
                    customProgress1.show(this,"发表中",false,null);
                    String url=URLUtil.URL+URLUtil.path;
                    //发送评论按钮
                    manager = new OkManager();
                    Map<String, String> params = new HashMap<>();
                    params.put("token", token);
                    params.put("action", "strong_review");
                    params.put("rec_id", tour_id);
                    params.put("review_content",edit_content.getText().toString() );
                    manager.sendComplexForm(url, params, new OkManager.Func4() {
                        @Override
                        public void onResponse(JSONObject jsonObject) throws JSONException {
                            customProgress1.todismiss();
                            if (jsonObject != null) {
                                String result = jsonObject.getString("strong_review");
                                if (result.equals("yes")) {
                                    Toast.makeText(TourDetailsActivity.this, "发表成功", Toast.LENGTH_SHORT).show();
                                    TourDetails.ReviewBean reviewBean = new TourDetails.ReviewBean();
                                    reviewBean.setUser_img(sharedpreference.getString("userImage", ""));
                                    reviewBean.setUser_name(sharedpreference.getString("userName", "名字"));
                                    reviewBean.setUser_type(sharedpreference.getString("userType", "1"));
                                    reviewBean.setReview_content(edit_content.getText().toString());
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String date = format.format(new Date());
                                    reviewBean.setReview_date(date);
                                    data.add(0, reviewBean);
                                    adapter.notifyDataSetChanged();
                                    edit_content.setText("");
                                } else {
                                    Toast.makeText(TourDetailsActivity.this, "发送失败,请重试", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }, new OkManager.Fail() {
                        @Override
                        public void onFiled() {
                            if (customProgress1!=null){
                                customProgress1.todismiss();
                            }
                            Toast.makeText(TourDetailsActivity.this, "发送失败,网络不给力", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(TourDetailsActivity.this,"请不要连续点击",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view.getId()==R.id.listview){
            listView.setFocusable(true);
            listView.setFocusableInTouchMode(true);
            listView.requestFocus();
            listView.requestFocusFromTouch();
        }else {
        all_linear.setFocusable(true);
        all_linear.setFocusableInTouchMode(true);
        all_linear.requestFocus();
        all_linear.requestFocusFromTouch();
        }
        return false;
    }
}
