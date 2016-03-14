package com.yunos.pay;


import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.yunos.pay.R;
import com.yunos.pay.util.Config;

public class YunOSOrder extends Activity {

	private Button paybutton;
	private Button unsignButton;
	private EditText orderIdEdit;
	private EditText priceEdit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		orderIdEdit = (EditText) findViewById(R.id.orderid);
		priceEdit = (EditText) findViewById(R.id.price);
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		orderIdEdit.setText(sdf.format(dt));
		unsignButton = (Button) findViewById(R.id.button1);
		unsignButton.setOnClickListener(new OnClickListener() {
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
		});
		paybutton = (Button) findViewById(R.id.paybutton);
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

						/***********************************订单生成*********************************************/
						//注意事项：
						//目前demo方便写代码，所以支付订单生成在客户端中完成，但为了安全保证私钥不泄露及后续能更换私钥。所把支付订单生成在贵司服务端完成

		            	PayClient payer = new PayClient();
						YunOSOrderManager orderManager = new YunOSOrderManager();
						orderManager.GenerateOrder(Config.getPrikey(), Config.getPartner(), "123", "测试应用内支付1", priceEdit.getText().toString(), Config.getPartnerNotifyUrl(), orderIdEdit.getText().toString());
						String order = orderManager.getOrder();
						String sign = orderManager.getSign();

						/***********************************订单生成结束*******************************************/


						YunOSPayResult payResult = null;
						String errorMsg = "";
						String msg = "";
						Bundle bundle = new Bundle();
						bundle.putString("provider", "alipay");
						try {
							if (payer != null) {
								payResult = payer.YunPay(getBaseContext(), order, sign, bundle);
							}
						} catch (Exception e) {
							errorMsg = e.getMessage() + "----" + e.getLocalizedMessage();
						}
						if (payResult != null) {
							msg = payResult.getPayResult() ? "付款成功" : "付款失败," + payResult.getPayFeedback();
						} else {
							msg = "支付调起失败 :" + errorMsg;
						}
		               return msg;
		            }
		            protected void onPostExecute(String result) {
		            	Toast.makeText(getApplicationContext(), result, 20).show();
		            	paybutton.setClickable(true);
		            	Date dt = new Date();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
						orderIdEdit.setText(sdf.format(dt));
		            };
		         }.execute(); 
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
