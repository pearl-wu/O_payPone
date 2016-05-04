package com.yunos.paydemo.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;

import org.apache.cordova.*;
import org.json.JSONException;
import org.json.JSONObject;

import com.aliyun.pay.client.PayClient;
import com.aliyun.pay.client.YunOSPayResult;
import com.aliyun.pay.order.YunOSOrderManager;

import com.yunos.paydemo.util.Config;

public class MainActivity extends CordovaPlugin {
    	
    	  protected static final String LOG_TAG = "YunOSOrder__pay";
    		
    	  public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException{
    			
    		if(action.equals("Pay")){			
    		   final JSONObject options = args.getJSONObject(0);			
    		   //activity_pay(options, callbackContext);
    		   activity_pay pay = new activity_pay(options);// this is the line 36
    	       pay.execute();
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
    		}
    	     return false;
    	  
    	  }

    		public void Resultecho(Boolean boo, String meg, CallbackContext callbackContext){
    		   if(boo){
    			//Toast.makeText(cordova.getActivity(), meg, Toast.LENGTH_SHORT).show();
    	            	callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, meg));
    		   } else {
    		        //Toast.makeText(cordova.getActivity(), meg, Toast.LENGTH_LONG).show();
    		        callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, meg));
    		   }			
    		}	
    		
    		
    		class activity_pay extends AsyncTask<Void, Void, String>{
    			JSONObject options;
    			public activity_pay(JSONObject options) {
    				this.options = options;
    	        	}

    			public String doInBackground(Void... params) {
    			String partner_order_no = null;
    			String subject_id = null;
    			String subject = null;
    			String price = null;
    			String partner_notify_url = null;
				Date dt = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
    			        try {
    				           subject_id = options.getString("subject_id");
    				           subject = options.getString("subject");
    				           price = options.getString("price");
    				           partner_notify_url = options.getString("partner_notify_url");
    				           partner_order_no = options.getString("partner_order_no");
    				    } catch (Exception e) {
    				    	return null;
    				           /* PluginResult result = new PluginResult(PluginResult.Status.ERROR, e.getLocalizedMessage());
    				            result.setKeepCallback(false); // release status callback in JS side
    				            callbackContext.sendPluginResult(result);*/
    				    }
    					
    					
    				if(partner_order_no.equals("")){

    				   partner_order_no = sdf.format(dt);
    			        }
    					
    				/***********************************CREATE**********************************************/
    				
    				PayClient payer = new PayClient();
    				YunOSOrderManager orderManager = new YunOSOrderManager();
    				orderManager.GenerateOrder(Config.getPrikey(), Config.getPartner(), subject_id, subject, price, Config.getPartnerNotifyUrl(), partner_order_no);
    				//orderManager.GenerateOrder(Config.getPrikey(), Config.getPartner(), "123", "test1", "1", "http://paydemo.yundev.cn/index.php", "123456");
    				String order = orderManager.getOrder();
    				String sign = orderManager.getSign();

    					
    				/***********************************END*******************************************/
    					YunOSPayResult payResult = null;
    					String errorMsg = "";
    					String msg = "";
    					Bundle bundle = new Bundle();
    					bundle.putString("provider", "alipay");
    					try {
    					   if (payer != null) {
    						 payResult = payer.YunPay(cordova.getActivity().getApplication(), order, sign, bundle);
    					   }
    					} catch (Exception e) {
    					   errorMsg = e.getMessage() + "----" + e.getLocalizedMessage();
    					}
    					if (payResult != null) {
    					   msg = payResult.getPayResult() ? "PAY success " : "PAY error," + payResult.getPayFeedback();
    					} else {
    					   msg = "(YunPay)ERROR:" + errorMsg;
    					}
    				
    				return msg;
    			}
    		}
    }
