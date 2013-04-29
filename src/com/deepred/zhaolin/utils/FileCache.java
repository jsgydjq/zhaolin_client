package com.deepred.zhaolin.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import android.net.Uri;

public class FileCache {
	// Cache first
	public static Uri getImageURI(String path, File cache) throws Exception {
		String name = path.substring(path.lastIndexOf("/"), path.lastIndexOf("."));
		File file = new File(cache, name);
		
		if (file.exists()) {
			return Uri.fromFile(file);//Uri.fromFile(path)’
		} else {
			URL url = new URL(path);
				InputStream is = url.openConnection().getInputStream();
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buffer = new byte[10240*3];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}
				
				is.close();
				fos.close();
				return Uri.fromFile(file);
		}
	}
}
