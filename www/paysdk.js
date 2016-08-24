var exec = require("cordova/exec");

module.exports = {

    ENTER_OPTIONS: {
	partner_order_no: "2016032100",
        subject_id: "baistv01",
        subject: "百世天猫测试TV",
        price: "1", 
        partner_notify_url: "http://paydemo.yundev.cn/index.php"
    },

    pay: function (options, successCallback, errorCallback) {
        options = this.merge(this.ENTER_OPTIONS, options);
        cordova.exec(successCallback, errorCallback, "paysdk", "Pay", [options]);
    },
	
    iandroid: function (mag, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "paysdk", "Iandroid", [mag]);
    },
    
    packageinfo: function (mag, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "paysdk", "Packageinfo", [mag]);
    },
    
    sign: function (mag, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "paysdk", "Sign", [mag]);
    },
    
    echo: function (mag, duration, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "paysdk", "Echo", [mag,duration]);
    },
    
    merge: function () {
        var obj = {};
        Array.prototype.slice.call(arguments).forEach(function(source) {
            for (var prop in source) {
                obj[prop] = source[prop];
            }
        });
        return obj;
    }
};
