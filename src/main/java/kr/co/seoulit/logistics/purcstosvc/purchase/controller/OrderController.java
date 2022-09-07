package kr.co.seoulit.logistics.purcstosvc.purchase.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import kr.co.seoulit.logistics.purcstosvc.purchase.service.PurchaseService;
import kr.co.seoulit.logistics.purcstosvc.purchase.to.OrderInfoTO;

@RestController
@RequestMapping("/purchase/*")
public class OrderController{
	
	@Autowired
	private PurchaseService purchaseService;
	
	
	ModelMap map = null;

	// GSON 라이브러리
	private static Gson gson = new GsonBuilder().serializeNulls().create(); // 속성값이 null 인 속성도 json 변환
	
	@RequestMapping(value="/checkOrderInfo.do" , method=RequestMethod.POST)
	public ModelMap checkOrderInfo(HttpServletRequest request, HttpServletResponse response) {

		map = new ModelMap();
		
		String orderNoListStr = request.getParameter("orderNoList");
		System.out.println("orderNoListStr??"+orderNoListStr);
		
		ArrayList<String> orderNoArr = gson.fromJson(orderNoListStr,new TypeToken<ArrayList<String>>(){}.getType());
			
		map=purchaseService.checkOrderInfo(orderNoArr);

		return map;
		
	}
	
	@RequestMapping(value="/getOrderList.do" , method=RequestMethod.POST)
	public ModelMap getOrderList(HttpServletRequest request, HttpServletResponse response) {

		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		System.out.println("startDate??"+startDate);
		System.out.println("endDate??"+endDate);
		
		map = new ModelMap();

	 map = purchaseService.getOrderList(startDate, endDate); 

		return map;
	}

	
	@RequestMapping(value="/showOrderDialog.do" , method=RequestMethod.POST)
	public ModelMap openOrderDialog(HttpServletRequest request, HttpServletResponse response) {

		String mrpNoListStr = request.getParameter("mrpGatheringNoList");
		System.out.println("mrpNoListStr가뭔데??"+mrpNoListStr);

		map = new ModelMap();

		map = purchaseService.getOrderDialogInfo(mrpNoListStr);
		
		return map;
		
	}
	
	//이것도 jsp페이지에서 요청하는 부분이 없다. ->70기꺼 복붙
	@RequestMapping(value="/showOrderInfo.do" , method=RequestMethod.POST)
	public ModelMap showOrderInfo(HttpServletRequest request, HttpServletResponse response) {

		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		
		map = new ModelMap();
		
		ArrayList<OrderInfoTO> orderInfoList = purchaseService.getOrderInfoList(startDate,endDate);
		
		map.put("gridRowJson", orderInfoList);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");

		return map;
	}
	
	//이것도 jsp페이지에서 요청하는 부분이 없다. ->70기꺼 복붙
	@RequestMapping(value="/searchOrderInfoListOnDelivery.do" , method=RequestMethod.POST)
	public ModelMap searchOrderInfoListOnDelivery(HttpServletRequest request, HttpServletResponse response) {

		map = new ModelMap();

		ArrayList<OrderInfoTO> orderInfoListOnDelivery = purchaseService.getOrderInfoListOnDelivery();
		
		map.put("gridRowJson", orderInfoListOnDelivery);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");

		return map;
	}

	
	@RequestMapping(value="/order.do" , method=RequestMethod.POST)
	public ModelMap order(HttpServletRequest request, HttpServletResponse response) {

		String mrpGatheringNoListStr = request.getParameter("mrpGatheringNoList");
		System.out.println("mrpGatheringNoListStr??"+mrpGatheringNoListStr);
		
		map = new ModelMap();
		
		 ArrayList<String> mrpGaNoArr = gson.fromJson(mrpGatheringNoListStr , new TypeToken<ArrayList<String>>() {
	      }.getType());
		 
		 System.out.println("mrpGaNoArr??"+mrpGaNoArr);
		 map = purchaseService.order(mrpGaNoArr);

		return map;
	}

	//07-12 order취합발주하는거 
	//그다음꺼 둘다안됨
	
	
	@RequestMapping(value="/optionOrder.do" , method=RequestMethod.POST)
	public ModelMap optionOrder(HttpServletRequest request, HttpServletResponse response) {

		map = new ModelMap();

		String itemCode = request.getParameter("itemCode");
		String itemAmount = request.getParameter("itemAmount");

		map = purchaseService.optionOrder(itemCode, itemAmount);

		return map;
	}
}