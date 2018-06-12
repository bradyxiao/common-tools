package com.heqingwei.iflashlight;

import com.common.update.UpdateInfo;
import com.common.update.UpdateManager;
import com.google.zxing.WriterException;
import com.tencent.qcloud.flashligth.R;
import com.zxing.encoding.EncodingHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class IFLAboutActivity extends Activity {
	
	private AlertDialog mUpdateDialog;
	
	private IFLAboutContext mMyCtx;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		mMyCtx = new IFLAboutContext(this);
		
		initView();
	}
	
	private void initView() {
		((TextView)findViewById(R.id.version_name)).setText(IFLSysUtil.getVersionName(this));
		
		findViewById(R.id.goto_rank_button).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mMyCtx.gotoRank();
			}
			
		});
		
		findViewById(R.id.show_download_qrcode_button).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					IFLUiHelper.showDownloadQRCode(IFLAboutActivity.this, EncodingHandler.createQRCode(IFLAboutActivity.this, 
							IFLConfig.LATEST_VERSION_DOWNLOAD_URL, 640));
				} catch (WriterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					IFLUiHelper.toastMsg(IFLAboutActivity.this, "Fail to generate QRCode image");
				}
			}});
		
		findViewById(R.id.check_update_button).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//IFLUiHelper.toastMsg(IFLAboutActivity.this, "Funtion not finished yet:(");
				checkUpdate();
			}
			
		});
		
	}
	
	public static class UpdateHandler extends Handler {};
	private Handler mUpdateHandler = new UpdateHandler() {

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case UpdateManager.MSG_UPDATE_GET_UPDATE_INFO_ERROR:
				getUpdateInfoError();
				break;
				
			case UpdateManager.MSG_UPDATE_ALLREADY_LATEAST:
				versionAllreadyLatest();
				break;
				
			case UpdateManager.MSG_UPDATE_NEED_UPDATE:
				needUpdate((UpdateInfo)msg.obj);
				break;
			
			case UpdateManager.MSG_DO_UPDATE:
				doUpdate();
				break;
			}
		}};
	
	private void getUpdateInfoError() {
		mUpdateDialog.dismiss();
		IFLUiHelper.showCommMsgBox(this, getResources().getString(R.string.get_udpate_info_error), false);
	}
	
	private void versionAllreadyLatest() {
		mUpdateDialog.dismiss();
		IFLUiHelper.showCommMsgBox(this, getResources().getString(R.string.allready_latest_version), false);
	}
	
	private void needUpdate(UpdateInfo updateInfo) {
		mUpdateDialog.dismiss();
		IFLUiHelper.showUpdateDialog(this, mUpdateHandler, updateInfo.getUpdateLog(), updateInfo.getVersionName());
	}
	
	private void doUpdate() {
		IFLUiHelper.toastMsg(this, this.getResources().getString(R.string.download_tip));
		mMyCtx.doUpdate();
	}
	
	private void checkUpdate() {
		mUpdateDialog = IFLUiHelper.showCommMsgBox(this, this.getResources().getString(R.string.getting_update_info), true);
		mMyCtx.checkUpdate(mUpdateHandler);
	}

}
