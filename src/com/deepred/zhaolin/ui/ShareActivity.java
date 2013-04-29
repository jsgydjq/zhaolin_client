package com.deepred.zhaolin.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.deepred.zhaolin.BaseActivity;
import com.deepred.zhaolin.R;
import com.deepred.zhaolin.utils.ZhaolinConstants;
import com.deepred.zhaolin.entity.DetailEntity;
import com.google.gson.Gson;

/**
 * @author Administrator
 */
public class ShareActivity extends BaseActivity {
	List<DetailEntity> list = new ArrayList<DetailEntity>();
	List<JsonEntity> json = new ArrayList<JsonEntity>();
	List<JsonEntity> cancleJson = new ArrayList<JsonEntity>();
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.share);

		final PackageManager pm = getPackageManager();
		// get a list of installed apps.
		List<ApplicationInfo> packages = pm
				.getInstalledApplications(PackageManager.GET_META_DATA);

		showTip("Share Activity: " + renrenId + ": "+ userName);
		for (ApplicationInfo packageInfo : packages) {
			if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
				DetailEntity de_1 = new DetailEntity();
				JsonEntity je = new JsonEntity();
				// set element layout of each
				de_1.setLayoutID(R.layout.my_appitem);
				try {
					// get app version
					PackageInfo pk = pm.getPackageInfo(packageInfo.packageName,
							0);
					de_1.setText("v" + pk.versionName);

				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
				// get pak name
				String name = pm.getApplicationLabel(packageInfo).toString();
				de_1.setTitle(name);
				je.setName(name);
				// get pak icon
				Drawable icon = pm.getApplicationIcon(packageInfo);
				de_1.setImg(icon);
				// set the image in File
				je.setImg(icon);
				// get package size
				File f = new File(packageInfo.sourceDir);
				de_1.setSize(f.length());
				je.setSize("" + f.length());
				// add to the list
				list.add(de_1);
				json.add(je);
			}
		}
		
		// Share all apps
		new EncodeUploadTask().execute(json, true);
		ListView lv = (ListView) this.findViewById(R.id.listView_my);
		MyAdapter ma = new MyAdapter(this, list, cancleJson);
		lv.setAdapter(ma);
		Button btn = (Button) findViewById(R.id.confirm_share);
		
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO cancle share some apps
				//new EncodeUploadTask().execute(cancleJson, false);
				Intent intent = new Intent(ShareActivity.this, MainActivity.class);
		    	ShareActivity.this.startActivity(intent);
		    	//ShareActivity.this.finish();
			}
		});
	}

	
	/**
	 * @author Administrator
	 */
	class MyAdapter implements ListAdapter {

		private List<DetailEntity> list;
		private List<JsonEntity> json;

		/** XML布局 */
		private LayoutInflater layoutInflater;

		public MyAdapter(Context context, List<DetailEntity> list,
				List<JsonEntity> json) {
			this.list = list;
			this.json = json;
			layoutInflater = LayoutInflater.from(context);
		}

		@Override
		public void registerDataSetObserver(DataSetObserver observer) {
		}

		@Override
		public void unregisterDataSetObserver(DataSetObserver observer) {

		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		/**
		 * 控制ITEM布局
		 */
		@SuppressWarnings("deprecation")
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				// 加载布局
				convertView = layoutInflater.inflate(list.get(position)
						.getLayoutID(), null);
				ImageView iv = (ImageView) convertView.findViewById(R.id.img);
				iv.setBackgroundDrawable(list.get(position).getImg());

				TextView tv_1 = (TextView) convertView.findViewById(R.id.title);
				tv_1.setText(list.get(position).getTitle());

				TextView tv_2 = (TextView) convertView.findViewById(R.id.text);
				tv_2.setText(list.get(position).getText());
				CheckBox cb = (CheckBox) convertView
						.findViewById(R.id.checkbox);
				cb.setSelected(false);
				cb.setChecked(false);
				
				cb.setFocusable(true);// 不接受焦点，焦点给VIEW
				cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton checkbox,
							boolean checked) {
						if (checked) {
							JsonEntity je = new JsonEntity();
							je.setName(list.get(position).getTitle());
							json.add(je);
						}
					}
				});
			}
			return convertView;
		}

		@Override
		public int getItemViewType(int position) {
			return position;
		}

		@Override
		public int getViewTypeCount() {
			return list.size();
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		/**
		 * 是否ITEM 监听
		 */
		@Override
		public boolean isEmpty() {
			return true;
		}

		/**
		 * true 所有项目可选择可点击
		 */
		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}

		/**
		 *  是否现实分割线
		 */
		@Override
		public boolean isEnabled(int position) {
			return true;
		}
	}



	class JsonEntity {
		/*
		 * 必要字段
		 */
		private String name;

		private Drawable img;

		public Drawable getImg() {
			return img;
		}

		public void setImg(Drawable img) {
			this.img = img;
		}

		private String version;

		private String size;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		public String getSize() {
			return size;
		}

		public void setSize(String size) {
			this.size = size;
		}
	}

	public class EncodeUploadTask extends AsyncTask<Object, Long, String> {
		/*
		 * this class starts a async thread to encode JsonEntity list,
		 * especially the image into base64 encoded strings, to be packaged into
		 * JSON objects
		 */

		@Override
		protected String doInBackground(Object... params) {
			String response = "";
			List<jObject> list = new ArrayList<jObject>();
			@SuppressWarnings("unchecked")
			List<JsonEntity> jList = (List<JsonEntity>) params[0];
			boolean bShare = (Boolean)params[1];
			for (JsonEntity je : jList) {
				if (je != null) {
					Drawable icon = je.getImg();
					String iconString = convertToString(icon);
					jObject jo = new jObject();
					jo.setImg(iconString);
					jo.setName(Base64.encodeToString(je.getName().getBytes(), Base64.DEFAULT));
					jo.setSize(je.getSize());
					list.add(jo);
				}
			}

			// 打包成JSON对象
			uploadPackage up = new uploadPackage();
			up.setRenrenId(userPrimkey);
			up.setAppList(list);
			
			Gson gson = new Gson();
			String message = gson.toJson(up);
			// TODO modify uploadpackage to adapt to server requuirement
			// 上传JSON对象
			String url;
			if(bShare){
				url = ZhaolinConstants.shareAppsUrl+"?share=True";
			}
			else{
				url = ZhaolinConstants.shareAppsUrl+"?share=False";
			}
			DefaultHttpClient httpClient = new DefaultHttpClient();
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			HttpPost postMethod = new HttpPost(url);
			postMethod.setHeader( "Content-Type", "application/json");
			
			try {
				postMethod.setEntity(new ByteArrayEntity(message
						.getBytes("UTF8")));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			try {
				response = httpClient.execute(postMethod, responseHandler);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// SharedPreference
			/*
			 * SharedPreferences prefs = getSharedPreferences(PREFERENCES_NAME,
			 * Activity.MODE_PRIVATE); boolean ifFirst =
			 * prefs.getBoolean("firstTime", true); SharedPreferences.Editor
			 * edit = prefs.edit(); if (ifFirst){ edit.putBoolean("firstTime",
			 * false); } else{ edit.putBoolean("firstTime", true); }
			 */
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			showTip(result);
			/*Intent myIntent = new Intent(ShareActivity.this,
					DiscoverActivity.class);
			myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(myIntent);
			ShareActivity.this.finish();*/
		}

	}

	private static String convertToString(Drawable icon) {
		Bitmap bitmap = ((BitmapDrawable) icon).getBitmap();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] bitmapdata = stream.toByteArray();
		String imgString = Base64.encodeToString(bitmapdata, Base64.DEFAULT);
		return imgString;
	}
}

class uploadPackage {
	private long primkey;
	private List<jObject> appList;

	public long getRenrenId() {
		return primkey;
	}

	public void setRenrenId(long renrenId) {
		this.primkey = renrenId;
	}

	public List<jObject> getAppList() {
		return appList;
	}

	public void setAppList(List<jObject> appList) {
		this.appList = appList;
	}
}

class jObject {
	private String name;
	private String img;
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	private String size;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
}
