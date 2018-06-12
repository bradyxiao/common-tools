package com.heqingwei.iflashlight;

import android.content.Context;
import android.widget.LinearLayout;

/**
 * 常用颜色按钮管理器
 * @author apache
 *
 */

public class IFLCommColorManager {

	private static final IFLCommColorManager mInstance = new IFLCommColorManager();
	
	private IFLCommColorManager() {}
	public static IFLCommColorManager getInstance() {
		return mInstance;
	}
	
	public void initCommColorButton(Context ctx, LinearLayout container) {
		for(int color: IFLConfig.COMMD_COLOR_VALUES) {
			container.addView(new IFLCommColorButton(ctx, color));
		}
	}
	
}
