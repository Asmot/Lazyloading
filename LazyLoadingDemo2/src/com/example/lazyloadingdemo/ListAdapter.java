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

	//��ȡ��apk�ļ���ȡ��Ϣ�ĸ�����
	InfoLoader mLoader;
	
	//���apk��Ϣ���б�
	List<ApkInfo> apkInfos;
	
	//������
	Context mContext;
	
	//ʹ�ö��߳�
	private ExecutorService mPool;
	
	//һ�»������������Ժ�ͼƬ���Գ��������ˡ�����
	//����취 �ڻ�����ʱ�򲻼���
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
		
		//�ȴ��������
		//���ȵõ�������Щ���׵õ���
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
		 * ����������޸�֮��ſ���getview�вſ�����������
		 * ApkInfo apkInfo = (ApkInfo) getItem(position);
		 * 
		 * �����return null;
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
		
		//viewHolder �Ż� ����׸��
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
		
	
		//���ж� apk��Ϣ�Ƿ�����
		if(apkInfo.hasDetail) {
			viewHolder.iconIv.setImageDrawable(apkInfo.icon);
			viewHolder.describeTv.setText(apkInfo.title);
		} else {
			//û�д��ڻ���״̬
			if(!isBusy) {
				//�ȷ�һ��Ĭ�ϵ�ͼƬ
				viewHolder.iconIv.setImageResource(R.drawable.ic_launcher);
				viewHolder.describeTv.setText("title");
				//Ȼ�󿪸��߳�ȥ������ȡ
				mPool.submit(new Runnable() {
					
					@Override
					public void run() {
						//��ȡͼƬ
						mLoader.setApkDetailInfo(apkInfo);
						System.out.println("mpool " + apkInfo.path);
						System.out.println("mpool " + apkInfo.title);
						//ˢ�½���
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
