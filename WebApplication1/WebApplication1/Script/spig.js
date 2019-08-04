//右键菜单
jQuery(document).ready(function ($) {
    $("#spig").mousedown(function (e) {
        if(e.which==3){
        showMessage(right_click, 10000);
}
});
$("#spig").bind("contextmenu", function(e) {
    return false;
});
});

//鼠标在消息上时
jQuery(document).ready(function ($) {
    $("#message").hover(function () {
       $("#message").fadeTo("100", 1);
     });
});


//鼠标在上方时
jQuery(document).ready(function ($) {
    $(".mumu").mouseover(function () {
       $(".mumu").fadeTo("300", 0.3);
       //msgs = ["我隐身了，你看不到我", "我会隐身哦！嘿嘿！", "别动手动脚的，把手拿开！", "把手拿开我才出来！"];
       msgs = over_array;
	   var i = Math.floor(Math.random() * msgs.length);
        showMessage(msgs[i]);
    });
    $(".mumu").mouseout(function () {
        $(".mumu").fadeTo("300", 1)
    });
});

//开始
jQuery(document).ready(function ($) {
        var now = (new Date()).getHours();
        if (now > 0 && now <= 5) {
            showMessage(visitor + ' 夜猫还不睡觉，明天起的来么你？', 6000);
        } else if (now > 5 && now <= 6) {
            showMessage(visitor + ' 新的一天开始了哦!', 6000);
        } else if (now > 6 && now <= 10) {
            showMessage(visitor + ' 早上好，你是鸟儿还是虫儿？嘻嘻！', 6000);
        } else if (now > 10 && now <= 13) {
            showMessage(visitor + ' 中午了，吃饭了么？不要饿着唔！', 6000);
        } else if (now > 13 && now <= 16) {
            showMessage(visitor + ' 下午的时光真难熬！还好有你在！', 6000);
        } else if (now > 16 && now <= 17) {
            showMessage(visitor + ' 终于等到你了！', 6000);
        } else if (now > 21 && now <= 23) {
            showMessage(visitor + ' 夜深了，还不睡嘛？', 6000);
        } else {
            showMessage(visitor + ' 快来逗我玩吧！', 6000);
        }
    $(".spig").animate({
        top: $(".spig").offset().top + 300,
        left: document.body.offsetWidth - 160
    },
	{
	    queue: false,
	    duration: 1000
	});
});

//鼠标在某些元素上方时
/*jQuery(document).ready(function ($) {
    $('h2 a').click(function () {//标题被点击时
        showMessage('正在用吃奶的劲加载《<span style="color:#0099cc;">' + $(this).text() + '</span>》请稍候');
    });
	
	//移动到新发表的标题链接时
    $('.fl_by div a.xi2').mouseover(function () {
        showMessage('要看看《<span style="color:#0099cc;">' + $(this).text() + '</span>》这篇主题么？');
    });
	
	//移动到评论框时
    $('#fastpostmessage').mouseover(function(){
        showMessage('说点什么吧！');
    });
	
	//移动到发表新主题的按钮时
    $('#newspecial').mouseover(function(){
        showMessage('发吧，发吧，发吧！');
    });
});*/


//无聊讲点什么
jQuery(document).ready(function ($) {

    window.setInterval(function () {
       // msgs = ["陪我聊天吧！", "好无聊哦，你都不陪我玩！", "…@……!………", "^%#&*!@*(&#)(!)(", "我可爱吧！嘻嘻!~^_^!~~","谁淫荡呀?~谁淫荡?，你淫荡呀!~~你淫荡！~~","从前有座山，山上有座庙，庙里有个老和尚给小和尚讲故事，讲：“从前有座……”"];
        msgs = bored_array;
		var i = Math.floor(Math.random() * msgs.length);
        showMessage(msgs[i], 10000);
    }, 35000);
});

//无聊动动
jQuery(document).ready(function ($) {
    window.setInterval(function () {
        //msgs = ["乾坤大挪移！", "我飘过来了！~", "我飘过去了", "我得意地飘！~飘！~"];
        msgs = move_array;
        //判断是否开启左右移动
        if (onoff_move!=0){
    		var i = Math.floor(Math.random() * msgs.length);
            s = [0.1, 0.2, 0.3, 0.4, 0.5, 0.6,0.7,0.75,-0.1, -0.2, -0.3, -0.4, -0.5, -0.6,-0.7,-0.75];
            var i1 = Math.floor(Math.random() * s.length);
            var i2 = Math.floor(Math.random() * s.length);
                $(".spig").animate({
                left: document.body.offsetWidth/2*(1+s[i1]),
                top:  document.body.offsetHeight/2*(1+s[i1])
            },
    			{
    			    duration: 2000,
    			    complete: showMessage(msgs[i])
    			});
        }
    }, 45000);
});

//搜索的时候
/*jQuery(document).ready(function ($) {
    $("#scbar_txt").click(function () {
        showMessage('搜你想要的哦！');
        $(".spig").animate({
            top: $("#author").offset().top - 70,
            left: $("#author").offset().left - 170
        },
		{
		    queue: false,
		    duration: 1000
		});
    });
});*/

var spig_top = 50;
//滚动条移动
jQuery(document).ready(function ($) {
    var f = $(".spig").offset().top;
    $(window).scroll(function () {
        $(".spig").animate({
            top: $(window).scrollTop() + f +300
        },
		{
		    queue: false,
		    duration: 1000
		});
    });
});

//鼠标点击时
jQuery(document).ready(function ($) {
    var stat_click = 0;
    $(".mumu").click(function () {
        if (!ismove) {
            stat_click++;
            if (stat_click > 4) {
                msgs = ["你有完没完呀？", "你已经摸我" + stat_click + "次了", "非礼呀！救命!"];
                var i = Math.floor(Math.random() * msgs.length);
                //showMessage(msgs[i]);
            } else {
                msgs = touch_array;
				var i = Math.floor(Math.random() * msgs.length);
                //showMessage(msgs[i]);
            }
            //判断是否开启左右移动
            if (onoff_move!=0){
                s = [0.1, 0.2, 0.3, 0.4, 0.5, 0.6,0.7,0.75,-0.1, -0.2, -0.3, -0.4, -0.5, -0.6,-0.7,-0.75];
                var i1 = Math.floor(Math.random() * s.length);
                var i2 = Math.floor(Math.random() * s.length);
                    $(".spig").animate({
                    left: document.body.offsetWidth/2*(1+s[i1]),
                    top:  document.body.offsetHeight/2*(1+s[i1])
                    },
        			{
        			    duration: 500,
        			    complete: showMessage(msgs[i])
        			});
            }
        } else {
            ismove = false;
        }
    });
});
//显示消息函数 
function showMessage(a, b) {
    if (b == null) b = 10000;
    jQuery("#message").hide().stop();
    jQuery("#message").html(a);
    jQuery("#message").fadeIn();
    jQuery("#message").fadeTo("1", 1);
    jQuery("#message").fadeOut(b);
};

//拖动
var _move = false;
var ismove = false; //移动标记
var _x, _y; //鼠标离控件左上角的相对位置
jQuery(document).ready(function ($) {
    $("#spig").mousedown(function (e) {
        _move = true;
        _x = e.pageX - parseInt($("#spig").css("left"));
        _y = e.pageY - parseInt($("#spig").css("top"));
     });
    $(document).mousemove(function (e) {
        if (_move) {
            var x = e.pageX - _x; 
            var y = e.pageY - _y;
            var wx = $(window).width() - $('#spig').width();
            var dy = $(document).height() - $('#spig').height();
            if(x >= 0 && x <= wx && y > 0 && y <= dy) {
                $("#spig").css({
                    top: y,
                    left: x
                }); //控件新位置
            ismove = true;
            }
        }
    }).mouseup(function () {
        _move = false;
    });
});
