package com.shiyuesoft.util;

/**
 * Created by wangg on 2017/7/25.
 */

public class URLUtil {

    public static String URL="http://www.briup.cn:8989";

    public static String path="/travel/travel_app.php";

    public static String LOGIN_URL="http://www.briup.cn:8787/cas/getUserInfo.do";

    public static String getType(String type){
        if ("1".equals(type)){
          return "学生";
        }else if ("2".equals(type)){
            return  "老师";
        }else if ("3".equals(type)){
            return "家长";
        }
        return  "身份";
    }
}
