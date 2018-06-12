package com.heqingwei.iflashlight;

import android.content.Context;
import android.view.View;
import android.widget.Button;

/**
 * 常用的颜色按钮
 * @author apache
 *
 */
public class IFLCommColorButton extends Button {
	
	private int mColor;
	
	public IFLCommColorButton(Context context) {
		super(context);
	}
	
	public IFLCommColorButton(final Context context, final int color) {
		this(context);
		
		mColor = color;
		setBackgroundColor(color);
		
		this.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				IFLDeviceManager.getInstance().clickVibrate();
				((IFLLightScreenActivity)context).getHandler()
					.obtainMessage(IFLLightScreenActivity.MSG_SET_AND_SABE_NEW_COLOR, color, 0).sendToTarget();
			}
			
		});
		
	}
	
	public int getColor() {
		return mColor;
	}
}