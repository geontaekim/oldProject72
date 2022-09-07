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
	<script src="${pageContext.request.contextPath}/js/modal.js?v=<%=System.currentTimeMillis()%>" defer></script>
<link rel="stylesheet"
	href="https://unpkg.com/ag-grid-community/dist/styles/ag-grid.css">
<link rel="stylesheet"
	href="https://unpkg.com/ag-grid-community/dist/styles/ag-theme-balham.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/datepicker.css">
	
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
	<article class="estimate">
		<div class="estimate__Title">
			<h5>📋 문의 게시판</h5>
			<div class="menuButton">
				<button id="write">글쓰기</button>
				<!--write 로 변경 -->
				<button id="showcontent" >내용보기</button>
				<div class="menuButton__selectCode">
					<button id="codeDeleteButton">상태확인</button>
					<button id="contentdelete">삭제</button>
					
				</div>
				
			</div>
		</div>
	</article>
	<article class="estimateGrid">
	
		<div align="center">
		
			<div id="myGrid" class="ag-theme-balham"
				style="height: 500px; width: auto; text-align: center;"></div>
				
		</div>
		<div align="right"><button id="checkmycon" style="background:#0B7903; color:white; ">내가쓴글확인</button></div>
		
	</article>

	<div class="modal fade" id="codeSearch" role="dialog">
		<div class="modal-dialog modal-xl">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title">CODE LIST</h5>
					<button type="button" class="close" data-dismiss="modal"
						style="padding-top: 0.5px">&times;</button>
				</div>
				<div class="modal-body">
					<div id="codeGrid" class="ag-theme-balham"
						style="height: 1000px; width: auto;"></div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
	
	 <div id="modalcontent" class="modal fade" role="dialog">
    <div class="modal-dialog">
         <!-- Modal content-->
         <div class="modal-content">
            <div class="modal-header">
               <h4 class="modal-title">contents</h4>
              
               <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
			
            <div class="modal-body">
            	 <span>작성자:</span><span id="username" name="username" value="${sessionScope.empName}"
						name="username">${sessionScope.empName}</span>   
               <div id="contents-body" style="width: 100%; height: 500px;">
               <textarea id="frmname" name="frmname" cols="50%" rows="10%"
							style="color: #010101; width:95% ; height:30px; " readonly >
							</textarea>
               <textarea placeholder="최대 1000글자 이내 작성 부탁드립니다."
							id="frmContents" name="frmContents" cols="50%" rows="10%"
							style="color: #010101; " readonly></textarea>
							</div>
            </div>

            <div class="modal-footer">
               <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
         </div>
      </div>
   </div>

	<%--Amount Modal--%>
	<script>
	  const myGrid = document.querySelector('#myGrid');
	  const myGrid2 = document.querySelector('#myGrid2');
	  const write = document.querySelector('#write');
	  const itemList = document.querySelector('#itemList');
	  const unitList = document.querySelector('#unitList');
	  const amountList = document.querySelector('#amountList');
	  const showcontent = document.querySelector('#showcontent');
	  const frmname = document.querySelector('#frmname');
	  const frmContents = document.querySelector('#frmContents');
	  const contentdelete = document.querySelector('#contentdelete');
	  const checkmycon = document.querySelector('#checkmycon');
	  

	 //-----------------------------------------코드 조회------------------------------------------// 
	  
	  // code 조회
	  let estColumn = [
	   {headerName: "NO", checkboxSelection: true,  width: 50, cellStyle: {'textAlign': 'center'}, headerCheckboxSelection: true },
	    {headerName: "유형", field: "errnum", editable: true },
	    {headerName: "제목", field: "frmTitle",editable: true},
	    {headerName: "작성자", field: "username",editable: false},
	    {headerName: "상태", field: "status", editable: true},
	    {headerName: "번호", field:"seq_num", editable: true}
	    
	  ];  							//윗단 목차만들어주는 로직
	 
	  let rowData = [];
	  let contractRowNode;
	  // event.colDef.field
	  let estGridOptions = {
	    columnDefs: estColumn,
	    rowSelection: 'single',					//선택하나만 가능
	    rowData: rowData,
	    paginationAutoPageSize: true,
	    pagination: true,
	    defaultColDef: { resizable : true},
	    overlayNoRowsTemplate: "문의 내용이 없습니다." ,  
	    onGridReady: function (event) {		// onload 이벤트와 유사 ready 이후 필요한 이벤트 삽입한다.
	      event.api.sizeColumnsToFit();    
	    },
	    onGridSizeChanged: function (event) {
	      event.api.sizeColumnsToFit();
	    },
	    onCellValueChanged: function(event) {   	 		//포커스가 셀을 떠날때 사용자 지정값을 커밋
           if(event.data.status != "INSERT" && event.data.status != "DELETE"){	  	
        	  if(event.data.codeChangeAvailable != "변경불가능"){
	              console.log(event.data);
	              event.data.status = "UPDATE"; 
	              estGridOptions.api.updateRowData({update: [event.data]});			//사용자 지정값을 DB에 저장
        	  }
           }
         },
	    getSelectedRowData() {
	        let selectedNodes = this.api.getSelectedNodes();
	        let selectedData = selectedNodes.map(node => node.data);
	        return selectedData;
	      }
	    }  
	  const showContentGrid = () => {
		    let xhr = new XMLHttpRequest();
		    xhr.open('POST', '${pageContext.request.contextPath}/help/boardSaveCre.do' , true)
		    xhr.setRequestHeader('Accept', 'application/json');
		    xhr.send();
		    xhr.onreadystatechange = () => {
		      if (xhr.readyState == 4 && xhr.status == 200) {
		        let txt = xhr.responseText;
		        txt = JSON.parse(txt);
		        if (txt.errorCode < 0) {
		          swal.fire("오류", txt.errorMsg, "error");
		          return;
		        }
		        console.log(txt);
		        
		        estGridOptions.api.setRowData(txt.contentList);   
		      
		      }
		    }
		  }
	  
	  const getmyContent = () => {
		    let xhr = new XMLHttpRequest();
		    xhr.open('POST', '${pageContext.request.contextPath}/help/myboardSaveCre.do' +
		        "?username="+"${sessionScope.empName}",
		        true)
		    xhr.setRequestHeader('Accept', 'application/json');
		    xhr.send();
		    xhr.onreadystatechange = () => {
		      if (xhr.readyState == 4 && xhr.status == 200) {
		        let txt = xhr.responseText;
		        txt = JSON.parse(txt);
		        if (txt.errorCode < 0) {
		          swal.fire("오류", txt.errorMsg, "error");
		          return;
		        }
		        console.log(txt);
		        
		        estGridOptions.api.setRowData(txt.contentList);   
		      
		      }
		    }
		  }
	

			 const getcontent = () => {
				 let contents=estGridOptions.getSelectedRowData();
				 
				 let xhr = new XMLHttpRequest();
				 xhr.open('POST', '${pageContext.request.contextPath}/help/getboardcontent.do'+
				       "?seq_num="+contents[0].seq_num,
				        true);
				    xhr.setRequestHeader('Accept', 'application/json');
				    xhr.send();
				    xhr.onreadystatechange = () => {
				      if (xhr.readyState == 4 && xhr.status == 200) {
				        let txt = xhr.responseText;
				         txt = JSON.parse(txt); 
				        if (txt.errorCode < 0) {
				          swal.fire("오류", txt.errorMsg, "error");
				          return;
				        }
				        console.log("txt는?"+txt.coninfo.frmTitle);
				        frmname.innerHTML=txt.coninfo.frmTitle;
				        frmContents.innerHTML=txt.coninfo.frmContents;
				      }
				    }
				  }
		
			 
			 const deletecontent = () => {
				 let contents=estGridOptions.getSelectedRowData();
				 let xhr = new XMLHttpRequest();
				 xhr.open('DELETE', '${pageContext.request.contextPath}/help/boardDelete.do'+
				       "?seq_num="+contents[0].seq_num,
				        true);
				    xhr.setRequestHeader('Accept', 'application/json');
				    xhr.send();
				    xhr.onreadystatechange = () => {
				      if (xhr.readyState == 4 && xhr.status == 200) {
				        let txt = xhr.responseText;
				         txt = JSON.parse(txt); 
				        if (txt.errorCode < 0) {
				          swal.fire("오류", txt.errorMsg, "error");
				          return;
				        }
				        console.log("삭제된 제목:");
				       
				      	location.href="${pageContext.request.contextPath}/help/board.html";
				      }
				    }
				  }
	  	
	checkmycon.addEventListener("click", () => { 			//내가쓴글 확인하기
		getmyContent(); });	   
			 
			 
	  write.addEventListener("click", () => { 			// 글쓰기 버튼 창넘기기(write)
	    
	  		location.href="${pageContext.request.contextPath}/help/writeboard.html";	
	     });
	  
	  showcontent.addEventListener("click", () => {			//글쓴 내용보기
		  												
		  $("#modalcontent").modal();
		  getcontent();
	  		
	     });
	  
	   contentdelete.addEventListener("click", () => {			
 
	   Swal.fire({
	  		  title: "정말삭제 하시겠습니까?",
	  		  icon: 'warning',
	  		  showCancelButton: true,
	  		  confirmButtonColor: '#3085d6',
	  		  cancelButtonColor: '#d33',
	  		  confirmButtonText: '확인',
	  		  cancelButtonText: '취소'
	  		}).then((result) => {
	  		  if (result.value) {
	  			 deletecontent();			//확인 버튼 누를시 실행될 내용.
	  		  }
	  		})
 
    });    

	    
	    
		   
	//---------------------------------------------코드 상세조회-------------------------------------------------------------//
   
  		 /////////////////////////////////////////////이벤트 처리부분////////////////////////////////////////////////////// 
	 
	 
	
	// 삭제
	  function deleteRow(event) {       
	     if (event.id =="codeDeleteButton"){
	        let estGrid = estGridOptions.getSelectedRowData();
	        estGrid.forEach(function(estGrid,index){                   
	          console.log("estGrid는?"+estGrid);
	         if(estGrid.status == 'INSERT')
	            estGridOptions.api.updateRowData({remove: [estGrid]});
	         else{
	          estGrid.status = '답변완료'
	             estGridOptions.api.updateRowData({update: [estGrid]});        
	          console.log(estGridOptions);
	         }
	       });          
	     }
	  } 
	     
	/////////////////////////////////////////////일괄저장//////////////////////////////////////////////////////
	  
	  
	  
	  
	  
	  document.addEventListener('DOMContentLoaded', () => {
	       new agGrid.Grid(myGrid, estGridOptions);
	       showContentGrid();
	       
	       // 모달창 관련
	         /*  $("#warehouseLocationButton").on("click", function() {
	            $("div#googleMapModal").modal();  */
	     })
	  // O setup the grid after the page has finished loading
	</script>
</body>
</html>