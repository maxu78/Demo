$(function(){
    show();
    $("#save").click(executeSave);
    $("#haquery").click(cancelThis);
});

function cancelThis(){
    window.close();
}

function downloadFile(){
    var fileName = "User.xlsx";
    window.open("/oper/downModel?fileName=导入.xlsx");
}

function loading(value){
    $("<div class=\"datagrid-mask\"></div>").css({display:"block",width:"100%",height:$(window).height()}).appendTo("body");
    $("<div class=\"datagrid-mask-msg\"></div>").html("<font size=2>"+value+"</font>").appendTo("body").css({display:"block",left:($(document.body).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2});
}

function unLoading(){
    $("body").children("div.datagrid-mask-msg").remove();
    $("body").children("div.datagrid-mask").remove();
}
function show(){
    $("#filepath").html("");
    document.getElementById("batchimportfile").value="";
    $("#batchTaskDiv").height($(document).height()-350);
    $("#batchTaskDivBody").height($(document).height()-450);
    $("#batchTaskDiv").show("slow");
}

function getFileName(o){
    var pos=o.lastIndexOf("\\");
    return o.substring(pos+1);
}
function executeSave(){
    var obj = $("#BatchTaskTable").find("td");
    if(obj == null || obj == undefined || obj.length == 0){
        $.messager.alert('错误','请选择xls文件!','error');
        return;
    }
    var queryParams = "action=BatchImport";
    $.messager.confirm('提示框', '确定要保存信息吗?',function(r){
        if(r){
            loading("信息保存中,请稍候...");
            var dataType="json";
            $.ajax({
                url:'/oper/batchAdd',
                type:'POST',
                dataType:dataType,
                cache:false,
                async:true,
                success:function(result,status){
                    if(result.status=="ok"){
                        unLoading();
                        $.messager.alert('成功','信息保存成功','info',function(){
                            window.opener.location.reload();
                            window.close();
                        });

                    }else{
                        unLoading();
                        $.messager.alert('错误','信息保存失败!','error');
                    }
                }
            });
        }
    });
}

function importTask(){
    var fileName=getFileName($("#batchimportfile").val());
    var formData = new FormData($("#queryform")[0]);
    if(fileName.endWith(".xls") || fileName.endWith(".xlsx")){
        $.ajax({
            url: "/oper/upload",
            type: "POST",
            data: formData,
            async: false,
            cache: false,
            contentType: false,
            timeout: 600000,
            processData: false, //prevent jQuery from automatically transforming the data into a query string
            success: function (data) {
                $("#BatchTaskTable").find("tbody").empty();
                $(data).each(function(i){
                    var tr='<tr>';
                    tr+='<td width="9%">'+data[i].seq+'</td>';
                    tr+='<td width="9%">'+data[i].username+'</td>';
                    tr+='<td width="9%">'+data[i].description+'</td>';
                    tr+='<td width="9%">'+data[i].status+'</td>';
                    tr+='</tr>';
                    $("#BatchTaskTable").find("tbody").append(tr);
                });
            },
            error: function (data, status, e) {
                unLoading();
                $.messager.alert('错误',e,'error');
            }
        });
    }else{
        $.messager.alert('提示','请选择xls或xlsx文件!','warning');
    }
}

String.prototype.endWith=function(str){
    var reg=new RegExp(str+"$");
    return reg.test(this);
};