package com.yunos.pay.util;



public class Config {

	/**
	 * 商户ID   1322 线上
	 */
	private static String partner          = "hzbashiqunxian01@163.com";
	/**
	 * 支付完后云OS支付后端用来通过商户支付结果的url
	 */
	private static String partnerNotifyUrl = "http://paydemo.yundev.cn/index.php";
	/**
	 * 商户密钥字符串
	 */
	private static String prikey           = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAMBn3rm3vCARqsiEbJRkQM8jCKGw1Nj2amxIX8Q7+/u6e0cnaqmt/jj2YmqQLg/Fo2crEhEUWWfKxhaNAQG7lx/db0E1lLWyIDiFQ9+SEIJfFvtPxJT3sqRdKds65KGagMRx7tEE5lIMLybW/jPuW9png/cKS1BW8SoFz1e3o1YzAgMBAAECgYB937ZTc8OMi2Jael/6mLfZi4brGR+2rUckfhjA7alFn1NlUoEybtUBeRcbtiWcReVH6mKQb+qpv3vmZusAsjQEDVLmHnSbAR+cBQYQTwx1MkgyA6pIylCWvQgo/9+AuVsQkIYQ0GEKaGtn+Jqw/XOCLoUExS0ypJzBZHDC+8XkeQJBAPnIZE8zbb9FCxMxoIsWIl0q3f1MBI6tzFSsyi94FnoCLKlXE3HOP06eQz3AlFmeIZ5SAyyK5I5rW5cv6vvaW78CQQDFMd+byzmYqzmM+bQ7+gVzAf70jDq2jE9i+7hPywvjmV9kMfcnnqOxJEn7Cgmlcc4BkhZjc3U5bdJLSU+IbLKNAkB+afzaWJzigH7qZ+NogPtDS76twxtLWopfEFQDUjON7CvOSFN+9XL0xwQg7KYn9O/uwHL6yBCEQ7FHAulaLVjhAkBgbGi414DMZD1578fv4+uyNvGiRseW57kR36BadCDWI/L/HWeLWgkU33fFZz0cRy/CmNqVuqlBca/43FoljXEpAkAncMsfecfzAFlN400rg9jGI0kdHgy5e52eKDCb7i8MIbvA+FvZz9H4TPGPV9kvmCM4/QrlLWdsjy/b4FmJ2ETP";
	
	
	public static String getPartner() {
		return partner;
	}

	public static String getPartnerNotifyUrl(String url) {
	
		partnerNotifyUrl = url.isEmpty() ? url : partnerNotifyUrl;		
	
		return partnerNotifyUrl;
	}

	public static String getPrikey() {
		return prikey;
	}
}
