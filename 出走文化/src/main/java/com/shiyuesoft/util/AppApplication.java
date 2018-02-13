package com.shiyuesoft.util;

import android.app.Application;

import com.mob.MobApplication;
import com.mob.MobSDK;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * 描述：配置ImageLoader相应文件
 */
public class AppApplication extends MobApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader();
        MobSDK.init(this, this.a(), this.b());
    }
    protected String a() {
        return null;
    }

    protected String b() {
        return null;
    }

    private void initImageLoader() {
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);
    }

}
