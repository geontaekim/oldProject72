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
import kr.co.seoulit.logistics.logiinfosvc.compinfo.to.FinancialAccountAssociatesTO;

@RestController
@RequestMapping("/compinfo/*")
public class FinancialAccountAssociatesController{


	@Autowired
	private CompInfoService compInfoService;
	
	
	ModelMap map = null;

	// GSON 라이브러리
	private static Gson gson = new GsonBuilder().serializeNulls().create(); // 속성값이 null 인 속성도 JSON 변환

	
	@RequestMapping(value = "/searchFinancialAccountAssociatesList.do", method = RequestMethod.POST)
	public ModelMap searchFinancialAccountAssociatesList(HttpServletRequest request, HttpServletResponse response) {

		String searchCondition = request.getParameter("searchCondition");

		String workplaceCode = request.getParameter("workplaceCode");

		ArrayList<FinancialAccountAssociatesTO> financialAccountAssociatesList = null;

		map = new ModelMap();

			financialAccountAssociatesList = compInfoService.getFinancialAccountAssociatesList(searchCondition,
					workplaceCode);

			map.put("gridRowJson", financialAccountAssociatesList);
			map.put("errorCode", 1);
			map.put("errorMsg", "성공!");

			return map;
	}

	@RequestMapping(value = "/batchFinancialAccountAssociatesListProcess.do", method = RequestMethod.POST)
	public ModelMap batchListProcess(HttpServletRequest request, HttpServletResponse response) {

		String batchList = request.getParameter("batchList");

		ArrayList<FinancialAccountAssociatesTO> financialAccountAssociatesList = gson.fromJson(batchList,
				new TypeToken<ArrayList<FinancialAccountAssociatesTO>>() {
				}.getType());

		map = new ModelMap();

			HashMap<String, Object> resultMap = compInfoService
					.batchFinancialAccountAssociatesListProcess(financialAccountAssociatesList);

			map.put("result", resultMap);
			map.put("errorCode", 1);
			map.put("errorMsg", "성공!");

			return map;
	}

}
