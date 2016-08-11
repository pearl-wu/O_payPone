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

}
