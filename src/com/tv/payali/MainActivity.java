package com.tv.payali;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.util.Log;
import android.widget.Toast;

import com.de.aligame.api.AliTvSdk;
import com.de.aligame.core.api.Listeners;
import com.de.aligame.core.api.Listeners.IPayListener;
import com.de.aligame.core.ui.view.AliSmartViewFocusFrame;

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
	AliSmartViewFocusFrame smartView;
	private DemoPayListener mPayListener = new DemoPayListener();

	public static Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case MSG_SHOW_TOAST:
					String errorStr = (String) msg.obj;
					Log.d(LOG_TAG, "handle message. obj=" + msg.obj);
					try {
						//Toast.makeText(cordova.getActivity(), errorStr, Toast.LENGTH_LONG).show();
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				default:
					break;
			}
		}

	};

	public void initTvSDK(Context context) {

		AliTvSdk.logSwitch(true);

		String appkey = TestConstants.APPKEY;
		String appSecret = TestConstants.APP_SECRET;

		AliTvSdk.init(context, appkey, appSecret, new Listeners.IInitListener() {

			public void onInitFinish() {
				TestToast.show("init aliTvSdk ok. get auth = " + AliTvSdk.Account.isAuth());
				Log.d(LOG_TAG, "onInitFinish");
		
				AliTvSdk.Web.openWebviewOnStart(new Listeners.IWebListener() {

					public void onWebOpen() {
						TestToast.show("init openWebviewOnStart onWebOpen" );
					}

					public void onWebClose() {
						TestToast.show("init openWebviewOnStart onWebClose");

					}
				}, null);
			}

			public void onInitError(String errorMsg) {
				TestToast.show(errorMsg);
			}
		}, new TestAccount());

	}


	public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException{

		if(action.equals("Pay")){
			
			final JSONObject options = args.getJSONObject(0);
			
			initTvSDK(cordova.getActivity());
			Toast.makeText(cordova.getActivity(), "PAY PAY PAY", Toast.LENGTH_LONG).show();
			activity_pay(options);

			// Resultecho(true, pay.toString(), callbackContext);
			return true;
		}else if(action.equals("Change")){
			boolean boo = args.getBoolean(0);
			if(!boo){
				//Resultecho(true, "---(IdChange)false---", callbackContext);
				return false;
			}else{
				//Resultecho(true, "---(IdChange)true---", callbackContext);
				new Thread() {
					public void run() {
						Intent intent = new Intent();
						intent.setClassName("com.yunos.tv.payment", "com.yunos.tv.payment.TVPayMainActivity");
						intent.putExtra("operate", "alitv.unsign");
						cordova.getActivity().startActivity(intent);
					}
				}.start();
			}
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


	private ContextWrapper getResources() {
		// TODO Auto-generated method stub
		return null;
	}


	public void Resultecho(Boolean boo, String meg, CallbackContext callbackContext){
		if(boo){
			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, meg));
		} else {
			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, meg));
		}
	}


	private class DemoPayListener implements IPayListener {

		public void onSuccess(String title, int amount) {
			TestToast.show(title + " 支付成功");
			setAnimOn();
		}

		public void onCancel(String title, int amount) {
			TestToast.show(title + " 支付取消");
			setAnimOn();
		}

		public void onError(String title, int amount, String errMsg) {
			TestToast.show(title + " 支付失败。" + errMsg);
			setAnimOn();
		}
	}

	private  Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg)
		{
			if( msg.what == 1){
				smartView.setAnimOn(true);
			}
		}
	};

	private void setAnimOn() {
		mHandler.sendMessage(mHandler.obtainMessage(1));
	}


	// 单机无激励支付
	private void doPay(String title, String amount, IPayListener payListener) {
		AliTvSdk.pay(title, amount, payListener);
	}

	// 单机有激励支付
	private void doPay(String title,String subjectId, String amount, IPayListener payListener) {
		AliTvSdk.pay(title, subjectId, amount, payListener);
	}

	// CP服务器生成订单号后支付（无激励）
	private void doPayFromServer(String title, String amount, String orderId, String notifyUrl, IPayListener payListener) {
		AliTvSdk.payFromServer(title, amount, orderId, notifyUrl, payListener);
	}
	// CP服务器生成订单号后支付（有激励）
	private void doPayFromServer(String title, String amount, String orderId, String notifyUrl,String subjectId, IPayListener payListener) {
		AliTvSdk.payFromServer(title, amount, orderId, notifyUrl, subjectId, payListener);
	}


	private void activity_pay(JSONObject options){

		String partner_order_no = null;
		//String subject_id = null;
		String subject = null;
		String price = null;
		String partner_notify_url = null;
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");

		try {
			//subject_id = options.getString("subject_id");
			subject = options.getString("subject");
			price = options.getString("price");
			partner_notify_url = options.getString("partner_notify_url");
			partner_order_no = options.getString("partner_order_no");
		} catch (Exception e) {
			//PluginResult result = new PluginResult(PluginResult.Status.ERROR, e.getLocalizedMessage());
			// result.setKeepCallback(false); // release status callback in JS side
			// callbackContext.sendPluginResult(result);
		}

		if(partner_order_no.equals("")){
			partner_order_no = sdf.format(dt);
		}

		//doPay(subject, price, mPayListener);
		//doPay(subject, product1.mSubjectId, price, mPayListener);
		doPayFromServer(subject, price, partner_order_no, partner_notify_url, mPayListener);
		//doPayFromServer(subject, price, partner_order_no, partner_notify_url, product3.mSubjectId, mPayListener);

	}

}
