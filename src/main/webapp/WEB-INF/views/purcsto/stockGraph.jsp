<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="https://code.jquery.com/jquery-3.5.0.js"></script>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
    
	  let stockChart;		// 가공된 데이터를 넣을 전역변수
	  
      google.charts.load('current', {'packages':['corechart', 'bar']});
      google.charts.setOnLoadCallback(drawStuff);
      
      /* ajax로 받아와보기 */
      $.ajax({
			url : "${pageContext.request.contextPath}/stock/searchStockChart.do",
		    dataType : "json",
		    method : "POST",
		    success : (data)=>{
		    	console.log(typeof data.gridRowJson);
		    	stockData=data.gridRowJson;		
		    		// data={ gridRowJson:[{d:b,c:d...}],errorCode:n,errorMsg:n}
		    		/* 받은 JSON을 구글 chart에 맞는 Array형성하기 */
		    		stockChart = stockData.map((obj)=>{
		    		const datas = [];
		    		datas.push(obj.itemName);
		    		datas.push(parseInt(obj.stockAmount));
		    		datas.push(parseInt(obj.saftyAmount));
		    		datas.push(parseInt(obj.allowanceAmount));
		    		return datas;
		    	})
		    	stockChart.unshift([ '품목', '전체 재고','안전 재고','사용가능 재고']);
		    	
		    	if(data.errorCode != 1 ){
	                swal({
	                    text: '데이터를 불러드리는데 오류가 발생했습니다.'
	                    	  +data.errorMsg ,
	                    icon: 'error',
	                });
	                return;
		    	}
		    }
		})
		
      google.charts.load('current', {'packages':['corechart', 'bar']});
      google.charts.setOnLoadCallback(drawStuff);
      function drawStuff() {

        var button = document.getElementById('change-chart');
        var chartDiv = document.getElementById('chart_div');
        var data = google.visualization.arrayToDataTable(stockChart);

        var materialOptions = {
          width: 3300,
          chart: {
            title: '품목별 현 재고량',
            subtitle: '전체재고, 안전재고, 가용재고 도출 그래프'
          },
          axes: {
            y: {
              distance: {label: 'parsecs'}, // Left y-axis.
              brightness: {side: 'right', label: 'apparent magnitude'}, // Right y-axis.
              fuc: {side: 'right', label:'parsecs'}
            }
          },
        titleTextStyle:{
            fontName : 'Arial',
            bold: false,
            fontSize: 20
            },
          animation:{
        	  startup: true,
        	  duration: 1000,
        	  easing: 'out'
          }
        };

        var classicOptions = {
          width: 1300,
          title: '- 품목별 현 재고량 - 전체재고, 안전재고, 가용재고 도출 그래프 ',
          vAxes: {
            // Adds titles to each axis.
            0: {title: ''},
            1: {title: ''}
          },          
       	 titleTextStyle:{
            fontName : 'Arial',
            bold: false,
            fontSize: 20,
            color : '#1E90FF'
             },
          animation:{
        	  startup: true,
        	  duration: 1000,
        	  easing: 'out'
          }
        };

        function drawMaterialChart() {
          var materialChart = new google.charts.Bar(chartDiv);	
          materialChart.draw(data, google.charts.Bar.convertOptions(materialOptions));
          button.innerText = '포괄 전개';
          button.onclick = drawClassicChart;
        }

        function drawClassicChart() {
          var classicChart = new google.visualization.ColumnChart(chartDiv);
          classicChart.draw(data, classicOptions);
          button.innerText = '상세 전개';
          button.onclick = drawMaterialChart;
        }

        drawClassicChart();
    };
    </script>
</head>
<body>

	
    <button id="change-chart">상세 전개</button>
  	<button id="go-covid" onClick="location.href='${pageContext.request.contextPath}/hello2.html'" >코로나 현황</button>
  	<button id="how-to-visit" onClick="location.href='${pageContext.request.contextPath}/hello4.html'" >오시는 길</button>
    <br/>
    <div id="chart_div" style="width: auto; height: 60%;"></div>


</body>
</html>