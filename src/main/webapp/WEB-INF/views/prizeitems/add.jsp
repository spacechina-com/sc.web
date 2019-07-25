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
        <a href="">奖品池管理</a>
        <a>
          <cite>新增奖品</cite></a>
      </span>
    </div>
    <div class="x-body">
        <form enctype="multipart/form-data" class="layui-form" method="post" action="<%=request.getContextPath()%>/prizeitems/save">
          <div class="layui-form-item">
              <label for="L_username" class="layui-form-label">
                  <span class="x-red">*</span>奖品图片
              </label>
              <div class="layui-input-inline">
                  <table id="fileTable">
                  		<tr><td><input id="file" type="file" lt="image" name="fil" onchange="showImg(this)" accept="image/*" lay-verify="nikename"/></td><td><img alt="展位图片"  width="100px" id="image" style="display:none;cursor:pointer;"/></td><td></td></tr>
                  		<tr style="border:none;font-size:12px;"><td colspan="3" style="color:red;">为保证显示效果,强烈建议上传30*30大小的图片</td></tr>
                  	</table>
              </div>
          </div>
          <div class="layui-form-item">
              <label for="L_username" class="layui-form-label">
                  <span class="x-red">*</span>奖品介绍
              </label>
              <div class="layui-input-inline">
                  <textarea placeholder="请输入内容" id="desc2" name="DESCRIPTION" class="layui-textarea"></textarea>
              </div>
          </div>
          <div class="layui-form-item">
              <label for="L_username" class="layui-form-label">
                  <span class="x-red">*</span>等值
              </label>
              <div class="layui-input-inline">
                  <input type="text" oninput="value=value.replace(/[^0-9]/g,'')" id="L_username14" name="SAMEMONEY" required="" lay-verify="nikenamew"
                  autocomplete="off" class="layui-input">
              </div>
          </div>
          <div class="layui-form-item">
              <label for="username" class="layui-form-label">
                  <span class="x-red">*</span>自动处理
              </label>
              <div class="layui-input-inline">
                  <select id="shippingQ" name="AUTOHANDER" class="valid">
                    <option value="1">是</option>
                    <option value="0">否</option>
                  </select>
              </div>
          </div>
          <div class="layui-form-item">
              <label for="username" class="layui-form-label">
                  <span class="x-red">*</span>处理类型
              </label>
              <div class="layui-input-inline">
                  <select id="goods" name="HANDERTYPE_ID" class="valid" lay-filter="goodsQA">
                    <c:forEach var="ht" items="${handertypeData}">
                    	<option value="${ht.HANDERTYPE_ID}">${ht.DESCRIPTION}</option>
                    </c:forEach>
                  </select>
              </div>
          </div>
          <div class="layui-form-item">
              <label for="L_repass" class="layui-form-label">
              </label>
              <button  class="layui-btn" lay-filter="add" lay-submit="" type="submit">
                   确认
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
                return '群组图片不许为空';
              }
            }
          });
          
          form.verify({
              nikename1: function(value){
                if(value.length < 1){
                  return '二维码图片不许为空';
                }
              }
            });
          
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
        
        function showImg(obj){
        	var imageSrc = window.URL?window.URL.createObjectURL(obj.files[0]):obj.value;
        	$("#"+$(obj).attr("lt"))[0].src=imageSrc;
        	$("#"+$(obj).attr("lt")).css("display","");
        }
        
    </script>
  </body>

</html>