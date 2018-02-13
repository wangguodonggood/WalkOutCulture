package com.shiyuesoft.util;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
/**
 * Created by wangg on 2017/4/12.
 */
public class OkManager {
    private OkHttpClient client;
    private volatile static  OkManager manager;
    private final String TAG = OkManager.class.getSimpleName();//获得类名
    private Handler handler;
    //提交json数据
    private static  final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    //提交字符串
    private static  final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown;charset=utf-8");

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    public OkManager(Context context){
        X509TrustManager xtm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }
            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                X509Certificate[] x509Certificates = new X509Certificate[0];
                return x509Certificates;
            }
        };
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");

            sslContext.init(null, new TrustManager[]{xtm}, new SecureRandom());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        Cache cache  = new Cache(context.getCacheDir(), 10240*1024);
//      client = new OkHttpClient();
        client = new OkHttpClient.Builder()
                 .sslSocketFactory(sslContext.getSocketFactory())
                 .hostnameVerifier(DO_NOT_VERIFY)
                .addNetworkInterceptor(new MyInterceptor(context))
                .cache(cache)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        handler = new Handler(Looper.getMainLooper());

    }
    public OkManager(){
        X509TrustManager xtm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }
            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                X509Certificate[] x509Certificates = new X509Certificate[0];
                return x509Certificates;
            }
        };
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");

            sslContext.init(null, new TrustManager[]{xtm}, new SecureRandom());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        client = new OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory())
                .hostnameVerifier(DO_NOT_VERIFY)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        handler = new Handler(Looper.getMainLooper());
    }
    /**
     *  请求指定的url返回的结果是json字符串
     * @param url
     * @param callBack
     */
    public  void  asyncJsonStringByURL(String url, final Func1 callBack,final Fail onfail){
        final Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call arg0, IOException e) {
                // TODO Auto-generated method stub
//                e.printStackTrace();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onfail.onFiled();
                    }
                });
            }
            @Override
            public void onResponse(Call arg0, Response response) throws IOException {
                // TODO Auto-generated method stub
                if (response!=null&&response.isSuccessful()){
                    onSuccessJsonStringMethod(response.body().string(),callBack);
                }
            }
        });
    }
    /**
     * get 请求
     * 请求返回的是一个Json对象
     * @param url
     * @param callBack
     */
    public void asyncJsonObjectByURL(String url, final Func4 callBack){
        final Request request=new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call arg0, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call arg0, Response response) throws IOException {
                if (response!=null&&response.isSuccessful()) {
                    onSuccessJsonObjectMethod(response.body().string(),callBack);
                }
            }
        });
    }
    /**
     * okHttp上传单张或多张照片
     * List<String> mImageUrls 图片的URL
     *
     */
    public void upLoadImage(List<String> mImgUrls, final Func4 callBack, String BASE_URL,
                            Map<String,String> map, final Fail onfail, final Context context){
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        List<String> list=new ArrayList<>();
        for (int i = 0; i <mImgUrls.size() ; i++) {
            String name = mImgUrls.get(i).substring(mImgUrls.get(i).lastIndexOf("/")).substring(1);
            // 压缩成800*480
            Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromFd(mImgUrls.get(i), 480, 800);
            byte[] bs = Utils.BitmapToString(bitmap);
            File file=new File(context.getCacheDir(),name);
            list.add(file.getAbsolutePath());
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            OutputStream out = null;
            try {
                out = new FileOutputStream(file);
                InputStream is = new ByteArrayInputStream(bs);
                byte[] buff = new byte[1024];
                int len = 0;
                while((len=is.read(buff))!=-1){
                    out.write(buff, 0, len);
                    out.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
           /* if (f!=null) {
                builder.addFormDataPart("img"+i, f.getName(), RequestBody.create(MEDIA_TYPE_PNG,f));
            }*/
        }
        for (int j=0;j<list.size();j++){
            String name = list.get(j).substring(list.get(j).lastIndexOf("/")).substring(1);
            Log.i("TAG", "test: "+name+"---------"+list.get(j));
            File f=new File(list.get(j));
              if (f!=null) {
                builder.addFormDataPart("img"+j, name, RequestBody.create(MEDIA_TYPE_PNG,f));
               }
        }
        for (String key:map.keySet()){
          builder.addFormDataPart(key,map.get(key));
        }
        MultipartBody requestBody = builder.build();
        Log.i("TAG", "onTest: "+requestBody.toString());
        //构建请求
        Request request = new Request.Builder()
                .url(BASE_URL)//地址
                .post(requestBody)//添加请求体
                .build();
        Log.i("TAG", "onTest: "+request.toString());
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("上传失败:e.getLocalizedMessage() = " + e.getLocalizedMessage());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onfail.onFiled();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response!=null&&response.isSuccessful()){
                    deleteFilesByDirectory(context.getCacheDir());
                    onSuccessJsonObjectMethod(response.body().string(), callBack);
                }
            }
        });
    }
    /**
     * 请求返回的是一个字节数组
     * @param url
     * @param callBack
     */
    public void asyncGetByteURL(String url, final Func2 callBack){
        final Request request=new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call arg0, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call arg0, Response response) throws IOException {
                if (response!=null&&response.isSuccessful()) {
                    onSuccessByteMethod(response.body().bytes(), callBack);
                }
            }

        });
    }
    public void asyncDownLoadImageByURL(String url, final Func3 callback){
        final Request request=new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call arg0, IOException e) {
                // TODO Auto-generated method stub
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call arg0, Response response) throws IOException {
                if (response!=null&&response.isSuccessful()) {
                    byte[] data=response.body().bytes();
                Bitmap bitmap= BitmapFactory.decodeByteArray(data, 0, data.length);
                callback.onResponse(bitmap);
            }
            }
        });
    }
    //////上传数据的封装
    /**
     *   模拟表单提交
     * @param url
     * @param params
     * @param callBack
     */
    public void sendComplexForm(String url, Map<String, String> params, final Func4 callBack,final Fail onfail){
        //表单对象，包含以input开始的对象，以html表单为主
        FormBody.Builder form_builder =new FormBody.Builder();
        if (params!=null&&!params.isEmpty()) {
            for(Map.Entry<String, String> entry :params.entrySet()){
                form_builder.add(entry.getKey(), entry.getValue());
            }
        }
        RequestBody request_body =form_builder.build();
        Request request = new Request.Builder().url(url).post(request_body).build();//采用post方式提交
        Log.i("TAG", "sendComplexForm: "+request);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call arg0, Response response) throws IOException {
                if (response!=null&&response.isSuccessful()){
                    onSuccessJsonObjectMethod(response.body().string(), callBack);
                }
            }
            @Override
            public void onFailure(Call arg0, IOException e) {
                e.printStackTrace();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onfail.onFiled();
                    }
                });
            }
        });
    }
    /**
     * 向服务器提交String请求
     * @param url
     * @param content
     * @param callBack
     */
    public void sendStringByPostMethod(String url, String content, final Func4 callBack){
        Request request = new Request.Builder()
                .url(url).post(RequestBody.create(MEDIA_TYPE_MARKDOWN,content)).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response!=null&&response.isSuccessful()){
                    onSuccessJsonObjectMethod(response.body().string(),callBack);
                }
            }
        });
    }
    /**
     * 向服务器提交Json请求
     * @param url
     * @param callBack
     */
    public void sendJsonByPostMethod(String url, JSONObject json, final Func4 callBack){
          Request request=new Request.Builder().url(url).post(RequestBody.create(JSON,json.toString())).build();

          client.newCall(request).equals(new Callback(){
              @Override
              public void onFailure(Call call, IOException e) {
              }
              @Override
              public void onResponse(Call call, Response response) throws IOException {
                  if (response!=null&&response.isSuccessful()){
                      onSuccessJsonObjectMethod(response.body().string(),callBack);
                  }
              }
          });
    }
    /**
     * 请求返回的结果是json字符串
     * @param jsonValue
     * @param callBack
     */
    private void onSuccessJsonStringMethod(final String jsonValue, final Func1 callBack){
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack!=null){
                    try{
                        callBack.onResponse(jsonValue);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    /**请求返回的是byte[] 数组
     * @param data
     * @param callBack
     */
    private void onSuccessByteMethod(final byte[] data,final Func2 callBack){
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack!=null){
                    callBack.onResponse(data);
                }
            }
        });
    }
    /** 返回响应的结果是json对象
     * @param jsonValue
     * @param callBack
     */
    private void onSuccessJsonObjectMethod(final String jsonValue, final Func4 callBack){
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack!=null){
                    try{
                        callBack.onResponse(new JSONObject(jsonValue));
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 当验证登陆和注销状态成功返回json时
     * @param result
     * @param callBack
     */
    private void onSuccessStatusMethod(final String result, final Status callBack){
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack!=null){
                        callBack.getStatus(result);
                }
            }
        });
    }

    //定义接口的意义：拿到不同的返回值
    public  interface Func1{
        void  onResponse(String result) throws JSONException;
    }
    public interface Func2{
        void  onResponse(byte[] result);
    }
    public interface Func3{
        void  onResponse(Bitmap bitmap);
    }
    public interface Func4{
        void  onResponse(JSONObject jsonObject) throws JSONException;
    }
    public interface Status{
        void getStatus(String result);
    }
    public interface Fail{
        void onFiled();
    }

    public class MyInterceptor implements Interceptor {
        private Context context;

        public  MyInterceptor(Context context){
            this.context=context;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Log.d("Test", "================进入拦截器");
            Request request = chain.request();
            Log.e("MainActivity", "新请求 =request==" + request.toString());
            Response response = chain.proceed(request); //===========
            Response response_build ;
            if (NetUtil.checkNet(context)) {
                int maxAge = 120; // 有网络的时候从缓存保存的时间
                response_build = response.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                int maxStale =60 * 60 * 24 ; // 无网络缓存保存一天
                response_build = response.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
            return response_build;
        }
    }
    /**
     * * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * *
     *
     * @param directory
     */
    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }
}
