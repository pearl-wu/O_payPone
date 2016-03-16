var exec = require("cordova/exec");

module.exports = {

    ENTER_OPTIONS: {
	partner_order_no: "",
        subject_id: "123456789",
        subject: "test",
        price: "100", 
        partner_notify_url: "http://www.bais.com.tw"
    },

    pay: function (options, successCallback, errorCallback) {
        options = this.merge(this.ENTER_OPTIONS, options);
          exec(successCallback, errorCallback, "YunOSOrder", "Pay", [options]);
    },
	
    change: function (mag, successCallback, errorCallback) {
          exec(successCallback, errorCallback, "YunOSOrder", "Change", [mag]);
    },
	
    iandroid: function (mag, successCallback, errorCallback) {
          exec(successCallback, errorCallback, "YunOSOrder", "Iandroid", [mag]);
    }
	
};
