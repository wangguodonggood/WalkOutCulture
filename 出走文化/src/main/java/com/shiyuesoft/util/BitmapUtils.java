package com.shiyuesoft.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import com.lzy.imagepicker.util.BitmapUtil;

import java.io.IOException;

/**
 * 压缩文件
 */
public class BitmapUtils {

	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			//首先获取原图高度和宽度的一半
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;
			//循环，如果halfHeight和halfWidth同时大于最小宽度和最小高度时，inSampleSize乘2，
			//最后得到的宽或者高都是最接近最小宽度或者最小高度的
			while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}

	/**
	 * 根据Resources压缩图片
	 *
	 * @param res
	 * @param resId
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		options.inJustDecodeBounds = false;
		Bitmap src = BitmapFactory.decodeResource(res, resId, options);
		return src;
	}
	/**
	 * 旋转图片
	 * @param angle
	 * @param bitmap
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {
		//旋转图片 动作
		Matrix matrix = new Matrix();;
		matrix.postRotate(angle);
		System.out.println("angle2=" + angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}
	/**
	 * 读取图片属性：旋转的角度
	 * @param path 图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree  = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 根据地址压缩图片
	 *
	 * @param pathName
	 * @param reqWidth 最小宽度
	 * @param reqHeight 最小高度
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromFd(String pathName, int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		// 若要对图片进行压缩，必须先设置Option的inJustDecodeBounds为true才能进行Option的修改
		options.inJustDecodeBounds = true;
		// 第一次decodeFile是获取到options.outHeight和options.outWidth
		BitmapFactory.decodeFile(pathName, options);
		// options.inSampleSize是图片的压缩比，例如原来大小是100*100，options.inSampleSize为1，则不变，
		// options.inSampleSize为2，则压缩成50*50
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		// 重新设置options.inJustDecodeBounds为false，不能修改option
		options.inJustDecodeBounds = false;
		// 根据options重新加载图片
		Bitmap src = BitmapFactory.decodeFile(pathName, options);
		/**
		 * 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转
		 */
		int degree = BitmapUtils.readPictureDegree(pathName);
		/**
		 * 把图片旋转为正的方向
		 */
		src = BitmapUtils.rotaingImageView(degree, src);
		return src;
	}

}
