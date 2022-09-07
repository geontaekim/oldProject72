package kr.co.seoulit.logistics.logiinfosvc.compinfo.controller;

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

import kr.co.seoulit.logistics.logiinfosvc.compinfo.service.CompInfoService;
import kr.co.seoulit.logistics.logiinfosvc.compinfo.to.CompanyTO;

@RestController
@RequestMapping("/compinfo/*")
public class CompanyController{

	@Autowired
	private CompInfoService compInfoService;
	

	ModelMap map = null;
	
	// GSON 라이브러리
	private static Gson gson = new GsonBuilder().serializeNulls().create(); // 속성값이 null 인 속성도 JSON 변환

	
	@RequestMapping(value = "/searchCompany.do", method = RequestMethod.GET)
	public ModelMap searchCompanyList(HttpServletRequest request, HttpServletResponse response) {
		
		map = new ModelMap();
		System.out.println("@@동기둥기");
		
		ArrayList<CompanyTO> companyList  = compInfoService.getCompanyList();

		map.put("gridRowJson", companyList);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공!");
	
		System.out.println("@@@@@동기@@@@@@");
		System.out.println(companyList.get(0).getCompanyCode());
		System.out.println(companyList.get(0).getCompanyCeoName());
		System.out.println(companyList.get(0).getCompanyName());
		System.out.println("@@@@@동기@@@@@@");
		
		
		return map;
	
	}
	
	@RequestMapping(value = "/batchCompanyListProcess.do", method = RequestMethod.GET)
	public ModelMap batchListProcess(HttpServletRequest request, HttpServletResponse response) {

		String batchList = request.getParameter("batchList");

		ArrayList<CompanyTO> companyList = gson.fromJson(batchList, new TypeToken<ArrayList<CompanyTO>>() {
		}.getType());

		map = new ModelMap();

		HashMap<String, Object> resultMap = compInfoService.batchCompanyListProcess(companyList);

		map.put("result", resultMap);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공!");
		
		return map;
	}
}