package kr.co.seoulit.logistics.prodcsvc.quality.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import kr.co.seoulit.logistics.prodcsvc.quality.dao.WorkOrderDAO;
import kr.co.seoulit.logistics.prodcsvc.quality.to.ProductionPerformanceInfoTO;
import kr.co.seoulit.logistics.prodcsvc.quality.to.WorkOrderInfoTO;

@Component
public class QualityServiceImpl implements QualityService {
	
	@Autowired
	private WorkOrderDAO workOrderDAO;
	
	
	@Override
	public ModelMap getWorkOrderableMrpList() {
		
		ModelMap resultMap = new ModelMap();
		
		System.out.println("2차확인 파사드");
        
		
		HashMap<String, Object> map = new HashMap<>();
		
		workOrderDAO.getWorkOrderableMrpList(map);
		
		  resultMap.put("errorCode", map.get("ERROR_CODE"));
		  resultMap.put("errorMsg", map.get("ERROR_MSG"));
		  resultMap.put("result", map.get("RESULT"));
		
		return resultMap;
		
	}
	
	@Override
	public ModelMap getWorkOrderSimulationList(String mrpGatheringNoList ,String mrpNoList) {
		
		mrpGatheringNoList=mrpGatheringNoList.replace("[", "").replace("]", "").replace("{", "").replace("}", "").replace("\"", "");
		mrpNoList=mrpNoList.replace("[", "").replace("]", "").replace("{", "").replace("}", "").replace("\"", "");
		
		ModelMap resultMap = new ModelMap();
		HashMap<String, Object> map = new HashMap<>();
		map.put("mrpGatheringNo", mrpGatheringNoList);
		map.put("mrpNo", mrpNoList);
		workOrderDAO.getWorkOrderSimulationList(map);
		
		  resultMap.put("errorCode", map.get("ERROR_CODE"));
		  resultMap.put("errorMsg", map.get("ERROR_MSG"));
		  resultMap.put("result", map.get("RESULT"));
		  System.out.println("resultMap"+resultMap);
		

		return resultMap;
	}
	
	@Override
	public ModelMap workOrder(String mrpGatheringNo,String workPlaceCode,String productionProcess,String mrpNo) {
		
		mrpGatheringNo=mrpGatheringNo.replace("[", "").replace("]", "").replace("{", "").replace("}", "").replace("\"", "");
		mrpNo=mrpNo.replace("[", "").replace("]", "").replace("{", "").replace("}", "").replace("\"", "");
		
		ModelMap resultMap = new ModelMap();
		HashMap<String, Object> map = new HashMap<>();
		map.put("mrpGatheringNo", mrpGatheringNo);
		map.put("workPlaceCode", workPlaceCode);
		map.put("productionProcess", productionProcess);
		map.put("mrpNo", mrpNo);
		
		workOrderDAO.workOrder(map);
		 resultMap.put("errorMsg", map.get("ERROR_MSG"));
		 resultMap.put("result", map.get("RESULT"));
		

    	return resultMap;
		
	}

	@Override
	public ArrayList<WorkOrderInfoTO> getWorkOrderInfoList() {

		ArrayList<WorkOrderInfoTO> workOrderInfoList = null;

		workOrderInfoList = workOrderDAO.selectWorkOrderInfoList();
		
		System.out.println("동규");
		System.out.println(workOrderInfoList);
		
		return workOrderInfoList;
		
	}

	@Override
	public ModelMap workOrderCompletion(String workOrderNo,String actualCompletionAmount) {

		ModelMap resultMap = new ModelMap();
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("workOrderNo",workOrderNo);
		map.put("actualCompletionAmount",actualCompletionAmount);
		
		workOrderDAO.workOrderCompletion(map);
		
		 resultMap.put("errorMsg", map.get("ERROR_MSG"));
		 resultMap.put("result", map.get("RESULT"));
		
		
		
    	return resultMap;
		
	}

	@Override
	public ArrayList<ProductionPerformanceInfoTO> getProductionPerformanceInfoList() {

		ArrayList<ProductionPerformanceInfoTO> productionPerformanceInfoList = null;

		productionPerformanceInfoList = workOrderDAO.selectProductionPerformanceInfoList();

		return productionPerformanceInfoList;

	}

	@Override
	public ModelMap showWorkSiteSituation(String workSiteCourse,String workOrderNo,String itemClassIfication) {

		ModelMap resultMap = new ModelMap();
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("workSiteCourse",workSiteCourse);
		map.put("workOrderNo",workOrderNo);
		map.put("itemClassIfication",itemClassIfication);

		workOrderDAO.selectWorkSiteSituation(map);
		
		resultMap.put("errorCode", map.get("ERROR_CODE"));
		resultMap.put("errorMsg", map.get("ERROR_MSG"));
		resultMap.put("result", map.get("RESULT"));

		return resultMap;

	}

	@Override
	public ModelMap workCompletion(String workOrderNo, String itemCode ,  ArrayList<String> itemCodeListArr) {

		String itemCodeList=itemCodeListArr.toString().replace("[", "").replace("]", "");
		
			ModelMap resultMap = new ModelMap();
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		

		System.out.println("workOrderNo"+workOrderNo);
		System.out.println("itemCode"+itemCode);
		System.out.println("itemCodeListArr"+itemCodeListArr);

		map.put("workOrderNo", workOrderNo);
		map.put("itemCode", itemCode);
		map.put("itemCodeListArr", itemCodeListArr);

		workOrderDAO.updateWorkCompletionStatus(map);
		
		resultMap.put("errorCode", map.get("ERROR_CODE"));
		resultMap.put("errorMsg", map.get("ERROR_MSG"));
		

		return resultMap;

	}

	@Override
	public ModelMap workSiteLogList(String workSiteLogDate) {
		
		ModelMap resultMap = new ModelMap();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("workSiteLogDate", workSiteLogDate);
		
		workOrderDAO.workSiteLogList(map);
		resultMap.put("result", map);
		System.out.println("result??"+resultMap);

		return resultMap;
	}

}
