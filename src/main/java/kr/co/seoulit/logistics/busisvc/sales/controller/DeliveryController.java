package kr.co.seoulit.logistics.busisvc.sales.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

import kr.co.seoulit.logistics.busisvc.sales.service.SalesService;
import kr.co.seoulit.logistics.busisvc.logisales.to.ContractDetailTO;
import kr.co.seoulit.logistics.busisvc.logisales.to.ContractInfoTO;
import kr.co.seoulit.logistics.busisvc.sales.to.DeliveryInfoTO;

@RestController
@RequestMapping("/sales/*")
public class DeliveryController{
	
	@Autowired
	private SalesService salesService;
	
	
	ModelMap map=null;

	private static Gson gson = new GsonBuilder().serializeNulls().create(); // 속성값이 null 인 속성도 변환

	@RequestMapping(value="/searchDeliveryInfoList.do" ,method=RequestMethod.POST)
	public ModelMap searchDeliveryInfoList(HttpServletRequest request, HttpServletResponse response) {

		map = new ModelMap();

		ArrayList<DeliveryInfoTO> deliveryInfoList = salesService.getDeliveryInfoList();
		map.put("gridRowJson", deliveryInfoList);
		map.put("errorCode", 0);
		map.put("errorMsg", "성공");

		return map;
		
	}

	@RequestMapping(value="/batchListProcess.do" ,method=RequestMethod.POST)
	public ModelMap batchListProcess(HttpServletRequest request, HttpServletResponse response) {

		map = new ModelMap();

		String batchList = request.getParameter("batchList");

		List<DeliveryInfoTO> deliveryTOList = gson.fromJson(batchList, new TypeToken<ArrayList<DeliveryInfoTO>>() {
		}.getType());

		HashMap<String, Object> resultMap = salesService.batchDeliveryListProcess(deliveryTOList);

		map.put("result", resultMap);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");

		return map;
	}

	@RequestMapping(value="/searchDeliverableContractList.do" ,method=RequestMethod.POST)
	public ModelMap searchDeliverableContractList(HttpServletRequest request, HttpServletResponse response) {
		
		map = new ModelMap();

		String ableContractInfo =request.getParameter("ableContractInfo");
			
		HashMap<String,String> ableSearchConditionInfo = gson.fromJson(ableContractInfo, new TypeToken<HashMap<String,String>>() {
		}.getType());
		
		ArrayList<ContractInfoTO> deliverableContractList = null;
		
		deliverableContractList = salesService.getDeliverableContractList(ableSearchConditionInfo);
		
		map.put("gridRowJson", deliverableContractList);
		map.put("errorCode", 0);
		map.put("errorMsg", "성공");

		return map;
	}
	
	@RequestMapping(value="/searchSalesList.do" ,method=RequestMethod.POST)
	public ModelMap searchSalesHistoryList(HttpServletRequest request, HttpServletResponse response) {
		

		String salesInfo =request.getParameter("ableContractInfo");
		System.out.println("히스토리salesInfo은?"+salesInfo);
			
		map = new ModelMap();
		
		HashMap<String,String> sales = gson.fromJson(salesInfo, new TypeToken<HashMap<String,String>>() {
		}.getType());
			
			ArrayList<ContractInfoTO> salesList = null;
			
			salesList = salesService.getSalesContractList(sales); 
			
			map.put("salesList", salesList);
			map.put("errorCode", 0);
			map.put("errorMsg", "성공");

	
		return map;
	}

	@RequestMapping(value="/deliver.do" ,method=RequestMethod.POST)
	public ModelMap deliver(ContractDetailTO detail) {

		map = new ModelMap();
		
		
		System.out.println("detail?"+detail);
		map = salesService.deliver(detail.getContractDetailNo()); 
			
		return map;
	}
	
	/*
	 * @RequestMapping(value="/searchDeliverableContractList.do"
	 * ,method=RequestMethod.POST) public ModelMap
	 * searchDeliverableContractList(HttpServletRequest request, HttpServletResponse
	 * response) {
	 * 
	 * map = new ModelMap();
	 * 
	 * ArrayList<DeliveryInfoTO> deliveryInfoList =
	 * salesService.getDeliveryInfoList(); map.put("gridRowJson", deliveryInfoList);
	 * map.put("errorCode", 0); map.put("errorMsg", "성공");
	 * 
	 * return map;
	 * 
	 * }
	 */
	
	

}
