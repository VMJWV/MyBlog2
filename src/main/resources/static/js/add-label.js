function watchLabel() {
    var color = $("#color").val();
    var text = $("#labelName").val();
    $("#previewLabel").css("background", color);
    $("#previewLabel").text(text);
    $("#previewLabel").attr("label-bg", color);
    $("#previewLabel").attr("label-name", text);
}

function updateLabel() {
    var bg = $("#previewLabel").attr("label-bg");
    var name = $("#previewLabel").attr("label-name");
    var id = $("#previewLabel").attr("label-id");
    var pageNum = $("#table").attr("page-num");
    var pageSize = $("#table").attr("page-size");
    var uploadPath = "/label/update";
    $.ajax({
        method: "post",
        url: uploadPath,
        data: {
            "labelName": name,
            "labelColor": bg,
            "pageNum": pageNum,
            "pageSize": pageSize,
            "id": id
        },
        success: function (data) {
            if (data.code == 200) {
                window.location.href = data.message;
            } else {
                alert("更新标签失败")
            }
        },
        error: function () {
            alert("网络连接失败")
        }
    });
}

function uploadLabel() {
    var type = $("#upload").attr("func");
    var bg = $("#previewLabel").attr("label-bg");
    var name = $("#previewLabel").attr("label-name");
    var uploadPath = "/label/add";
    $.ajax({
        method: "post",
        url: uploadPath,
        data: {
            "labelName": name,
            "labelColor": bg,
            "type": type
        },
        success: function (data) {
            if (data.code == 200 && type == 1) {
                window.location.href = data.message;
            } else if (data.code == 200 && type == 2) {
                try {
                    if ($("#labelBox").children().length == 5) {
                        alert("标签不能超过5个");
                        return;
                    }
                    var id = Number(data.message);
                    var l = '<span onclick="removeLabel(this)" style="background:' + bg + '; margin:5px;" class="label" label-id="' + id + '">' + name +
                        '</span>';
                    $("#labelBox").append(l);
                } catch (exception) {
                }
            } else {
                alert("添加标签失败")
            }
        },
        error: function () {
            alert("网络连接失败")
        }
    });
    $("#addLabel").modal('hide');
}

$(function () {
    $('#color').colorpicker({
        allowEmpty: true, //允许为空,显示清楚颜色按钮
        color: "#337AB7", //初始化颜色
        showInput: true, //显示输入
        containerClassName: "full-spectrum",
        showInitial: true, //显示初始颜色,提供现在选择的颜色和初始颜色对比
        showPalette: true, //显示选择器面板
        showSelectionPalette: true, //记住选择过的颜色
        showAlpha: true, //显示透明度选择
        maxPaletteSize: 7, //记住选择过的颜色的最大数量
        preferredFormat: "hex" //输入框颜色格式,(hex十六进制,hex3十六进制可以的话只显示3位,hsl,rgb三原
    });
})