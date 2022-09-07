<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>사내 게시판</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<script>
	$(document).ready(function(){
		
		$("#regBtn").click(function(){
			console.log("게시물 쓰기 클릭");
			console.log("동기${pageContext.request.contextPath}");
			
			
			//이 버튼을 누르면 게시물을 쓰는 페이지로 이동해야한다.			
/*			$.ajax({
		        url: "${pageContext.request.contextPath}/logisales/registerForm.do",
		        type: "get",
		        dataType: "json",
		        contentType: "application/json",
		        success: function(resultData) {
		            // TODO : 결과로 받은 resultData로 작업 !
		        },
		        error: function(jqXHR, textStatus, errorThrown) {
		            // 에러 로그는 아래처럼 확인해볼 수 있다. 
		            alert("업로드 에러\ncode : " + jqXHR.status + "\nerror message : " + jqXHR.responseText);
		        }
		});
*/		
*
*
		})
		
		
	})
</script>

</head>
<body>

	<div class="container">
		<h2>사내 게시판</h2>
		<div class="panel panel-default">
			<div class="panel-heading">건의 사항
			<button id="regBtn" type="button" class="btn btn-xs pull-right btn-primary">게시물쓰기</button>
			</div>
			<div class="panel-body">
				<div class="table-responsive">
				
					<table class="table table-hover">
						<thead>
								<tr>
									<td>게시물 번호</td>
									<td>제목</td>
									<td>등록자</td>
									<td>등록일</td>
									<td>조회수</td>
								</tr>			
						</thead>
						<tbody>
								
						</tbody>
					</table>
				
				</div>
			</div>

		</div>
		<div class="panel-footer">(주)동기</div>
	</div>
	</div>
</body>
</html>