package kr.co.seoulit.logistics.busisvc.sales.controller;

import java.util.ArrayList;
import java.util.HashMap;

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

import kr.co.seoulit.logistics.busisvc.sales.service.SalesService;
import kr.co.seoulit.logistics.busisvc.sales.to.SalesPlanTO;

@RestController
@RequestMapping("/sales/*")
public class SalesPlanController{

	@Autowired
	private SalesService salesService;
	
	ModelMap map = null;

	private static Gson gson = new GsonBuilder().serializeNulls().create(); // 속성값이 null 인 속성도 json 변환

	@RequestMapping(value="/searchSalesPlan.do", method=RequestMethod.POST)
	public ModelMap searchSalesPlanInfo(HttpServletRequest request, HttpServletResponse response) {

		map = new ModelMap();
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String dateSearchCondition = request.getParameter("dateSearchCondition");

		ArrayList<SalesPlanTO> salesPlanTOList = salesService.getSalesPlanList(dateSearchCondition, startDate, endDate);

		map.put("gridRowJson", salesPlanTOList);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");

		return map;
	}

	@RequestMapping(value="/batchSalesPlanListProcess.do", method=RequestMethod.POST)
	public ModelMap batchListProcess(HttpServletRequest request, HttpServletResponse response) {

		map = new ModelMap();

		String batchList = request.getParameter("batchList");
		
		ArrayList<SalesPlanTO> salesPlanTOList = gson.fromJson(batchList, new TypeToken<ArrayList<SalesPlanTO>>() {
		}.getType());

		HashMap<String, Object> resultMap = salesService.batchSalesPlanListProcess(salesPlanTOList);

		map.put("result", resultMap);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");

		return map;
	}
}
