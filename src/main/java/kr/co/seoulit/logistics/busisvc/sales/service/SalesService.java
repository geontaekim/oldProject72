package kr.co.seoulit.logistics.busisvc.sales.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.ui.ModelMap;

import kr.co.seoulit.logistics.busisvc.logisales.to.ContractDetailTO;
import kr.co.seoulit.logistics.busisvc.logisales.to.ContractInfoTO;
import kr.co.seoulit.logistics.busisvc.sales.to.DeliveryInfoTO;
import kr.co.seoulit.logistics.busisvc.sales.to.SalesPlanTO;

public interface SalesService {

	// SalesPlanApplicationServiceImpl
	public ArrayList<ContractInfoTO> getDeliverableContractList(HashMap<String,String> ableSearchConditionInfo);
	
	public ArrayList<SalesPlanTO> getSalesPlanList(String dateSearchCondition, String startDate, String endDate);
	
	public HashMap<String, Object> batchSalesPlanListProcess(ArrayList<SalesPlanTO> salesPlanTOList);

	public HashMap<String, Object> batchDeliveryListProcess(List<DeliveryInfoTO> deliveryTOList);

	public ModelMap deliver(String detail);
	
	public ArrayList<DeliveryInfoTO> getDeliveryInfoList();

	public ArrayList<ContractInfoTO> getSalesContractList(HashMap<String,String> sales);
	
}
