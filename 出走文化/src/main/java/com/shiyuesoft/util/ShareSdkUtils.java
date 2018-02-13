package com.shiyuesoft.util;

import android.content.Context;
import android.util.Log;

import com.shiyuesoft.R;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
/**
 * Created by wangg on 2017/8/8.
 *
 * sharedSDK 分享工具类
 */

public class ShareSdkUtils {

    public static void showShare(String id, Context context, final WeixinCallBack listener,String title,String content) {
        OnekeyShare oks = new OnekeyShare();
//关闭sso授权
        oks.disableSSOWhenAuthorize();
// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle(title);
// titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("http://blog.csdn.net/lmj623565791/article/details/47911083");
// text是分享文本，所有平台都需要这个字段
        Log.i("TAG", "showShare: "+content+"-------");
        oks.setText(content);
// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
// url仅在微信（包括好友和朋友圈）中使用
        String ImageUrlOrPath="http://t2.qlogo.cn/mbloghead/88fca5afabf9ec19a8aa/180";
        if(ImageUrlOrPath != null &&ImageUrlOrPath.contains("/sdcard/")){
            //imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
            oks.setImagePath(ImageUrlOrPath);
        }else if(ImageUrlOrPath != null){
            oks.setImageUrl(ImageUrlOrPath); //网络地址
        }
        oks.setUrl("http://www.briup.cn:8989/travel/transpond.php?action=see&rec_id="+id);
//       oks.setUrl("http://blog.csdn.net/maojunjunjun/article/details/51113660");
        //    oks.setImageUrl("https://www.baidu.com/baidu.php?url=fW3K00Kga1xr6lpppg3-xJ_aaQ0oLrOzZqaK3edlyZTmTptZg2iCh2zDEdPjDQL_Rl7PSwztOM-cV5z4LnO25DYD6lPS_-KZjhCtxqt6cS7Q1R8_aa5fp0YRAjbAyladA7yLXox5qhbaEtJzcE9a40Ui7a3HlJqI-ur5HRvycJ2ItlxHis.7b_NR2Ar5Od66u8kYTDx-6YG1c6vSOX7WUdumx7fhIdsRP5QAeKPa-BSlPrMo6CpXgih4SjikA-hpAoo6CpXy7MHWuxJBmuk3lD1n5pGJIGHz3qis1f_UVd-hwf.U1Yk0ZDqYqZjzsKY5IQAVJOPkoMWzEvSLfKGUHYznWR0u1dBugK1nfKdpHdBmy-bIfKspyfqnHb0mv-b5HR40AdY5HnznW9xnH0kPdtknjD4g1nvnjD0pvbqn0KzIjYzrHR0uy-b5HDYPHIxnWDsrHuxnHTsnj7xnWckPW00mhbqnW0Yg1DdPfKVm1YknjFxPWfdPjDkPWTsg1mYPj63rj04nNts0Z7spyfqn0Kkmv-b5H00ThIYmyTqn0KEIhsqnH0zrH0VuZGxnH01PWnVuZGxnH0Yn10Vn-tknjbznBdbX-tknjbLnzdbX-tknH0YnBYkg1DknjTYQHwxnHDkPW0VuZGxnHDYnjfVuZGxnHDdn1TVndtknHRYPzdbX-tknHmLriY3g1DkP1R4Qywlg1Dznj04QHFxnHckQHwxnHczQywlg1DznWn4Qywlg1DznWTkQywlg1DznW6vQywlg1Dzn1fLQHwxnHcLrHfVPNtknWbYPidbX-tkn1DzridbX-tkn1D3ridbX-tkn1ckridbX-tkn1nLradbX-tkn1n3radbX-tkn1f1PidbX-tkn1mzPzYzg1D1PWRsQywlg1D1PWmzQywlg1D1PW6kQywlg1D1PWbzQywlg1D1P16sQH7xnHn3njbVuZGxnHn3nH0VuZGxnHn4n1RVuZGxnHn4rHmVnNtkPj01PzdbX-tkPj0YPBYkg1DYnj6kQywlg1DYnjbLQywlg1DYnHDkQywlg1DYnHc4Qywlg1DYnHmvQywlg1csPYsVnH0Lg1cvPWbVuZGxnWmvrN6VuZGxPHnYQHm0mycqn7ts0ANzu1Ys0ZKs5HD4rjD1nW0Yn100UMus5H08nj0snj0snj00Ugws5H00uAwETjYs0ZFJ5H00uANv5gKW0AuY5H00TA6qn0KET1Ys0AFL5HDs0A4Y5H00TLCq0ZwdT1YknWcsPjcdnH6sP1mkPjfkPj0YrfKzug7Y5HDdnjD4rHmLnWn3PWT0Tv-b5yDYmyw9rHP9nj0sn10vnvD0mLPV5H01PWKArDFKnH9DwRmdrHf0mynqnfKsUWYs0Z7VIjYs0Z7VT1Ys0ZGY5HD0UyPxuMFEUHYsg1Kxn7ts0Aw9UMNBuNqsUA78pyw15HD3nWc4nWNxn7tsg100TA7Ygvu_myTqn0KbIA-b5H00ugwGujYVnfK9TLKWm1Ys0ZNspy4Wm1Ys0Z7VuWYkP6KhmLNY5H00uMGC5H00uh7Y5H00XMK_Ignqn0K9uAu_myTqnfK_uhnqn0KWThnqnH0vP10&ck=1350.2.43.176.212.344.226.617&shh=www.baidu.com&sht=baiduhome_pg&us=1.0.1.0.0.0.0&ie=utf-8&f=8&tn=baiduhome_pg&wd=%E5%8F%8B%E7%9B%9F&oq=%E5%8F%8B%E7%9B%9F&rqlang=cn&euri=cfb0b77956054555893097f289b18358");
// comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("我是测试评论文本");
// site是分享此内容的网站名称，仅在QQ空间使用s
        oks.setSite(context.getString(R.string.app_name));
// siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://www.briup.cn:8989/travel/transpond.php?action=see&rec_id="+id);
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                   if (listener!=null){
                       listener.OnComplete();
                   }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                   if (listener!=null){
                       listener.onError();
                   }
            }

            @Override
            public void onCancel(Platform platform, int i) {
                   if (listener!=null){
                       listener.onCancel();
                   }
            }
        });
// 启动分享GUI
        oks.show(context);
    }
     public interface  WeixinCallBack{
         void OnComplete();
          void  onError();
           void  onCancel();
       }
}
