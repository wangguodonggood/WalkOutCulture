package com.shiyuesoft.activity;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.shiyuesoft.R;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        TextView textView = (TextView) findViewById(R.id.text);
       /* String html="<font>"+"草莓草莓草莓草莓草莓草莓草莓草莓草莓草莓草莓草莓草莓草莓草莓草莓草莓草莓草莓草莓草莓"+"</font>&nbsp;&nbsp;&nbsp;&nbsp;<img src=‘strawberry’>";*/

       String html="<font>"+"草莓草莓草莓草莓草莓草莓草莓草莓草莓草莓草莓草莓草莓草莓草莓草莓草莓草莓草莓草莓草莓"+"</font>&nbsp;&nbsp;&nbsp;&nbsp;"+"<font color='#C9132C'>(审核中...)</font>";
       CharSequence text= Html.fromHtml(html, new Html.ImageGetter() {

            public Drawable getDrawable(String source) {
                //根据图片资源ID获取图片
                Log.d("source", source);
                if(source.equals("‘strawberry’")){
                    Drawable draw=getResources().getDrawable(R.drawable.jing);
                    draw.setBounds(0, 0, draw.getIntrinsicWidth(), draw.getIntrinsicHeight());
                    return draw;
                }
                return null;
            }
        }, null);
        textView.setText(text);
    }
}
