package com.tv.payali;

import com.de.aligame.api.AliTvSdk;
import com.de.aligame.core.api.AliBaseError;
import com.de.aligame.core.api.Listeners.IAuthListener;
import com.de.aligame.core.api.Listeners.IGetUserinfoListener;
import com.de.aligame.core.api.Listeners.UserInfo;
import com.tv.payali.TestToast;
import android.util.Log;

public class TestAccount implements IAuthListener {

    private static String TAG = TestAccount.class.getSimpleName();

    /**
     * 检查是否已经登录，不会弹出登录框
     */
    public static void checkAuthWithoutLogin() {
        boolean isAuth = AliTvSdk.Account.isAuth();
        if (isAuth) {
            TestToast.show("用户已授权");
        } else {
            TestToast.show("用户未授权");
        }
    }

    /**
     * 登录授权。如果没有登录，会弹出扫码登录框
     */
    public static void checkAuthAndLogin() {
        boolean isAuth = AliTvSdk.Account.checkAuthAndLogin();
        if (isAuth) {
            TestToast.show("用户已授权");
        } else {
            TestToast.show("用户未授权，请扫码登录");
        }
    }

    public static void getUserInfo() {
        AliTvSdk.Account.getUserInfo(new IGetUserinfoListener() {

            @Override
            public void onSuccess(UserInfo userInfo) {
                Log.d(TAG, "userinfo nick:" + userInfo.getUserNick() + ", id:" + userInfo.getUserId()
                		+ " headUrl:"+userInfo.getAvatarUrl());
                TestToast.show("nick:" + userInfo.getUserNick() + ", id:" + userInfo.getUserId()
                		+ " headUrl:"+userInfo.getAvatarUrl());
            }

            @Override
            public void onError(int errCode) {
                Log.d(TAG, "get userinfo error:" + errCode + " " + AliBaseError.getErrMsg(errCode));
                TestToast.show(AliBaseError.getErrMsg(errCode) + "=====");
            }
        });
    }

    public static void changeAccount() {
        AliTvSdk.Account.changeAccount();
    }
    
    @Override
    public void onAuthSucess(int from) {
        Log.d(TAG, "onAuthSucess. from=" + from);
        TestToast.show("授权成功===" + from);
    }

    @Override
    public void onAuthError(int errCode, String errMsg) {
        Log.d(TAG, "onAuthError:" + errCode + " errMsg:" + errMsg);
        if (errCode == AliBaseError.INT_ERROR_AUTH_OTHER) {
        }
        TestToast.show(errMsg);
    }

    @Override
    public void onLogout() {
        TestToast.show("用户退出");
    }

    @Override
    public void onAuthCancel() {
        TestToast.show("取消授权");
    }
}
