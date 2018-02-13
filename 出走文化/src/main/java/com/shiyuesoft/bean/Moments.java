package com.shiyuesoft.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangg on 2017/7/20.
 * 发表的游记动态bean对象
 * 不要删
 */

public class Moments implements Serializable{
      private static final long serialVersionUID = 2189052605715370758L;
      private int titleimageurl;
      private String moments_name;
      private String moments_identity;
      private String moments_time;
      private String moments_title;
      private String moments_content;
      private String moments_address;
      public List<String> urlList=new ArrayList<>();
      public boolean isShowAll = false;
      private int zans;
      private int shards;

    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }

    public boolean isShowAll() {
        return isShowAll;
    }

    public void setShowAll(boolean showAll) {
        isShowAll = showAll;
    }

    private int comments;
      private boolean flag;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getTitleimageurl() {
        return titleimageurl;
    }

    public void setTitleimageurl(int titleimageurl) {
        this.titleimageurl = titleimageurl;
    }

    public String getMoments_name() {
        return moments_name;
    }

    public void setMoments_name(String moments_name) {
        this.moments_name = moments_name;
    }

    public String getMoments_identity() {
        return moments_identity;
    }

    public void setMoments_identity(String moments_identity) {
        this.moments_identity = moments_identity;
    }

    public String getMoments_time() {
        return moments_time;
    }

    public void setMoments_time(String moments_time) {
        this.moments_time = moments_time;
    }

    public String getMoments_title() {
        return moments_title;
    }

    public void setMoments_title(String moments_title) {
        this.moments_title = moments_title;
    }

    public String getMoments_content() {
        return moments_content;
    }

    public void setMoments_content(String moments_content) {
        this.moments_content = moments_content;
    }

    public String getMoments_address() {
        return moments_address;
    }

    public void setMoments_address(String moments_address) {
        this.moments_address = moments_address;
    }

    public int getZans() {
        return zans;
    }

    public void setZans(int zans) {
        this.zans = zans;
    }

    public int getShards() {
        return shards;
    }

    public void setShards(int shards) {
        this.shards = shards;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Moments{" +
                "titleimageurl=" + titleimageurl +
                ", moments_name='" + moments_name + '\'' +
                ", moments_identity='" + moments_identity + '\'' +
                ", moments_time='" + moments_time + '\'' +
                ", moments_title='" + moments_title + '\'' +
                ", moments_content='" + moments_content + '\'' +
                ", moments_address='" + moments_address + '\'' +
                ", zans=" + zans +
                ", shards=" + shards +
                ", comments=" + comments +
                ", flag=" + flag +
                '}';
    }
}
