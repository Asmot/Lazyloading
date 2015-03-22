package com.example.lazyloadingdemo;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {

	//获取从apk文件获取消息的辅助类
	InfoLoader mLoader;
	
	//存放apk消息的列表
	List<ApkInfo> apkInfos;
	
	//上下文
	Context mContext;
	
	public ListAdapter(Context mContext) {
		this.mContext = mContext;
		mLoader = new InfoLoader(mContext);
		
		//先处理好数据
		//直接一下处理完
		//这个操作会花费一些时间。。。
		apkInfos = mLoader.getApkInfos();
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return apkInfos.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		/*
		 * 这个方法，修改之后才可以getview中才可以这样调用
		 * ApkInfo apkInfo = (ApkInfo) getItem(position);
		 * 
		 * 最初是return null;
		 */
		return apkInfos.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		
		//viewHolder 优化 不在赘述
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.describeTv = (TextView) convertView.findViewById(R.id.describe_tv);
			viewHolder.iconIv = (ImageView) convertView.findViewById(R.id.icon_iv);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		ApkInfo apkInfo = (ApkInfo) getItem(position);
		viewHolder.describeTv.setText(apkInfo.title);
		viewHolder.iconIv.setImageDrawable(apkInfo.icon);
		
		return convertView;
	}

	private class ViewHolder {
		TextView describeTv;
		ImageView iconIv;
	}
}
