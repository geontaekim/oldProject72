package kr.co.seoulit.logistics.prodcsvc.quality.controller;


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
import com.itextpdf.text.log.SysoCounter;

import kr.co.seoulit.logistics.prodcsvc.quality.service.QualityService;
import kr.co.seoulit.logistics.prodcsvc.quality.to.ProductionPerformanceInfoTO;
import kr.co.seoulit.logistics.prodcsvc.quality.to.WorkOrderInfoTO;
import kr.co.seoulit.logistics.prodcsvc.quality.to.WorkOrderableMrpListTO;

@RestController
@RequestMapping("/quality/*")
public class WorkOrderController{

	@Autowired
	private QualityService qualityService;
	
	
	ModelMap map = null;

	// gson 라이브러리
	private static Gson gson = new GsonBuilder().serializeNulls().create();

	
	@RequestMapping(value="/getWorkOrderableMrpList.do" , method=RequestMethod.POST)	
	public ModelMap getWorkOrderableMrpList(HttpServletRequest request, HttpServletResponse response) {

		map = new ModelMap();
		
		System.out.println("컨트롤러단 확인");
		map = qualityService.getWorkOrderableMrpList();
		

		return map;
	}

	//jsp에서 요청하는게 없다.
	@RequestMapping(value="/showWorkOrderDialog.do" , method=RequestMethod.POST)
	public ModelMap showWorkOrderDialog(HttpServletRequest request, HttpServletResponse response) {

		String mrpGatheringNoList = request.getParameter("mrpGatheringNoList");
		String mrpNoList = request.getParameter("mrpNoList");
		
		System.out.println("mrpGatheringNoList??"+mrpGatheringNoList);
		System.out.println("mrpNoList??"+mrpNoList);
		
		map = new ModelMap();
		
		map = qualityService.getWorkOrderSimulationList(mrpGatheringNoList,mrpNoList);
		System.out.println("map??"+map);
		
		return map;
	}

	
	@RequestMapping(value="/workOrder.do" , method=RequestMethod.POST)
	public ModelMap workOrder(HttpServletRequest request, HttpServletResponse response) {

		String mrpGatheringNo = request.getParameter("mrpGatheringNo");
		String workPlaceCode = request.getParameter("workPlaceCode");
		String productionProcess = request.getParameter("productionProcessCode");
		String mrpNo = request.getParameter("mrpNo");
		
		System.out.println("mrpGatheringNo??"+mrpGatheringNo);
		System.out.println("workPlaceCode??"+workPlaceCode);
		System.out.println("productionProcess??"+productionProcess);
		System.out.println("mrpNo??"+mrpNo);
		
		map = new ModelMap();

		map = qualityService.workOrder(mrpGatheringNo,workPlaceCode,productionProcess,mrpNo);

		return map;
	}

	
	@RequestMapping(value="/showWorkOrderInfoList.do" , method=RequestMethod.POST)
	public ModelMap showWorkOrderInfoList(HttpServletRequest request, HttpServletResponse response) {

		map = new ModelMap();

		ArrayList<WorkOrderInfoTO> workOrderInfoList = null;

		workOrderInfoList = qualityService.getWorkOrderInfoList();

		map.put("gridRowJson", workOrderInfoList);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");

		return map;
	}
	
	@RequestMapping(value="/workOrderCompletion.do" , method=RequestMethod.POST)
	public ModelMap workOrderCompletion(HttpServletRequest request, HttpServletResponse response) {

		String workOrderNo=request.getParameter("workOrderNo");
		String actualCompletionAmount=request.getParameter("actualCompletionAmount");
		
		System.out.println("workOrderNo"+workOrderNo);
		System.out.println("actualCompletionAmount"+actualCompletionAmount);
		
		map = new ModelMap();

		map = qualityService.workOrderCompletion(workOrderNo,actualCompletionAmount);

		return map;
	}
	
	//jsp 페이지가 없다.
	@RequestMapping(value="/getProductionPerformanceInfoList.do" , method=RequestMethod.POST)
	public ModelMap getProductionPerformanceInfoList(HttpServletRequest request, HttpServletResponse response) {

		map = new ModelMap();
		
		ArrayList<ProductionPerformanceInfoTO> productionPerformanceInfoList = null;

		productionPerformanceInfoList = qualityService.getProductionPerformanceInfoList();

		map.put("gridRowJson", productionPerformanceInfoList);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");

		return map;
	}
	
	//jsp 페이지가 없다.
	@RequestMapping(value="/showWorkSiteSituation.do" , method=RequestMethod.POST)
	public ModelMap showWorkSiteSituation(HttpServletRequest request, HttpServletResponse response) {
		
		map = new ModelMap();
		
		String workSiteCourse = request.getParameter("workSiteCourse");//원재료검사:RawMaterials,제품제작:Production,판매제품검사:SiteExamine
		String workOrderNo = request.getParameter("workOrderNo");//작업지시일련번호	
		String itemClassIfication = request.getParameter("itemClassIfication");//품목분류:완제품,반제품,재공품	
		System.out.println("workSiteCourse??"+workSiteCourse);
		System.out.println("workOrderNo??"+workOrderNo);
		System.out.println("itemClassIfication??"+itemClassIfication);

		map = qualityService.showWorkSiteSituation(workSiteCourse,workOrderNo,itemClassIfication);

		return map;
	}
	
	@RequestMapping(value="/workCompletion.do" , method=RequestMethod.POST)
	public ModelMap workCompletion(HttpServletRequest request, HttpServletResponse response) {

		map = new ModelMap();
		
		String workOrderNo = request.getParameter("workOrderNo");
		String itemCode = request.getParameter("itemCode");
		String itemCodeList = request.getParameter("itemCodeList");
		
		System.out.println("workOrderNo"+workOrderNo);
		System.out.println("itemCode"+itemCode);
		System.out.println("itemCodeList"+itemCodeList);
		
		ArrayList<String> itemCodeListArr = gson.fromJson(itemCodeList,
				new TypeToken<ArrayList<String>>() {}.getType());
		
		map=qualityService.workCompletion(workOrderNo,itemCode,itemCodeListArr);

		return map;
	}
	
	@RequestMapping(value="/workSiteLog.do" , method=RequestMethod.POST)
	public ModelMap workSiteLogList(HttpServletRequest request, HttpServletResponse response) {

		String workSiteLogDate = request.getParameter("workSiteLogDate");
		
		System.out.println("workSiteLogDate??"+workSiteLogDate);
		
		map = new ModelMap();

		map=qualityService.workSiteLogList(workSiteLogDate);

		return map;
	}
	
}
