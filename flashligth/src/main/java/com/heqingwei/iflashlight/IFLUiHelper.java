package com.heqingwei.iflashlight;

import com.common.update.UpdateManager;
import com.tencent.qcloud.flashligth.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.ColorPicker.OnColorChangedListener;

public class IFLUiHelper {
	
	@SuppressLint("InflateParams")
	public static void pickColorDialog(final Context ctx) {
		AlertDialog.Builder adb = new AlertDialog.Builder(ctx);
		LayoutInflater inflater = LayoutInflater.from(ctx);
		final View pickColorView = inflater.inflate(R.layout.activity_pick_color, null);
		LinearLayout ll = (LinearLayout) pickColorView.findViewById(R.id.commcolor_layout);
		IFLCommColorManager.getInstance().initCommColorButton(ctx, ll);
		final ColorPicker picker = (ColorPicker)pickColorView.findViewById(R.id.picker);
		picker.setOldCenterColor(IFLConfig.getLastColor());
		picker.setColor(IFLConfig.getLastColor());
		picker.setOnColorChangedListener(new OnColorChangedListener() {

			@Override
			public void onColorChanged(int color) {
				picker.setOldCenterColor(color);
				((IFLLightScreenActivity)ctx).getHandler().obtainMessage(
						IFLLightScreenActivity.MSG_SET_AND_SABE_NEW_COLOR, color, 0).sendToTarget();
			}});
		
		adb.setView(pickColorView);

		adb.show();
	}
	
	public static void toastMsg(Context ctx, final String msg) {
		Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
	}
	
	
	private static AlertDialog showDialogWithView(Context ctx, View view) {
		AlertDialog.Builder adb = new AlertDialog.Builder(ctx);
		adb.setView(view);
		return adb.show();
	}
	
	public static void showDownloadQRCode(Context ctx, final Drawable img) {
		AlertDialog.Builder adb = new AlertDialog.Builder(ctx);
		
		LayoutInflater factory = LayoutInflater.from(ctx);
		final View qrcodeView =factory.inflate(R.layout.download_qrcode, null);
		ImageView qrcodeImg = (ImageView) qrcodeView.findViewById(R.id.qrcode);
		qrcodeImg.setBackground(img);
		
		//showDialogWithView(ctx, qrcodeImg);
		adb.setView(qrcodeView);
		adb.show();
	}
	
	public static AlertDialog showCommMsgBox(Context ctx, String text, boolean showPrgBar) {
		//AlertDialog.Builder adb = new AlertDialog.Builder(ctx);
		LayoutInflater factory = LayoutInflater.from(ctx);
		final View view =factory.inflate(R.layout.comm_msgbox, null);
		((TextView)view.findViewById(R.id.comm_msgbox_text)).setText(text);
		if(!showPrgBar) {
			view.findViewById(R.id.comm_msgbox_prgbar).setVisibility(View.GONE);
		}
		
		return showDialogWithView(ctx, view);
		//adb.setView(view);
		//return adb.show();
	}
	
	public static void showUpdateDialog(Context ctx, final Handler handler, String updateLog, String versionName) {
		LayoutInflater factory = LayoutInflater.from(ctx);
		final View view =factory.inflate(R.layout.update_dialog, null);
		((TextView)view.findViewById(R.id.update_info_text)).setText(updateLog);
		((TextView)view.findViewById(R.id.version_name_text)).setText(versionName);
		
		final AlertDialog dialog = showDialogWithView(ctx, view);
		((Button)view.findViewById(R.id.update_button)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				handler.sendEmptyMessage(UpdateManager.MSG_DO_UPDATE);
			}
			
		});

		((Button)view.findViewById(R.id.cancel_update_button)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
			
		});
	}
	
}
