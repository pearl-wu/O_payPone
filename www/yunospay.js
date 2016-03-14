var exec = require("cordova/exec");

module.exports = {

    ENTER_OPTIONS: {
        subject_id: 123456789,
        subject: "test",
        price: 100,     //以,分為單位
        partner_notify_url: "http://www.bais.com.tw"
    },

    pay: function (options, successCallback, errorCallback) {
        options = this.merge(this.ENTER_OPTIONS, options);
          exec(successCallback, errorCallback, "YunOSOrder", "Pay", [options]);
    },
	
    idchange: function (mag, successCallback, errorCallback) {
        options = this.merge(this.ENTER_OPTIONS, options);
          exec(successCallback, errorCallback, "YunOSOrder", "IdChange", [mag]);
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
