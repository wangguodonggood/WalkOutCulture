package com.shiyuesoft.bean;

import java.util.List;
/**
 * Created by wangg on 2017/7/31.
 * 游记详情
 */
public class TourDetails {
    /**
     * strong : {"rec_id":"2","cover_img":"http://10.0.0.137/travel/img/strong/img1.jpg","content":"鹿儿岛是XXXXXXX","title":"漫游2"}
     * review : [{"rev_id":"8","user_name":"系统管理员","user_img":"http://10.0.0.137/basic/pic/2017/4/8a28dd5b53a2b6cb0153a2b716e30206.jpg?_=1493128873089","user_type":"2","review_date":"2017-07-28 15:10:35","review_content":"好美的地方"},{"rev_id":"9","user_name":"系统管理员","user_img":"http://10.0.0.137/basic/pic/2017/4/8a28dd5b53a2b6cb0153a2b716e30206.jpg?_=1493128873089","user_type":"2","review_date":"2017-07-28 15:10:35","review_content":"好美的地方"},{"rev_id":"10","user_name":"系统管理员","user_img":"http://10.0.0.137/basic/pic/2017/4/8a28dd5b53a2b6cb0153a2b716e30206.jpg?_=1493128873089","user_type":"2","review_date":"2017-07-28 15:10:35","review_content":"好美的地方"},{"rev_id":"11","user_name":"系统管理员","user_img":"http://10.0.0.137/basic/pic/2017/4/8a28dd5b53a2b6cb0153a2b716e30206.jpg?_=1493128873089","user_type":"2","review_date":"2017-07-28 15:10:35","review_content":"好美的地方"}]
     */

    private StrongBean strong;
    private List<ReviewBean> review;

    public StrongBean getStrong() {
        return strong;
    }

    public void setStrong(StrongBean strong) {
        this.strong = strong;
    }

    public List<ReviewBean> getReview() {
        return review;
    }

    public void setReview(List<ReviewBean> review) {
        this.review = review;
    }

    public static class StrongBean {
        /**
         * rec_id : 2
         * cover_img : http://10.0.0.137/travel/img/strong/img1.jpg
         * content : 鹿儿岛是XXXXXXX
         * title : 漫游2
         */

        private String rec_id;
        private String cover_img;
        private String strong_content;
        private String title;

        public String getRec_id() {
            return rec_id;
        }

        public void setRec_id(String rec_id) {
            this.rec_id = rec_id;
        }

        public String getCover_img() {
            return cover_img;
        }

        public void setCover_img(String cover_img) {
            this.cover_img = cover_img;
        }

        public String getContent() {
            return strong_content;
        }

        public void setContent(String content) {
            this.strong_content = content;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public static class ReviewBean {
        /**
         * rev_id : 8
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
