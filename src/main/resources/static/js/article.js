function commentsSwitch(val){
    var boxId = $(val).attr("boxId");
    $("#box"+boxId).slideToggle();
}


// 阻力，数值越大，滑动越慢
const drag = 4;
const position = 0;
// 滑动到顶部
function scrollToTop(){
    // 距离顶部的距离
    const gap = document.documentElement.scrollTop || document.body.scrollTop;
    if (gap > position) {
        window.requestAnimationFrame(scrollToTop);
        window.scrollTo(0, gap - gap / drag);
    }
};

function reply(val){
    var parentId = $(val).attr("parentId");
    var name = $(val).attr("name");
    console.log(parentId);
    $("#parentId").val(parentId);
    $("#content").prop("placeholder","回复"+name);
    $("#replyName").val(name);
    var href = window.location.href;
    var pos = href.indexOf("#");
    if(pos != -1){
        href = href.substr(0,pos);
    }
    window.location.href = href +  "#content";
}

$(function (){
    console.log(window.location.href);
    var text = window.location.href;
    var qrcode = new QRCode("qrcode", {
        text: window.location.href ,
        width: 120,
        height: 120,
        colorDark : "#000000",
        colorLight : "#ffffff",
        correctLevel : QRCode.CorrectLevel.H
    });
    $("#wechatQR").mouseover(function (){
       $("#article-QRCode").css("display","block");
    });
    $("#wechatQR").mouseout(function (){
        $("#article-QRCode").css("display","none");
    });
    new katelog({
        //解析目录容器的id
        contentEl: 'wrapper',
        //生成目录的容器id
        catelogEl: 'catelogList',
        //目录项的类
        linkClass: 'k-catelog-link',
        //当前选中目录项的类
        linkActiveClass: 'k-catelog-link-active',
        supplyTop: 20,
        selector: ['h1','h2', 'h3','h4'],
        active: function (el) {
            // console.log(el);
        }
    });
    $("#catelog").bind("click",function (){
       $("#catelogList").toggle(300);
    });

    setTimeout(() => {
        var href = window.location.href;
        if (href.search("pageNum") != -1) {
            var pos = href.indexOf("#");
            if (pos != -1) {
                href = href.substr(0, pos);
            }
            window.location.href = href + "#comment-title";
        }
    },700);
});
