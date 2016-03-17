package com.yunos.paydemo.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.util.Log;


public class CommonUtil {
	
	public static String readRSAKey(InputStream privateKey) {
		try {
			String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(privateKey));
			StringBuffer sb = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				if (line == "" || (line.length() > 4 && line.substring(0, 5).equals("-----"))) {
					continue;
				}
				sb.append(line);
			}
			return sb.toString().trim();
		} catch (IOException e) {
			Log.e("error", "InputStream to string error ,", e);
		}
		return null;
	}
}
