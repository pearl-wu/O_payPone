package com.yunos.pay;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aliyun.pay.client.PayClient;
import com.aliyun.pay.client.YunOSPayResult;
import com.aliyun.pay.order.YunOSOrderManager;
import com.yunos.pay.util.Config;

public class YunOSOrder extends CordovaPlugin {
	
	protected static final String LOG_TAG = "YunOSOrder__pay";
	private CallbackContext callbackContext = null;
	
	
	public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException{
		
		if(action.equals("Pay")){			
			final JSONObject options = args.getJSONObject(0);
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
			activity_pay(sdf.format(dt), options);
			
			
			///Log.i(LOG_TAG, "Pay Pay Pay Pay");
			return true;
		}else if(action.equals("IdChange")){
			boolean boo = args.getBoolean(0);
				if(!boo){
					
					Toast.makeText(cordova.getActivity().getApplicationContext(), "---關閉此功能---", 20).show();
					return false;
				}
					
					new Thread() {
						public void run() {
							Intent intent = new Intent();
							intent.setClassName("com.yunos.tv.payment", "com.yunos.tv.payment.TVPayMainActivity");
							intent.putExtra("operate", "alitv.unsign");
			    			cordova.getActivity().startActivity(intent);
			    		}
			    	}.start();
				return true;
		}
	
		return false;
	}
	
	
	/**********************************產生訂單**********************************************/
	public void activity_pay(final String dt, final JSONObject options){	
		
			
			String msg = "false";
			String subject_id = null;
	        String subject = null;
	        String price = null;
	        String partner_notify_url = null;
	        
	        PayClient payer = new PayClient();
			YunOSOrderManager orderManager = new YunOSOrderManager();
			
			
			try {
		           subject_id = options.getString("subject_id");
		           subject = options.getString("subject");
		           price = options.getString("price");
		           partner_notify_url = options.getString("partner_notify_url");
		            
		           Log.d(LOG_TAG, "subject_id: " + subject_id + " , subject:" + subject + " , price:" + price + " , partner_notify_url" + partner_notify_url + " , partner_order_no:" + dt);
		            
		        } catch (Exception e) {
		            PluginResult result = new PluginResult(PluginResult.Status.ERROR, e.getLocalizedMessage());
		            result.setKeepCallback(false); // release status callback in JS side
		            callbackContext.sendPluginResult(result);
		            callbackContext = null;
		        }
			
			
			// TODO Auto-generated method stub
			orderManager.GenerateOrder(Config.getPrikey(), Config.getPartner(), subject_id, subject, price, Config.getPartnerNotifyUrl(partner_notify_url), dt);
			String order = orderManager.getOrder();
			String sign = orderManager.getSign();
			
			
			/***********************************訂單結束*******************************************/
			/*YunOSPayResult payResult = null;
			String errorMsg = "";
			
			Bundle bundle = new Bundle();
			bundle.putString("provider", "alipay");
			try {
				if (payer != null) {
					payResult = payer.YunPay(cordova.getActivity().getBaseContext(), order, sign, bundle);
				}
			} catch (Exception e) {
				errorMsg = e.getMessage() + "----" + e.getLocalizedMessage();
			}
			if (payResult != null) {
				msg = payResult.getPayResult() ? "付款成功" : "付款失敗," + payResult.getPayFeedback();
			} else {
				msg = "支付調啟失敗:" + errorMsg;
			}
			
			Toast.makeText(cordova.getActivity().getApplicationContext(), msg, 20).show();
			return msg;*/
		
			//Toast.makeText(cordova.getActivity().getApplicationContext(), result, 20).show();

	}
	

	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.main, menu);
		return false;
	}

}
