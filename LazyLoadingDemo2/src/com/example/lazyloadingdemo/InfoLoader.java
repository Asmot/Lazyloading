package com.example.lazyloadingdemo;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

public class InfoLoader {
	
	//Ĭ��λ�� ��ȡϵͳӦ�ó����б�
	String path = "/system/app";
	
	private PackageManager mPm;
	
	public InfoLoader(Context context) {
		mPm = context.getPackageManager();
	}
	
	
	/**
	 * ��ȡ���е�apk�Ĳ�����Ϣ��apk·��
	 * Ȼ�󷵻�
	 * @return
	 */
	public ArrayList<ApkInfo> getBriefApkInfos() {
		
		ArrayList<ApkInfo> apkInfos = new ArrayList<ApkInfo>();
		
		if(mPm != null) {
			File dirFile = new File(path);
			for(File file : dirFile.listFiles()) {
				String filePath = file.getAbsolutePath();
				
				//�������apk��β��  ��ȡ��apk����Ϣ
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
	 * ����apkinfo ��ȡapplication��ͼ��
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
				
				//��� apkinfo ��Ϣ����
				apkInfo.hasDetail = true;
				
				Thread.sleep(new Random().nextInt(5) * 1000);
				
			} catch (Throwable t) {
				
			}
		}
		return apkInfo;
	}
}
