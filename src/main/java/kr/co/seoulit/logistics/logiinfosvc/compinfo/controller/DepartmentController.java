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
import kr.co.seoulit.logistics.logiinfosvc.compinfo.to.DepartmentTO;

@RestController
@RequestMapping("/compinfo/*")
public class DepartmentController{

	@Autowired
	private CompInfoService compInfoService;
	
	ModelMap map = null;

	// GSON 라이브러리
	private static Gson gson = new GsonBuilder().serializeNulls().create(); // 속성값이 null 인 속성도 JSON 변환

	@RequestMapping(value = "/searchDepartment.do", method = RequestMethod.POST)
	public ModelMap searchDepartmentList(HttpServletRequest request, HttpServletResponse response) {

		String searchCondition = request.getParameter("searchCondition");

		String companyCode = request.getParameter("companyCode");

		String workplaceCode = request.getParameter("workplaceCode");

		
		ArrayList<DepartmentTO> departmentList = null;

		map = new ModelMap();

		departmentList = compInfoService.getDepartmentList(searchCondition, companyCode, workplaceCode);

		map.put("gridRowJson", departmentList);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공!");
		
		return map;
	}

	@RequestMapping(value = "/batchDepartmentListProcess.do", method = RequestMethod.POST)
	public ModelMap batchListProcess(HttpServletRequest request, HttpServletResponse response) {

		String batchList = request.getParameter("batchList");

		ArrayList<DepartmentTO> departmentList = gson.fromJson(batchList, new TypeToken<ArrayList<DepartmentTO>>() {
		}.getType());

		map = new ModelMap();

		HashMap<String, Object> resultMap = compInfoService.batchDepartmentListProcess(departmentList);

		map.put("result", resultMap);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공!");
			
		return map;
	}
}
