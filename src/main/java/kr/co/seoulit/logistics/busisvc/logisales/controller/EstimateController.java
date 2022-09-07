package kr.co.seoulit.logistics.busisvc.logisales.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import kr.co.seoulit.logistics.busisvc.logisales.service.LogisalesService;
import kr.co.seoulit.logistics.busisvc.logisales.to.ContractDetailTO;
import kr.co.seoulit.logistics.busisvc.logisales.to.EstimateDetailTO;
import kr.co.seoulit.logistics.busisvc.logisales.to.EstimateTO;


@RestController
@RequestMapping("/busi/*")
public class EstimateController{

	@Autowired
	private LogisalesService logisalesService;
	
	ModelMap map=null;
	
	// GSON 라이브러리
	private static Gson gson = new GsonBuilder().serializeNulls().create(); // 속성값이 null 인 속성도 json 변환
	

	@RequestMapping(value="/searchEstimate.do", method=RequestMethod.POST)
	public ModelMap searchEstimateInfo(HttpServletRequest request, HttpServletResponse response) {

		map=new ModelMap();
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String dateSearchCondition = request.getParameter("dateSearchCondition");
		System.out.println("dateSearchCondition이 뭔데?:"+dateSearchCondition);
		System.out.println("1");
		ArrayList<EstimateTO> estimateTOList = logisalesService.getEstimateList(dateSearchCondition, startDate, endDate);

		System.out.println("2");
		map.put("gridRowJson", estimateTOList);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");
			
		return map;
	}
	
	@RequestMapping(value="/searchEstimateDetailInfo.do", method=RequestMethod.POST)
	public ModelMap searchEstimateDetailInfo(HttpServletRequest request, HttpServletResponse response) {

		map =new ModelMap();

		String estimateNo = request.getParameter("estimateNo");

		ArrayList<EstimateDetailTO> estimateDetailTOList = logisalesService.getEstimateDetailList(estimateNo);
		
		map.put("gridRowJson", estimateDetailTOList);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");

		return map;
	}

	@RequestMapping(value="/addNewEstimate.do", method=RequestMethod.POST)
	public ModelMap addNewEstimate(HttpServletRequest request, HttpServletResponse response) {

		map = new ModelMap();

		String estimateDate = request.getParameter("estimateDate");
		System.out.println("estimateDate는?"+estimateDate);
		String newEstimateInfo = request.getParameter("newEstimateInfo"); 
		System.out.println("newEstimateInfo는?"+newEstimateInfo);
		EstimateTO newEstimateTO = gson.fromJson(newEstimateInfo, EstimateTO.class);

		HashMap<String, Object> resultList = logisalesService.addNewEstimate(estimateDate, newEstimateTO);
		
		map.put("result", resultList);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");
		
		
		return map;
	}
	
	@PostMapping(value="/deleteEstimate.do")
	public ModelMap deleteEstimateInfo(EstimateTO eto,HttpServletRequest request, HttpServletResponse response) {

		
		System.out.println("eto??"+eto.getEstimateNo());
		/*
		 * ArrayList<EstimateDetailTO>
		 * detail=logisalesService.getEstimateDetailList(eto.getEstimateNo());
		 * 
		 * //일련번호로 상세일련번호 찾는 메서드 System.out.println("detail??"+detail);
		 * for(EstimateDetailTO arr : detail) {
		 * System.out.println("arr??"+arr.getEstimateDetailNo()); }
		 */
		map = new ModelMap();
		Map<String,Object> map2=new HashMap<>();
		ArrayList<EstimateDetailTO> edt=logisalesService.getEstimateDetailNo(eto.getEstimateNo());
		for(EstimateDetailTO est: edt) {	
			map2.put("estimateNo",est.getEstimateNo()); 
			map2.put("estimateDetailNo",est.getEstimateDetailNo());
			map.put("estimateNo",est.getEstimateNo()); 
			map.put("estimateDetailNo",est.getEstimateDetailNo());
			
		}	
		 
		logisalesService.removeEstimate(map2); 
		 
		 
		
		//견적일자와 견적,견적상세의 json객체를 EstimateTO로 변환한 newEstimateTO를 map에 담음
		/*
		map.put("result", resultList);
		map.put("errorCode", 1);
		 */
		map.put("errorMsg", "성공");
	
	
		return map;
	}
	

	@RequestMapping(value="/batchEstimateDetailListProcess.do", method=RequestMethod.POST)
	public ModelMap batchListProcess(HttpServletRequest request, HttpServletResponse response) {

		map = new ModelMap();

		String batchList = request.getParameter("batchList");
		//String estimateNo = request.getParameter("estimateNo");
		ArrayList<EstimateDetailTO> estimateDetailTOList = gson.fromJson(batchList,
				new TypeToken<ArrayList<EstimateDetailTO>>() {
				}.getType());

		HashMap<String, Object> resultList = logisalesService.batchEstimateDetailListProcess(estimateDetailTOList,estimateDetailTOList.get(0).getEstimateNo());

		map.put("result", resultList);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");
		
		return map;
	}
}