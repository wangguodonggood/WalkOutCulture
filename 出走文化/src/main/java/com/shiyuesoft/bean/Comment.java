package com.shiyuesoft.bean;

/**
 * Created by wangg on 2017/7/19.
 * 不要删
 */

public class Comment {
    private int comment_image;
    private String comment_name;
    //身份
    private String comment_identity;
    private String comment_time;
    private String comment_content;

    public int getComment_image() {
        return comment_image;
    }

    public void setComment_image(int comment_image) {
        this.comment_image = comment_image;
    }

    public String getComment_name() {
        return comment_name;
    }

    public void setComment_name(String comment_name) {
        this.comment_name = comment_name;
    }

    public String getComment_identity() {
        return comment_identity;
    }

    public void setComment_identity(String comment_identity) {
        this.comment_identity = comment_identity;
    }

    public String getComment_time() {
        return comment_time;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "comment_image=" + comment_image +
                ", comment_name='" + comment_name + '\'' +
                ", comment_identity='" + comment_identity + '\'' +
                ", comment_time='" + comment_time + '\'' +
                ", comment_content='" + comment_content + '\'' +
                '}';
    }
}
