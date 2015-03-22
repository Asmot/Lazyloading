package com.example.lazyloadingdemo;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class InfoLoader {
	
	//默认位置 获取系统应用程序列表
	String path = "/system/app/";
	
	private PackageManager mPm;
	
	public InfoLoader(Context context) {
		mPm = context.getPackageManager();
	}
	
	
	/**
	 * 一次性获取所有的apk的消息
	 * 然后返回
	 * @return
	 */
	public ArrayList<ApkInfo> getApkInfos() {
		
		ArrayList<ApkInfo> apkInfos = new ArrayList<ApkInfo>();
		
		if(mPm != null) {
			File dirFile = new File(path);
			for(File file : dirFile.listFiles()) {
				String path = file.getAbsolutePath();
				
				//如果是以apk结尾的  提取该apk的信息
				if (path.toLowerCase().endsWith("apk")) {
					try {
						PackageInfo packageInfo = mPm.getPackageArchiveInfo(file.getPath(), 0);
						packageInfo.applicationInfo.sourceDir = path;
						packageInfo.applicationInfo.publicSourceDir = path;
						ApkInfo apkInfo = new ApkInfo();
						apkInfo.packageInfo = packageInfo;
						apkInfo.title = mPm.getApplicationLabel(packageInfo.applicationInfo).toString().trim();						
						apkInfo.icon = mPm.getApplicationIcon(apkInfo.packageInfo.applicationInfo);
						//一次性获取完所有东西
						
						apkInfos.add(apkInfo);
						
					} catch (Throwable t) {
						
					}
				}
			}
		}
		
		
		return apkInfos;
	}
}
