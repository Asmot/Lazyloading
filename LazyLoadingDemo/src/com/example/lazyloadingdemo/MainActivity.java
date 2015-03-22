package com.example.lazyloadingdemo;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;


public class MainActivity extends ActionBarActivity {

	private ListView mListView;
	ListAdapter mAdapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        setUpViews();
    }

	private void setUpViews() {
		mListView = (ListView) findViewById(R.id.main_list);
		
		mAdapter = new ListAdapter(this);
		
		mListView.setAdapter(mAdapter);
		
	}


}
