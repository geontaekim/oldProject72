package kr.co.seoulit.logistics.logiinfosvc.logiinfo.controller;

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

import kr.co.seoulit.logistics.logiinfosvc.logiinfo.service.LogiInfoService;
import kr.co.seoulit.logistics.logiinfosvc.logiinfo.to.WarehouseTO;

@RestController
@RequestMapping("/logiinfo/*")
public class WarehouseController{

	@Autowired
	private LogiInfoService logiInfoService;
	
	
	ModelMap map = null;

	// GSON 라이브러리
	private static Gson gson = new GsonBuilder().serializeNulls().create();
	
	@RequestMapping(value = "/searchwarehouseList.do", method = RequestMethod.POST)
	public ModelMap getWarehouseList(HttpServletRequest request, HttpServletResponse response) {
		
		map = new ModelMap();
		
		ArrayList<WarehouseTO> WarehouseTOList = logiInfoService.getWarehouseInfoList();
		map.put("gridRowJson", WarehouseTOList);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");
		
		return map;
	}

	
	@RequestMapping(value = "/batchwarehouseProcess.do", method = RequestMethod.POST)
	public ModelMap modifyWarehouseInfo(HttpServletRequest request, HttpServletResponse response) {

		String batchList = request.getParameter("batchList");
		
		map = new ModelMap();
			
		ArrayList<WarehouseTO> warehouseTOList = gson.fromJson(batchList,
				new TypeToken<ArrayList<WarehouseTO>>() {
				}.getType());
			 
		logiInfoService.batchWarehouseInfo(warehouseTOList);
			
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");

		return map;
	}
	
	@RequestMapping(value = "/warehousePageList.do", method = RequestMethod.POST)
	public ModelMap findLastWarehouseCode(HttpServletRequest request, HttpServletResponse response){

		map = new ModelMap();
		
		String warehouseCode = logiInfoService.findLastWarehouseCode();
			
		map.put("lastWarehouseCode", warehouseCode);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");

		return map;
	}
}
