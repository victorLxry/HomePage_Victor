package com.victor.homelaunchvic.menu.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class DrawingCacheView extends View {
	private Bitmap bitmap;

	public DrawingCacheView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DrawingCacheView(Context context) {
		this(context, null);
	}

	public void setBitmap(Bitmap bit) {
		bitmap = bit;

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if (bitmap != null) {
			if (flag) {
				bitmap = drawImageDropShadow(bitmap);
				flag = false;
			}
			canvas.drawBitmap(bitmap, 0, 0, null);
		}
	}

	public void setFlag(boolean is) {
		flag = is;
	}

	// private Bitmap adjustOpacity( Bitmap bitmap )
	// {
	// int width = bitmap.getWidth();
	// int height = bitmap.getHeight();
	// Bitmap dest = Bitmap.createBitmap(width, height,
	// Bitmap.Config.ARGB_8888);
	// int[] pixels = new int[width * height];
	// bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
	// dest.setPixels(pixels, 0, width, 0, 0, width, height);
	// return dest;
	// }

	private boolean flag = true;

	private Bitmap drawImageDropShadow(Bitmap originalBitmap) {
//
//		BlurMaskFilter blurFilter = new BlurMaskFilter(3,
//				BlurMaskFilter.Blur.NORMAL);
//		Paint shadowPaint = new Paint();
//		shadowPaint.setAlpha(120);
//		shadowPaint.setAntiAlias(true);
//		shadowPaint.setDither(true);
//		shadowPaint.setColor(0xFFFF0000);
//		shadowPaint.setStyle(Paint.Style.STROKE);
//		shadowPaint.setStrokeJoin(Paint.Join.ROUND);
//		shadowPaint.setStrokeCap(Paint.Cap.ROUND);
//		shadowPaint.setStrokeWidth(12);
//
//		// shadowPaint.setColor(getResources()
//		// .getColor(R.color.black));
//		shadowPaint.setMaskFilter(blurFilter);
//
//		int[] offsetXY = new int[2];
//		Bitmap shadowBitmap = originalBitmap
//				.extractAlpha(shadowPaint, offsetXY);
//
//		Bitmap shadowImage32 = shadowBitmap.copy(Bitmap.Config.ARGB_8888, true);
//		Canvas c = new Canvas(shadowImage32);
//		c.drawBitmap(originalBitmap, offsetXY[0], offsetXY[1], null);
		return originalBitmap;
	}

}
