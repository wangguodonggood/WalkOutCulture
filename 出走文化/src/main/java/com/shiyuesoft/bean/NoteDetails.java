package com.shiyuesoft.bean;

import java.util.List;

/**
 * Created by wangg on 2017/8/2.
 * 游记详情bin类
 */

public class NoteDetails {

    /**
     * record : {"rec_id":"11","img":["http://10.0.0.137/travel/img/record/img1.jpg","http://10.0.0.137/travel/img/record/img1.jpg","http://10.0.0.137/travel/img/record/img1.jpg","http://10.0.0.137/travel/img/record/img1.jpg","http://10.0.0.137/travel/img/record/img1.jpg","http://10.0.0.137/travel/img/record/img1.jpg","http://10.0.0.137/travel/img/record/img1.jpg"],"content":"鹿儿岛是XXXXXX","title":"漫游","user_name":"屈智力","user_type":"2","publish_date":"2017-07-27 23:22:14","place":"江苏昆山","transpond":"3","collect":"5","good":"5","review_num":"4","if_good":"1","if_collect":"1"}
     * review : [{"rev_id":"17","user_name":"系统管理员","user_img":"http://10.0.0.137/basic/pic/2017/4/8a28dd5b53a2b6cb0153a2b716e30206.jpg?_=1493128873089","user_type":"2","review_date":"2017-07-28 15:10:35","review_content":"好美的地方"},{"rev_id":"18","user_name":"系统管理员","user_img":"http://10.0.0.137/basic/pic/2017/4/8a28dd5b53a2b6cb0153a2b716e30206.jpg?_=1493128873089","user_type":"2","review_date":"2017-07-28 15:10:35","review_content":"好美的地方"},{"rev_id":"19","user_name":"系统管理员","user_img":"http://10.0.0.137/basic/pic/2017/4/8a28dd5b53a2b6cb0153a2b716e30206.jpg?_=1493128873089","user_type":"2","review_date":"2017-07-28 15:10:35","review_content":"好美的地方"},{"rev_id":"22","user_name":"系统管理员","user_img":"http://10.0.0.137/basic/pic/2017/4/8a28dd5b53a2b6cb0153a2b716e30206.jpg?_=1493128873089","user_type":"2","review_date":"2017-07-28 15:10:35","review_content":"好美的地方"}]
     */

    private RecordBean record;
    private List<ReviewBean> review;

    public RecordBean getRecord() {
        return record;
    }

    public void setRecord(RecordBean record) {
        this.record = record;
    }

    public List<ReviewBean> getReview() {
        return review;
    }

    public void setReview(List<ReviewBean> review) {
        this.review = review;
    }

    public static class RecordBean {
        /**
         * rec_id : 11
         * img : ["http://10.0.0.137/travel/img/record/img1.jpg","http://10.0.0.137/travel/img/record/img1.jpg","http://10.0.0.137/travel/img/record/img1.jpg","http://10.0.0.137/travel/img/record/img1.jpg","http://10.0.0.137/travel/img/record/img1.jpg","http://10.0.0.137/travel/img/record/img1.jpg","http://10.0.0.137/travel/img/record/img1.jpg"]
         * content : 鹿儿岛是XXXXXX
         * title : 漫游
         * user_name : 屈智力
         * user_type : 2
         * publish_date : 2017-07-27 23:22:14
         * place : 江苏昆山
         * transpond : 3
         * collect : 5
         * good : 5
         * review_num : 4
         * if_good : 1
         * if_collect : 1
         */

        private String rec_id;
        private String content;
        private String title;
        private String user_name;
        private String user_type;
        private String publish_date;
        private String place;
        private String transpond;
        private String collect;
        private String good;
        private String review_num;
        private String if_good;
        private String if_collect;
        private List<String> img;
        private String user_img;
        private String elite;
        private String state;

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getElite() {
            return elite;
        }

        public void setElite(String elite) {
            this.elite = elite;
        }

        public String getUser_img() {
            return user_img;
        }

        public void setUser_img(String user_img) {
            this.user_img = user_img;
        }

        public String getRec_id() {
            return rec_id;
        }

        public void setRec_id(String rec_id) {
            this.rec_id = rec_id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getUser_type() {
            return user_type;
        }

        public void setUser_type(String user_type) {
            this.user_type = user_type;
        }

        public String getPublish_date() {
            return publish_date;
        }

        public void setPublish_date(String publish_date) {
            this.publish_date = publish_date;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public String getTranspond() {
            return transpond;
        }

        public void setTranspond(String transpond) {
            this.transpond = transpond;
        }

        public String getCollect() {
            return collect;
        }

        public void setCollect(String collect) {
            this.collect = collect;
        }

        public String getGood() {
            return good;
        }

        public void setGood(String good) {
            this.good = good;
        }

        public String getReview_num() {
            return review_num;
        }

        public void setReview_num(String review_num) {
            this.review_num = review_num;
        }

        public String getIf_good() {
            return if_good;
        }

        public void setIf_good(String if_good) {
            this.if_good = if_good;
        }

        public String getIf_collect() {
            return if_collect;
        }

        public void setIf_collect(String if_collect) {
            this.if_collect = if_collect;
        }

        public List<String> getImg() {
            return img;
        }

        public void setImg(List<String> img) {
            this.img = img;
        }
    }
    public static class ReviewBean {
        /**
         * rev_id : 17
         * user_name : 系统管理员
         * user_img : http://10.0.0.137/basic/pic/2017/4/8a28dd5b53a2b6cb0153a2b716e30206.jpg?_=1493128873089
         * user_type : 2
         * review_date : 2017-07-28 15:10:35
         * review_content : 好美的地方
         */
        private String rev_id;
        private String user_name;
        private String user_img;
        private String user_type;
        private String review_date;
        private String review_content;

        public String getRev_id() {
            return rev_id;
        }

        public void setRev_id(String rev_id) {
            this.rev_id = rev_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getUser_img() {
            return user_img;
        }

        public void setUser_img(String user_img) {
            this.user_img = user_img;
        }
        public String getUser_type() {
            return user_type;
        }
        public void setUser_type(String user_type) {
            this.user_type = user_type;
        }
        public String getReview_date() {
            return review_date;
        }
        public void setReview_date(String review_date) {
            this.review_date = review_date;
        }
        public String getReview_content() {
            return review_content;
        }
        public void setReview_content(String review_content) {
            this.review_content = review_content;
        }
    }
}
