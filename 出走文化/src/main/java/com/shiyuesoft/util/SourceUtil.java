package com.shiyuesoft.util;

import com.shiyuesoft.R;
import com.shiyuesoft.bean.Comment;
import com.shiyuesoft.bean.Moments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wangg on 2017/7/19.
 */

public class SourceUtil {
    private static int[] images = {R.drawable.img1, R.drawable.img2, R.drawable.img1, R.drawable.img2, R.drawable.img1, R.drawable.img2};
    private static int[] image = {R.drawable.hugh, R.drawable.hugh, R.drawable.hugh,R.drawable.hugh, R.drawable.hugh, R.drawable.hugh};
    private static String[] names = {"张三", "李四", "王五", "赵六", "王五", "赵六"};
    private static String[] titles = {"鹿儿岛", "张家界", "走进东西区", "走进陆家嘴", "走进那那那", "走进这这这"};
    private static String[] indentiys = {"学生", "老师", "学生", "学生", "老师", "学生"};
    private static String[] content = {"鹿儿岛县，日本九州最南端的县，西南以奄美群岛与冲绳县相对，拥有以世界自然遗产屋久岛为首的各种" +
            "特色岛屿和樱岛等火山，茂密的森林。", "鹿儿岛县，日本九州最南端的县，西南以奄美群岛与冲绳县相对，拥有以世界自然遗产屋久岛为首的各种" +
            "特色岛屿和樱岛等火山，茂密的森林。", "鹿儿岛县，日本九州最南端的县，西南以奄美群岛与冲绳县相对，拥有以世界自然遗产屋久岛为首的各种" +
            "特色岛屿和樱岛等火山，茂密的森林。", "今天好热哈！！！", "今天确实很热！！!!", "今天真的热的不行啊！！！"};
    public static String[] mUrls = new String[]{"http://d.hiphotos.baidu.com/image/h%3D200/sign=201258cbcd80653864eaa313a7dca115/ca1349540923dd54e54f7aedd609b3de9c824873.jpg",
            "http://d.hiphotos.baidu.com/image/h%3D200/sign=ea218b2c5566d01661199928a729d498/a08b87d6277f9e2fd4f215e91830e924b999f308.jpg",
            "http://img4.imgtn.bdimg.com/it/u=3445377427,2645691367&fm=21&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=2644422079,4250545639&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=1444023808,3753293381&fm=21&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=882039601,2636712663&fm=21&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=4119861953,350096499&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=2437456944,1135705439&fm=21&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=3251359643,4211266111&fm=21&gp=0.jpg",
            "http://img4.duitang.com/uploads/item/201506/11/20150611000809_yFe5Z.jpeg",
            "http://img5.imgtn.bdimg.com/it/u=1717647885,4193212272&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=2024625579,507531332&fm=21&gp=0.jpg"};
    public static List<Integer> getImage() {
        List<Integer> list = new ArrayList<>();
        for (Integer i : images) {
            list.add(i);
        }
        return list;
    }

    public static List<Comment> getCommentDatas() {
        List<Comment> list = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            Comment comment = new Comment();
            comment.setComment_image(image[i]);
            comment.setComment_name(names[i]);
            comment.setComment_identity(indentiys[i]);
            //"yyyy-MM-dd HH:mm:ss"
            SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
            String time = format.format(new Date());
            comment.setComment_time(time);
            comment.setComment_content(content[i]);
            list.add(comment);
        }
        return list;
    }

    public static List<Moments> getMomentsDatas() {
        List<Moments> data = new ArrayList<>();
        int index=9;
        for (int i = 0; i < names.length; i++) {
            Moments moments = new Moments();
            List<String> list=new ArrayList<>();
            for (int j=0;j<index;j++){
                list.add(mUrls[j]);
            }
            moments.setUrlList(list);
            moments.setTitleimageurl(image[i]);
            moments.setComments(1);
            moments.setMoments_address("苏州-昆山");
            moments.setMoments_content(content[i]);
            moments.setMoments_identity(indentiys[i]);
            moments.setZans(1);
            moments.setShards(5);
            //"yyyy-MM-dd HH:mm:ss"
            SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
            String time = format.format(new Date());
            moments.setMoments_time(time);
            moments.setMoments_name(names[i]);
            moments.setMoments_title(titles[i]);
            moments.setFlag(true);
            data.add(moments);
            index--;
        }
        return data;
    }
}
