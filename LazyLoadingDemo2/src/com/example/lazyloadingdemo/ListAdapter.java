package com.example.lazyloadingdemo;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
	
	//使用多线程
	private ExecutorService mPool;
	
	//一下滑动到最后面的以后图片就显出不出来了。。。
	//解决办法 在滑动的时候不加载
	private boolean isBusy = false; 
	
	public boolean isBusy() {
		return isBusy;
	}
	public void setBusy(boolean isBusy) {
		this.isBusy = isBusy;
	}

	public ListAdapter(Context mContext, ExecutorService mPool) {
		this.mContext = mContext;
		mLoader = new InfoLoader(mContext);
		
		//先处理好数据
		//首先得到包名这些容易得到的
		apkInfos = mLoader.getBriefApkInfos();
		
		this.mPool = mPool;
		
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
		
		final ApkInfo apkInfo = (ApkInfo) getItem(position);
		
	
		//先判断 apk信息是否完整
		if(apkInfo.hasDetail) {
			viewHolder.iconIv.setImageDrawable(apkInfo.icon);
			viewHolder.describeTv.setText(apkInfo.title);
		} else {
			//没有处于滑动状态
			if(!isBusy) {
				//先放一张默认的图片
				viewHolder.iconIv.setImageResource(R.drawable.ic_launcher);
				viewHolder.describeTv.setText("title");
				//然后开个线程去慢慢获取
				mPool.submit(new Runnable() {
					
					@Override
					public void run() {
						//获取图片
						mLoader.setApkDetailInfo(apkInfo);
						System.out.println("mpool " + apkInfo.path);
						System.out.println("mpool " + apkInfo.title);
						//刷新界面
						notifyDataSetChanged();
					}
				});
			} else {
				viewHolder.describeTv.setText("fliing");
			}
		}
		return convertView;
	}

	private class ViewHolder {
		TextView describeTv;
		ImageView iconIv;
	}
}
