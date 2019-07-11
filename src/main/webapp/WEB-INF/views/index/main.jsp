<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ include file="../common/head.jsp"%>
<html  class="x-admin-sm">
<head>
	<meta charset="UTF-8">
	<title>${USER_SESSION.COMPANY_NAME}</title>
	<meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi" />
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/static/xadmin/css/font.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/static/xadmin/css/xadmin.css">

    <script type="text/javascript" src="<%=request.getContextPath()%>/static/xadmin/js/jquery.min.3.2.1.js"></script>
    <script type="text/javascript"src="<%=request.getContextPath()%>/static/xadmin/js/md5.min.2.10.0.js"></script>
    
    <script src="<%=request.getContextPath()%>/static/xadmin/lib/layui/layui.js" charset="utf-8"></script>

    <script type="text/javascript" src="<%=request.getContextPath()%>/static/xadmin/js/xadmin.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/xadmin/js/cookie.js"></script>
    <script>
         // 是否开启刷新记忆tab功能
         //var is_remember = false;
    </script>
</head>
<body>
    <!-- 顶部开始 -->
    <div class="container">
        <div class="logo"><a href="<%=request.getContextPath()%>/index">营销管理后台中心</a></div>
        <div class="left_open">
            <i title="展开左侧栏" class="iconfont" style="color:#000000;font-size:18px;">&#xe699;</i>
        </div>
        <ul class="layui-nav right" lay-filter="">
          <li class="layui-nav-item" style="font-size:14px;">
           		${USER_SESSION.USERNAME} 管理员
          </li>
        </ul>
        
    </div>
    <!-- 顶部结束 -->
    <!-- 中部开始 -->
     <!-- 左侧菜单开始 -->
    <div class="left-nav">
      <div id="side-nav">
        <ul id="nav">
            <c:forEach items="${MENU_LIST}" var="menu1" >
	      		<c:if test="${menu1.pid eq 0 && menu1.url ne '#'}">
	      			<li date-refresh="1" id="li_${menu1.id}">
		                <a href="<%=request.getContextPath()%>/${menu1.url}" target="mainFrame">
		                    <i class="iconfont">${menu1.icon}</i>
		                    <cite>${menu1.name}</cite>
		                </a>
	                </li>
	      		</c:if>
	      		<c:if test="${menu1.pid eq 0 && menu1.url eq '#'}">
		      		<li id="li_${menu1.id}">
		                <a href="javascript:;">
		                    <i class="iconfont">${menu1.icon}</i>
		                    <cite>${menu1.name}</cite>
		                    <i class="iconfont nav_right">&#xe697;</i>
		                </a>  
	                <ul class="sub-menu">
	                <c:forEach items="${menu1.subMenu}" var="menu2">
	                    <li date-refresh="1">
	                        <a _href="<%=request.getContextPath()%>/${menu2.url}">
	                            <i class="iconfont">&#xe6a7;</i>
	                            <cite>${menu2.name}</cite>
	                        </a>
	                    </li >
	                </c:forEach>
	                </ul>
	            </li>
	      	</c:if>
	    </c:forEach>   
        </ul>
      </div>
    </div>
    <!-- <div class="x-slide_left"></div> -->
    <!-- 左侧菜单结束 -->
    <!-- 右侧主体开始 -->
    <div class="page-content">
        <div class="layui-tab tab" lay-filter="xbs_tab" lay-allowclose="false">
          <!--<ul class="layui-tab-title">
            <li class="home"><i class="layui-icon">&#xe68e;</i>我的桌面</li>
          </ul>
          <div class="layui-unselect layui-form-select layui-form-selected" id="tab_right">
                <dl>
                    <dd data-type="this">关闭当前</dd>
                    <dd data-type="other">关闭其它</dd>
                    <dd data-type="all">关闭全部</dd>
                </dl>
          </div>
		  -->
          <div class="layui-tab-content">
            <div class="layui-tab-item layui-show">
                <iframe id="mainFrame" name="mainFrame" src='' frameborder="0" scrolling="yes" class="x-iframe"></iframe>
            </div>
          </div>
          <div id="tab_show"></div>
        </div>
    </div>
    <div class="page-content-bg"></div>
    <!-- 右侧主体结束 -->
    <!-- 中部结束 -->
    <!-- 底部开始 -->
    <!-- <div class="footer">
        <div class="copyright">Copyright ©2019 ${SYSTEM_NAME} All Rights Reserved</div>  
    </div>
     -->
    <!-- 底部结束 -->
    <script type="text/javascript">
    	
    	setTimeout(function(){$('.left-nav #nav li').eq(0).click();$("#mainFrame").attr("src","<%=request.getContextPath()%>/${MENU_FIRST_DEFAULT}")},150);
    	
    	function logout(){
    		layer.confirm('确认立即退出系统?',function(index){
                location.href = '<%=request.getContextPath()%>/logout';
            });	
    	}
    </script>
</body>
</html>