package com.shiyuesoft.bean;

import java.util.List;

/**
 * Created by wangg on 2017/7/31.
 * 游记详情页面
 */

public class TravelNotes {


    /**
     * record : [{"if_collect":"0","if_good":"0","rec_id":"42","user_id":"8a28dd5b53a2b6cb0153a2b716e30206","title":"测试动态","content":"哈哈哈哈哈","img":["http://10.0.0.137/travel/img/record/Screenshot_2017-05-22-09-47-20.png","http://10.0.0.137/travel/img/record/Screenshot_2017-05-22-09-47-21.png","http://10.0.0.137/travel/img/record/Screenshot_2017-05-22-09-47-27.png"],"transpond":"0","good":"0","elite":"0","place":"昆山","collect":"0","publish_date":"2017-08-03 10:31:56","name":"系统管理员","user_type":"2","user_img":"http://10.0.0.137/basic/pic/2017/4/8a28dd5b53a2b6cb0153a2b716e30206.jpg?_=1493128873089","review_num":"0"},{"if_collect":"0","if_good":"0","rec_id":"41","user_id":"8a28dd5b53a2b6cb0153a2b716e30206","title":"测试标题","content":"测试正文测试正文测试正文测试正文测试正文测试正文测试正文测试正","img":"","transpond":"0","good":"0","elite":"0","place":"测试假装获取了位置- -","collect":"0","publish_date":"2017-08-03 10:27:48","name":"系统管理员","user_type":"2","user_img":"http://10.0.0.137/basic/pic/2017/4/8a28dd5b53a2b6cb0153a2b716e30206.jpg?_=1493128873089","review_num":"0"},{"if_collect":"0","if_good":"0","rec_id":"40","user_id":"8a28dd5b53a2b6cb0153a2b716e30206","title":"测试标题","content":"测试正文测试正文测试正文测试正文测试正文测试正文测试正文测试正","img":"","transpond":"0","good":"0","elite":"0","place":"测试假装获取了位置- -","collect":"0","publish_date":"2017-08-03 10:25:59","name":"系统管理员","user_type":"2","user_img":"http://10.0.0.137/basic/pic/2017/4/8a28dd5b53a2b6cb0153a2b716e30206.jpg?_=1493128873089","review_num":"0"},{"if_collect":"0","if_good":"0","rec_id":"39","user_id":"8a28dd5b53a2b6cb0153a2b716e30206","title":"测试标题","content":"测试正文测试正文测试正文测试正文测试正文测试正文测试正文测试正","img":"","transpond":"0","good":"0","elite":"0","place":"测试假装获取了位置- -","collect":"0","publish_date":"2017-08-03 10:24:40","name":"系统管理员","user_type":"2","user_img":"http://10.0.0.137/basic/pic/2017/4/8a28dd5b53a2b6cb0153a2b716e30206.jpg?_=1493128873089","review_num":"0"},{"if_collect":"0","if_good":"0","rec_id":"38","user_id":"8a28dd5b53a2b6cb0153a2b716e30206","title":"测试标题","content":"测试正文测试正文测试正文测试正文测试正文测试正文测试正文测试正","img":"","transpond":"0","good":"0","elite":"0","place":"测试假装获取了位置- -","collect":"0","publish_date":"2017-08-03 10:22:15","name":"系统管理员","user_type":"2","user_img":"http://10.0.0.137/basic/pic/2017/4/8a28dd5b53a2b6cb0153a2b716e30206.jpg?_=1493128873089","review_num":"0"}]
     * page : 1
     * total_page : 2
     * next_page : 2
     */

    private String page;
    private String total_page;
    private String next_page;
    private List<RecordBean> record;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getTotal_page() {
        return total_page;
    }

    public void setTotal_page(String total_page) {
        this.total_page = total_page;
    }

    public String getNext_page() {
        return next_page;
    }

    public void setNext_page(String next_page) {
        this.next_page = next_page;
    }

    public List<RecordBean> getRecord() {
        return record;
    }

    public void setRecord(List<RecordBean> record) {
        this.record = record;
    }

    public static class RecordBean {
        /**
         * if_collect : 0
         * if_good : 0
         * rec_id : 42
         * user_id : 8a28dd5b53a2b6cb0153a2b716e30206
         * title : 测试动态
         * content : 哈哈哈哈哈
         * img : ["http://10.0.0.137/travel/img/record/Screenshot_2017-05-22-09-47-20.png","http://10.0.0.137/travel/img/record/Screenshot_2017-05-22-09-47-21.png","http://10.0.0.137/travel/img/record/Screenshot_2017-05-22-09-47-27.png"]
         * transpond : 0
         * good : 0
         * elite : 0
         * place : 昆山
         * collect : 0
         * publish_date : 2017-08-03 10:31:56
         * name : 系统管理员
         * user_type : 2
         * user_img : http://10.0.0.137/basic/pic/2017/4/8a28dd5b53a2b6cb0153a2b716e30206.jpg?_=1493128873089
         * review_num : 0
         */

        private String if_collect;
        private String if_good;
        private String state;

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        private String rec_id;
        private String user_id;
        private String title;
        private String content;
        private String transpond;
        private String good;
        private String elite;
        private String place;
        private String collect;
        private String publish_date;
        private String name;
        private String user_type;
        private String user_img;
        private String review_num;
        private List<String> img;

        public String getIf_collect() {
            return if_collect;
        }

        public void setIf_collect(String if_collect) {
            this.if_collect = if_collect;
        }

        public String getIf_good() {
            return if_good;
        }

        public void setIf_good(String if_good) {
            this.if_good = if_good;
        }

        public String getRec_id() {
            return rec_id;
        }

        public void setRec_id(String rec_id) {
            this.rec_id = rec_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTranspond() {
            return transpond;
        }

        public void setTranspond(String transpond) {
            this.transpond = transpond;
        }

        public String getGood() {
            return good;
        }

        public void setGood(String good) {
            this.good = good;
        }

        public String getElite() {
            return elite;
        }

        public void setElite(String elite) {
            this.elite = elite;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public String getCollect() {
            return collect;
        }

        public void setCollect(String collect) {
            this.collect = collect;
        }

        public String getPublish_date() {
            return publish_date;
        }

        public void setPublish_date(String publish_date) {
            this.publish_date = publish_date;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUser_type() {
            return user_type;
        }

        public void setUser_type(String user_type) {
            this.user_type = user_type;
        }

        public String getUser_img() {
            return user_img;
        }

        public void setUser_img(String user_img) {
            this.user_img = user_img;
        }

        public String getReview_num() {
            return review_num;
        }

        public void setReview_num(String review_num) {
            this.review_num = review_num;
        }

        public List<String> getImg() {
            return img;
        }

        public void setImg(List<String> img) {
            this.img = img;
        }
    }
}
