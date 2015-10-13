var ZebrusheLoader = function (require, exports, module) {
    
    var exec = require('cordova/exec');
    
    var Zebrushe = function () {
	
    };
    
    Zebrushe.prototype.printText = function (macaddress, text, successCallback, errorCallback) {
        
        if (successCallback === null) {
            successCallback = function (response) {
                console.log('Zebrushe.printText sukses: ' + response);
            };
        }
        
        if (errorCallback === null) {
            errorCallback = function (error) {
                console.error('Zebrushe.printText deshtim: ' + error);
            };
        }
        
        if (typeof errorCallback != "function") {
            console.error("Zebrushe.printText failure: parametri deshtimit nuk eshte funksion");
            return;
        }
        
        if (typeof successCallback != "function") {
            console.error("Zebrushe.printText failure: parametri callback i suksesit duhet te jete patjeter funksion");
            return;
        }
        
        exec(
            successCallback,
            errorCallback,
            "Zebrushe",
            "printText",
            [macaddress, text]
        );
    };
    
    module.exports = new Zebrushe();
};

ZebrusheLoader(require, exports, module);
cordova.define("cordova/plugin/Zebrushe", ZebrusheLoader);
