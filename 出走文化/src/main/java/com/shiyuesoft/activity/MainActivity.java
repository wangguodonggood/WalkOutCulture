package com.shiyuesoft.activity;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.shiyuesoft.R;
import com.shiyuesoft.fragment.MineFragment;
import com.shiyuesoft.fragment.NewTourFragment;
import com.shiyuesoft.fragment.TravelNotesFragment;
import com.shiyuesoft.util.Config;
import com.shiyuesoft.util.DepthPageTransformer;
import com.shiyuesoft.util.MD5;
import com.shiyuesoft.util.OkManager;
import com.shiyuesoft.util.URLUtil;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 主Activity 包含三个片段 和下方的三个导航按钮
 */
public class MainActivity extends FragmentActivity
        implements RadioGroup.OnCheckedChangeListener,ViewPager.OnPageChangeListener {
    //底部导航栏的三个底部
    private RadioButton r1, r2, r3;
    //导航栏分组
    private RadioGroup radioGroup;
    //导航栏支持滑动
    private ViewPager pager;
    //存放底部三个导航栏对应的片段
    private List<Fragment> fraglist;
    //跳转到写游记页面
    private LinearLayout layout_edit;
    //OKManger界面
    private OkManager manager;
    //测试数据
    private String username="admin";
    private String pwd="sysesp";
    String url=URLUtil.LOGIN_URL;
    //用来存放token值
    private SharedPreferences tokenshared;
    private SharedPreferences.Editor editor;
    private LinearLayout linear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化组件
        initView();
    }
    private void initView() {
        linear=findViewById(R.id.linear);
        linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tokenshared=getSharedPreferences("tokenValue",MODE_APPEND);
        editor=tokenshared.edit();
        manager=new OkManager();
        layout_edit=findViewById(R.id.layout_edit);
        layout_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SendNotesActivity.class));
            }
        });
        fraglist = new ArrayList<>();
        fraglist.add(new NewTourFragment());
        fraglist.add(new TravelNotesFragment());
        fraglist.add(new MineFragment());
        r1 =  findViewById(R.id.r1);
        r2 =  findViewById(R.id.r2);
        r3 =  findViewById(R.id.r3);
        radioGroup = findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(this);
        //配置viewpager相应参数
        pager =  findViewById(R.id.viewpager);
        pager.setPageTransformer(true, new DepthPageTransformer());
        pager.addOnPageChangeListener(this);
        pager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        /**
         * 获取当前设备的屏幕密度等基本参数
         */
        getDeviceDensity();
        //判断用户是否登录，获取token值
        checkLogin();
    }
    private void checkLogin() {
        Map<String, String> params=new HashMap<>();
        params.put("username",username);
        params.put("password", MD5.getMD5Str(pwd));
        Log.i("TAG", "checkLogin: "+url);
        manager.sendComplexForm(url, params, new OkManager.Func4() {
            @Override
            public void onResponse(JSONObject jsonObject) throws JSONException {
                Log.i("TAG", "onResponse: " + jsonObject.toString());
                //请求成功
                if (jsonObject != null) {
                    String token = jsonObject.getString("token");
                    String userName = jsonObject.getString("userName");
                    String userImage = jsonObject.getString("userImg");
                    String userType = jsonObject.getString("userType");
                    if (token != "" && token != null) {
                        editor.putString("token", token);
                        editor.putString("userName", userName);
                        editor.putString("userImage", userImage);
                        editor.putString("userType", userType);
                        editor.commit();
                        //跳转回登录页面
                    } else {
                        Toast.makeText(MainActivity.this, "你还没登录！", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }, new OkManager.Fail() {
            @Override
            public void onFiled() {
                //请求失败
                Toast.makeText(MainActivity.this, "亲，请检查你的网络！", Toast.LENGTH_LONG).show();
            }
        });
     }
    @Override
     public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
     }
     @Override
     public void onPageSelected(int position) {
         switch (position) {
             case 0:
                 r1.setChecked(true);
                 break;
             case 1:
                 r2.setChecked(true);
                 break;
             case 2:
                 r3.setChecked(true);
                 break;
             default:
                 break;
         }
     }
     @Override
     public void onPageScrollStateChanged(int state) {
     }
     @Override
     public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
         if (checkedId == R.id.r1) {
             r1.setTextColor(ContextCompat.getColor(this, R.color.blue));
             r2.setTextColor(ContextCompat.getColor(this, R.color.grey));
             r3.setTextColor(ContextCompat.getColor(this, R.color.grey));
             pager.setCurrentItem(0);
         } else if (checkedId == R.id.r2) {
             r2.setTextColor(ContextCompat.getColor(this, R.color.blue));
             r1.setTextColor(ContextCompat.getColor(this, R.color.grey));
             r3.setTextColor(ContextCompat.getColor(this, R.color.grey));
             pager.setCurrentItem(1);
         } else if (checkedId == R.id.r3) {
             r3.setTextColor(ContextCompat.getColor(this, R.color.blue));
             r1.setTextColor(ContextCompat.getColor(this, R.color.grey));
             r2.setTextColor(ContextCompat.getColor(this, R.color.grey));
             pager.setCurrentItem(2);
         }
     }
    /**
     * 自定义Viewpager适配器
     */
    private class MyAdapter extends FragmentStatePagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fraglist.get(position);
        }

        @Override
        public int getCount() {
            return fraglist.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
        }
    }
    /**
     * 获取当前设备的屏幕密度等基本参数
     */
    protected void getDeviceDensity() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Config.EXACT_SCREEN_HEIGHT = metrics.heightPixels;
        Config.EXACT_SCREEN_WIDTH = metrics.widthPixels;
    }
 }
