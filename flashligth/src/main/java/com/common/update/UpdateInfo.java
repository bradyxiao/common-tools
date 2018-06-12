package com.common.update;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 更新信息类
 * @author apache
 *
 */
public class UpdateInfo {
	
	private int mVersionCode;
	private String mVersionName;
	private String mDownloadUrl;
	private String mUpdateLog;
	
	public int getVersionCode() {
		return mVersionCode;
	}
	public void setVersionCode(int versionCode) {
		this.mVersionCode = versionCode;
	}
	public String getVersionName() {
		return mVersionName;
	}
	public void setVersionName(String versionName) {
		this.mVersionName = versionName;
	}
	public String getDownloadUrl() {
		return mDownloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.mDownloadUrl = downloadUrl;
	}
	public String getUpdateLog() {
		return mUpdateLog;
	}
	public void setUpdateLog(String updateLog) {
		this.mUpdateLog = updateLog;
	}
	
	public static UpdateInfo parse(String jsonStr) throws JSONException {
		UpdateInfo updateInfo = new UpdateInfo();
		JSONObject jsonObject = new JSONObject(jsonStr).getJSONObject("android");
		updateInfo.setVersionCode(jsonObject.getInt("versionCode"));
		updateInfo.setVersionName(jsonObject.getString("versionName"));
		updateInfo.setDownloadUrl(jsonObject.getString("downloadUrl"));
		updateInfo.setUpdateLog(jsonObject.getString("updateLog"));
        return updateInfo;
	}
}