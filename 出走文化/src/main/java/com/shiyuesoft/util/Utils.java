package com.shiyuesoft.util;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.util.Base64;

public class Utils {
	// 将bitmap转成string类型通过Base64
	public static byte[] BitmapToString(Bitmap bitmap) {

		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		// 将bitmap压缩成50%
		bitmap.compress(Bitmap.CompressFormat.JPEG,20, bao);
		// 将bitmap转化为一个byte数组
		byte[] bs = bao.toByteArray();
		// 将byte数组用BASE64加密
//		String photoStr = Base64.encodeToString(bs, Base64.DEFAULT);
		// 返回String
//		String photo=new String(bs,0,bs.length);
		return bs;
	}
}
