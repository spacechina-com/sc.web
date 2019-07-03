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
        <a href="">抽奖池管理</a>
        <a>
          <cite>列表查询</cite></a>
      </span>
    </div>
    <div class="x-body">
    <form class="layui-form layui-col-md12 x-so" method="post" action="<%=request.getContextPath()%>/activities/listPage">
      <div class="layui-row">
          <input type="text" name="keywords"  placeholder="请输入关键字" autocomplete="off" class="layui-input" value="${page.pd.keywords}">
          <div class="layui-input-inline">
          <select id="MODALITIES_ID" name="MODALITIES_ID" class="valid">
          	<option value="">所属类型</option>
            <c:forEach var="ml" items="${modalitiesData}">
            	<option value="${ml.MODALITIES_ID}" <c:if test="${ml.MODALITIES_ID eq page.pd.MODALITIES_ID}">selected="selected"</c:if>>${ml.DESCRIPTION}</option>
            </c:forEach>
          </select>
          </div>        
          <button class="layui-btn"  lay-submit="" lay-filter="sreach"><i class="layui-icon">&#xe615;</i>搜索</button>
        
      </div>
      <xblock>
        <a class="layui-btn" onclick="commonSave('<%=request.getContextPath()%>/activities/goAdd')" href="javascript:;"><i class="layui-icon"></i>添加</a>
      </xblock>
      <table class="layui-table x-admin">
        <thead>
          <tr>
            <th>背景图片</th>
            <th>主题</th>
            <th>描述</th>
            <th>分类</th>
            <th>开始 / 结束时间</th>
            <th>操作</th>
            </tr>
        </thead>
        <tbody>
          <c:forEach var="var" items="${page.data}">
          	<tr>
           	<td><img src="<%=request.getContextPath()%>/file/image?FILENAME=${var.BACK_PATH}" alt="图片" width="100"/></td>
            <td>${var.TOPIC}</td>
            <td>${var.DESCRIPTION}</td>
            <td>${var.MDESCRIPTION}</td>
            <td>${var.START_TIME} / ${var.END_TIME}</td>
            <td class="td-manage">
              <a title="编辑" class="layui-btn layui-btn-warm"  onclick="commonEdit('<%=request.getContextPath()%>/activities/goEdit?ACTIVITIES_ID=${var.ACTIVITIES_ID}')" href="javascript:;">
                <i class="layui-icon">&#xe642;</i>编辑
              </a>
          	   <a title="删除" class="layui-btn layui-btn-danger" onclick="commonDelete('<%=request.getContextPath()%>/activities/delete?ACTIVITIES_ID=${var.ACTIVITIES_ID}');" href="javascript:;">
                 <i class="layui-icon">&#xe640;</i>删除
               </a>
               <a title="查看" class="layui-btn" onclick="commonInfo('<%=request.getContextPath()%>/activities/goInfo?ACTIVITIES_ID=${var.ACTIVITIES_ID}');" href="javascript:;">
                <i class="layui-icon">&#xe63c;</i>查看
              </a>
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