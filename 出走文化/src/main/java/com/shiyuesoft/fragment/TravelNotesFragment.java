package com.shiyuesoft.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shiyuesoft.R;
import com.shiyuesoft.adapter.CusTabFragmentPagerAdpter;
import com.shiyuesoft.util.TabStripRunable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangg on 2017/7/19.
 * 游记片段 里面包含两个子片段
 */

public class TravelNotesFragment extends Fragment{
    private ViewPager pager;
    private TabLayout tab;
    private List<Fragment> fragmentList;
    private List<String> titleList;
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragmenrt_travelnotes,container,false);
        initView(view);
        return view;
    }
    private void initView(View view) {
        tab= (TabLayout) view.findViewById(R.id.main_tab);
        pager= (ViewPager) view.findViewById(R.id.main_viewpager);
        fragmentList=new ArrayList<>();
        titleList=new ArrayList<>();
        initDatas();
    }
    private void initDatas() {
        titleList.add("全部");
        titleList.add("精华");
        fragmentList.add(new NotesChildFragment1());
        fragmentList.add(new NotesChildFragment2());
        pager.setAdapter(new CusTabFragmentPagerAdpter(getActivity().getSupportFragmentManager(),fragmentList,titleList));
        //设置tab的模式
       tab.setTabMode(TabLayout.MODE_FIXED);
        //添加tab选项卡
        for(String s:titleList){
            tab.addTab(tab.newTab().setText(s));
        }
        tab.post(new TabStripRunable(tab));
        //把tab和viewpager联系起来
        tab.setupWithViewPager(pager);
    }

}
