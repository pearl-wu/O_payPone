package com.pay.sdk;

import android.annotation.SuppressLint;
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

	protected static final String LOG_TAG = "Order__pay";
	AliSmartViewFocusFrame smartView;
	private DemoPayListener mPayListener = new DemoPayListener();

	public void initTvSDK() {

		AliTvSdk.logSwitch(true);

		String appkey = TestConstants.APPKEY;
		String appSecret = TestConstants.APP_SECRET;
		
		AliTvSdk.init(cordova.getActivity(), appkey, appSecret, new Listeners.IInitListener() {

			public void onInitFinish() {
				
				Log.d(LOG_TAG, "onInitFinish");
				//Toast.makeText(cordova.getActivity(), "init aliTvSdk ok. get auth = " + AliTvSdk.Account.isAuth(), Toast.LENGTH_LONG).show();
				AliTvSdk.Web.openWebviewOnStart(new Listeners.IWebListener() {

					public void onWebOpen() {
						//TestToast.show("init openWebviewOnStart onWebOpen" );
						Toast.makeText(cordova.getActivity(), "init openWebviewOnStart onWebOpen", Toast.LENGTH_LONG).show();
					}

					public void onWebClose() {
						//TestToast.show("init openWebviewOnStart onWebClose");
						Toast.makeText(cordova.getActivity(), "init openWebviewOnStart onWebClose", Toast.LENGTH_LONG).show();
					}
				}, null);
			}

			public void onInitError(String errorMsg) {
				//TestToast.show(errorMsg);
				Toast.makeText(cordova.getActivity(), errorMsg, Toast.LENGTH_LONG).show();
			}
		}, new TestAccount());

	}
	
	
	

	public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException{

		if(action.equals("Pay")){			
			final JSONObject options = args.getJSONObject(0);
						
			initTvSDK();
			buy(options);

			return true;
			
		}else if(action.equals("Iandroid")){
			
			boolean tr = args.getBoolean(0);
			if(tr){
				//String uuid = AliTvSdk.Account.getUUID();
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
			return true;
			
		}else if(action.equals("Sign")){
    			String mag = RSASign.sign(args.getString(0), Config.getPrikey(), "utf-8" );
    			Resultecho(true, mag, callbackContext);
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
	
	
	private class DemoPayListener implements IPayListener {
		CallbackContext callbackContext;
		@Override
		public void onSuccess(String title, int amount) {
			//TestToast.show(title + " 支付成功");
			Resultecho(true, "PAY success", callbackContext);
			setAnimOn();
		}

		@Override
		public void onCancel(String title, int amount) {
			//TestToast.show(title + " 支付取消");
			Resultecho(true, title + " 支付取消", callbackContext);
			setAnimOn();
		}

		@Override
		public void onError(String title, int amount, String errMsg) {
			//TestToast.show(title + " 支付失败。" + errMsg);
			Resultecho(true, title + " PAY error," + errMsg, callbackContext);
			setAnimOn();
		}
	}
	
	
	
	public void buy(JSONObject options){
		
		String partner_order_no = null;
		//String subject_id = null;
		String subject = null;
		String price = "1";
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
			           /* PluginResult result = new PluginResult(PluginResult.Status.ERROR, e.getLocalizedMessage());
						result.setKeepCallback(false); // release status callback in JS side
						callbackContext.sendPluginResult(result);*/
			    }
				
				
			if(partner_order_no.equals("")){
			   partner_order_no = sdf.format(dt);
		    }
			//doPay(subject, ""+ (price), mPayListener);
			doPayFromServer(subject, price, partner_order_no, partner_notify_url, mPayListener);
				
	}
	
		
		// CP服务器生成订单号后支付（无激励）
		private void doPayFromServer(String title, String amount, String orderId, String notifyUrl, IPayListener payListener) {
			AliTvSdk.payFromServer(title, amount, orderId, notifyUrl, payListener);
		}

		@SuppressLint("HandlerLeak")
		private Handler mHandler = new Handler(){
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
	

}
