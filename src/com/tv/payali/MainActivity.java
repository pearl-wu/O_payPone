package com.tv.payali;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.util.Log;
import android.widget.Toast;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends CordovaPlugin {

	protected static final String LOG_TAG = "aliOrder__pay";
	public static final int MSG_SHOW_TOAST = 1;

	public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException{

		if(action.equals("Pay")){
			
			final JSONObject options = args.getJSONObject(0);

			Toast.makeText(cordova.getActivity(), "PAY PAY PAY", Toast.LENGTH_LONG).show();

			// Resultecho(true, pay.toString(), callbackContext);
			return true;
			
		}else if(action.equals("Iandroid")){
			
			boolean tr = args.getBoolean(0);
			if(tr){
				String androidId = Secure.getString(cordova.getActivity().getContentResolver(), Secure.ANDROID_ID);
				Resultecho(true, androidId, callbackContext);
			}
			return true;
			
		}else if(action.equals("Packageinfo")){
			
			int no = args.getInt(0);
			String packageinfo="";
			if(no==1){
				//packageName
				packageinfo = cordova.getActivity().getPackageName();
			}else if(no==2){
				//strVersionCode
				try {
					PackageInfo packageInfo = cordova.getActivity().getPackageManager().getPackageInfo(cordova.getActivity().getPackageName(),0);
					packageinfo = String.valueOf(packageInfo.versionCode);
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(no==3){
				try {
					PackageInfo packageInfo = cordova.getActivity().getPackageManager().getPackageInfo(cordova.getActivity().getPackageName(),0);
					packageinfo = packageInfo.versionName;
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					packageinfo = "Cannot load Version!";
				}
			}
			Resultecho(true, packageinfo, callbackContext);
			return true;
			
		}else if(action.equals("Echo")){
			
			String context = args.getString(0);
			int duration = args.getInt(1);
			Toast.makeText(cordova.getActivity(), context, Toast.LENGTH_LONG).show();

    			/*Toast mToast = new Toast(cordova.getActivity());
                                mToast.setGravity(Gravity.TOP, 0, 0);
                                LayoutInflater inflater = cordova.getActivity().getLayoutInflater();
                                View view = inflater.inflate(R.layout.toast, null);
                                TextView tvMesssage = (TextView) view.findViewById(R.id.textview);
                                mToast.setView(view);
                                mToast.setDuration(Toast.LENGTH_LONG);
                                tvMessage.setText("test"+mToast.getDuration());
                                mToast.show();*/

			return true;
			
		}else if(action.equals("Sign")){

    			/*String mag = RSASign.sign(args.getString(0), Config.getPrikey(), "utf-8" );
    			          Resultecho(true, mag, callbackContext);*/

			String source = args.getString(0);

			final String publicKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCyiWG0LoDRe+kDF9jiHI832/WF5StlzWPyELerpH7AF5W3WecatWT3b3tkOTWtbdNxd4L8KMxNks/Wh9DoNM5jyAUUdfRWuwKN7VuSoAJ0oJjxdBqJSCr1eLaMaUkJntCyTJ+U0EWXi30KzVj6IScuk2H2r+KV1E9N619jE5EUHwIDAQAB";
			final String privateKeyStr = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALKJYbQugNF76QMX2OIcjzfb9YXlK2XNY/IQt6ukfsAXlbdZ5xq1ZPdve2Q5Na1t03F3gvwozE2Sz9aH0Og0zmPIBRR19Fa7Ao3tW5KgAnSgmPF0GolIKvV4toxpSQme0LJMn5TQRZeLfQrNWPohJy6TYfav4pXUT03rX2MTkRQfAgMBAAECgYEAkFWMTVRDBBf+emevCK06A1iplwN8ICL5p9poAjVL2xO2D7J4qRj4dSkFQjMV+A0PoW2S0TRSZmxH5hGKjA8UwjBof1KygiyEzgD+avCGjDjKvQRn+O9lrK+3IIXT5pzK17aFPT4JBnzrSxzdPZSXcg2uK24Jskn0KbArCzy0cJECQQDV4I90mNJNv5Sgc1usL5N3fpr4pcIsgv11ldtnzR8pXraKhaXgHSY16LBxUHJXL1vGB6rEIsAmQFK0rKtYNBSLAkEA1bL9/4qiEegEVliwuDr+AAcWt0T5ji1dlImltFFTc3n7hQqs8tBu6LM6a5wD2hARAuhbTe+09eBsa/1aX1ttPQJASw88SVc4t0B9ELJrgcpQnqc4C/tgYe62tQWaspsyTHqI5aRxym1wc4ruIVZySla8hos6SwtHFCWO4QmYOKROcQJATtaUIlyQ4i5Iu1pJCA6renMjDEXkE3HlHlGR0m5WbTnJcxG3MHV5FVxZ5y1NtntK61mxpZUTm8pJ9aUoXBmsuQJBAKniWRntexbddWU04Rv0RY0IBRcdU42YCechwusEOHbOSoBTQGB9acf78Kpv8S9d1UAsiSaFbuqiVarZlVaRTfg=";

			try {

				PublicKey publicKey = RSAUtils.loadPublicKey(publicKeyStr);
				byte[] encryptByte = RSAUtils.encryptData(source.getBytes(), publicKey);
				String afterencrypt = Base64Utils.encode(encryptByte);

	                /* PrivateKey privateKey = RSAUtils.loadPrivateKey(privateKeyStr);
                                        // byte[] dstr =  Base64Utils.decode(den);
                                        byte[] context = RSAUtils.decryptData(den.getBytes(), privateKey);*/

				Resultecho(true, afterencrypt, callbackContext);
				// Log.i("RSAUtils.decryptByPrivatekey", new String(afterencrypt));

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			byte[] data = source.getBytes();

			try {

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return true;
		}

		return false;

	}

	public void Resultecho(Boolean boo, String meg, CallbackContext callbackContext){
		if(boo){
			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, meg));
		} else {
			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, meg));
		}
	}

}
