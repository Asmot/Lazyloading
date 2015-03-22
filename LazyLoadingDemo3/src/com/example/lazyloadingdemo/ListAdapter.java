package com.example.lazyloadingdemo;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {
	
	public final static int GET_APKINFO_DOWN = 0;

	//��ȡ��apk�ļ���ȡ��Ϣ�ĸ�����
	InfoLoader mLoader;
	
	//���apk��Ϣ���б�
	List<ApkInfo> apkInfos;
	
	//������
	Context mContext;
	
	//ʹ�ö��߳�
	private ExecutorService mPool;
	
	//ʹ���첽����
	MyTask myTask;
	
	
	//һ�»������������Ժ�ͼƬ���Գ��������ˡ�����
	//����취 �ڻ�����ʱ�򲻼���
	private boolean isBusy = false; 
	
	public boolean isBusy() {
		return isBusy;
	}
	public void setBusy(boolean isBusy) {
		this.isBusy = isBusy;
		
		
		
//		notifyDataSetChanged();
	}
	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case GET_APKINFO_DOWN:
				notifyDataSetChanged();
				break;

			default:
				break;
			}
		}
		
	};
	

	public ListAdapter(Context mContext, ExecutorService mPool) {
		this.mContext = mContext;
		mLoader = new InfoLoader(mContext);
		
		//�ȴ��������
		//���ȵõ�������Щ���׵õ���
		apkInfos = mLoader.getBriefApkInfos();
		
		this.mPool = mPool;
		
		
		myTask = new MyTask();
//		myTask.executeOnExecutor(mPool, null);
		
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
		System.out.println("getview:  " + position);
		final ApkInfo apkInfo = (ApkInfo) getItem(position);
		//û�д��ڻ���״̬
		if(!isBusy) {
			// ���ж� apk��Ϣ�Ƿ�����
			if (apkInfo.hasDetail) {
				viewHolder.iconIv.setImageDrawable(apkInfo.icon);
				viewHolder.describeTv.setText(apkInfo.title);
			} else {
				// �ȷ�һ��Ĭ�ϵ�ͼƬ
				viewHolder.iconIv.setImageResource(R.drawable.ic_launcher);
				viewHolder.describeTv.setText("title");

				// �첽���� ��ֻ��ִ��һ�ε� �������ÿ�������½�һ���µĶ���
				new MyTask().executeOnExecutor(mPool, apkInfo);

			}
		} else {
			viewHolder.describeTv.setText("fliing");
		}
		return convertView;
	}

	private class ViewHolder {
		TextView describeTv;
		ImageView iconIv;
	}
	
	private class MyTask extends AsyncTask<ApkInfo, Integer, Integer> {

		@Override
		protected Integer doInBackground(ApkInfo... arg0) {
			ApkInfo apkInfo = arg0[0];
			
			mLoader.setApkDetailInfo(apkInfo);
			
			System.out.println("mpool task --------------------");
			System.out.println("mpool " + apkInfo.title);
			//ˢ�½���
			return null;
		}

		@Override
		protected void onPostExecute(Integer result) {
			handler.sendEmptyMessage(GET_APKINFO_DOWN);
			super.onPostExecute(result);
		}
		
	}
}
