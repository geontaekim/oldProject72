package kr.co.seoulit.logistics.purcstosvc.stock.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import kr.co.seoulit.logistics.purcstosvc.stock.dao.BomDAO;
import kr.co.seoulit.logistics.purcstosvc.stock.dao.StockDAO;
import kr.co.seoulit.logistics.purcstosvc.stock.to.BomDeployTO;
import kr.co.seoulit.logistics.purcstosvc.stock.to.BomInfoTO;
import kr.co.seoulit.logistics.purcstosvc.stock.to.BomTO;
import kr.co.seoulit.logistics.purcstosvc.stock.to.StockChartTO;
import kr.co.seoulit.logistics.purcstosvc.stock.to.StockLogTO;
import kr.co.seoulit.logistics.purcstosvc.stock.to.StockTO;

@Component
public class StockServiceImpl implements StockService {

	@Autowired
	private BomDAO bomDAO;
	
	@Autowired
	private StockDAO stockDAO;
	
	@Override
	public ArrayList<BomDeployTO> getBomDeployList(String deployCondition, String itemCode,
			String itemClassificationCondition) {

		ArrayList<BomDeployTO> bomDeployList = null;

		bomDeployList = bomDAO.selectBomDeployList(deployCondition, itemCode, itemClassificationCondition);

		return bomDeployList;
	}

	@Override
	public ArrayList<BomInfoTO> getBomInfoList(String parentItemCode) {

		ArrayList<BomInfoTO> bomInfoList = null;

		bomInfoList = bomDAO.selectBomInfoList(parentItemCode);

		return bomInfoList;
	}


	@Override
	public ArrayList<BomInfoTO> getAllItemWithBomRegisterAvailable() {

		ArrayList<BomInfoTO> allItemWithBomRegisterAvailable = null;

		allItemWithBomRegisterAvailable = bomDAO.selectAllItemWithBomRegisterAvailable();

		return allItemWithBomRegisterAvailable;
	}

	@Override
	public HashMap<String, Object> batchBomListProcess(ArrayList<BomTO> batchBomList) {

		HashMap<String, Object> resultMap = new HashMap<>();

		int insertCount = 0;
		int updateCount = 0;
		int deleteCount = 0;

		for (BomTO TO : batchBomList) {

			String status = TO.getStatus();

			switch (status) {

			case "INSERT":

				bomDAO.insertBom(TO);

				insertCount++;

				break;

			case "UPDATE":

				bomDAO.updateBom(TO);

				updateCount++;

				break;

			case "DELETE":

				bomDAO.deleteBom(TO);

				deleteCount++;

				break;

			}

		}

		resultMap.put("INSERT", insertCount);
		resultMap.put("UPDATE", updateCount);
		resultMap.put("DELETE", deleteCount);

		return resultMap;

	}


	@Override
	public ArrayList<StockTO> getStockList() {

		ArrayList<StockTO> stockList = null;

		stockList = stockDAO.selectStockList();

		return stockList;
	}

	@Override
	public ModelMap getStockLogList(String startDate, String endDate) {
		ModelMap resultMap = new ModelMap();
		System.out.println("startDate??"+startDate);
		System.out.println("endDate??"+endDate);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		
		stockDAO.selectStockLogList(map);
		
		resultMap.put("result", map);
		System.out.println("map은??"+resultMap);

		return resultMap;
	}


	@Override
	public ModelMap warehousing(ArrayList<String> orderNoArr) {

		ModelMap resultMap = new ModelMap();
		
		String orderNoList = orderNoArr.toString().replace("[", "").replace("]", "");
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		map.put("orderNoList",orderNoList);
		
		stockDAO.warehousing(map);
		
		resultMap.put("errorCode",map.get("ERROR_CODE"));
		resultMap.put("errorMsg",map.get("ERROR_MSG"));
		
		System.out.println("resultMap???"+resultMap);
		
    	return resultMap;
	}


	@Override
	public ModelMap changeSafetyAllowanceAmount(String itemCode, String itemName,
			String safetyAllowanceAmount) {

		ModelMap resultMap = null;

		resultMap = stockDAO.updatesafetyAllowance(itemCode,itemName,safetyAllowanceAmount);

		return resultMap;
	}

	
	@Override
	public ArrayList<StockChartTO> getStockChart() {

		ArrayList<StockChartTO> stockChart  = null;

		stockChart = stockDAO.selectStockChart();

		return stockChart;

	}
	
	@Override
	public ArrayList<StockTO> getWarehouseStockList(String warehouseCode) {

		ArrayList<StockTO> stockList = null;

		stockList = stockDAO.selectWarehouseStockList(warehouseCode);

		return stockList;
	}
	
	@Override
	public void batchStockProcess(ArrayList<StockTO> stockTOList) {

		for (StockTO bean : stockTOList) {
				
			String status = bean.getStatus();
				
			switch (status) {
				
				case "DELETE":
					stockDAO.deleteStock(bean);
					break;
						
				case "INSERT":
					stockDAO.insertStock(bean);
						break;
					
				case "UPDATE":
					stockDAO.updateStock(bean);
			}
				
		}

	}
}
