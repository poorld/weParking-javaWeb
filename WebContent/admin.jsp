<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="root" value="${pageContext.request.contextPath }"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>车位锁管理</title>
<style type="text/css">
.body {
	width: 100%;
	height: 100%;
	background-color: #6E6E6E;
}
</style>
</head>
<body>
	<div class="body">
		<div class="ui inverted segment">
			<div class="ui inverted secondary pointing menu">
				<a class="active item" data-tab="first"> 管理 </a> 
				<a class="item" data-tab="second"> 用户 </a> 
				<a class="item" data-tab="third"> 调试锁</a> 
				<!-- 添加车位 -->
				<a class="item" href="javascript:void(0)" onclick="addParking()">
					<i class="add circle icon"></i>
				</a>
				
			</div>
		</div>

		<div class="ui bottom attached tab segment active mybg"
			data-tab="first"
			style="background-color: #6E6E6E; border-top-width: 0px; border-bottom-width: 0px;">
			<table class="ui selectable inverted table"
				style="background-color: #68838B;">
				<thead>
					<tr>
						<th>编号</th>
						<th>状态</th>
						<th>经度</th>
						<th>纬度</th>
						<th>地址</th>
						<th class="right aligned">修改</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="parking" items="${parkings}">
						<tr>
							<td>${parking.lookid }</td>
							<c:if test="${parking.state == 1 }">
								<td><i class="large green checkmark icon"></i>可用</td>
							</c:if>
							<c:if test="${parking.state == 2 || parking.state == 3}">
								<td><i class="icon close"></i>不可用</td>
							</c:if>
							<td>${parking.longitude}</td>
							<td>${parking.latitude}</td>
							<td>${parking.address}</td>
							<td class="right aligned">
								<button class="circular ui icon button" onclick="update(this)">
									<i class="icon settings"></i>
								</button>
							</td>
						</tr>
					</c:forEach>

				</tbody>
			</table>

		</div>
		<div class="ui bottom attached tab segment mybg" data-tab="second"
			style="background-color: #6E6E6E; border-top-width: 0px; border-bottom-width: 0px;">
			<table class="ui selectable inverted table"
				style="background-color: #778899;">
				<thead>
					<tr>
						<th>头像</th>
						<th>用户名</th>
						<th>省份</th>
						<th>城市</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="user" items="${users }">
						<tr>
							<td><img
								src="${user.avatarurl}"
								style="width: 40px; height: 40px; border-radius: 50%;"></td>
							<td>${user.nickName }</td>
							<td>${user.province }</td>
							<td>${user.city }</td>
						</tr>
					</c:forEach>

				</tbody>
			</table>

		</div>
		<%-- <div class="ui bottom attached tab segment mybg" data-tab="third"
			style="background-color: #6E6E6E; border-top-width: 0px; border-bottom-width: 0px;">
			<table class="ui selectable inverted table"
				style="background-color: #8B7D6B;">
				<thead>
					<tr>
						<th>订单编号</th>
						<th>金额</th>
						<th class="right aligned">时间</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="order" items="${orders }">
						<tr>
							<td>${order.orderId }</td>
							<td>${order.spend }</td>
							<td class="right aligned">${order.time }</td>
						</tr>
					</c:forEach>
					
				</tbody>
			</table>

		</div> --%>
	<div class="ui bottom attached tab segment mybg" data-tab="third"
			style="background-color: #E3E3E3; border-top-width: 0px; border-bottom-width: 0px;height:80%">
  			<button class="ui button primary" onclick="myaction('openlook')">开锁</button>
			<button class="ui button primary" onclick="myaction('closelook')">关锁</button>
			<button class="ui primary button" onclick="myaction('querylook')"> 查询状态</button>
			<button class="ui orange button" onclick="manage('available')"> 一键可用</button>
			<button class="ui orange button" onclick="manage('disabled')"> 一键不可以</button>
			<button class="ui orange button" onclick="manage('offline')"> 一键下线</button>
			
			<div class="ui message" id="msg" style="height:80%">
  			<div class="header">
    			消息：
  			</div>
  			<p class="tip">等待连接</p>
			</div>
	</div>
	</div>

	<!--模态框-->
	<div class="ui modal">
		<i class="close icon"></i>
		<div class="header mytitle">修改车位锁</div>
		<div class="image content" style="background-color: #E0E0E0;">
			<form class="ui form" style="width: 100%;">
				<div class="ui form">
					<div class="field">
						<label>编号</label> <input type="text" value="1" name="id"
							class="required" disabled="disabled">
					</div>
				</div>

				<div class="ui segment">
					<div class="field">
						<div class="ui toggle checkbox">
							<input type="checkbox" class="required" name="state"
								checked="checked"><label>车锁状态</label>
						</div>
					</div>
				</div>

				<div class="ui form">
					<div class="field">
						<label>经度</label> <input type="text" name="longitude"
							class="required" value="120.1111">
					</div>
				</div>

				<div class="ui form">
					<div class="field">
						<label>纬度</label> <input type="text" name="latitude"
							class="required" value="120.1111">
					</div>
				</div>

				<div class="ui form">
					<div class="field">
						<label>地址</label> <input type="text" name="address"
							class="required" value="广西职业技术学院">
					</div>
				</div>
			</form>

		</div>
		<div class="actions">
			<a class="ui red label" href="javascript:void(0)" onclick="remove()">删除</a>
			<div class="ui button" onclick="cancel()">取消</div>
			<div class="ui button" onclick="confirm()">确定</div>
		</div>
	</div>

	<link rel="stylesheet" href="${root}/css/semantic.min.css" />
	<script type="text/javascript" src="${root}/js/jquery.min.js"></script>
	<script type="text/javascript" src="${root}/js/semantic.min.js"></script>
	<script type="text/javascript">
	var gg = "sg"
	var run = false;
/* 	$(document).ready(function(){
		var interval = setInterval(function(){
			//if(!run){
			//	clearInterval(interval);
			//} 
			//do whatever here..
			$.ajax({
				type: "POST",
				url: "${root}/socket",
				success: function(res) {
					console.log(res)
					if (res != "null"){
						run = true
						var $msg = $("#msg")
						if ($msg.children().length > 10) {
							$msg.children().eq(1).remove()
						};
						$msg.append("<p>"+res+"</p>")
					}
					//clearInterval(interval);停止
				}
			})
			}, 500);
	});  */
		var id ;
		parking = new Object();
		parking.id = 0;
		parking.state = 1;
		parking.longitude = 0;
		parking.latitude = 0;
		parking.address = "";
		$('.menu .item').tab();
	

		function update(Obj) {
			$(".mytitle").text("修改车位锁")
			id = 1;
			attrVal = 5;
			$parent = $(Obj).parents("tr");
			for (var i = 0; i < attrVal; i++) {
				$(".required").eq(i).val($parent.children().eq(i).text())
				if (i == 1) {
					$state = $("input[name=state]:checked");
					if ($parent.children().eq(i).text() == "可用") {
						$state.attr("checked",true)
					}else {
						$state.attr("checked",false)
					}
				}
			}
			$('.ui.modal').modal("show");
		}
		
		
		function cancel() {
			$('.ui.modal').modal("hide");
		}
		function confirm() {
			attrVal = 5
			for (var i = 2; i < attrVal; i++) {
				var value = $(".required").eq(i).val()
				if (value == "" || value.length == 0) {
					alert("不能为空!")
					return
				}
			}
			parking.id = $("input[name=id]").val()
			parking.state = $("input[name=state]").prop("checked")
			parking.longitude = $("input[name=longitude]").val()
			parking.latitude = $("input[name=latitude]").val()
			parking.address = $("input[name=address]").val()
			//console.log(parking)
			console.log(JSON.stringify(parking))
			var url = "${root}/updateParking";
			if (id == 2) {
				url = "${root}/addParking"
			}
			console.log(id)
			$('.ui.modal').modal("hide");
			$.ajax({
				type: "POST",
				url: url,
				dataType: "json",
				data: JSON.stringify(parking),
				success: function(res) {
					$('.ui.modal').modal("hide");
					window.location.reload();
				}
			}) 
		}

		function addParking() {
			$(".mytitle").text("添加车位锁")
			id = 2;
			$("input[name=id]").attr("disabled","disabled")
			$("input[name=id]").val("");
			$("input[name=state]").val("");
			$("input[name=longitude]").val("");
			$("input[name=latitude]").val("");
			$("input[name=address]").val("");
			$('.ui.modal').modal("show");
		}
		
		function remove(){
			//openlook
			//closelook
			//querylook
			var lookid = $("input[name=id]").val();
			console.log(lookid)
			$.ajax({
				type: "POST",
				url: "${root}/removeParking",
				data: {
					lookid: lookid
				},
				success: function(res) {
					$('.ui.modal').modal("hide");
					window.location.reload();
				}
			})
		}
		var i = 0;
		
		function myaction(res) {
			var $msg = $("#msg")
			if ($msg.children().length > 10) {
					$msg.children().eq(1).remove()
				};
			$msg.append("<p>"+res+"</p>")
			var data = {
				command: res
			}
			$.ajax({
				type: "POST",
				url: "${root}/socket",
				data: data,
				dataType: "json",
				success: function(re) {
					/* console.log(re)
					if ($msg.children().length > 10) {
						$msg.children().eq(1).remove()
					};
					$msg.append("<p style='color:red'>"+re.msg+" state:"+re.state+"</p>") */
				},
			})
		}
		
		function manage(res) {
			//available 一键可用
			//disabled 一键不可以
			//offline 一键下线
			var data = {
					"action": res
			}
			alert(res)
			$.ajax({
				type: "POST",
				url: "${root}/modifyState",
				data: data,
				dataType: "json",
				success: function(){
					window.location.reload();
				}
			})
		}
	</script>
</body>
</html>