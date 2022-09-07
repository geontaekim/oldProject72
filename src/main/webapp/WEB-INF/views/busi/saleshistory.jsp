<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <script src="https://unpkg.com/ag-grid-community/dist/ag-grid-community.min.noStyle.js"></script>
    <link rel="stylesheet" href="https://unpkg.com/ag-grid-community/dist/styles/ag-grid.css">
    <link rel="stylesheet" href="https://unpkg.com/ag-grid-community/dist/styles/ag-theme-balham.css">
    <script src="${pageContext.request.contextPath}/js/modal.js?v=<%=System.currentTimeMillis()%>" defer></script>
    <script src="${pageContext.request.contextPath}/js/datepicker.js" defer></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/datepicker.css">
    <script>
            // O 날짜 설정
            $(function() {
              // set default dates
              let start = new Date();
              start.setDate(start.getDate() - 20);
              // set end date to max one year period:
              let end = new Date(new Date().setYear(start.getFullYear() + 1));
              // o set searchDate
              $('#datepicker').datepicker({
                todayHiglght: true,
                autoHide: true,
                autoaShow: true,
              });
              // o set searchRangeDate
              $('#fromDate').datepicker({
                startDate: start,
                endDate: end,
                minDate: "-10d",
                todayHiglght: true,
                autoHide: true,
                autoaShow: true,
                // update "toDate" defaults whenever "fromDate" changes
              })
              $('#toDate').datepicker({
                startDate: start,
                endDate: end,
                todayHiglght: true,
                autoHide: true,
                autoaShow: true,
              })
              $('#fromDate').on("change", function() {
                //when chosen from_date, the end date can be from that point forward
                var startVal = $('#fromDate').val();
                $('#toDate').data('datepicker').setStartDate(startVal);
              });
              $('#toDate').on("change", function() {
                //when chosen end_date, start can go just up until that point
                var endVal = $('#toDate').val();
                $('#fromDate').data('datepicker').setEndDate(endVal);
              });
      
            });
    </script>
    <style>
        .fromToDate {
            display: inline-block;
            margin-bottom: 7px;
        }

        #searchCustomerBox {
            display: none;
            margin-bottom: 7px;
        }

        #datepicker {
            margin-bottom: 7px;
        }

        button {
            background-color: #0B7903;
            border: none;
            color: white;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            font-size: 14px;
            border-radius: 3px;
            margin-bottom: 10px;
        }

        .ag-header-cell-label {
            justify-content: center;
        }
        .ag-cell-value {
            padding-left: 5px;
        }
    </style>
</head>
<body>
<article class="delivery">
    <div class="delivery__Title" style="color: black">
        <h5>🗏 거래내역</h5>
        <b>수주 검색 조건</b><br/>
        <div>
            <label for="searchByPeriodRadio">기간 검색</label>
            <input type="radio" name="searchCondition" value="searchByDate" id="searchByPeriodRadio" checked>
            &nbsp;<label for="searchByCustomerRadio">거래처 검색</label>
            <input type="radio" name="searchCondition" value="searchByCustomer" id="searchByCustomerRadio">
        </div>

        <form autocomplete="off">
            <select name='searchCustomerBox' id='searchCustomerBox' style='width: 152px; height:26px;'>
            </select>
            <div class="fromToDate">
                <input type="text" id="fromDate" placeholder="YYYY-MM-DD 📅" size="15" style="text-align: center">
                &nbsp; ~ &nbsp;<input type="text" id="toDate" placeholder="YYYY-MM-DD 📅" size="15"
                                    style="text-align: center">
            </div>
        </form>
        <button id="searchsaleslist">거래내역 조회</button>
         
        <button id="DeliverableContractDelete">삭 제</button>
        &nbsp;&nbsp;
    </div>
</article>
<article class="contractGrid">
    <div align="center">
        <div id="myGrid" class="ag-theme-balham" style="height:50vh; width:auto; text-align: center;"></div>
    </div>
</article>

<div class="modal fade" id="contractType" role="dialog">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">CONTRACT TYPE</h5>
                <button type="button" class="close" data-dismiss="modal" style="padding-top: 0.5px">&times;</button>
            </div>
            <div class="modal-body">
                <div id="contractGrid" class="ag-theme-balham" style="height:500px;width:auto;">
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>
<script>
  const myGrid = document.querySelector("#myGrid");
  const myGrid2 = document.querySelector("#myGrid2");
  const searchByPeriodRadio = document.querySelector("#searchByPeriodRadio");     // 기간검색
  const searchByCustomerRadio = document.querySelector("#searchByCustomerRadio"); // 거래처검색
  const searchCustomerBox = document.querySelector("#searchCustomerBox");
  const fromToDate = document.querySelector(".fromToDate");
  const startDatePicker = document.querySelector("#fromDate"); //  시작일자
  const endDatePicker = document.querySelector("#toDate");     //  종료일자
  const searchsaleslistBtn = document.querySelector("#searchsaleslist"); // 거래내역 조회
  const deliverableContractDeleteBtn = document.querySelector("#DeliverableContractDelete"); // 납품 가능목록 삭제
                         
  
  // O setup the grid after the page has finished loading
  document.addEventListener('DOMContentLoaded', () => {
    getCustomerCode(); // 거래처 select태그 세팅 
    new agGrid.Grid(myGrid, deliverableContractGridOptions);
  })
  
// 기간 검색, 거래처 검색 ==============================================
  searchByPeriodRadio.addEventListener("click", () => { 
    fromToDate.style.display = "inline-block";
    searchCustomerBox.style.display = "none";
  });
  searchByCustomerRadio.addEventListener("click", () => {
    searchCustomerBox.style.display = "inline-block";
    fromToDate.style.display = "none";
  });
  const getCustomerCode = () => { 
     getListData("CL-01");
    setTimeout(() => {
      let data = jsonData;
      let target = searchCustomerBox;
      for (let index of data.detailCodeList) {
        let node = document.createElement("option");
        node.value = index.detailCode;
        let textNode = document.createTextNode(index.detailCodeName);
        node.appendChild(textNode);
        target.appendChild(node);
      }
    }, 100)
  }
// ===============================================================
  // O deliverableContract Grid 첫번째 그리드
  let deliverableContractColumn = [
	  {
		  headerName: '수주일련번호',
	      field: "contractNo",
	      checkboxSelection: true,
	      width: 200,
	      headerCheckboxSelection: false,
	      headerCheckboxSelectionFilteredOnly: true,
	      suppressRowClickSelection: true
	    },
    {headerName: "견적일련번호", field: "estimateNo"},
    {headerName: "수주유형분류", field: "contractTypeName"},
    {headerName: "거래처코드", field: "customerCode", hide: true},
    {headerName: "거래처명", field: "customerName"},
    {headerName: "출고일자", field: "deliveryCompletionData" },
    {headerName: "수주담당자명", field: "empNameInCharge"},
    {headerName: "비고", field: "description"},
    {headerName: "수주유형", field: "contractType", hide: true},
    {headerName: "담당자", field: "personCodeInCharge", hide: true}
  ];
  // o 첫번째 그리드 옵션들
  let deliverableContractRowData = [];
  let deliverableContractGridOptions = {
    columnDefs: deliverableContractColumn,
    rowSelection: 'single',
    rowData: deliverableContractRowData,
    getRowNodeId: function(data) {
      return data.contractNo;
    },
    defaultColDef: {editable: false, resizable : true},
    overlayNoRowsTemplate: "출고 된 리스트가 없습니다.",
    onGridReady: function(event) {// onload 이벤트와 유사 ready 이후 필요한 이벤트 삽입한다.
      event.api.sizeColumnsToFit();
    },
    onGridSizeChanged: function(event) {
      event.api.sizeColumnsToFit();
    },
    getRowStyle: function (param) {
      return {'text-align': 'center'};
    },
  }
 
// 그리드 띄워주고 해당 옵션들 위치 끝====================================================================   
//========================================================
   // 거래내역조회버튼
  const deliverableContract = (searchCondition, startDate, endDate, customerCode) => {
     
    deliverableContractGridOptions.api.setRowData([]);
    ableContractInfo = {"searchCondition":searchCondition,"startDate":startDate,"endDate":endDate,"customerCode":customerCode};
    ableContractInfo=encodeURI(JSON.stringify(ableContractInfo));
    console.log("searchCondition : "+searchCondition);
    console.log("startDate : "+startDate);
    console.log("endDate : "+endDate);
    console.log("customerCode : "+customerCode);
    let xhr = new XMLHttpRequest();
    xhr.open('POST', "${pageContext.request.contextPath}/sales/searchSalesList.do"
        + "?ableContractInfo=" + ableContractInfo,
        true);
    xhr.setRequestHeader('Accept', 'application/json');
    xhr.send();
    xhr.onreadystatechange = () => {
      if (xhr.readyState == 4 && xhr.status == 200) {
        let txt = xhr.responseText;
        txt = JSON.parse(txt);
        console.log(txt);
        if (txt.salesList == "") {
          Swal.fire("알림", "조회 가능 리스트가 없습니다.", "info");
          return;
        } else if (txt.errorCode < 0) {
          Swal.fire("알림", txt.errorMsg, "error");
          return;
        }
        console.log(txt);
        deliverableContractGridOptions.api.setRowData(txt.salesList);
      }
    }
  }
  
  
   const deletecontent = () => {
		 let deletedel=deliverableContractGridOptions.api.getSelectedRows();
		 console.log("deletedel:"+deletedel);
		 let xhr = new XMLHttpRequest();
		 xhr.open('POST', "${pageContext.request.contextPath}/sales/searchDeliverableContractDelete.do"+
		       "?method=searchDeliverableContractDelete&contractNo="+deletedel[0].contractNo,
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
		       
		      	 location.href="${pageContext.request.contextPath}/sales/deliveryInfo.html"; 
		      }
		    }
		  } 
  
  const deliverableContractdel = (searchCondition, startDate, endDate, customerCode) => {
	     
	    deliverableContractGridOptions.api.setRowData([]);
	    ableContractInfo = {"searchCondition":searchCondition,"startDate":startDate,"endDate":endDate,"customerCode":customerCode};
	    ableContractInfo=encodeURI(JSON.stringify(ableContractInfo));
	    console.log("searchCondition : "+searchCondition);
	    console.log("startDate : "+startDate);
	    console.log("endDate : "+endDate);
	    console.log("customerCode : "+customerCode);
	    let xhr = new XMLHttpRequest();
	    xhr.open('POST', "${pageContext.request.contextPath}/sales/searchDeliverableContractList.do"
	        + "?method=searchDeliverableContractDelete"
	        + "&ableContractInfo=" + ableContractInfo,
	        true);
	    xhr.setRequestHeader('Accept', 'application/json');
	    xhr.send();
	    xhr.onreadystatechange = () => {
	      if (xhr.readyState == 4 && xhr.status == 200) {
	        let txt = xhr.responseText;
	        txt = JSON.parse(txt);
	        if (txt.gridRowJson == "") {
	          Swal.fire("알림", "삭제가 완료되었습니다.", "info");
	          return;
	        } else if (txt.errorCode < 0) {
	          Swal.fire("알림", txt.errorMsg, "error");
	          return;
	        }
	        console.log(txt);
	        deliverableContractGridOptions.api.setRowData(txt.gridRowJson);
	      }
	    }
	  }
  
  DeliverableContractDelete.addEventListener("click", () => {			  
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
  
  searchsaleslistBtn.addEventListener("click", () => { 
	    let searchCondition = (searchByPeriodRadio.checked) ? searchByPeriodRadio.value : searchByCustomerRadio.value;
	    //기간검색과 거래처검색
	    
	    let startDate = "";
	    let endDate = "";
	    let customerCode = "";
	    if (searchCondition == 'searchByDate') {
	      if (startDatePicker.value == "" || endDatePicker.value == "") {
	        Swal.fire("입력", "시작일과 종료일을 입력하십시오.", "info");
	        return
	      } else {
	        startDate = startDatePicker.value;
	        endDate = endDatePicker.value;
	      }
	    } else if (searchCondition == 'searchByCustomer'){
	      customerCode = searchCustomerBox.value;
	    }
	    deliverableContract(searchCondition, startDate, endDate, customerCode);
	  });
  
 
</script>
</body>
</html>