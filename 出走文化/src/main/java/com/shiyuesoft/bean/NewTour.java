package com.shiyuesoft.bean;

import java.util.List;

/**
 * Created by wangg on 2017/7/27.
 * 壮游bean类
 */

public class NewTour {

    /**
     * strong : [{"id":"1","cover_img":"http://10.0.0.137/travel/img/strong/img1.jpg"},{"id":"2","cover_img":"http://10.0.0.137/travel/img/strong/img1.jpg"},{"id":"4","cover_img":"http://10.0.0.137/travel/img/strong/img1.jpg"},{"id":"5","cover_img":"http://10.0.0.137/travel/img/strong/img1.jpg"},{"id":"6","cover_img":"http://10.0.0.137/travel/img/strong/img1.jpg"}]
     * page : 1
     * total_page : 2
     * next_page : 2
     */

    private int page;
    private int total_page;
    private int next_page;
    private List<StrongBean> strong;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_page() {
        return total_page;
    }

    public void setTotal_page(int total_page) {
        this.total_page = total_page;
    }

    public int getNext_page() {
        return next_page;
    }

    public void setNext_page(int next_page) {
        this.next_page = next_page;
    }

    public List<StrongBean> getStrong() {
        return strong;
    }

    public void setStrong(List<StrongBean> strong) {
        this.strong = strong;
    }

    public static class StrongBean {
        /**
         * rec_id : 1
         * cover_img : http://10.0.0.137/travel/img/strong/img1.jpg
         */
        private String rec_id;
        private String cover_img;

        public String getId() {
            return rec_id;
        }

        public void setId(String id) {
            this.rec_id = id;
        }

        public String getCover_img() {
            return cover_img;
        }

        public void setCover_img(String cover_img) {
            this.cover_img = cover_img;
        }

        @Override
        public String toString() {
            return "StrongBean{" +
                    "id='" + rec_id + '\'' +
                    ", cover_img='" + cover_img + '\'' +
                    '}';
        }
    }
}
