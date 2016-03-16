var exec = require("cordova/exec");

module.exports = {

    ENTER_OPTIONS: {
	partner_order_no: "",
        subject_id: "123456789",
        subject: "百世天貓測試TV",
        price: "1", 
        partner_notify_url: "http://kyytv.ebais.com.tw/yunospay.php"
    },

    pay: function (options, successCallback, errorCallback) {
        options = this.merge(this.ENTER_OPTIONS, options);
        cordova.exec(successCallback, errorCallback, "YunOSOrder", "Pay", [options]);
    },
	
    change: function (mag, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "YunOSOrder", "Change", [mag]);
    },
	
    iandroid: function (mag, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "YunOSOrder", "Iandroid", [mag]);
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
