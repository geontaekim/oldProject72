<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%
request.setCharacterEncoding("UTF-8");
%>
<%@ taglib prefix="J2H" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Document</title>
<script
	src="https://unpkg.com/ag-grid-community/dist/ag-grid-community.min.noStyle.js"></script>
<link rel="stylesheet"
	href="https://unpkg.com/ag-grid-community/dist/styles/ag-grid.css">
<link rel="stylesheet"
	href="https://unpkg.com/ag-grid-community/dist/styles/ag-theme-balham.css">
<script
	src="${pageContext.request.contextPath}/js/modal.js?v=<%=System.currentTimeMillis()%>"
	defer></script>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/datepicker.css">
<script>

/* $(document).ready(function(){
    $("#frmInsert").click(sendData);
});
	
	function sendData(){ */
		
	/* 	if ($('#frmTitle').val() == "" || $('#frmTitle').val() == null) {
			alert("제목을 입력하세요.");
			$('#frmTitle').focus();
			return;
		}
		if ($('#frmContents').val() == "" || $('#frmContents').val() == null) {
			alert("본문을 입력하세요.");
			$('#frmContents').focus();
			return;
		} */
	//	if ($('frmfup').val() != "" && $('frmfup').val() != null) {
			//alert("파일 "+$('#frmfup').val()+"를 첨부합니다.");
	//	}
		/* $('#frmCreate').attr(
						{
							action : "${pageContext.request.contextPath}/help/boardSaveCreation.do"
									+ "?method=addContent"
									+ "&frmTitle="
									+ $('#frmTitle').val()
									+ "&errnum="
									+ $('#errnum option:selected').val()
									+ "&username="
									+ $('#username').html()
									+ "&frmContents=" 
									+ $('#frmContents').val()
						}).submit(); */
	
		
		//이 버튼을 누르면 게시물을 쓰는 페이지로 이동해야한다.	
	/* 	  let jsonData={
				"frmTitle":$('#frmTitle').val(),
				"errnum":$('#errnum option:selected').val(),
				"username":$('#username').html(),
				"frmContents":$('#frmContents').val()
			}
			$.ajax({
	        url: "${pageContext.request.contextPath}/help/boardSaveCreation.do",
	        type: "post",
	        data:JSON.stringify(jsonData),
	        dataType: "json",
	        contentType: "application/json",
	        success: function(resultData) {
	           /*  location.href="${pageContext.request.contextPath}/help/board.html"; */
	          /*  console.log("전송와써유"+resultData)
	        }
	       
	});

	} */
 
	function fnGoBoardList() {
		$('#frmCreate').attr({
			action : "/help/board.html"
		}).submit();
	}

	/* 저장 버튼을 누르면 실행된다. */
	 function fnSave() {
		
		if ($('#frmTitle').val() == "" || $('#frmTitle').val() == null) {
			alert("제목을 입력하세요.");
			$('#frmTitle').focus();
			return;
		}
		if ($('#frmContents').val() == "" || $('#frmContents').val() == null) {
			alert("본문을 입력하세요.");
			$('#frmContents').focus();
			return;
		}
		if ($('frmfup').val() != "" && $('frmfup').val() != null) {
			//alert("파일 "+$('#frmfup').val()+"를 첨부합니다.");
		}
		$('#frmCreate').attr(
						{
							action : "${pageContext.request.contextPath}/help/boardSaveCreation.do"
									+ "?frmTitle="
									+ $('#frmTitle').val()
									+ "&errnum="
									+ $('#errnum option:selected').val()
									+ "&username="
									+ $('#username').html()
									+ "&frmContents=" 
									+ $('#frmContents').val()
						}).submit();
	}
	
	 

</script>


<style>
* {
	margin: 0px;
}

h5 {
	margin-top: 3px;
	margin-bottom: 3px;
}

input {
	padding: 2px 0 2px 0;
	text-align: center;
	border-radius: 3px;
	
}

.ag-header-cell-label {
	justify-content: center;
}

.ag-cell-value {
	padding-left: 50px;
}

.estimate {
	margin-bottom: 10px;
}

.estimateDetail {
	margin-bottom: 10px;
}

.menuButton {
	margin-top: 10px;
}

/*  글쓰기 / 수정 /  삭제 */
.menuButton button {
	background-color: #0B7903;
	border: none;
	color: white;
	text-align: center;
	text-decoration: none;
	display: inline-block;
	font-size: 14px;
	border-radius: 3px;
}

.menuButton__selectCode {
	display: inline-block;
}
</style>
</head>
<body>
	<h2 style="padding: 0.1em 1em;">문의내용</h2>
	<!-- 파일 첨부를 위해서는 multipart/form-data를 추가해야 한다. -->
	<form id="frmCreate" name="frmCreate" method="post" enctype="multipart/form-data" action="${pageContext.request.contextPath}/help/boardSaveCreation.do" >
		<input type="hidden" id="frmSave" name="frmSave"  />
		<table id="tcreate">
			<thead>
				<tr>
					<th>제목</th>
					<!-- input type이 text -->
					<td><input id="frmTitle" name="frmTitle" type="text"
						style="height: auto; width: 200%; color: #010101" /></td>
					<!-- 제목 -->
				</tr>
				<tr>
					<th>유형</th>
					<td>
					<select name="errnum" id="errnum"  size="5">                
							<option  value="실행오류"  selected> 실행오류</option>        
							<option  value="서버오류"> 서버오류  </option>
							<option  value="버그관련"> 버그관련 </option> 
							<option  value="고객불만"> 고객불만</option>
							<option  value="기타"> 기타 </option>
					</select> <span>작성자:</span><span id="username2" name="username2"
						value="${sessionScope.empName}" name="username" >${sessionScope.empName}</span>
							<input type="hidden" id="username" name="username" value="${sessionScope.empName}" />
					</td>
	

				</tr>
			</thead>
			<tbody>
				<tr>
					<th>본문</th>
					<!-- input type이 textarea -->
					<td><textarea placeholder="최대 1000글자 이내 작성 부탁드립니다."
							id="frmContents" name="frmContents" cols="50%" rows="10%"
							style="color: #010101; height: auto; width: 200%;"></textarea></td>
				</tr>
				<!-- <tr>
					<th>파일첨부</th>
					input type이 file
					<td><input accept="image/*,.txt" multiple required
						style="font-family: '바탕체', Georgia, serif; font-size: 14px;"
						type="file"  name="frmfup" /></td>
				</tr> -->
			</tbody>
		</table>

		<br />
	
		<div class="mybutton">
			<input id="frmInsert" name="frmInsert" type="submit" value=" 문의 " /> 
				<input id="frmCancel" name="frmCancel" type="button" onclick="fnGoBoardList()" value=" 취소 " />
		</div>
	</form>


</body>
</html>