package com.deepred.zhaolin.utils;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.deepred.zhaolin.R;
import com.deepred.zhaolin.entity.DetailEntity;
import com.deepred.zhaolin.ui.MainActivity;
import com.deepred.zhaolin.ui.SplashActivity;
import com.deepred.zhaolin.utils.FileCache;

public class CollectionActivity extends Activity{	
	public static BlueAdapter ba;
	public static List<DetailEntity> list;
	public static ListView lv;
	private File cache;
	public static TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);	
		//SplashActivity.sdao.deleteAll();
		list = MainActivity.convertToDetail(SplashActivity.sdao.list());
		cache = new File(Environment.getExternalStorageDirectory(), "cache2");
		//List<DetailEntity> list =null;
		//list = new ArrayList<DetailEntity>();//MainActivity2.list; 
		ba = new BlueAdapter(this, R.layout.blueadapter, list);
		this.setContentView(R.layout.collection);
		tv = (TextView) this.findViewById(R.id.empty);
		lv = (ListView) this.findViewById(R.id.bluetooth);	
		if (ba.isEmpty()){
			lv.setEmptyView(tv);
		}
		else {
			lv.setAdapter(ba);
		}
		//lv.setAdapter(ba);
		//TextView tv = new TextView(this);
		//tv.setText("Empty");
		//v.setEmptyView(tv);
		//List<SavedApp> saveds = SplashActivity.sdao.list();
		//for (SavedApp sa : saveds){
		//	aa.add(sa);
		//} 
				
		/*
		蓝牙搜索并展示代码
		list = new ArrayList<String>();
		list.add("hello");
		mArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, list);
		this.setContentView(R.layout.collection);
		ListView lv = (ListView) this.findViewById(R.id.bluetooth);
		lv.setAdapter(mArrayAdapter);

		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		Set<BluetoothDevice> devices = adapter.getBondedDevices();
		for(int i=0; i<devices.size(); i++){
			BluetoothDevice device = (BluetoothDevice) devices.iterator().next();
		}
				
		Context context = getApplicationContext();
		// 设置广播信息过滤
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
		intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
		intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		// 注册广播接收器，接收并处理搜索结果
		context.registerReceiver(receiver, intentFilter);
		// 寻找蓝牙设备，android会将查找到的设备以广播形式发出去
		adapter.startDiscovery();	
		
		 */
	}
	
	public static void refresh(){
		ba.clear();
		List<DetailEntity> temp = MainActivity.convertToDetail(SplashActivity.sdao.list());
		for(DetailEntity item:temp){
			ba.add(item);
		}
		ba.notifyDataSetChanged();
		if (ba.isEmpty()){
			lv.setEmptyView(tv);
		}
		else{
			lv.setAdapter(ba);
		}
	}
	
	@Override
	public void onResume() {
	    super.onResume();  
	}

	class BlueAdapter extends ArrayAdapter<DetailEntity> implements ListAdapter {
		private List<DetailEntity> list;
		private LayoutInflater layoutInflater;
		
		public BlueAdapter(Context context, int resourceId, List<DetailEntity> list) {
			super(context, resourceId, list);
			this.list = list;	
			layoutInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public DetailEntity getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
		class ViewHolder {
			public TextView textView;
			public TextView textView2;
			public ImageView imgView;
		}
		
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			//ViewHolder holder;
			if (convertView == null) {
				//加载布局-
				
				convertView = layoutInflater.inflate(R.layout.blueadapter, null);
				ImageView iv = (ImageView) convertView.findViewById(R.id.imgB);
				try {
					iv.setImageURI(FileCache.getImageURI(list.get(position).getImageUrl(), cache));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				TextView tv_1 = (TextView) convertView.findViewById(R.id.titleB);
				tv_1.setText(list.get(position).getTitle());
			}
			
			return convertView;
		}
		
		@Override
		public int getItemViewType(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return list.size();
		}
		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean isEmpty() {
			// TODO Auto-generated method stub
			return list.size() == 0;
		}

		@Override
		public boolean areAllItemsEnabled() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isEnabled(int position) {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public void registerDataSetObserver(DataSetObserver observer) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void unregisterDataSetObserver(DataSetObserver observer) {
			// TODO Auto-generated method stub
			
		}
	}
}
	