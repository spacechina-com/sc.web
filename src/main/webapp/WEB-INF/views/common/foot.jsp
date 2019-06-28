﻿<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
 <script type="text/javascript" src="<%=request.getContextPath()%>/static/xadmin/js/jquery.min.3.2.1.js"></script>   
 <script type="text/javascript">
 	function goPage(num){
		$("#page_currentPage").val(num);
		$($("form")[0]).submit();
	}
 	
 	function commonHref(url){
 		location.href = url;
 	}
 	
 	function commonSave(url){
 		location.href = url;
 	}
 	
 	function commonEdit(url){
 		location.href = url;
 	}
 	
 	function commonInfo(url){
 		location.href = url;
 	}
 	
 	function commonAlert(msg){
 		layer.alert(msg);
 	}
 	
 	function commonDelete(url,msg){
 		var info = msg || '确认要删除该条数据吗？';
 		layer.confirm(info,function(index){
            location.href = url;
        });
 	}
 	
 	var info = '${param.msg}';
 	if(info){
 		setTimeout(function(){
 			info = decodeURIComponent(info);
			layer.msg(info,function(){
               
            });
		},1000);
 	}
 	
 </script>