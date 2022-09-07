package kr.co.seoulit.logistics.busisvc.sales.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import kr.co.seoulit.logistics.busisvc.logisales.dao.ContractDAO;
import kr.co.seoulit.logistics.busisvc.logisales.to.ContractDetailTO;
import kr.co.seoulit.logistics.busisvc.logisales.to.ContractInfoTO;
import kr.co.seoulit.logistics.busisvc.sales.dao.DeliveryDAO;
import kr.co.seoulit.logistics.busisvc.sales.dao.SalesPlanDAO;
import kr.co.seoulit.logistics.busisvc.sales.to.DeliveryInfoTO;
import kr.co.seoulit.logistics.busisvc.sales.to.SalesPlanTO;


@Component
public class SalesServiceImpl implements SalesService {

	@Autowired
	private ContractDAO contractDAO;
	
	@Autowired
	private SalesPlanDAO salesPlanDAO;
	
	@Autowired
	private DeliveryDAO deliveryDAO;
	


	@Override
	public ArrayList<ContractInfoTO> getDeliverableContractList(HashMap<String,String> ableSearchConditionInfo) {

		ArrayList<ContractInfoTO> deliverableContractList = null;

		deliverableContractList = contractDAO.selectDeliverableContractList(ableSearchConditionInfo);

		for (ContractInfoTO bean : deliverableContractList) { // 해당 수주의 수주상세 리스트 세팅 

			bean.setContractDetailTOList(contractDAO.selectDeliverableContractDetailList(bean.getContractNo()));

		}

		return deliverableContractList;
	}
	
	
	@Override
	public ArrayList<ContractInfoTO> getSalesContractList(HashMap<String,String> sales){
		
		
		ArrayList<ContractInfoTO> saleshistorylist=null;
		
		
		System.out.println("파사드에서 sales?"+sales);
		saleshistorylist=contractDAO.selectSalesContractList(sales); 
		System.out.println("saleshistorylist반환되니?"+saleshistorylist);
		
		
		return saleshistorylist;

	}
	
	@Override
	public ArrayList<SalesPlanTO> getSalesPlanList(String dateSearchCondition, String startDate, String endDate) {

		ArrayList<SalesPlanTO> salesPlanTOList = null;

		salesPlanTOList = salesPlanDAO.selectSalesPlanList(dateSearchCondition, startDate, endDate);
		
		return salesPlanTOList;
	}

	@Override
	public ModelMap batchSalesPlanListProcess(ArrayList<SalesPlanTO> salesPlanTOList) {

		ModelMap resultMap = new ModelMap();
		
		ArrayList<String> insertList = new ArrayList<>();
		ArrayList<String> updateList = new ArrayList<>();
		ArrayList<String> deleteList = new ArrayList<>();

		for (SalesPlanTO bean : salesPlanTOList) {

			String status = bean.getStatus();

			switch (status) {

			case "INSERT":

				String newSalesPlanNo = getNewSalesPlanNo(bean.getSalesPlanDate());

				bean.setSalesPlanNo(newSalesPlanNo);

				salesPlanDAO.insertSalesPlan(bean);

				insertList.add(newSalesPlanNo);

				break;

			case "UPDATE":

				salesPlanDAO.updateSalesPlan(bean);

				updateList.add(bean.getSalesPlanNo());
				
				break;

			case "DELETE":

				salesPlanDAO.deleteSalesPlan(bean);

				deleteList.add(bean.getSalesPlanNo());

				break;

			}

		}

		resultMap.put("INSERT", insertList);
		resultMap.put("UPDATE", updateList);
		resultMap.put("DELETE", deleteList);

		return resultMap;
	}

	public String getNewSalesPlanNo(String salesPlanDate) {

		StringBuffer newEstimateNo = null;

		int newNo = salesPlanDAO.selectSalesPlanCount(salesPlanDate);

		newEstimateNo = new StringBuffer();

		newEstimateNo.append("SA");
		newEstimateNo.append(salesPlanDate.replace("-", ""));
		newEstimateNo.append(String.format("%02d", newNo)); // 2자리 숫자
		
		return newEstimateNo.toString();
	}

	@Override
	public ArrayList<DeliveryInfoTO> getDeliveryInfoList() {

		ArrayList<DeliveryInfoTO> deliveryInfoList = null;

		deliveryInfoList = deliveryDAO.selectDeliveryInfoList();

		return deliveryInfoList;
	}


	@Override
	public ModelMap batchDeliveryListProcess(List<DeliveryInfoTO> deliveryTOList) {
		
		ModelMap resultMap = new ModelMap();
		
		ArrayList<String> insertList = new ArrayList<>();
		ArrayList<String> updateList = new ArrayList<>();
		ArrayList<String> deleteList = new ArrayList<>();

		for (DeliveryInfoTO bean : deliveryTOList) {

			String status = bean.getStatus();

			switch (status.toUpperCase()) {

			case "INSERT":

				String newDeliveryNo = "새로운";

				bean.setDeliveryNo(newDeliveryNo);
				deliveryDAO.insertDeliveryResult(bean);
				insertList.add(newDeliveryNo);
					
				break;

			case "UPDATE":

				deliveryDAO.updateDeliveryResult(bean);

				updateList.add(bean.getDeliveryNo());

				break;

			case "DELETE":

				deliveryDAO.deleteDeliveryResult(bean);

				deleteList.add(bean.getDeliveryNo());
				
				break;

			}

		}

		resultMap.put("INSERT", insertList);
		resultMap.put("UPDATE", updateList);
		resultMap.put("DELETE", deleteList);

		return resultMap;
	}

	@Override
	public ModelMap deliver(String detail) {

		ModelMap resultMap = new ModelMap();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("contractDetailNo", detail);		//xml의 키값을 맞춰주기
		System.out.println("contractDetailNo??"+detail);
	
		deliveryDAO.deliver(map);

		resultMap.put("errorCode",map.get("ERROR_CODE"));
		resultMap.put("errorMsg",map.get("ERROR_MSG"));
		
		return resultMap;
	}
	
	
	
	
}
