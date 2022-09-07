package kr.co.seoulit.logistics.purcstosvc.stock.controller;

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

import kr.co.seoulit.logistics.purcstosvc.stock.service.StockService;
import kr.co.seoulit.logistics.purcstosvc.stock.to.BomDeployTO;
import kr.co.seoulit.logistics.purcstosvc.stock.to.BomInfoTO;
import kr.co.seoulit.logistics.purcstosvc.stock.to.BomTO;

@RestController
@RequestMapping("/stock/*")
public class BomController{

	@Autowired
	private StockService stockService;
	
	
	ModelMap map = null;

	// GSON 라이브러리
	private static Gson gson = new GsonBuilder().serializeNulls().create();

	@RequestMapping(value="/searchBomDeploy.do", method=RequestMethod.POST)
	public ModelMap searchBomDeploy(HttpServletRequest request, HttpServletResponse response) {

		String deployCondition = request.getParameter("deployCondition");
		
		String itemCode = request.getParameter("itemCode");
		
		String itemClassificationCondition = request.getParameter("itemClassificationCondition");
		
		map = new ModelMap();

		ArrayList<BomDeployTO> bomDeployList = stockService.getBomDeployList(deployCondition, itemCode, itemClassificationCondition);

		map.put("gridRowJson", bomDeployList);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");

		return map;
	}
	
	@RequestMapping(value="/searchBomInfo.do", method=RequestMethod.POST)
	public ModelMap searchBomInfo(HttpServletRequest request, HttpServletResponse response) {

		String parentItemCode = request.getParameter("parentItemCode");

		map = new ModelMap();

		ArrayList<BomInfoTO> bomInfoList = stockService.getBomInfoList(parentItemCode);

		map.put("gridRowJson", bomInfoList);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");

		return map;

	}

	//이것도 호출하는 jsp페이지가 없다. 그냥 70기꺼 복붙
	@RequestMapping(value="/searchAllItemWithBomRegisterAvailable.do", method=RequestMethod.POST)
	public ModelMap searchAllItemWithBomRegisterAvailable(HttpServletRequest request,
			HttpServletResponse response) {

		map = new ModelMap();

		ArrayList<BomInfoTO> allItemWithBomRegisterAvailable = stockService.getAllItemWithBomRegisterAvailable();

		map.put("gridRowJson", allItemWithBomRegisterAvailable);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");

		return map;

	}

	
	@RequestMapping(value="/batchBomListProcess.do", method=RequestMethod.POST)
	public ModelMap batchBomListProcess(HttpServletRequest request, HttpServletResponse response) {

		String batchList = request.getParameter("batchList");
		
		ArrayList<BomTO> batchBomList = gson.fromJson(batchList, new TypeToken<ArrayList<BomTO>>() {
		}.getType());

		map = new ModelMap();

		HashMap<String, Object> resultList = stockService.batchBomListProcess(batchBomList);

		map.put("result", resultList);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");

		return map;

	}
}