package com.shiyuesoft.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shiyuesoft.R;
import com.shiyuesoft.adapter.NotedetailsAdapter;
import com.shiyuesoft.bean.NoteDetails;
import com.shiyuesoft.util.ButtonClickUtil;
import com.shiyuesoft.util.CircleImageView;
import com.shiyuesoft.util.CustomProgress;
import com.shiyuesoft.util.ImageLoaderUtil;
import com.shiyuesoft.util.MyListView;
import com.shiyuesoft.util.NineGridTestLayout;
import com.shiyuesoft.util.OkManager;
import com.shiyuesoft.util.ShareSdkUtils;
import com.shiyuesoft.util.URLUtil;

import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 游记详情页面
 */
public class NotesDetailsActivity extends AppCompatActivity implements View.OnTouchListener,View.OnClickListener{
    //头像
    private CircleImageView imageViewPlus;
    //姓名
    private TextView notedetails_name;
    //身份
    private TextView notedetails_identity;
    //时间
    private TextView notedetails_time;
    //内容标题
    private TextView notedetails_title;
    //游记的具体内容
    private TextView notedetails_content;
    //显示游记的图片
    private NineGridTestLayout nineGridTestLayout;
    //地址
    private TextView notedetails_address,ifGood,ifColloct;
    private RadioButton r1,r2,r3,r4;
    //评论布局
    private LinearLayout notedetails_linear,note_allLinear,back,layout_edit;
    private MyListView notedetails_listview;
    //获取游记id
    private Intent intent;
    private String note_id;
    //登录成功就会有Token返回值
    private String token;
    private OkManager manager;
    private Button send;
    //评论的内容
    private EditText edit_content;
    private SharedPreferences sharedpreference;
    //评论的集合
    private List<NoteDetails.ReviewBean> commentData;
    private String url= URLUtil.URL+URLUtil.path+"?action=travel_record_info";
    //显示评论内容的适配器
    private NotedetailsAdapter adapter;
    private CustomProgress customProgress,customProgress1;
    private ImageView jingFlag;
    //评论数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_details);
        initView();
    }
    private void initView() {
        //customProgress自定义的加载框...
        customProgress=new CustomProgress(this);
        customProgress.show(this,"加载中",true,null);
        sharedpreference=getSharedPreferences("tokenValue",this.MODE_APPEND);
        token=sharedpreference.getString("token","1");
        intent=getIntent();
        note_id=intent.getExtras().getString("note_id");
        layout_edit= (LinearLayout) findViewById(R.id.note_layout_edit);
        back= (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(this);
        layout_edit.setOnClickListener(this);
        send= (Button) findViewById(R.id.send);
        send.setOnClickListener(this);
        ifGood= (TextView) findViewById(R.id.ifGood);
        ifColloct= (TextView) findViewById(R.id.ifColloct);
        edit_content= (EditText) findViewById(R.id.edit_content);
        imageViewPlus= (CircleImageView) findViewById(R.id.notedetails_ImageView);
        notedetails_name= (TextView) findViewById(R.id.notedetails_name);
        notedetails_identity= (TextView) findViewById(R.id.notedetails_identity);
        notedetails_time= (TextView) findViewById(R.id.notedetails_time);
        notedetails_title= (TextView) findViewById(R.id.notedetails_title);
        notedetails_content= (TextView) findViewById(R.id.notedetails_content);
        notedetails_address= (TextView) findViewById(R.id.notedetails_address);
        nineGridTestLayout= (NineGridTestLayout) findViewById(R.id.layout_nine_grid);
        notedetails_linear= (LinearLayout) findViewById(R.id.notedetails_linear);
        jingFlag= (ImageView) findViewById(R.id.jingFlag);
        r1= (RadioButton) findViewById(R.id.m_r1);
        r2= (RadioButton) findViewById(R.id.m_r2);
        r3= (RadioButton) findViewById(R.id.m_r3);
        r4= (RadioButton) findViewById(R.id.m_r4);
        r1.setOnClickListener(this);
        r2.setOnClickListener(this);
        r3.setOnClickListener(this);
        notedetails_linear.setFocusable(true);
        notedetails_linear.setFocusableInTouchMode(true);
        notedetails_linear.requestFocus();
        notedetails_linear.requestFocusFromTouch();
        note_allLinear= (LinearLayout) findViewById(R.id.note_allLinear);
        notedetails_listview= (MyListView) findViewById(R.id.notedetails_listview);
        notedetails_listview.setOnTouchListener(this);
        note_allLinear.setOnTouchListener(this);
        notedetails_listview.setOnTouchListener(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (customProgress!=null){
                    customProgress.todismiss();
                }
            }
        },4000);
        //加载网络数据
        initDatas();
    }
    private void initDatas() {
          String URL=url+"&token="+token+"&rec_id="+note_id;
          Log.i("TAG", "initDatas: "+URL);
          manager=new OkManager();
          manager.asyncJsonStringByURL(URL, new OkManager.Func1() {
              @Override
              public void onResponse(String result) throws JSONException {
                   //游记详情
                  Gson gson=new Gson();
                  NoteDetails noteDetails=gson.fromJson(result, NoteDetails.class);
                  if (noteDetails!=null){
                      NoteDetails.RecordBean bean=noteDetails.getRecord();
                      commentData=noteDetails.getReview();
                      if (bean!=null){
                          setViewValue(bean);
                      }
                      setCommentValue(commentData);
                  }
                  if (customProgress!=null){
                      customProgress.todismiss();
                  }
              }
              /*设置评论内容*/
              private void setCommentValue(List<NoteDetails.ReviewBean> commentData) {
                  adapter=new NotedetailsAdapter(NotesDetailsActivity.this,commentData);
                  notedetails_listview.setAdapter(adapter);
              }
              private void setViewValue(NoteDetails.RecordBean bean) {
                  //没有头像的图片
                  ImageLoaderUtil.getImageLoader(NotesDetailsActivity.this).displayImage(bean.getUser_img(),
                          imageViewPlus,ImageLoaderUtil.getPhotoImageOption());
                  notedetails_name.setText(bean.getUser_name());
                  notedetails_identity.setText(URLUtil.getType(bean.getUser_type()));
                  notedetails_time.setText(bean.getPublish_date());
                  notedetails_title.setText(bean.getTitle());
                  notedetails_content.setText(bean.getContent());
                  nineGridTestLayout.setUrlList(bean.getImg());
                  notedetails_address.setText(bean.getPlace());
                  if (bean.getElite().equals("1")){
                      jingFlag.setVisibility(View.VISIBLE);
                  }else {
                      jingFlag.setVisibility(View.GONE);
                  }
                  if (bean.getState().equals("0")){
                      //代表审核中的游记
                      r1.setFocusable(false);
                      r1.setClickable(false);

                      r2.setFocusable(false);
                      r2.setClickable(false);

                      r3.setFocusable(false);
                      r3.setClickable(false);

                      edit_content.setFocusable(false);
                      edit_content.setClickable(false);

                  }
                  if (bean.getIf_good().equals("1")){
                      r3.setChecked(true);
                      ifGood.setText("good");
                  }else {
                      r3.setChecked(false);
                      ifGood.setText("false");
                  }
                  if (bean.getIf_collect().equals("1")){
                      r2.setChecked(true);
                      ifColloct.setText("colloct");
                  }else {
                      r2.setChecked(false);
                      ifColloct.setText("nocolloct");
                  }
                  r1.setText(bean.getTranspond());
                  r3.setText(bean.getGood());
                  r4.setText(bean.getReview_num());
              }
          },new OkManager.Fail(){
              @Override
              public void onFiled() {
                  if (customProgress != null) {
                      customProgress.todismiss();
                  }
                  Toast.makeText(NotesDetailsActivity.this,"请检查网络！",Toast.LENGTH_SHORT).show();
              }
          });
    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view.getId()==R.id.notedetails_listview){
            notedetails_listview.setFocusable(true);
            notedetails_listview.setFocusableInTouchMode(true);
            notedetails_listview.requestFocus();
            notedetails_listview.requestFocusFromTouch();
        }else {
            note_allLinear.setFocusable(true);
            note_allLinear.setFocusableInTouchMode(true);
            note_allLinear.requestFocus();
            note_allLinear.requestFocusFromTouch();
        }
        return false;
    }
    @Override
    public void onClick(View view) {
        int id=view.getId();
        if (id==R.id.back){
         finish();
        }else if (id==R.id.note_layout_edit){
            startActivity(new Intent(this,SendNotesActivity.class));
        }else if (id==R.id.send){
            //发送评论
            if ("".equals(edit_content.getText().toString())){
                Toast.makeText(NotesDetailsActivity.this,"请填写评论内容",Toast.LENGTH_SHORT).show();
            }else {
                if (!ButtonClickUtil.isFastDoubleClick(view.getId())){
                    customProgress1=new CustomProgress(this);
                    customProgress1.show(this,"发表中",true,null);
                    String url=URLUtil.URL+URLUtil.path;
                    //发送评论按钮
                    manager = new OkManager();
                    Map<String, String> params = new HashMap<>();
                    params.put("token", token);
                    params.put("action", "strong_review");
                    params.put("rec_id", note_id);
                    params.put("review_content",edit_content.getText().toString() );
                    manager.sendComplexForm(url, params, new OkManager.Func4() {
                        @Override
                        public void onResponse(JSONObject jsonObject) throws JSONException {
                            customProgress1.todismiss();
                            if (jsonObject != null) {
                                String result = jsonObject.getString("strong_review");
                                if (result.equals("yes")) {
                                    if (!r4.getText().toString().equals("")){
                                        int num=Integer.parseInt(r4.getText().toString());
                                        r4.setText((++num)+"");
                                    }
                                    Toast.makeText(NotesDetailsActivity.this, "发表成功", Toast.LENGTH_SHORT).show();
                                    NoteDetails.ReviewBean reviewBean = new NoteDetails.ReviewBean();
                                    reviewBean.setUser_img(sharedpreference.getString("userImage", ""));
                                    reviewBean.setUser_name(sharedpreference.getString("userName", "名字"));
                                    reviewBean.setUser_type(sharedpreference.getString("userType", "1"));
                                    reviewBean.setReview_content(edit_content.getText().toString());
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String date = format.format(new Date());
                                    reviewBean.setReview_date(date);
                                    commentData.add(0, reviewBean);
                                    adapter.notifyDataSetChanged();
                                    edit_content.setText("");
                                } else {
                                    Toast.makeText(NotesDetailsActivity.this, "发送失败,请重试", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }, new OkManager.Fail() {
                        @Override
                        public void onFiled() {
                            if (customProgress1!=null){
                                customProgress1.todismiss();
                            }
                            Toast.makeText(NotesDetailsActivity.this, "发送失败,网络不给力", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(NotesDetailsActivity.this,"请不要连续点击",Toast.LENGTH_SHORT).show();
                }
            }
        }else if (id==R.id.m_r2){
            //收藏
            if (!ButtonClickUtil.isFastDoubleClick(view.getId())){
                if (ifColloct.getText().toString().equals("colloct")){
                    r2.setChecked(false);
                    ifColloct.setText("nocolloct");
                }else {
                    r2.setChecked(true);
                    ifColloct.setText("colloct");
                }
                checkColloct(ifColloct.getText().toString());
            }else {
                Toast.makeText(NotesDetailsActivity.this,"请不要连续点击",Toast.LENGTH_SHORT).show();
            }
        }else if (id==R.id.m_r1){
            final String url=URLUtil.URL+"/travel/transpond.php?action=transpond&rec_id="+id;
            //分享
            ShareSdkUtils.showShare(note_id, NotesDetailsActivity.this, new ShareSdkUtils.WeixinCallBack() {
                @Override
                public void OnComplete() {
                     Toast.makeText(NotesDetailsActivity.this,"成功了！",Toast.LENGTH_SHORT).show();
                    putStatuToServer(url);
                }
                @Override
                public void onError() {
                     Toast.makeText(NotesDetailsActivity.this,"失敗了！",Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onCancel() {
                     Toast.makeText(NotesDetailsActivity.this,"取消了！",Toast.LENGTH_SHORT).show();
                }
            },notedetails_title.getText().toString(),notedetails_content.getText().toString());
        }else if (id==R.id.m_r3){
            //点赞
            if (!ButtonClickUtil.isFastDoubleClick(view.getId())){
                int num=Integer.parseInt(r3.getText().toString());
                if (ifGood.getText().toString().equals("good")){
                    r3.setChecked(false);
                    ifGood.setText("false");
                    if (num>0){
                        r3.setText(--num+"");
                    }
                }else {
                    r3.setChecked(true);
                    ifGood.setText("good");
                    r3.setText(++num+"");
                }
                checkGood(ifGood.getText().toString());
            }else {
                Toast.makeText(NotesDetailsActivity.this,"请不要连续点击",Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    //收藏 点赞 发送的网络请求
    public void putStatuToServer(String url){
        manager=new OkManager();
        manager.asyncJsonStringByURL(url, new OkManager.Func1() {
            @Override
            public void onResponse(String result) throws JSONException {
                Log.i("TAG", "onResponse: "+result);
            }
        }, new OkManager.Fail() {
            @Override
            public void onFiled() {
                Toast.makeText(NotesDetailsActivity.this,"请检查网络状态",Toast.LENGTH_SHORT).show();
            }
        });
    }
    //收藏
    public void checkColloct(String ifColloct){
        if (ifColloct.equals("colloct")){
            //收藏
            String url=URLUtil.URL+URLUtil.path+"?action=travel_collect&token="+token+"&rec_id="+note_id;
            putStatuToServer(url);
        }else if (ifColloct.equals("nocolloct")){
            //取消收藏
            String url=URLUtil.URL+URLUtil.path+"?action=travel_collect&token="+token+"&rec_id="+note_id+"&collect_delete=1";
            putStatuToServer(url);
        }
    }
    //点赞
    public void checkGood(String ifGood){
        //http://10.0.0.137/travel/travel_app.php?action=travel_good&token=xxx&rec_id=xx
        //http://10.0.0.137/travel/travel_app.php?action=travel_good&token=xxx&rec_id=xx&good_delete=1
        if (ifGood.equals("good")){
            //点赞
            String url=URLUtil.URL+URLUtil.path+"?action=travel_good&token="+token+"&rec_id="+note_id;
            putStatuToServer(url);
        }else if(ifGood.equals("false")){
            //取消点赞
            String url=URLUtil.URL+URLUtil.path+"?action=travel_good&token="+token+"&rec_id="+note_id+"&good_delete=1";
            putStatuToServer(url);
        }
    }
}
