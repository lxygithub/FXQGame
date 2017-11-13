/**
 * Created by 主人 on 2017/3/25.
 */
var Dialog = function (msg, msgBtn, dismissCallback) {
    var html = '\
        <link href="css/dialog.css" rel="stylesheet" type="text/css" charset ="utf-8">\
        <div id="div_mask"></div>\
        <div id="div_dialog">\
        <div id="div_container">\
            <img id="img_" src="image/dialog.png">\
            <div id="div_msg">\
           ' + msg + '\
            </div>\
        </div>\
        <input id="input_btn" type="button" value=\
        ' + msgBtn + ' \
        >\
        </div>\
    ';

    var body = window.document.getElementsByTagName("body")[0];
    body.insertAdjacentHTML("beforeEnd", html);

    var btn = document.getElementById("input_btn");
    btn.onclick = function () {
        if (dismissCallback != null) {
            dismissCallback();
        }
        document.getElementById("div_mask").style.display = 'none';
        document.getElementById("div_dialog").style.display = 'none';
    };


    this.showDialog = function () {
        document.getElementById("div_mask").style.display = 'block';
        document.getElementById("div_dialog").style.display = 'block';
    };

};

