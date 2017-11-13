var querySize=0;
var appendTimes=0;
var delFlag = "";
var okFlag = "";
$(function(){
//	setReSise();
    $("#querybtn").click(query);
    $("#importbtn").click(add);
	$("#delbtn").click(del);
	$("#addbtn").click(newElement);
    $("#exportbtn").click(exportExcel);
    query();
});

//监听回车
$(document).keypress(function(e){
    if(e.which==13){
        query();
    }
});

function query(){
    query1();
}
//type为1
function query1(){
    var defaultpageSize;
    if(querySize>0){
        var pageopt = $('#resultTable').datagrid('getPager').data("pagination").options;
        defaultpageSize=pageopt.pageSize;
    }else{
        defaultpageSize=15;
    }
    querySize++;
    var userName = $("#userName").val();
    var description = $("#description").val();
    $("#resultTable").datagrid({
        url : "/oper/query/?username="+userName+"&description="+description+"&rn="+Math.random(),
        fitColumns : true,//列宽度自适应table宽度
        remoteSort : false,//禁止远程排序
//		rownumbers : true,//显示行号
//		singleSelect : true,//只选择一行
        pagination : true,//分页
        pageSize : defaultpageSize,//每页显示数（在pagination中设置不管用）
        pageList : [15, 30, 50 ],//可以设置每页记录条数的列表
        nowrap : false,//折行显示
        border : false,
//		height : $("#result").height(),
        columns : [ [{
                field : 'checkboxid',
                checkbox: true
             },
            {
                field : 'id',
                title : 'id',
                align : 'center',
                width : '25%'
            },{
                field : 'username',
                title : '用户名',
                align : 'center',
                width : '25%'
            },{
                field : 'description',
                title : '详细',
                width : '25%',
                align : 'center',
            },{
                field : 'oper',
                title : '操作',
                width : '24%',
                align : 'center',
                formatter : function(value, row, index) {
                    return "<a href='javascript:void(0);' style='TEXT-DECORATION:none;' onclick='update(\""+row.id+"\", \""+row.username+"\", \""+row.description+"\");'>修改</a>";
                }
            }
        ] ],
        singleSelect: false,
        selectOnCheck: false,
        checkOnSelect: false,
        onLoadSuccess:function(data){
//			var h = $($(this)[0]).parent().height();
//			if(h>500){
//				h = 500;
//			}
//			$($(this)[0]).parent().css("height", h);
        },
        loadMsg:'正在加载中...',
        onClickRow: function (rowIndex, rowData) {
            $(this).datagrid('unselectRow', rowIndex);
        },
        loadFilter: function(data){
            console.log(data);
            var value = {
                total:data.total,
                rows:data.list,
            };
            console.log(value);
            return value;
        }
    });
    //设置分页控件
    var p = $('#resultTable').datagrid('getPager');
    $(p).pagination({
        beforePageText: '第',//页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录&nbsp;&nbsp;&nbsp;&nbsp;'
    });
}

function exportExcel(){
    var tac = $("#tac").val();
    var nodename = $("#nodename").val();
    var staticpara1 = $("#staticpara1").val();
    var typeID = $("#typeID").val();
    var paras = "&typeID="+typeID+"&nodename="+nodename+"&staticpara1="+staticpara1+"&tac="+tac;
    $.ajax({
        type: "POST",
        url: "CustomerDataBaseExec.jsp?action=ExportExcel"+paras,
        dataType: "text",
        success: function(data){
            window.open("exportExcel.jsp?path="+data.trim(),"exportwin");
        }
    });
}

function update(id, username, description){

    update1(id, username, description);

}

//type为1
function update1(id, username, description){
    $('#dlgUpdate').form('load',{
        username: username,
        description: description
    });
    $('#dlgUpdate').dialog('open').dialog('center').dialog('setTitle', '修改');
    $("#id").val(id);
}


function saveUpdate(){
    $('#updateFm').form('submit', {
        url: "/oper/update",
        onSubmit: function(){
            // do some check
            // return false to prevent submit;

        },
        success:function(data){
            var res = eval('(' + data + ')');
//	    	console.log(res);
            var status = res.status;
            if(status.trim() == "ok"){
                alert("修改成功");
//				window.location.reload();
                confirmChange("mod");
            }
        }
    });

}

function check(typeID){
    if(typeID == 1){
        var nodename = $("#updateFm input[name=nodename]").val().trim();
        var staticpara1 = $("#updateFm input[name=staticpara1]").val().trim();
        if(nodename == ""){
            alert("请输入省份");
            return false;
        }
        if(staticpara1 == ""){
            alert("请输入前缀");
            return false;
        }
        //防止出现1.2.3.0的情况
        if(staticpara1.charAt(staticpara1.length-1) == "0"){
            alert("请输入正确的前缀");
            return false;
        }
        if(IPFormatting(staticpara1+"255") == "Error"){
            alert("请输入正确的前缀");
            return false;
        }
        var existFlag = "";
        $.ajax({
            type: "POST",
            async: false,
            url: "CustomerDataBaseExec.jsp",
            data: {action: "checkSame", nodename: encodeURI(nodename), staticpara1: encodeURI(staticpara1), typeID: typeID, rn: Math.random()},
            success: function(data){
                if(data.trim() == "exist"){
                    existFlag = "ok";
                }
            }
        });
        if(existFlag == "ok"){
            alert("数据已存在");
            return false;
        }
    } else if(typeID == 3){
        var nodename = $("#updateFm input[name=nodename]").val().trim();
        var staticpara1 = $("#updateFm input[name=staticpara1]").val().trim();
        if(nodename == ""){
            alert("请输入省份");
            return false;
        }
        if(staticpara1 == ""){
            alert("请输入省份共享LNS IP");
            return false;
        }
        if(IPFormatting(staticpara1) == "Error"){
            alert("请输入正确的省份共享LNS IP");
            return false;
        }
        var existFlag = "";
        $.ajax({
            type: "POST",
            async: false,
            url: "CustomerDataBaseExec.jsp",
            data: {action: "checkSame", nodename: encodeURI(nodename), staticpara1: encodeURI(staticpara1), typeID: typeID, rn: Math.random()},
            success: function(data){
                if(data.trim() == "exist"){
                    existFlag = "ok";
                }
            }
        });
        if(existFlag == "ok"){
            alert("数据已存在");
            return false;
        }
    } else if(typeID == 2 || typeID == 4){
        var nodename = $("#updateFm1 input[name=nodename]").val().trim();
        var staticpara1 = $("#updateFm1 input[name=staticpara1]").val().trim();
        var staticpara2 = $("#updateFm1 input[name=staticpara2]").val().trim();
        var staticpara1Int = ipStrToInt(staticpara1);
        var staticpara2Int = ipStrToInt(staticpara2);
        if(nodename == ""){
            alert("请输入省份");
            return false;
        }
        if(staticpara1 == ""){
            alert("请输入开始地址");
            return false;
        }
        if(IPFormatting(staticpara1) == "Error"){
            alert("请输入正确的开始地址");
            return false;
        }
        if(staticpara2 == ""){
            alert("请输入结束地址");
            return false;
        }
        if(IPFormatting(staticpara2) == "Error"){
            alert("请输入正确的结束地址");
            return false;
        }
        if(staticpara1Int>staticpara2Int){
            alert("开始地址不能大于结束地址");
            return false;
        }
        var existFlag = "";
        $.ajax({
            type: "POST",
            async: false,
            url: "CustomerDataBaseExec.jsp",
            data: {action: "checkSame", nodename: encodeURI(nodename), staticpara1: encodeURI(staticpara1), staticpara2: encodeURI(staticpara2), typeID: typeID, rn: Math.random()},
            success: function(data){
                if(data.trim() == "exist"){
                    existFlag = "ok";
                }
            }
        });
        if(existFlag == "ok"){
            alert("数据已存在");
            return false;
        }
    } else if(typeID == 5){
        var nodename = $("#updateFm2 input[name=nodename]").val().trim();
        var staticpara1 = $("#updateFm2 input[name=staticpara1]").val().trim();
        var staticpara2 = $("#updateFm2 input[name=staticpara2]").val().trim();
        if(nodename == ""){
            alert("请输入使用省");
            return false;
        }
        if(staticpara1 == ""){
            alert("请输入号段");
            return false;
        }
        if(staticpara2 == ""){
            alert("请输入卡类型");
            return false;
        }
        if(isNaN(staticpara1)){
            alert("号段必须为正整数");
            return false;
        }
        var existFlag = "";
        $.ajax({
            type: "POST",
            async: false,
            url: "CustomerDataBaseExec.jsp",
            data: {action: "checkSame", nodename: encodeURI(nodename), staticpara1: encodeURI(staticpara1), staticpara2: encodeURI(staticpara2), typeID: typeID, rn: Math.random()},
            success: function(data){
                if(data.trim() == "exist"){
                    existFlag = "ok";
                }
            }
        });
        if(existFlag == "ok"){
            alert("数据已存在");
            return false;
        }
    }
    return true;
}

function add(){
    var typeID = $("#typeID").val();
    if(typeID == "1" || typeID == "3"){
        window.open("CustomerDataBaseBatchAddOne.jsp?typeID="+typeID);
    } else if(typeID == "2" || typeID == "4"){
        window.open("CustomerDataBaseBatchAddTwo.jsp?typeID="+typeID);
    } else if(typeID == "5"){
        window.open("CustomerDataBaseBatchAddThree.jsp?typeID="+typeID);
    }

}

function del(){
    var rows = $("#resultTable").datagrid("getChecked");
    //	console.log(rows);
    var ids = "";
    for (var i = 0; rows && i < rows.length; i++){
        var row = rows[i];
        ids += row.checkboxid+",";
    }

    if(ids != ""){
        ids = ids.substring(0, ids.length-1);
    } else {
        return;
    }
    $.messager.confirm('提示框', '确定要删除记录吗?',function(r){
        if(r){
            $.ajax({
                type: "POST",
                url: "CustomerDataBaseExec.jsp",
                data: {action: "DelBatch", ids: ids, rn: Math.random()},
                success: function(data){
                    if(data.trim() == "ok"){
                        query();
                    }
                }
            });
        }
    });
}

function loading(value){
    $("<div class=\"datagrid-mask\"></div>").css({display:"block",width:"100%",height:$(window).height()}).appendTo("body");
    $("<div class=\"datagrid-mask-msg\"></div>").html("<font size=2>"+value+"</font>").appendTo("body").css({display:"block",left:($(document.body).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2});
}

function unLoading(){
    $("body").children("div.datagrid-mask-msg").remove();
    $("body").children("div.datagrid-mask").remove();
}
function setReSise(){
    console.log($("#result"));
    var height1 = $("#result").height();
    if(height1>400){
        height1 = 400;
    }
    $("#result").attr("style","height:"+height1+"px");
    $("#result").layout("resize",{
        width:"98%",
        height:height1+"px"
    });
}

function newElement(){

    $('#dlg').dialog('open').dialog('center').dialog('setTitle', '新增');
    $('#fm').form('clear');

}

var i = 0;

function appendOne(){

        $("#addBody").append(
            "<tr class='listrow'> " +
            "                <td style='height: 25px; width: 180px; text-align: center'> " +
            "                  <input type='hidden' name='username'> " +
            "                  <span></span> " +
            "                </td> " +
            "                <td style='height: 25px; width: 180px; text-align: center'> " +
            "                  <input type='hidden' name='description'> " +
            "                  <span></span> " +
            "                </td> " +
            "                <td style='height: 25px; width: 180px; text-align: center'> " +
            "                  <a name='mod' id='bt_"+i+"' class='mygridbtn' onClick='javascript:updateOne(this)'><span>修改</span></a> " +
            "                  <a name='del' class='mygridbtn' onClick='javascript:deleteOne(this)'><span>删除</span></a> " +
            "                </td> " +
            "              </tr>");

    $("#bt_"+i).click();
    i++;
}


function updateOne(e){
    var typeID = $("#typeID").val();
    var lines = 2;
    if(typeID == "1" || typeID == "3"){
        lines = 2;
    }else if(typeID == "2" || typeID == "4"){
        lines = 3;
    }else if(typeID == "5"){
        lines = 3;
    }
    var tds = $(e).parent().parent().children();
    for(var i=0; i<lines; i++){
        var bakValue = $(tds[i]).find("input[type=hidden]").val();
        $(tds[i]).find("span").remove();
        $(tds[i]).append("<input type='text' name='updateInfo' value='"+bakValue+"'/>");
    }
    //删除修改 删除按钮
    $(tds[lines]).children().remove();
    //添加确定取消按钮
    $(tds[lines]).append("<a name='confirm' class='mygridbtn' onClick='javascript:confirmOne(this)'><span>确定</span></a>");
    $(tds[lines]).append("<a name='cancel' class='mygridbtn' onClick='javascript:cancelOne(this)'><span>取消</span></a>");
}

function confirmOne(e){
    var typeID = $("#typeID").val();
    var lines = 2;
    var tds = $(e).parent().parent().children();
    //判断数据是否正确
    var username = $(tds[0]).find("input[name=updateInfo]").val().trim();
    var description = $(tds[1]).find("input[name=updateInfo]").val().trim();
    if(username == ""){
        alert("请输入名称");
        return false;
    }

    var existFlag = "";
    $.ajax({
        type: "POST",
        async: false,
        url: "/oper/checkSame",
        data: {username: encodeURI(username), description: encodeURI(description), rn: Math.random()},
        success: function(data){
            if(data.trim() == "exist"){
                existFlag = "ok";
            }
        }
    });
    if(existFlag == "ok"){
        alert("数据已存在");
        return false;
    }

    //赋值结束
    for(var i=0; i<lines; i++){
        //赋值到隐藏框
        var textVal = $(tds[i]).find("input[name=updateInfo]").val();
        $(tds[i]).find("input[type=hidden]").val(textVal);
        //删除输入框
        $(tds[i]).find("input[name=updateInfo]").remove();
        //添加span
        $(tds[i]).append("<span>"+textVal+"</span>");

    }

    //修改按钮
    $(tds[lines]).children().remove();
    $(tds[lines]).append("<a name='mod' class='mygridbtn' onClick='javascript:updateOne(this)'><span>修改</span></a>");
    $(tds[lines]).append("<a name='del' class='mygridbtn' onClick='javascript:deleteOne(this)'><span>删除</span></a>");
}
function cancelOne(e){
    var typeID = $("#typeID").val();
    var lines = 2;
    var tds = $(e).parent().parent().children();
    var countIndex = 0;
    for(var i=0; i<lines; i++){
        //赋值到页面
        var textVal = $(tds[i]).find("input[type=hidden]").val();
        //删除输入框
        $(tds[i]).find("input[name=updateInfo]").remove();
        //添加span
        $(tds[i]).append("<span>"+textVal+"</span>");
        if(textVal.trim() == ""){
            countIndex++;
        }
    }
    //如果都为空删除该列
    if(countIndex == lines){
        deleteOne(e);
        return;
    }
    //修改按钮
    $(tds[lines]).children().remove();
    $(tds[lines]).append("<a name='mod' class='mygridbtn' onClick='javascript:updateOne(this)'><span>修改</span></a>");
    $(tds[lines]).append("<a name='del' class='mygridbtn' onClick='javascript:deleteOne(this)'><span>删除</span></a>");

}
function getRowIndex(a){
    //console.log($(a).parent().parent());
    var index = $(a).parent().parent()[0].rowIndex;
    return index;
}
function deleteOne(e){
    $(e).parent().parent().remove();
}

function submitAdd(){
    var typeID = $("#typeID").val();
    var addInfo = "";
    var trs = $("#addBody").children();
    if($("#dg").find("input[type=text]").length > 0){
        alert("请确定要新增的数据信息");
        return false;
    }

    //拼接信息
    for(var i=0; i<trs.length; i++){
        var oneRow = "";
        var tds = $(trs[i]).children();
        for(var j=0; j<tds.length-1; j++){
            var data = $(tds[j]).find("input[type=hidden]").val();
            oneRow += data+",";
        }
        if(oneRow != ""){
            oneRow = oneRow.substring(0, oneRow.length-1);
        }
        addInfo += oneRow+";";
    }
    if(addInfo != ""){
        addInfo = addInfo.substring(0, addInfo.length-1);
    }
    $("#addData").val(addInfo);
    $('#queryform').form('submit', {
        url: "/oper/add",
        onSubmit: function(){
            // do some check
            // return false to prevent submit;
        },
        success:function(data){
            var res = eval('(' + data + ')');
//	    	console.log(res);
            var status = res.status;
            if(status.trim() == "ok"){
                alert("添加成功");
                //window.location.reload();
                confirmChange("add");
            }
        }
    });
}

function confirmChange(type){
    query1();
    if(type == "add"){
        $('#dlg').dialog('close');
        $('#addBody').children().remove();
    }else if(type == "mod"){
        $('#dlgUpdate').dialog('close');
    }
}
