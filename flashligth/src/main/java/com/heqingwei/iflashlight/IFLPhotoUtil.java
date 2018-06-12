package com.heqingwei.iflashlight;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class IFLPhotoUtil {
	
	public static Drawable bitmap2Gray(Context ctx, int bmSrcResId) {
		return new BitmapDrawable(ctx.getResources(), bitmap2Gray(BitmapFactory.decodeResource(ctx.getResources(), bmSrcResId)));
	}
	
	public static Bitmap bitmap2Gray(Bitmap bmSrc) {
		// 得到图片的长和宽
		int width = bmSrc.getWidth();
		int height = bmSrc.getHeight();
		// 创建目标灰度图像
		Bitmap bmpGray = null;
		bmpGray = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		// 创建画布
		Canvas c = new Canvas(bmpGray);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmSrc, 0, 0, paint);
		
		return bmpGray;
	}
}
