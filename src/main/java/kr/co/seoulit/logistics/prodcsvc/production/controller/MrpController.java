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

import kr.co.seoulit.logistics.prodcsvc.production.service.ProductionService;
import kr.co.seoulit.logistics.prodcsvc.production.to.MrpGatheringTO;
import kr.co.seoulit.logistics.prodcsvc.production.to.MrpTO;

@RestController
@RequestMapping("/production/*")
public class MrpController{

	@Autowired
	private ProductionService productionService;
	
	
	ModelMap map = null;
	
	// gson 라이브러리
	private static Gson gson = new GsonBuilder().serializeNulls().create(); // 속성값이 null 인 속성도 json 변환	
	
	
	@RequestMapping(value="/getMrpList.do", method=RequestMethod.POST)
	public ModelMap getMrpList(HttpServletRequest request, HttpServletResponse response) {

		String mrpGatheringStatusCondition = request.getParameter("mrpGatheringStatusCondition");	
		String dateSearchCondition = request.getParameter("dateSearchCondition");
		String mrpStartDate = request.getParameter("mrpStartDate");
		String mrpEndDate = request.getParameter("mrpEndDate");
		String mrpGatheringNo = request.getParameter("mrpGatheringNo");
		System.out.println("mrpGatheringNo???"+mrpGatheringNo);
	
		map = new ModelMap();
		
		ArrayList<MrpTO> mrpList = null;
			
		
		if(mrpGatheringStatusCondition != null ) {
				
			mrpList = productionService.searchMrpList(mrpGatheringStatusCondition);
				
		} else if (dateSearchCondition != null) {
				
			mrpList = productionService.searchMrpList(dateSearchCondition, mrpStartDate, mrpEndDate);
				
		} else if (mrpGatheringNo != null) {
				
			mrpList = productionService.searchMrpListAsMrpGatheringNo(mrpGatheringNo);
				
		}
			
		map.put("gridRowJson", mrpList);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");

		return map;
	}
	
	@RequestMapping(value="/openMrp.do", method=RequestMethod.POST)
	public ModelMap openMrp(HttpServletRequest request, HttpServletResponse response) {
		
		String mpsNoListStr = request.getParameter("mpsNoList"); 
		System.out.println("mpsNoListStr??"+mpsNoListStr);
		
		ArrayList<String> mpsNoArr = gson.fromJson(mpsNoListStr,
				new TypeToken<ArrayList<String>>() { }.getType());
		
		 map = new ModelMap();
		 map = productionService.openMrp(mpsNoArr); 
		 
		 System.out.println("MRPmap에는?"+map);
		
		return map;
	}

	//jsp에서 요청하는 부분이 없다.
	@RequestMapping(value="/registerMrp.do", method=RequestMethod.POST)
	public ModelMap registerMrp(HttpServletRequest request, HttpServletResponse response) {

		String batchList = request.getParameter("batchList");
		String mrpRegisterDate = request.getParameter("mrpRegisterDate");
		System.out.println("batchList??"+batchList);
		System.out.println("mrpRegisterDate??"+mrpRegisterDate);
		ArrayList<String> mpsList	= gson.fromJson(batchList, 
				new TypeToken<ArrayList<String>>() { }.getType());
		
		map = new ModelMap();

		map = productionService.registerMrp(mrpRegisterDate, mpsList); 	 
		
		System.out.println("MRP컨틀로러의 map"+map);
		
		return map;
	}
	
	//jsp에서 요청하는 부분이 없다.
		@RequestMapping(value="/registerMrpGathering.do", method=RequestMethod.POST)
		public ModelMap registerMrpGathering(HttpServletRequest request, HttpServletResponse response) {

			String mrpGatheringRegisterDate = request.getParameter("mrpGatheringRegisterDate"); //선택한날짜
			String mrpNoList = request.getParameter("mrpNoList");
			String mrpNoAndItemCodeList = request.getParameter("mrpNoAndItemCodeList");
			
			
			System.out.println("mrpGatheringRegisterDate??"+mrpGatheringRegisterDate);
			System.out.println("mrpNoList??"+mrpNoList);
			System.out.println("mrpNoAndItemCodeList??"+mrpNoAndItemCodeList);
			ArrayList<String> mrpNoArr = gson.fromJson(mrpNoList,         
					new TypeToken<ArrayList<String>>() { }.getType());
			
			System.out.println("뭐가다른거지?mrpNoArr??"+mrpNoArr);  	//문자열의 ""가 없어짐  ex) "RP20220708-082" >> RP20220708-082
			HashMap<String, String> mrpNoAndItemCodeMap =  gson.fromJson(mrpNoAndItemCodeList, //mprNO : ItemCode 
	              new TypeToken<HashMap<String, String>>() { }.getType());
			System.out.println("컨트롤러에서mrpNoAndItemCodeMap??"+mrpNoAndItemCodeMap);
			map = new ModelMap();
			map.put("mrpGatheringRegisterDate",mrpGatheringRegisterDate);
			map.put("mrpNoArr",mrpNoArr);
			map.put("mrpGatheringRegisterDate",mrpGatheringRegisterDate);
			
			map  = productionService.registerMrpGathering(mrpGatheringRegisterDate, mrpNoArr,mrpNoAndItemCodeMap);		
			System.out.println("나왔니???????");

			return map;
		}
		
	
	
	@RequestMapping(value="/getMrpGatheringList.do", method=RequestMethod.POST)
	public ModelMap getMrpGatheringList(HttpServletRequest request, HttpServletResponse response) {
		
		String mrpNoList = request.getParameter("mrpNoList");
		String mrpNoList1 = request.getParameter("mrpNoList1");
		
		ArrayList<String> mrpNoArr = gson.fromJson(mrpNoList,
				new TypeToken<ArrayList<String>>() { }.getType());

		map = new ModelMap();

		ArrayList<MrpGatheringTO> mrpGatheringList = productionService.getMrpGathering(mrpNoArr);
			
		map.put("gridRowJson", mrpGatheringList);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");

		return map;
	}
	
	
	
	@RequestMapping(value="/searchMrpGathering.do", method=RequestMethod.POST)
	public ModelMap searchMrpGathering(HttpServletRequest request, HttpServletResponse response) {

		String searchDateCondition = request.getParameter("searchDateCondition");
		String startDate = request.getParameter("mrpGatheringStartDate");
		String endDate = request.getParameter("mrpGatheringEndDate");
		
		map = new ModelMap();

		ArrayList<MrpGatheringTO> mrpGatheringList = 
				productionService.searchMrpGatheringList(searchDateCondition, startDate, endDate);
			
		map.put("gridRowJson", mrpGatheringList);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");

		return map;
	}	
}