/**
 * Created by 59432 on 2017/3/23.
 */
//用户登记
var invite_code = "";
function login(name, companyId, phone, checkcode) {
    var yqm = window.location.href;
    invite_code = yqm.split("=")[1];//?yqm=5afu3psbp7
    var re = new RegExp("(^1[0-9]{10}$)");
    if ($.trim(name) == "") {
        alert("请输入姓名！");
        return false;
    }
    if ($.trim(phone) == "") {
        alert("请输入手机号！");
        return false;
    }
    if (!re.test(phone)) {
        alert("手机号不正确！");
        return false;
    }
    if ($.trim(companyId) == "") {
        alert("请输入公司名称！");
        return false;
    }
    if ($.trim(checkcode) == "") {
        alert("请输入验证码！");
        return false;
    }
    if (checkcode.length < 4) {
        alert("验证码输入有误！");
        return false;
    }


    $.ajax({
        crossDomain: true,
        type: "POST",
        dataType: "json",
        // contentType: "application/json",
        before: function () {
            console.log("提交中。。。。。");
        },
        async: true,
        url: Urls.LOGIN,
        data: {cname: name, companyid: companyId, mobile: phone, yzm: checkcode, yqm: invite_code},
        success: function (result, status, xhr) {
            console.log(result);
            var respCode = result.resultCode;
            switch (respCode) {
                case 1://注册成功
                case 6://已注册
                    localStorage.setItem("_mobile", result.mobile);
                    localStorage.setItem("_token", result.token);
                    window.location.href = "test.html";
                    break;
                case 2:
                case 3:
                case 4://失败
                    alert(result.detailMsg);
                    break;
                case 5://已经答过题
                    localStorage.setItem("_mobile", result.mobile);
                    localStorage.setItem("_token", result.token);
                    var dialog = new Dialog("您已参加过本次活动", "查看成绩", function () {
                        createLink();
                    });
                    dialog.showDialog();
                    break;
                default:
                    alert(result.detailMsg);
                    break;
            }
            console.log(result);
        },
        error: function (xhr, status, error) {
            console.log(error);
        },
        complete: function (xhr, status) {

        }
    });

}

//获取验证码
function checkCode(mobile) {
    console.log(mobile);
    $.ajax({
        crossDomain: true,
        type: "POST",
        dataType: "json",
        // contentType: "application/json",
        before: function () {
            console.log("提交中。。。。。");
        },
        async: true,
        url: Urls.GET_CHECK_CODE,
        data: {mobile: mobile},
        success: function (result, status, xhr) {
            var respCode = result.resultCode;
            if (respCode != 0) {
                alert(result.detailMsg);
            }
            console.log(result);
        },
        error: function (xhr, status, error) {
            console.log(error);
        },
        complete: function (xhr, status) {

        }
    });
}

var questionArr;
var index = 0;
var answeredMap = {};
//获取题目
function questions() {
    clearState();
    radioEnable(true);
    var token = localStorage.getItem("_token");
    if (token === null || token === "") {
        window.location.href = "login.html";
        return;
    }
    $.ajax({
        crossDomain: true,
        type: "POST",
        dataType: "json",
        async: true,
        url: Urls.QUESTION,
        // contentType: "application/json",
        data: {token: token},
        before: function () {
            console.log("提交中。。。。。");
        },
        success: function (result, status, xhr) {
            if (result.resultCode === 1) {
                questionArr = result.SelectDetail;
                var question1 = questionArr[index];
                $("#q-title").text(question1.question);
                $("#lab-a").text(question1.A);
                $("#lab-b").text(question1.B);
                $("#lab-c").text(question1.C);
                $("#lab-d").text(question1.D);
                timerStart();
            }
        },
        error: function (xhr, status, error) {
            console.log(error);
        },
        complete: function (xhr, status) {

        }
    });
}

//计时器

function timerStart() {

    var spanRemainTime = $("#span_remain_time");
    spanRemainTime.text(totalTime);
    var spanUseTime = $("#span_use_time");
    spanUseTime.text(useTime);

    var task = setInterval(function () {
        spanRemainTime.text((--totalTime) + "秒");
        spanUseTime.text((++useTime) + "秒");
        if (totalTime === 1) {
            clearInterval(task);
            var dialog = new Dialog("时间到了，只能交卷了！", function () {
                scoreSubmit(useTime, correctAnswerCount);
            });
            dialog.showDialog();

        }
    }, 1000);
}

//下一题和上一题
function next(isNext) {
    if (isNext) {
        if (index < questionArr.length - 1) {
            ++index;
        } else {
            scoreSubmit(useTime, correctAnswerCount)
        }
    } else {
        if (index > 0) {
            --index;
        }
    }
    var question1 = questionArr[index];
    if (String(questionArr[index].id) in answeredMap) {//存在
        var answer = answeredMap[String(questionArr[index].id)];
        radioEnable(false);
        if (answer.result) {//正确
            $("#span-result").text("回答正确");
            $("#img-result").attr("src", "image/icon_right.png");
        } else {//错误
            $("#span-result").text("回答错误，" + " 正确答案：" + answer.answer);
            $("#img-result").attr("src", "image/icon_error.png");
        }
        console.log(JSON.stringify(answer));
        switch (answer.select) {
            case "A":
                $('#radio_a').attr("checked", true);
                break;
            case "B":
                $('#radio_b').attr("checked", true);
                break;
            case "C":
                $('#radio_c').attr("checked", true);
                break;
            case "D":
                $('#radio_d').attr("checked", true);
                break;
        }
    } else {
        clearState();
        radioEnable(true);
    }
    $("#q-title").text(question1.question);
    $("#lab-a").text(question1.A);
    $("#lab-b").text(question1.B);
    $("#lab-c").text(question1.C);
    $("#lab-d").text(question1.D);
}
//答题
var totalTime = 599;
var useTime = 0;
var correctAnswerCount = 0;
var Answer = {
    createNew: function (id, select, answer, result) {
        var a = {};
        a.id = id;
        a.select = select;
        a.answer = answer;
        a.result = result;
        return a;
    }
};
function selectAnswer(select) {

    var token = localStorage.getItem("_token");
    if (token === null || token === "") {
        window.location.href = "login.html";
        return;
    }
    $.ajax({
        crossDomain: true,
        type: "POST",
        dataType: "json",
        async: true,
        url: Urls.CHECK_ANSWER,
        // contentType: "application/json",
        data: {tid: questionArr[index].id, answer: select, token: token},
        before: function () {
            console.log("提交中。。。。。");
        },
        success: function (result, status, xhr) {

            if (result.resultCode === 1) {//正确
                $("#span-result").text(result.detailMsg);
                $("#img-result").attr("src", "image/icon_right.png");
                answeredMap[String(questionArr[index].id)] = Answer.createNew(String(questionArr[index].id), select, select, true);
                correctAnswerCount++;
            } else if (result.resultCode === 2) {//错误
                $("#span-result").text(result.detailMsg + " 正确答案：" + result.SelectDetail);
                $("#img-result").attr("src", "image/icon_error.png");
                answeredMap[String(questionArr[index].id)] = Answer.createNew(String(questionArr[index].id), select, result.SelectDetail, false);
            } else {
                $("#img-result").attr("src", "image/touming.png");
            }

        },
        error: function (xhr, status, error) {
            console.log(error);
        },
        complete: function (xhr, status) {

        }
    });
    radioEnable(false);
}
//查询个人成绩
function testResult() {
    var mobile = localStorage.getItem("_mobile");
    var token = localStorage.getItem("_token");
    if (mobile === null || mobile === "" || token === null || token === "") {
        var yqm = window.location.search;//获取url中"?"符后的字串
        //invite_code = yqm.split("=")[1];
        if(yqm.indexOf("&fr")>-1){
        invite_code = yqm.split("&fr")[0];//浏览器里自动在邀请码后加上了&from=
        }
        else{
        	invite_code = yqm;
        }
        window.location.href = "index.html?yqm=" + invite_code;
        return;
    }
    $.ajax({
        crossDomain: true,
        type: "POST",
        dataType: "json",
        async: true,
        url: Urls.TEST_RESULT,
        // contentType: "application/json",
        data: {mobile: mobile, token: token},
        before: function () {
            console.log("提交中。。。。。");
        },
        success: function (result, status, xhr) {
            if (result.resultCode === 1) {
                $("#span_win_percent").text(result.SelectDetail.beyond);
                $("#span_correct").text(result.SelectDetail.truecount);
                $("#span_time").text(result.SelectDetail.haoshi);
                $("#span_point").text(result.SelectDetail.tgf);
            }
            console.log(result);
        },
        error: function (xhr, status, error) {
            console.log(error);
            var yqm = window.location.href;
            invite_code = yqm.split("=")[1];
            window.location.href = "index.html?yqm=" + invite_code;
        },
        complete: function (xhr, status) {

        }
    });
    radioEnable(false);
}
//清除选中状态
function clearState() {
    $("#span-result").text("");
    $("#img-result").attr("src", "image/touming.png");
    $('#radio_a').removeAttr("checked");
    $('#radio_b').removeAttr("checked");
    $('#radio_c').removeAttr("checked");
    $('#radio_d').removeAttr("checked");
}
//设置答案是否可修改
function radioEnable(enable) {
    if (enable) {
        $('#radio_a').removeAttr("disabled");
        $('#radio_b').removeAttr("disabled");
        $('#radio_c').removeAttr("disabled");
        $('#radio_d').removeAttr("disabled");
    } else {
        $('#radio_a').attr("disabled", "disabled");
        $('#radio_b').attr("disabled", "disabled");
        $('#radio_c').attr("disabled", "disabled");
        $('#radio_d').attr("disabled", "disabled");
    }

}
//提交成绩
function scoreSubmit(haoshi, trueCount) {
    var mobile = localStorage.getItem("_mobile");
    var token = localStorage.getItem("_token");
    if (mobile === null || mobile === "" || token === null || token === "") {
        window.location.href = "login.html";
        return;
    }
    $.ajax({
        crossDomain: true,
        type: "POST",
        dataType: "json",
        async: true,
        url: Urls.SCORE_SUBMIT,
        // contentType: "application/json",
        data: {mobile: mobile, haoshi: haoshi, truecount: trueCount, token: token},
        before: function () {
            console.log("提交中。。。。。");
        },
        success: function (result, status, xhr) {
            console.log(mobile, haoshi, trueCount, token, result);
            if (result.resultCode === 1) {
                createLink();
            }
        },
        error: function (xhr, status, error) {
            console.log(error);
        },
        complete: function (xhr, status) {

        }
    });
}
function createLink() {
    var mobile = localStorage.getItem("_mobile");
    var token = localStorage.getItem("_token");
    if (mobile === null || mobile === "" || token === null || token === "") {
        window.location.href = "login.html";
        return;
    }
    $.ajax({
        crossDomain: true,
        type: "POST",
        dataType: "json",
        data: {mobile: mobile, token: token},
        async: true,
        url: Urls.INVITE_CODE,
        // contentType: "application/json",
        before: function () {
            console.log("提交中。。。。。");
        },
        success: function (result, status, xhr) {
            if (result.resultCode === 1) {
                var invite_code = result.SelectDetail;
                $("#h_link").text("邀请链接：" + Urls.INDEX + ".jsp?yqm=" + invite_code);
                window.location.href = "result.html?yqm=" + invite_code;
            }
        },
        error: function (xhr, status, error) {
            console.log(error);
        },
        complete: function (xhr, status) {

        }
    });
}

//查看榜单
function ranking() {
    $.ajax({
        crossDomain: true,
        type: "POST",
        dataType: "json",
        async: true,
        url: Urls.RANKING_PHONE,
        // contentType: "application/json",
        before: function () {
            console.log("提交中。。。。。");
        },
        success: function (result, status, xhr) {
            if (result.resultCode === 1) {
                addRow(result.SelectDetail);
            }
        },
        error: function (xhr, status, error) {
            console.log(error);
        },
        complete: function (xhr, status) {

        }
    });
}
//动态生成榜单
function addRow(rankingArr) {
    var totalPlay = rankingArr.length;
    var tableRoot = document.getElementById("ranking-table");
    for (var i = 0; i < totalPlay; i++) {
        var html = "\
        <tr align='center' style='border: 1px;background: white;'>\
        <td style='width:50%; color: #0165a1;' align='center'>\
        " + rankingArr[i].name + "\
        </td>\
        <td style='width:25%; color: #0165a1;' align='center'>\
        " + "<canvas style='width: 100%;height: 50px;'></canvas>" + "\
        </td>\
        <td style='width:25%; color: #0165a1;padding: 10px;' align='center'>\
        " + rankingArr[i].gfrs + "\
        人</th>\
        </tr>\
        ";
        tableRoot.insertAdjacentHTML("beforeEnd", html);
        var canvasArray = document.getElementsByTagName("canvas");
        for (var j = 0; j < canvasArray.length; j++) {
            var canvasId = "canvas_".concat(String(j));
            canvasArray[j].setAttribute("id", canvasId);
            drawLine(canvasId, rankingArr[j].dtrs);
        }
    }
//绘制榜单百分比
    function drawLine(canvasId, percentLength) {
        var canvas = document.getElementById(canvasId);
        var totalLength = 250;
        var lineWidth = 30;
        var starX = 20;
        var end = 80;
        if (canvas === null) {
            return false;
        }
        var ctx = canvas.getContext("2d");
        ctx.beginPath();
        //设置线条颜色为蓝色
        ctx.strokeStyle = "#cccccc";
        ctx.lineWidth = lineWidth;
//        ctx.lineCap = "round";
        //设置路径起点坐标
        ctx.moveTo(starX, end);
        //定义中间点坐标1
        ctx.lineTo(totalLength + starX, end);
        ctx.stroke();
        //关闭绘制路径
        ctx.closePath();

        ctx.beginPath();
        //设置线条颜色为蓝色
        ctx.strokeStyle = "#0165a1";
        //设置路径起点坐标
        ctx.moveTo(starX, end);
        //定义中间点坐标1
        ctx.lineTo(starX + totalLength * parseFloat(percentLength / 100), end);
        ctx.stroke();
        //关闭绘制路径
        ctx.closePath();

    }


}


