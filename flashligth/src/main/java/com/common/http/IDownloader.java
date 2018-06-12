package com.common.http;

public interface IDownloader {
	
	void updateDownloadProgress(final HttpDownloader.DownloadProgress dp);
	void installApk(final String apkFilePath);
	void onSDCardNotFound();
	String getApkFilePath();
}
