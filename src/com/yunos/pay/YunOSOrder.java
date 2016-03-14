package com.yunos.pay;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

	private Button paybutton;
	private Button unsignButton;
	private EditText orderIdEdit;
	private EditText priceEdit;
	
	
	
	
	public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException{
		
		if(action.equals("pay")){
			//setContentView(R.layout.activity_main);
			//orderIdEdit = (EditText) findViewById(R.id.orderid);
			//priceEdit = (EditText) findViewById(R.id.price);
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
			orderIdEdit.setText(sdf.format(dt));

			//unsignButton = (Button) findViewById(R.id.button1);
			/*unsignButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new Thread() {
						public void run() {
							Intent intent = new Intent();
						    intent.setClassName("com.yunos.tv.payment", "com.yunos.tv.payment.TVPayMainActivity");
						    intent.putExtra("operate", "alitv.unsign");
	    					startActivity(intent);
	    				}
	    			}.start();
	    		}
			});*/
			
			
			//paybutton = (Button) findViewById(R.id.paybutton);
			paybutton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new AsyncTask<Integer, Object, String>() {
			            @Override
			            protected void onPreExecute() {
			            	paybutton.setClickable(false);
			            	super.onPreExecute();  
			            }
			            @Override
			            protected String doInBackground(Integer... params) {

							/**********************************產生訂單**********************************************/


			            	PayClient payer = new PayClient();
							YunOSOrderManager orderManager = new YunOSOrderManager();
							orderManager.GenerateOrder(Config.getPrikey(), Config.getPartner(), "123", "test pay", priceEdit.getText().toString(), Config.getPartnerNotifyUrl(), orderIdEdit.getText().toString());
							String order = orderManager.getOrder();
							String sign = orderManager.getSign();

							/***********************************訂單結束*******************************************/


							YunOSPayResult payResult = null;
							String errorMsg = "";
							String msg = "";
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
			               return msg;
			            }
			            protected void onPostExecute(String result) {
			            	Toast.makeText(cordova.getActivity().getApplicationContext(), result, 20).show();
			            	paybutton.setClickable(true);
			            	Date dt = new Date();
							SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
							orderIdEdit.setText(sdf.format(dt));
			            };
			         }.execute(); 
				}
			});
			
			
			
			return true;
		}
	
		return false;
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.main, menu);
		return false;
	}
}
