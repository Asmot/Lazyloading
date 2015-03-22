package com.example.lazyloadingdemo;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class InfoLoader {
	
	//Ĭ��λ�� ��ȡϵͳӦ�ó����б�
	String path = "/system/app/";
	
	private PackageManager mPm;
	
	public InfoLoader(Context context) {
		mPm = context.getPackageManager();
	}
	
	
	/**
	 * һ���Ի�ȡ���е�apk����Ϣ
	 * Ȼ�󷵻�
	 * @return
	 */
	public ArrayList<ApkInfo> getApkInfos() {
		
		ArrayList<ApkInfo> apkInfos = new ArrayList<ApkInfo>();
		
		if(mPm != null) {
			File dirFile = new File(path);
			for(File file : dirFile.listFiles()) {
				String path = file.getAbsolutePath();
				
				//�������apk��β��  ��ȡ��apk����Ϣ
				if (path.toLowerCase().endsWith("apk")) {
					try {
						PackageInfo packageInfo = mPm.getPackageArchiveInfo(file.getPath(), 0);
						packageInfo.applicationInfo.sourceDir = path;
						packageInfo.applicationInfo.publicSourceDir = path;
						ApkInfo apkInfo = new ApkInfo();
						apkInfo.packageInfo = packageInfo;
						apkInfo.title = mPm.getApplicationLabel(packageInfo.applicationInfo).toString().trim();						
						apkInfo.icon = mPm.getApplicationIcon(apkInfo.packageInfo.applicationInfo);
						//һ���Ի�ȡ�����ж���
						
						apkInfos.add(apkInfo);
						
					} catch (Throwable t) {
						
					}
				}
			}
		}
		
		
		return apkInfos;
	}
}
