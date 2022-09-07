package kr.co.seoulit.logistics.logiinfosvc.compinfo.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import kr.co.seoulit.logistics.logiinfosvc.compinfo.service.CompInfoService;
import kr.co.seoulit.logistics.logiinfosvc.compinfo.to.CustomerTO;


@RequestMapping("/compinfo/*")
public class CustomerController{

	//serviceFacade 참조변수 선언
	@Autowired
	private CompInfoService compInfoService;
	
	
	ModelMap map = null;

	// GSON 라이브러리
	private static Gson gson = new GsonBuilder().serializeNulls().create(); // 속성값이 null 인 속성도 JSON 변환

	@RequestMapping(value = "/searchCustomer.do", method=RequestMethod.GET)
	public ModelMap searchCustomerList(HttpServletRequest request, HttpServletResponse response) {

		String searchCondition = request.getParameter("searchCondition");

		String companyCode = request.getParameter("companyCode");
		String workplaceCode = request.getParameter("workplaceCode");
		String itemGroupCode = request.getParameter("itemGroupCode");
		ArrayList<CustomerTO> customerList = null;
		map = new ModelMap();

		customerList = compInfoService.getCustomerList(searchCondition, companyCode, workplaceCode,itemGroupCode);

		map.put("gridRowJson", customerList);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공!");
		
		return map;
	}

	
	
	@RequestMapping(value = "/batchCustomerListProcess.do", method=RequestMethod.GET)
	public ModelMap batchListProcess(HttpServletRequest request, HttpServletResponse response) {

		String batchList = request.getParameter("batchList");

		ArrayList<CustomerTO> customerList = gson.fromJson(batchList, new TypeToken<ArrayList<CustomerTO>>() {
		}.getType());

		map = new ModelMap();

		HashMap<String, Object> resultMap = compInfoService.batchCustomerListProcess(customerList);

		map.put("result", resultMap);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공!");
		
		return map;
	}

}
