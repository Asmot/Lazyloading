package com.example.lazyloadingdemo;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

public class InfoLoader {
	
	//默认位置 获取系统应用程序列表
	String path = "/system/app";
	
	private PackageManager mPm;
	
	public InfoLoader(Context context) {
		mPm = context.getPackageManager();
	}
	
	
	/**
	 * 获取所有的apk的部分消息，apk路径
	 * 然后返回
	 * @return
	 */
	public ArrayList<ApkInfo> getBriefApkInfos() {
		
		ArrayList<ApkInfo> apkInfos = new ArrayList<ApkInfo>();
		
		if(mPm != null) {
			File dirFile = new File(path);
			for(File file : dirFile.listFiles()) {
				String filePath = file.getAbsolutePath();
				
				//如果是以apk结尾的  提取该apk的信息
				if (filePath.toLowerCase().endsWith("apk")) {
					ApkInfo apkInfo = new ApkInfo();
					apkInfo.path = filePath;
					
					apkInfos.add(apkInfo);
				}
			}
		}
		
		
		return apkInfos;
	}
	
	
	/**
	 * 根据apkinfo 获取application的图标
	 * @param apkInfo
	 */
	public ApkInfo setApkDetailInfo(ApkInfo apkInfo) {
		if(mPm != null) {
			try {
				PackageInfo packageInfo = mPm.getPackageArchiveInfo(apkInfo.path, 0);
				packageInfo.applicationInfo.sourceDir = apkInfo.path;
				packageInfo.applicationInfo.publicSourceDir = apkInfo.path;
				apkInfo.packageInfo = packageInfo;
				apkInfo.title = mPm.getApplicationLabel(packageInfo.applicationInfo).toString().trim();						
				apkInfo.icon = mPm.getApplicationIcon(apkInfo.packageInfo.applicationInfo);
				
				//标记 apkinfo 信息完整
				apkInfo.hasDetail = true;
				
				Thread.sleep(new Random().nextInt(5) * 1000);
				
			} catch (Throwable t) {
				
			}
		}
		return apkInfo;
	}
}
