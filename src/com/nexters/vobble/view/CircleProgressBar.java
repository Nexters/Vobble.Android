package com.nexters.vobble.view;

import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;

public class CircleProgressBar extends View {

	public int a = 0;
	private Paint paint = null;
	private Canvas canvas = null;
	private RectF r = null;
	public CircleProgressBar(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void onDraw(Canvas canvas) {

		this.canvas = canvas;
		paint = new Paint();

		paint.setStrokeWidth(5);
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);
		r = new RectF(8, 8, 192, 192);

		paint.setColor(Color.WHITE);

		canvas.drawArc(r, -90, 360, true, paint); // 3�ð� 0��

		//paint.setColor(Color.WHITE);
		//paint.setAntiAlias(true);
		//paint.setColor(Color.GRAY);
		//canvas.drawArc(r, -90, a, true, paint);
	}

}
