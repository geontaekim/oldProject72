package kr.co.seoulit.logistics.prodcsvc.quality.service;

import java.util.ArrayList;

import org.springframework.ui.ModelMap;

import kr.co.seoulit.logistics.prodcsvc.quality.to.ProductionPerformanceInfoTO;
import kr.co.seoulit.logistics.prodcsvc.quality.to.WorkOrderInfoTO;
import kr.co.seoulit.logistics.prodcsvc.quality.to.WorkOrderableMrpListTO;

public interface QualityService {

	public ModelMap getWorkOrderableMrpList();
	
	public ModelMap getWorkOrderSimulationList(String mrpGatheringNoList,String mrpNoList);
	
	public ModelMap workOrder(String mrpGatheringNo,String workPlaceCode,String productionProcess,String mrpNo);
	
	public ArrayList<WorkOrderInfoTO> getWorkOrderInfoList();
	
	public ModelMap workOrderCompletion(String workOrderNo,String actualCompletionAmount);

	public ArrayList<ProductionPerformanceInfoTO> getProductionPerformanceInfoList();
	
	public ModelMap showWorkSiteSituation(String workSiteCourse,String workOrderNo,String itemClassIfication);
	
	public ModelMap workCompletion(String workOrderNo,String itemCode, ArrayList<String> itemCodeListArr);
	
	public ModelMap workSiteLogList(String workSiteLogDate);

}

