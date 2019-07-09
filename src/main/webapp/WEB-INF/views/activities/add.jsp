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
     <style>
     #fileTable{
     	min-width:550px;
     }
     #fileTable tr td{
     	  padding:10px;
     }
     #fileTable tr{
     	  border:1px #dddddd solid;
     }
     </style>
  </head>
  
  <body>
  	    <div class="x-nav">
      <span class="layui-breadcrumb">
        <a href="">抽奖池管理</a>
        <a>
          <cite>新增抽奖</cite></a>
      </span>
    </div>
    <div class="x-body">
        <form enctype="multipart/form-data" class="layui-form" method="post" action="<%=request.getContextPath()%>/activities/save" onsubmit="return checkCondition();">
          <div class="layui-form-item">
              <label for="username" class="layui-form-label">
                  <span class="x-red">*</span>所属类型
              </label>
              <div class="layui-input-inline">
                  <select id="MODALITIES_ID" name="MODALITIES_ID" class="valid">
                  	<option value="">请选择</option>
                    <c:forEach var="ml" items="${modalitiesData}">
                    	<option value="${ml.MODALITIES_ID}">${ml.DESCRIPTION}</option>
                    </c:forEach>
                  </select>
              </div>
          </div>
          <div class="layui-form-item">
              <label for="L_username" class="layui-form-label">
                  <span class="x-red">*</span>背景图片
              </label>
              <div class="layui-input-inline">
                  <table id="fileTable">
                  		<tr><td><input id="file" type="file" lt="image" name="fil" onchange="showImg(this)" accept="image/*" lay-verify="nikename"/></td><td><img alt="展位图片"  width="100px" id="image" style="display:none;cursor:pointer;"/></td><td></td></tr>
                  	</table>
              </div>
          </div>
          <div class="layui-form-item">
              <label for="L_username" class="layui-form-label">
                  	单人抽奖次数
              </label>
              <div class="layui-input-inline">
                  <input type="text" id="L_username143" name="SINGLE_LIMIT" lay-verify="nikenamewAs"
                  autocomplete="off" class="layui-input">
              </div>
          </div>
          <div class="layui-form-item">
              <label for="L_username" class="layui-form-label">
                  	单人日抽奖次数
              </label>
              <div class="layui-input-inline">
                  <input type="text" id="L_username143" name="DAY_LIMIT" lay-verify="nikenamewAsd"
                  autocomplete="off" class="layui-input">
              </div>
          </div>
          <div class="layui-form-item">
              <label for="L_username" class="layui-form-label">
                  <span class="x-red">*</span>主题
              </label>
              <div class="layui-input-inline">
                  <input type="text" id="L_username14" name="TOPIC" lay-verify="nikenamews"
                  autocomplete="off" class="layui-input">
              </div>
          </div>
          <div class="layui-form-item">
              <label for="L_username" class="layui-form-label">
                  <span class="x-red">*</span>抽奖介绍
              </label>
              <div class="layui-input-inline">
                  <textarea placeholder="请输入内容" id="desc2" name="DESCRIPTION" class="layui-textarea"></textarea>
              </div>
          </div>
          <div class="layui-form-item">
              <label for="L_username" class="layui-form-label">
                  <span class="x-red">*</span>开始时间
              </label>
              <div class="layui-input-inline">
                  <input class="layui-input" placeholder="开始日" name="START_TIME" id="start" lay-key="1" autocomplete="off" lay-verify="beginningDate">
              </div>
          </div>
          <div class="layui-form-item">
              <label for="L_username" class="layui-form-label">
                  <span class="x-red">*</span>结束时间
              </label>
              <div class="layui-input-inline">
                  <input class="layui-input" placeholder="结束日" name="END_TIME" id="end" lay-key="2" autocomplete="off" lay-verify="endDate">
              </div>
          </div>
          <div class="layui-form-item">
              <label for="username" class="layui-form-label">
                  <span class="x-red">*</span>关联产品
              </label>
              <div class="layui-input-inline">
                  <select id="goods" name="GOODS_ID" class="valid" lay-filter="goods">
                    <option value="">请选择</option>
                    <c:forEach var="g" items="${goodsData}">
                    	<option value="${g.GOODS_ID}">${g.GOODSNAME}</option>
                    </c:forEach>
                  </select>
              </div>
          </div>
          <div class="layui-form-item">
              <label for="username" class="layui-form-label">
                  	产品批次
              </label>
              
              <div class="layui-input-inline" style="width:500px;">
              		<input type="hidden" id="BATCH_ID" name="BATCH_ID"/>
              		<input type="text" id="BATCH_ID_NAME" lay-verify="nikenamewds"
                  autocomplete="off" class="layui-input" onclick="checkBatch();"/>
                  <!-- 
                  <select id="BATCH_ID" name="BATCH_ID" class="valid">
                  	<option value="">请选择</option>
                    <c:forEach var="b" items="${batchsData}">
                    	<option value="${b.BATCH_ID}">${b.BATCHNAME}</option>
                    </c:forEach>
                  </select>
                  -->
              </div>
          </div>
           <div class="layui-form-item">
              <label for="L_username" class="layui-form-label">
                  <span class="x-red">*</span>奖项管理
              </label>
              <div class="layui-input-inline" style="width:800px;">
                  <table class="layui-table x-admin">
                  		<tr><td colspan="4" style="border: none;"><button class="layui-btn layui-btn-normal add"  type="button" style="float: right;">添加奖品</button></td></tr>
                  		<tr class="thd"><td>奖品图片</td><td>奖品名称</td><td>中奖率</td><td>操作</td></tr>
                  		<tbody id="tbd">

                  		</tbody>
                  	</table>
              </div>
          </div>
          <div class="layui-form-item">
              <label for="username" class="layui-form-label">
                  <span class="x-red">*</span>状态
              </label>
              <div class="layui-input-inline">
                  <select id="shipping" name="STATE" class="valid">
                    <option value="1">启用</option>
                    <option value="0">暂停</option>
                  </select>
              </div>
          </div>
          <div class="layui-form-item">
              <label for="L_repass" class="layui-form-label">
              </label>
              <button  class="layui-btn" lay-filter="add" lay-submit="" type="submit">
                  增加
              </button>
          </div>
      </form>
    </div>
    <script>
        layui.use(['form','layer'], function(){
            $ = layui.jquery;
          var form = layui.form
          ,layer = layui.layer;
        
          //自定义验证规则
          form.verify({
            nikename: function(value){
              if(value.length < 1){
                return '背景图片不许为空';
              }
            }
          });
          
          form.on('select(goods)', function(data){
            		$("#BATCH_ID").val('');
            		$("#BATCH_ID_NAME").val('');
            		IDS = "";
                    NAMES = "";
        });
        
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
        });
        
        function showImg(obj){
        	var imageSrc = window.URL?window.URL.createObjectURL(obj.files[0]):obj.value;
        	$("#"+$(obj).attr("lt"))[0].src=imageSrc;
        	$("#"+$(obj).attr("lt")).css("display","");
        }
        
        $(".add1").click(function(){
        	$("#tbd").append(`<tr><td><input class="layui-input"/></td><td><input class="layui-input"/></td><td><button class="layui-btn layui-btn-primary delete" type="button">删除</button></td></tr>`);
        	$(".delete").click(function(){
            	$(this).parent().parent().remove();
            });
        });
        
       	$(".add").click(function(){
       		var htmlgp = "<div class='layui-form-item'><div class='layui-input-inline' style='width:100%;'><select id='PRIZEITEMS_ID_TEMP' class='valid'><option value=''>请选择奖品</option>";
       		<c:forEach var="pi" items="${prizeitemsData}">
       		htmlgp+="<option value='${pi.PRIZEITEMS_ID}' lt='${pi.IMAGE_PATH}'>${pi.DESCRIPTION}</option>";
            </c:forEach>
            htmlgp+="</select></div></div><div class='layui-form-item'><div class='layui-input-inline' style='width:100%;'><input id='PRIZEITEMS_ID_PERCENT_TEMP' class='layui-input' placeholder='商品中奖率'/></div></div><div class='layui-form-item'><div class='layui-input-inline' style='width:100%;color:red;'>友情提示:中奖率基数为1000.</div></div>";
       		layer.open({
           	  title:'选择奖品及设置中奖率',
           	  area: ['600px', '400px'],
           	  content: '<div>'+htmlgp+'</div>'
           	  ,btn: ['确认', '取消']
           	  ,yes: function(index, layero){
           		 if($("#PRIZEITEMS_ID_TEMP").val()==''){
           			layer.tips('请先选择奖品', '#PRIZEITEMS_ID_TEMP', {
                        tips: [2, '#0FA6D8'], //设置tips方向和颜色 类型：Number/Array，默认：2 tips层的私有参数。支持上右下左四个方向，通过1-4进行方向设定。如tips: 3则表示在元素的下面出现。有时你还可能会定义一些颜色，可以设定tips: [1, '#c00']
                        tipsMore: false, //是否允许多个tips 类型：Boolean，默认：false 允许多个意味着不会销毁之前的tips层。通过tipsMore: true开启
                        time:2000  //2秒后销毁，还有其他的基础参数可以设置。。。。这里就不添加了
                    });
           			return false;
           		 }
           		if($("#PRIZEITEMS_ID_PERCENT_TEMP").val()==''){
           			layer.tips('请设置择奖品中奖率', '#PRIZEITEMS_ID_PERCENT_TEMP', {
                        tips: [2, '#0FA6D8'], //设置tips方向和颜色 类型：Number/Array，默认：2 tips层的私有参数。支持上右下左四个方向，通过1-4进行方向设定。如tips: 3则表示在元素的下面出现。有时你还可能会定义一些颜色，可以设定tips: [1, '#c00']
                        tipsMore: false, //是否允许多个tips 类型：Boolean，默认：false 允许多个意味着不会销毁之前的tips层。通过tipsMore: true开启
                        time:2000  //2秒后销毁，还有其他的基础参数可以设置。。。。这里就不添加了
                    });
           			return false;
           		 }
           		if(parseInt($("#PRIZEITEMS_ID_PERCENT_TEMP").val())<0 || parseInt($("#PRIZEITEMS_ID_PERCENT_TEMP").val())>1000){
           			layer.tips('奖品中奖率范围[0-1000]', '#PRIZEITEMS_ID_PERCENT_TEMP', {
                        tips: [2, '#0FA6D8'], //设置tips方向和颜色 类型：Number/Array，默认：2 tips层的私有参数。支持上右下左四个方向，通过1-4进行方向设定。如tips: 3则表示在元素的下面出现。有时你还可能会定义一些颜色，可以设定tips: [1, '#c00']
                        tipsMore: false, //是否允许多个tips 类型：Boolean，默认：false 允许多个意味着不会销毁之前的tips层。通过tipsMore: true开启
                        time:2000  //2秒后销毁，还有其他的基础参数可以设置。。。。这里就不添加了
                    });
           			return false;
           		 }
           		if($("#PRIZEITEMS_ID_TEMP").val()!='' && $("#PRIZEITEMS_ID_PERCENT_TEMP").val()!=''){
           			var iid = $("#PRIZEITEMS_ID_TEMP").val();
           			var iname = $("#PRIZEITEMS_ID_TEMP").find("option:selected").text();
           			var path = $("#PRIZEITEMS_ID_TEMP").find("option:selected").attr("lt");
           			var ipercent = $("#PRIZEITEMS_ID_PERCENT_TEMP").val();
           			$("#tbd").append(`<tr><td><img src="<%=request.getContextPath()%>/file/image?FILENAME=`+path+`" alt="图片" width="80"/></td><td><input type="hidden" name="PRIZEITEMS_ID_PERCENT" value="`+iid+`_`+ipercent+`"/>`+iname+`</td><td>`+ipercent+`</td><td align="center"><button class="layui-btn layui-btn-primary delete" type="button">删除</button></td></tr>`);
                	$(".delete").click(function(){
                    	$(this).parent().parent().remove();
                    });
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
        });
        
        $(".delete").click(function(){
        	$(this).parent().parent().remove();
        });
        
        function checkCondition(){
        	if($("input[name='PRIZEITEMS_ID_PERCENT']").length==0){
        		layer.alert("该活动暂无关联的奖品,请完善奖品信息栏位.");
        		return false;
        	}
        	return true;
        }
        
        var IDS = "";
        var NAMES = "";
        
        
        function checkBatch(){
        	if(!$("#goods").val()){
        		layer.alert("请先选择关联产品")
        		return;
        	}else{
	        	$.ajax({
	    			type: "POST",
	    			url: '<%=request.getContextPath()%>/activities/findGoodsBatchs',
	    	    	data:{
	    	    		"GOODS_ID":$("#goods").val()
	    	    	},
	    	    	async: false,
	    			dataType:'json',
	    			cache: false,
	    			beforeSend:function(){
	    				
	    			},
	    			success: function(data){
						var list = data.data;
	    				var htmlgb = "<table class='layui-table' id='batchTable'>";
	    				$.each(list,function(index,value){
	    					var checkflag= "";
	    		    		if(IDS.indexOf(value.BATCH_ID+'') >= 0){
	    		    			checkflag = "checked='checked'";
	    		    		}
	    					htmlgb+="<tr><td>"+value.BATCHNAME+"</td><td><input type='checkbox' style='width:30px;' "+checkflag+" value='"+value.BATCH_ID+"' ln='"+value.BATCHNAME+"'/></td></tr>";
	    				});
	    				htmlgb += "</table>";
	    				layer.open({
	    		           	  title:'选择关联产品的批次信息',
	    		           	  area: ['600px', '400px'],
	    		           	  content: '<div>'+htmlgb+'</div>'
	    		           	  ,btn: ['确认', '取消']
	    		           	  ,yes: function(index, layero){
	    		    				
   		    				  IDS = "";
   		    		          NAMES = "";
   		    		          
	   		    		      $("#batchTable").find("input[type='checkbox']").each(function(){
	   		        	    	if($(this).is(':checked')){
	   		        	    		IDS+=$(this).val()+',';
	   		        	    		NAMES+=$(this).attr("ln")+',';
	   		        	    	}
	   		        	     });
   		        	    
   		        	    if(IDS.length > 0){
   		        	    	IDS = IDS.substr(0,IDS.length-1);
   		        	    	NAMES = NAMES.substr(0,NAMES.length-1).replace(/,/g,"  ,  ");
   		        	    }
   		        	    
   		        		$("#BATCH_ID").val(IDS);
   		        	    
   		        	    $("#BATCH_ID_NAME").val(NAMES);
	    		    		        
	    		    				
	    		           		layer.close(index);
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
	    			},
	    			error:function(){
	    				
	    			}
	    		});
        	}
        }
        
    </script>
  </body>

</html>