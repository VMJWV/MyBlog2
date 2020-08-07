function selectLabel(label) {
    // 判断当前标签有没有被选中
    var status = $(label).attr("status");
    if($("#labelBox").children().length == 5 && status == 0){
        alert("标签不能超过5个");
        $(label).find("input").prop("checked", false);
        return;
    }
    status == 0 ? status = 1 : status = 0;
    if (status) {
        $(label).find("input").prop("checked", true);
        // 获取标签颜色和名字 添加到文章的labelBox上去
        var bg = $(label).attr("label-bg");
        var name = $(label).attr("label-name");
        var id = $(label).attr("label-id");
        var l = '<span onclick="removeLabel(this)" style="background:' + bg + '; margin:5px;" class="label" label-id="' + id + '">' + name +
            '</span>'
        console.log(l);
        $("#labelBox").append(l);
    } else {
        $(label).find("input").prop("checked", false);
        //要删除labelbox的内容 我们凭 label id 删除
        var id = $(label).attr("label-id");
        console.log($("#labelBox").find())
        $("#labelBox").find("[label-id=" + id + "]").remove();
    }
    $(label).attr("status", status);
}

function uploadImage() {
    var formData = new FormData();
    formData.append('editormd-image-file', $('#file')[0].files[0]);
    if (typeof (file) == undefined) {
        alert("你还没选择文件");
        return;
    }
    $.ajax({
        url: '/picture/add',
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        success: function (data) {
            if (data.success == 1) {
                $("#picture").prop("src", data.url);
                $("#pictureUrl").val(data.url);
            } else {
                alert("文件上传失败");
            }
        },
        error: function () {
            alert("文件上传失败");
        }
    })
}

function getLabels(pageNum, pageSize) {
    $.ajax({
        method: "post",
        url: "/label/labels",
        data: {
            "pageNum": pageNum,
            "pageSize": pageSize
        },
        success: function (data) {
            console.log(data);
            var list = data.list;
            var count = 0;
            $("#labels").empty();
            var box = $("#labelBox");
            for (let i of list) {
                var tmp = box.children();
                var checked = '';
                var status = 0;
                for(var j = 0;j != tmp.length; ++j){
                    if(i.id == $(tmp[j]).attr("label-id")){
                        checked = "checked";
                        status = 1;
                        break;
                    }
                }
                var label = '<span onclick="selectLabel(this)" label-bg="' + i.labelColor +
                    '" label-name="' + i.labelName + '" status="' + status +'" label-id="'+ i.id +'" style="margin: 10px">'
                    + '<input type="checkbox" style="margin-right: 4px" ' + checked +'>' + '<span class="label" style="background:' + i.labelColor + '">'
                    + i.labelName + '</span>' + '</span>';
                $("#labels").append(label);
                if (++count == 3) {
                    $("#labels").append("<div style='margin-top: 20px'></div>")
                    count = 0;
                }
            }
            $("#selectLabels").modal("show");
            var pageNums = data.navigatepageNums;
            $("#navUl").empty();
            for (let i of pageNums) {
                $("#navUl").append('<li><a href="javascript:getLabels(' + i + ',6)">' + i + '</a></li>')
            }
        },
        error: function () {
            alert("网络连接失败");
        }
    });
}

function showAddLabel() {
    getLabels(1, 6);
}

function removeLabel(val) {
    $(val).remove();
}

function submitArticle(){
    var labels = $("#labelBox").children();
    var str = "";
    for(var j = 0 ; j != labels.length; ++j){
        str += $(labels[j]).attr("label-id");
        if(j != labels.length){
            str += ",";
        }
    }
    $("#labelStr").val(str);
    $("#articleForm").submit();

}

$(function () {
    var editor = editormd("editor", {
        width: "100%",
        height: "640px",
        // markdown: "xxxx",     // dynamic set Markdown text
        path: "/lib/editormd/lib/", // Autoload modules mode, codemirror, marked... dependents libs path
        imageName: "picture",  //上传图片名称
        imageUpload: true,  //开启图片上传
        imageFormats: ["jpg", "jpeg", "gif", "png", "bmp", "webp"],
        imageUploadURL: '/picture/add',
        onload: function (obj) {
            initPasteDragImg(this); //必须
        }
    });
});
