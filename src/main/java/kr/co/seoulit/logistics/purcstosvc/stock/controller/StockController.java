package kr.co.seoulit.logistics.purcstosvc.stock.controller;


import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import kr.co.seoulit.logistics.purcstosvc.stock.service.StockService;
import kr.co.seoulit.logistics.purcstosvc.stock.to.StockChartTO;
import kr.co.seoulit.logistics.purcstosvc.stock.to.StockLogTO;
import kr.co.seoulit.logistics.purcstosvc.stock.to.StockTO;

@RestController
@RequestMapping("/stock/*")
public class StockController{

	@Autowired
	private StockService stockService;
	
	ModelMap map = null;

	// GSON 라이브러리
	private static Gson gson = new GsonBuilder().serializeNulls().create(); // 속성값이 null 인 속성도 json 변환

	@RequestMapping(value="/searchStockList.do" , method=RequestMethod.POST)
	public ModelMap searchStockList(HttpServletRequest request, HttpServletResponse response) {

		
		map = new ModelMap();
		ArrayList<StockTO> stockList = stockService.getStockList();
		System.out.println("stockList??"+stockList);
		
		map.put("gridRowJson", stockList);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");

		return map;
	}
	


	//이것도 호출하는 jsp가 없다.
	@RequestMapping(value="/searchStockLogList.do" , method=RequestMethod.POST)
	public ModelMap searchStockLogList(HttpServletRequest request, HttpServletResponse response) {

		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		
		map = new ModelMap();
		
		map= stockService.getStockLogList(startDate,endDate);


		return map;
	}
	
	@RequestMapping(value="/warehousing.do" , method=RequestMethod.POST)
	public ModelMap warehousing(HttpServletRequest request, HttpServletResponse response) {

		String orderNoListStr = request.getParameter("orderNoList");

		ArrayList<String> orderNoArr = gson.fromJson(orderNoListStr,new TypeToken<ArrayList<String>>(){}.getType());	

		map = new ModelMap();
		System.out.println("orderNoArr??"+orderNoArr);
		map = stockService.warehousing(orderNoArr);

		return map;
	}
	
	//이것도 호출하는 jsp 페이지가 없음
	@RequestMapping(value="/safetyAllowanceAmountChange.do" , method=RequestMethod.POST)
	public ModelMap safetyAllowanceAmountChange(HttpServletRequest request, HttpServletResponse response) {

		String itemCode  = request.getParameter("itemCode");
		String itemName  = request.getParameter("itemName");
		String safetyAllowanceAmount  = request.getParameter("safetyAllowanceAmount");

		map = new ModelMap();

		map = stockService.changeSafetyAllowanceAmount(itemCode,itemName,safetyAllowanceAmount);

		return map;
	}
	
	@RequestMapping(value="/searchStockChart.do" , method=RequestMethod.POST)
	public ModelMap getStockChart(HttpServletRequest request, HttpServletResponse response) {

		map = new ModelMap();

		ArrayList<StockChartTO> stockList = stockService.getStockChart();

		map.put("gridRowJson", stockList);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");

		return map;
	}
	
	@RequestMapping(value="/searchWarehouseStockList.do" , method=RequestMethod.POST)
	public ModelMap searchWarehouseStockList(HttpServletRequest request, HttpServletResponse response) {

		String warehouseCode  = request.getParameter("warehouseCode");
		
		map = new ModelMap();

		ArrayList<StockTO> stockList = stockService.getWarehouseStockList(warehouseCode);

		map.put("gridRowJson", stockList);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");

		return map;
	}
	
	@RequestMapping(value="/batchStockProcess.do" , method=RequestMethod.POST)
	public ModelMap modifyStockInfo(HttpServletRequest request, HttpServletResponse response) {

		String batchList = request.getParameter("batchList");
		
		ArrayList<StockTO> stockTOList = gson.fromJson(batchList,
							new TypeToken<ArrayList<StockTO>>() {
							}.getType());
		
		map = new ModelMap();
		stockService.batchStockProcess(stockTOList);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");
		
		return map;
	}
}
