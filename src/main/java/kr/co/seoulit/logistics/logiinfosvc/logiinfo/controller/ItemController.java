package kr.co.seoulit.logistics.logiinfosvc.logiinfo.controller;

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

import kr.co.seoulit.logistics.logiinfosvc.logiinfo.service.LogiInfoService;
import kr.co.seoulit.logistics.logiinfosvc.logiinfo.to.ItemGroupTO;
import kr.co.seoulit.logistics.logiinfosvc.logiinfo.to.ItemInfoTO;
import kr.co.seoulit.logistics.logiinfosvc.logiinfo.to.ItemTO;


@RestController
@RequestMapping("/logiinfo/*")
public class ItemController{

	@Autowired
	private LogiInfoService logiInfoService;
	
	
	ModelMap map = null;

	// GSON 라이브러리
	private static Gson gson = new GsonBuilder().serializeNulls().create(); // 속성값이 null 인 속성도 JSON 변환

	@RequestMapping(value="/searchItem.do", method=RequestMethod.POST)
	public ModelMap searchItem(HttpServletRequest request, HttpServletResponse response) {

		String searchCondition = request.getParameter("searchCondition");
		String itemClassification = request.getParameter("itemClassification");
		String itemGroupCode = request.getParameter("itemGroupCode");
		String minPrice = request.getParameter("minPrice");
		String maxPrice = request.getParameter("maxPrice");

		ArrayList<ItemInfoTO> itemInfoList = null;
		String[] paramArray = null;

		map = new ModelMap();

		switch (searchCondition) {

		case "ALL":

			paramArray = null;
			break;

		case "ITEM_CLASSIFICATION":

			paramArray = new String[] { itemClassification };
			break;

		case "ITEM_GROUP_CODE":

			paramArray = new String[] { itemGroupCode };
			break;

		case "STANDARD_UNIT_PRICE":

			paramArray = new String[] { minPrice, maxPrice };
			break;

		}

		itemInfoList = logiInfoService.getItemInfoList(searchCondition, paramArray);

		map.put("gridRowJson", itemInfoList);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");

		return map;
	}
	
	@RequestMapping(value="/getStandardUnitPrice.do", method=RequestMethod.POST)
	public ModelMap getStandardUnitPrice(HttpServletRequest request, HttpServletResponse response) {

		String itemCode = request.getParameter("itemCode");

		int price = 0;
		
		map = new ModelMap();

		price = logiInfoService.getStandardUnitPrice(itemCode);

		map.put("gridRowJson", price);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");

		return map;
	}
	
	@RequestMapping(value="/getStandardUnitPriceBox.do", method=RequestMethod.POST)
	public ModelMap getStandardUnitPriceBox(HttpServletRequest request, HttpServletResponse response) {

		String itemCode = request.getParameter("itemCode");
	

		int price = 0;
		
		map = new ModelMap();

		price = logiInfoService.getStandardUnitPriceBox(itemCode);

		map.put("gridRowJson", price);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");

		return map;
	}
	
	@RequestMapping(value="/batchItemListProcess.do", method=RequestMethod.POST)
	public ModelMap batchListProcess(HttpServletRequest request, HttpServletResponse response) {

		String batchList = request.getParameter("batchList");

		ArrayList<ItemTO> itemTOList = gson.fromJson(batchList, new TypeToken<ArrayList<ItemTO>>() {
		}.getType());

		map = new ModelMap();

		ModelMap resultMap = logiInfoService.batchItemListProcess(itemTOList);

		map.put("result", resultMap);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");

		return map;
	}	

	//품목상세 조회
	@RequestMapping(value="/searchitemInfoList.do" , method=RequestMethod.POST)
	public ModelMap searchitemInfoList(HttpServletRequest request, HttpServletResponse response) {

		String ableContractInfo =request.getParameter("ableContractInfo");
		
		map = new ModelMap();

		HashMap<String,String> ableSearchConditionInfo = gson.fromJson(ableContractInfo, new TypeToken<HashMap<String,String>>() {
		}.getType());

		ArrayList<ItemInfoTO> itemCodeList = null;

		itemCodeList = logiInfoService.getitemInfoList(ableSearchConditionInfo);

		map.put("gridRowJson", itemCodeList);
		map.put("errorCode", 0);
		map.put("errorMsg", "성공");

		return map;
	}

	//품목그룹조회
	@RequestMapping(value="/searchitemGroupList.do" , method=RequestMethod.POST)
	public ModelMap searchitemGroupList(HttpServletRequest request, HttpServletResponse response) {

		map = new ModelMap();

		String ableContractInfo =request.getParameter("ableContractInfo");

		HashMap<String,String> ableSearchConditionInfo = gson.fromJson(ableContractInfo, new TypeToken<HashMap<String,String>>() {
		}.getType());

		ArrayList<ItemGroupTO> itemGroupList = null;

		itemGroupList = logiInfoService.getitemGroupList(ableSearchConditionInfo);

		map.put("gridRowJson", itemGroupList);
		map.put("errorCode", 0);
		map.put("errorMsg", "성공");

		return map;
	}


	  //품목그룹삭제
	@RequestMapping(value="/deleteitemgroup.do" , method=RequestMethod.POST)
	public ModelMap deleteitemgroup(HttpServletRequest request, HttpServletResponse response) { 

		map = new ModelMap();
		
		String ableContractInfo =request.getParameter("ableContractInfo");
		
		HashMap<String,String> ableSearchConditionInfo =
		gson.fromJson(ableContractInfo, new TypeToken<HashMap<String,String>>() {
		}.getType());
		
		logiInfoService.getdeleteitemgroup(ableSearchConditionInfo);
		
		map.put("errorCode", 0);
		map.put("errorMsg", "성공");

		return map;
	}


	//일괄저장
	@RequestMapping(value="/batchSave.do" , method=RequestMethod.POST)
	public ModelMap batchSave(HttpServletRequest request, HttpServletResponse response) {
		
		map = new ModelMap();
		
		String ableContractInfo =request.getParameter("ableContractInfo");
		
		ArrayList<ItemInfoTO> itemTOList = gson.fromJson(ableContractInfo, new TypeToken<ArrayList<ItemInfoTO>>() {
		}.getType());

		logiInfoService.getbatchSave(itemTOList);
		
		map.put("errorCode", 0);
		map.put("errorMsg", "성공");

		return map;
	}
}
