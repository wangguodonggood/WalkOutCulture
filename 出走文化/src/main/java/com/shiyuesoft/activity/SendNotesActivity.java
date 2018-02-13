package com.shiyuesoft.activity;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.shiyuesoft.R;
import com.shiyuesoft.SelectPhoto.GlideImageLoader;
import com.shiyuesoft.SelectPhoto.ImagePickerAdapter;
import com.shiyuesoft.SelectPhoto.SelectDialog;
import com.shiyuesoft.bean.TourDetails;
import com.shiyuesoft.util.CustomProgress;
import com.shiyuesoft.util.OkManager;
import com.shiyuesoft.util.URLUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*发送游记界面
* */
public class SendNotesActivity extends AppCompatActivity implements ImagePickerAdapter.OnRecyclerViewItemClickListener{
    @Override
    public void onItemClick(View view, int position) {

    }
  /*  //回退按钮 和发送按钮
    private LinearLayout back,layout_send;
    //Imagepicker 回调参数
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    //第三方ImagePicker 需要的适配器
    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 9;               //允许选择图片最大数
    //标题 和内容
    private EditText edit_title,edit_content;
    private SharedPreferences sharedpreference;
    private String token;
    private OkManager okmanager;
    //访问的URL地址
    private String URL= URLUtil.URL+URLUtil.path;
    //进度条
    private CustomProgress customProgress;
    //定位相关参数
    private Vibrator mVibrator01 =null;
    private String address="昆山浦东软件园";
    private static final int BAIDU_READ_PHONE_STATE =100;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    //处理上传图片耗时较长的操作
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //访问成功是 what=0
                case 0: {
                    if (customProgress != null) {
                        customProgress.todismiss();
                    }
                    JSONObject jsonObject= (JSONObject) msg.obj;
                    //{"publish":"yes"} {"publish":"no"}
                    if (jsonObject != null) {
                        Log.i("TAG", "onResponse: " + jsonObject.toString());
                        String result = null;
                        try {
                            result = jsonObject.getString("publish");
                            Log.i("TAG", "handleMessage: "+result);
                            if ("yes".equals(result)) {
                                finish();
                                Toast.makeText(SendNotesActivity.this, "上传成功哦,快查看你的游记吧！", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SendNotesActivity.this, "上传失败，请检查网络...！", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }break;
                //访问失败 what=1
                case 1:{
                    if (customProgress != null) {
                        customProgress.todismiss();
                    }
                    Toast.makeText(SendNotesActivity.this, "上传失败，请稍后重试！", Toast.LENGTH_SHORT).show();
                }
                break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notes);
        //最好放到 Application oncreate执行
        initImagePicker();
        //初始化组件
        initWidget();
        back= (LinearLayout) findViewById(R.id.back);
        layout_send= (LinearLayout) findViewById(R.id.layout_send);
        //发送游记
        layout_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit_title.getText().toString().equals("") && edit_content.getText().toString().equals("") && selImageList.size() == 0) {
                    Toast.makeText(SendNotesActivity.this, "请填写您的游记信息！再发送", Toast.LENGTH_SHORT).show();
                }else {
                customProgress = new CustomProgress(SendNotesActivity.this);
                customProgress.show(SendNotesActivity.this, "上传中，请稍等...", false, null);
                    new TaskThread().start();
                }
            }
        });
        //回退按钮
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    //初始化话ImagePicker  图片选择器
    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(true);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(maxImgCount);              //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(2000);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(2000);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(800);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(800);                         //保存文件的高度。单位像素
    }
    private void initWidget() {
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener( myListener );
        initLocation();
        mLocationClient.start();
        mVibrator01 =(Vibrator)this.getSystemService(Service.VIBRATOR_SERVICE);
        sharedpreference=getSharedPreferences("tokenValue",MODE_APPEND);
        token=sharedpreference.getString("token","1");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        edit_title= (EditText) findViewById(R.id.edit_title);
        edit_content= (EditText) findViewById(R.id.edit_content);
    }
   //显示大图的Dialog 是使用对话框更轻量级
    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(this, R.style
                .transparentFrameWindowStyle,
                listener, names);
        if (!this.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:
                List<String> names = new ArrayList<>();
                names.add("拍照");
                names.add("相册");
                showDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0: // 直接调起相机
                                *//**
                                 * 0.4.7 目前直接调起相机不支持裁剪，如果开启裁剪后不会返回图片，请注意，后续版本会解决
                                 *
                                 * 但是当前直接依赖的版本已经解决，考虑到版本改动很少，所以这次没有上传到远程仓库
                                 *
                                 * 如果实在有所需要，请直接下载源码引用。
                                 *//*
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent = new Intent(SendNotesActivity.this, ImageGridActivity.class);
                                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                                startActivityForResult(intent, REQUEST_CODE_SELECT);
                                break;
                            case 1:
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent1 = new Intent(SendNotesActivity.this, ImageGridActivity.class);
                                *//* 如果需要进入选择的时候显示已经选中的图片，
                                 * 详情请查看ImagePickerActivity
                                 * *//*
//                                intent1.putExtra(ImageGridActivity.EXTRAS_IMAGES,images);
                                startActivityForResult(intent1, REQUEST_CODE_SELECT);
                                break;
                            default:
                                break;
                        }

                    }
                }, names);
                break;
            default:
                //打开预览
                Intent intentPreview = new Intent(SendNotesActivity.this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }
    }
    ArrayList<ImageItem> images = null;
    //设置相关参数
    private void initLocation(){
        if(ActivityCompat
                .checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义)
            requestPermissions( new String[]{
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE

            },BAIDU_READ_PHONE_STATE);
        }
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span=1000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        mLocationClient.setLocOption(option);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
                    selImageList.clear();
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        }
    }
    *//**
     * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中 定位回调
     *//*
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            //获取定位结果
            StringBuffer sb = new StringBuffer(256);

            if (location.getLocType() == BDLocation.TypeGpsLocation){
                sb.append(location.getCity()+"-");
                sb.append(location.getDistrict()+"-");
                sb.append(location.getStreet());
               *//* sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");*//*

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
                sb.append(location.getCity()+"-");
                sb.append(location.getDistrict()+"-");
                sb.append(location.getStreet());
                // 网络定位结果
             *//*   sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息

                sb.append("\noperationers : ");
                sb.append(location.getOperators());    //获取运营商信息

                sb.append("\ndescribe : ");
                sb.append("网络定位成功");*//*

            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {

                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");

            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {

                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");

            }
              address=sb.toString();
//            handler.sendEmptyMessage(1);
        }
        @Override
        public void onConnectHotSpotMessage(String s, int i) {
        }
    }
    @Override
    public void onDestroy() {
        mLocationClient.stop();
        super.onDestroy();
    }
    //执行上传图片耗时请求s
    class TaskThread extends Thread {
        public void run() {
            System.out.println("-->耗时的任务，请求网络");
            List<String> urlList = new ArrayList<String>();
            final Map<String, String> map = new HashMap<String, String>();
            //发送游记
            for (ImageItem imageItem : selImageList) {
                urlList.add(imageItem.path);
            }
            map.put("place", address);
            map.put("token", token);
            map.put("title", edit_title.getText().toString());
            map.put("content", edit_content.getText().toString());
            map.put("action", "travel_publish");
            okmanager = new OkManager();
            okmanager.upLoadImage(urlList, new OkManager.Func4() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    Message message=new Message();
                    message.obj=jsonObject;
                    message.what=0;
                    handler.sendMessage(message);
                }
            }, URL, map, new OkManager.Fail() {
                @Override
                public void onFiled() {
                    handler.sendEmptyMessage(1);
                }
            },SendNotesActivity.this);
        }
    };
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions,grantResults);

        switch(requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case 1:
                BAIDU_READ_PHONE_STATE:

                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                    Log.i("TAG", "onRequestPermissionsResult: "+"-----------------111");
                } else{
                    Log.i("TAG", "onRequestPermissionsResult: "+"-----------------222");
                    // 没有获取到权限，做特殊处理
                }
                break;
            default:
                break;
        }
    }*/
}
