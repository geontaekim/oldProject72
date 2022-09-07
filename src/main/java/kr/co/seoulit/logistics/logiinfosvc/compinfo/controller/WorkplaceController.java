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
import kr.co.seoulit.logistics.logiinfosvc.compinfo.to.WorkplaceTO;

@RestController
@RequestMapping("/compinfo/*")
public class WorkplaceController{

	//serviceFacade 참조변수 선언
	@Autowired
	private CompInfoService compInfoService;
	
	
	ModelMap map = null;

	// GSON 라이브러리
	private static Gson gson = new GsonBuilder().serializeNulls().create(); // 속성값이 null 인 속성도 JSON 변환

	@RequestMapping(value = "/searchWorkplace.do", method = RequestMethod.GET)
	public ModelMap searchWorkplaceList(HttpServletRequest request, HttpServletResponse response) {
		
		String companyCode = request.getParameter("companyCode");
		ArrayList<WorkplaceTO> workplaceList = null;

		map = new ModelMap();

		workplaceList = compInfoService.getWorkplaceList(companyCode);
		
		map.put("gridRowJson", workplaceList);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공!");
		
		
		return map;
	}

	@RequestMapping(value = "/batchWorkplaceListProcess.do", method = RequestMethod.GET)
	public ModelMap batchListProcess(HttpServletRequest request, HttpServletResponse response) {

		String batchList = request.getParameter("batchList");

		ArrayList<WorkplaceTO> workplaceList = gson.fromJson(batchList, new TypeToken<ArrayList<WorkplaceTO>>() {
		}.getType());

		map = new ModelMap();

			HashMap<String, Object> resultMap = compInfoService.batchWorkplaceListProcess(workplaceList);

			map.put("result", resultMap);
			map.put("errorCode", 1);
			map.put("errorMsg", "성공!");

		return map;
	}
}
