package com.tv.payali;


import android.os.Message;

public class TestToast {

	public static void show(String msgStr) {
        Message msg = MainActivity.handler.obtainMessage(MainActivity.MSG_SHOW_TOAST);
        msg.obj = msgStr;
        msg.sendToTarget();
    }
}
