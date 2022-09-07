package kr.co.seoulit.logistics.prodcsvc.production.controller;

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

import kr.co.seoulit.logistics.busisvc.logisales.to.ContractDetailInMpsAvailableTO;
import kr.co.seoulit.logistics.prodcsvc.production.service.ProductionService;
import kr.co.seoulit.logistics.prodcsvc.production.to.MpsTO;
import kr.co.seoulit.logistics.prodcsvc.production.to.SalesPlanInMpsAvailableTO;

@RestController
@RequestMapping("/production/*")
public class MpsController{

	@Autowired
	private ProductionService productionService;
	
	
	ModelMap map = null;

	// gson 라이브러리
	private static Gson gson = new GsonBuilder().serializeNulls().create(); // 속성값이 null 인 속성도 json 변환

	@RequestMapping(value="/searchMpsInfo.do", method=RequestMethod.POST)
	public ModelMap searchMpsInfo(HttpServletRequest request, HttpServletResponse response) {

		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String includeMrpApply = request.getParameter("includeMrpApply");
		
   		map = new ModelMap();

		ArrayList<MpsTO> mpsTOList = productionService.getMpsList(startDate, endDate, includeMrpApply);

		map.put("gridRowJson", mpsTOList);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");

		return map;
	}

	
	@RequestMapping(value="/searchContractDetailInMpsAvailable.do", method=RequestMethod.POST)
	public ModelMap searchContractDetailListInMpsAvailable(HttpServletRequest request,
			HttpServletResponse response) {

		String searchCondition = request.getParameter("searchCondition");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");

		map = new ModelMap();

		ArrayList<ContractDetailInMpsAvailableTO> contractDetailInMpsAvailableList = productionService
				.getContractDetailListInMpsAvailable(searchCondition, startDate, endDate);

		map.put("gridRowJson", contractDetailInMpsAvailableList);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");

		return map;
	}

	
	//jsp페이지에서 호출하는 부분이 없다.
	@RequestMapping(value="/searchSalesPlanInMpsAvailable.do", method=RequestMethod.POST)
	public ModelMap searchSalesPlanListInMpsAvailable(HttpServletRequest request, HttpServletResponse response) {

		String searchCondition = request.getParameter("searchCondition");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");

		map = new ModelMap();

		ArrayList<SalesPlanInMpsAvailableTO> salesPlanInMpsAvailableList = productionService
				.getSalesPlanListInMpsAvailable(searchCondition, startDate, endDate);

		map.put("gridRowJson", salesPlanInMpsAvailableList);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");

		return map;
	}

	
	@RequestMapping(value="/convertContractDetailToMps.do", method=RequestMethod.POST)
	public ModelMap convertContractDetailToMps(HttpServletRequest request, HttpServletResponse response) {

		String batchList = request.getParameter("batchList");
		
		ArrayList<ContractDetailInMpsAvailableTO> contractDetailInMpsAvailableList = gson.fromJson(batchList,
				new TypeToken<ArrayList<ContractDetailInMpsAvailableTO>>() {
				}.getType());

		map = new ModelMap();

		HashMap<String, Object> resultMap = productionService
				.convertContractDetailToMps(contractDetailInMpsAvailableList);

		map.put("result", resultMap);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");

		return map;
	}

	//jsp에서 호출하는 부분이 없다.
	@RequestMapping(value="/convertSalesPlanToMps.do", method=RequestMethod.POST)
	public ModelMap convertSalesPlanToMps(HttpServletRequest request, HttpServletResponse response) {

		String batchList = request.getParameter("batchList");

		ArrayList<SalesPlanInMpsAvailableTO> salesPlanInMpsAvailableList = gson.fromJson(batchList,
				new TypeToken<ArrayList<SalesPlanInMpsAvailableTO>>() {
				}.getType());

		map = new ModelMap();


		HashMap<String, Object> resultMap = productionService.convertSalesPlanToMps(salesPlanInMpsAvailableList);

		map.put("result", resultMap);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");

		return map;
	}
}