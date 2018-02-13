package com.shiyuesoft.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by wangg on 2017/7/20.
 * FragmentPagerAdapter 卡片布局，实现在内部嵌套两个子片段
 */

public class CusTabFragmentPagerAdpter extends FragmentPagerAdapter{
    private List<Fragment> fragmentList;
    private List<String> titleList;

    public CusTabFragmentPagerAdpter(FragmentManager fm, List<Fragment> fragmentList, List<String> titleList) {
        super(fm);
        this.fragmentList=fragmentList;
        this.titleList=titleList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
    }
    @Override
    public CharSequence getPageTitle(int position) {

        return titleList.get(position);//页卡标题
    }
}
