package com.example.lazyloadingdemo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;


public class MainActivity extends ActionBarActivity {

	private ListView mListView;
	ListAdapter mAdapter;
	
	//使用多线程
	private ExecutorService mPool;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        init();
    }
    
	private void init() {
		mListView = (ListView) findViewById(R.id.main_list);
		
		//最多5条线程同时执行任务
		mPool = Executors.newCachedThreadPool();
		
		mAdapter = new ListAdapter(this, mPool);
		
		mListView.setAdapter(mAdapter);
		
		mListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				
				switch (arg1) {
				case OnScrollListener.SCROLL_STATE_IDLE:
					mAdapter.setBusy(false);
//					System.out.println()
					break;
				case OnScrollListener.SCROLL_STATE_FLING:
					//手起来了 在滑动
					mAdapter.setBusy(false);
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
					mAdapter.setBusy(false);
					break;
				default:
					break;
				}
			}
			
			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
//				mAdapter.setBusy(true);
			}
		});
		
	}
	
	
	@Override
	protected void onDestroy() {
		
		if(mPool != null ) {
			//关闭线程池
			mPool.shutdown();
		}
		
		super.onDestroy();
	}


}
