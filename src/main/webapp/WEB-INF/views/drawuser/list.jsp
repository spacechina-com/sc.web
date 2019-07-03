<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/head.jsp"%>
<html class="x-admin-sm">
  <head>
    <meta charset="UTF-8">
    <title>欢迎页面-X-admin2.1</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/static/xadmin/css/font.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/static/xadmin/css/xadmin.css">
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/xadmin/js/jquery.min.3.2.1.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/xadmin/lib/layui/layui.js" charset="utf-8"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/xadmin/js/xadmin.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/xadmin/js/cookie.js"></script>
    <!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
    <!--[if lt IE 9]>
      <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
      <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
  
  <body>
    <div class="x-nav">
      <span class="layui-breadcrumb">
      	<a href="">记录池管理</a>
        <a>
          <cite>列表查询</cite></a>
      </span>
    </div>
    <div class="x-body">
    <form class="layui-form layui-col-md12 x-so" method="post" action="<%=request.getContextPath()%>/drawuser/listPage?ACTIVITIES_ID=${pd.ACTIVITIES_ID}">
      <div class="layui-row">
          <input type="text" name="keywords"  placeholder="请输入关键字" autocomplete="off" class="layui-input" value="${page.pd.keywords}">
          
          <div class="layui-input-inline">
                  <select id="shipping21" name="STATE" class="valids">
                  	<option value="">全部状态</option>
                    <option value="1" <c:if test="${page.pd.STATE ne  null && page.pd.STATE eq '1'}">selected="selected"</c:if>>已处理</option>
                    <option value="0" <c:if test="${page.pd.STATE ne  null && page.pd.STATE eq '0'}">selected="selected"</c:if>>未处理</option>
                  </select>
          </div>
          
          
          <button class="layui-btn"  lay-submit="" lay-filter="sreach"><i class="layui-icon">&#xe615;</i>搜索</button>
        
      </div>
      <!-- 
      <xblock>
        <a class="layui-btn" onclick="commonSave('<%=request.getContextPath()%>/drawuser/goAdd')" href="javascript:;"><i class="layui-icon"></i>添加</a>
      </xblock>
       -->
      <table class="layui-table x-admin">
        <thead>
          <tr>
          	<th>所属抽奖</th>
            <th>奖品图片</th>
            <th>描述</th>
            <th>抽奖时间</th>
            <th>OPENID</th>
            <th>昵称</th>
            <th>头像</th>
            <th>操作</th>
            </tr>
        </thead>
        <tbody>
          <c:forEach var="var" items="${page.data}">
          	<tr>
          	<td>${var.TOPIC}</td>
           	<td><img src="<%=request.getContextPath()%>/file/image?FILENAME=${var.IMAGE_PATH}" alt="图片" width="100"/></td>
            <td>${var.DESCRIPTION}</td>
            <td>${var.CREATE_TIME}</td>
            <td>${var.OPENID}</td>
            <td>${var.NICKNAME}</td>
            <td><img width="60" src="${var.PHOTO}"/></td>
            <td class="td-manage">
              <c:choose>
              	<c:when test="${var.STATE eq 1}"><a href="javascript:;" class="layui-btn layui-btn-primary" disabled="disabled">已处理</a></c:when>
              	<c:when test="${var.STATE eq 0}"><a href="javascript:;" class="layui-btn layui-btn-normal" onclick="doHander('${var.DRAWUSER_ID}','${var.OPENID}','${var.HANDERTYPE_ID}','${var.SAMEMONEY}','${var.REALNAME}','${var.PHONE}','${var.ADDRESSDETIAL}')">未处理</a></c:when>
              	<c:otherwise>未知</c:otherwise>
              </c:choose>
            </td>
          </tr>
          </c:forEach>
          
        </tbody>
      </table>
      <div class="page">
       ${page.pageStr} 
      </div>
	</form>
    </div>
    <%@ include file="../common/foot.jsp"%>
    <script type="text/javascript">
    
    function sendNotice(oid){
    	$.ajax({
			type: "POST",
			url: '<%=request.getContextPath()%>/drawuser/sendNoAddress',
	    	data:{
	    		"OPENID":oid
	    	},
	    	async: false,
			dataType:'json',
			cache: false,
			beforeSend:function(){
				
			},
			success: function(data){
				if(data.flag){
					layer.alert("推送完善地址消息成功");	
				}else{
					layer.alert("推送完善地址消息失败");
				}
			},
			error:function(){
				
			}
		});
    }
    
    function express(did,no){
    	$.ajax({
			type: "POST",
			url: '<%=request.getContextPath()%>/drawuser/express',
	    	data:{
	    		"DRAWUSER_ID":did,
	    		"EXPRESSNO":no
	    	},
	    	async: false,
			dataType:'json',
			cache: false,
			beforeSend:function(){
				
			},
			success: function(data){
				if(data.flag){
					layer.alert("已处理,关联快递单号成功",function(){
						location.reload();
					});
				}else{
					layer.alert("快递处理失败");
				}
				
			},
			error:function(){
				
			}
		});
    }
    
	function handerMoney(did,oid,sm){
		$.ajax({
			type: "POST",
			url: '<%=request.getContextPath()%>/drawuser/money',
	    	data:{
	    		"DRAWUSER_ID":did,
	    		"OPENID":oid,
	    		"MONEY":sm
	    	},
	    	async: false,
			dataType:'json',
			cache: false,
			beforeSend:function(){
				
			},
			success: function(data){
				if(data.flag){
					layer.alert("已处理,发放红包成功",function(){
						location.reload();
					});
				}else{
					layer.alert(data.message);
				}
			},
			error:function(){
				
			}
		});
    }
    
    function doHander(did,oid,tid,sm,name,phone,address){
    	if(tid == '1'){
    		layer.alert("祝福类系统应该设置自动处理");
    		return;
    	} else if(tid == '2'){
    		layer.confirm("确认该用户商户已设置白名单?",function(index){
    			handerMoney(did,oid,sm);
    			layer.close(index);
            });
    	} else if(tid == '3'){
    		
    		if(name=="" || phone=="" || address==""){
    			layer.alert("该用户的收货地址不完整,通知完善地址.",function(index){
    				sendNotice(oid);
    				layer.close(index);
    			})
    		}else{
    			layer.open({
    	         	  title:'处理抽奖记录',
    	         	  area: ['400px', '200px'],
    	         	  content: '<div><input id="EXPRESSNO" class="layui-input" placeholder="快递单号"/></div>'
    	         	  ,btn: ['确认', '取消']
    	         	  ,yes: function(index, layero){
    	         		if($("#EXPRESSNO").val()==""){
    	         			layer.tips('快递单号不许为空', '#EXPRESSNO', {
    	                        tips: [2, '#0FA6D8'], //设置tips方向和颜色 类型：Number/Array，默认：2 tips层的私有参数。支持上右下左四个方向，通过1-4进行方向设定。如tips: 3则表示在元素的下面出现。有时你还可能会定义一些颜色，可以设定tips: [1, '#c00']
    	                        tipsMore: false, //是否允许多个tips 类型：Boolean，默认：false 允许多个意味着不会销毁之前的tips层。通过tipsMore: true开启
    	                        time:3000  //3秒后销毁，还有其他的基础参数可以设置。。。。这里就不添加了
    	                    });
    	         			return false;
    	         		}else{
	    	         		express(did,$("#EXPRESSNO").val()) ; 
	    	         		layer.close(index);
    	         		}
    	         	  }
    	         	  ,btn2: function(index, layero){
    	         	    //按钮【按钮二】的回调
    	         	    //return false 开启该代码可禁止点击该按钮关闭
    	         	  }
    	         	  ,cancel: function(){
    	         	    //右上角关闭回调
    	         	    //return false 开启该代码可禁止点击该按钮关闭
    	         	  }
    	         	});
    		}
    		
    	} else if(tid == '4'){
    		layer.alert("系统待完善该类型处理");
    		return;
    	}else{
    		layer.alert("暂不支持该类型处理");
    		return;
    	}
    }
	    
    	layui.use('laydate', function(){
	        var laydate = layui.laydate;
	        
	        //执行一个laydate实例
	        laydate.render({
	          elem: '#start' //指定元素
	        });
	
	        //执行一个laydate实例
	        laydate.render({
	          elem: '#end' //指定元素
	        });
	      });
    
    </script>
  </body>
</html>