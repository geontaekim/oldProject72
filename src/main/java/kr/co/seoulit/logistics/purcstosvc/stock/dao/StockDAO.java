package kr.co.seoulit.logistics.purcstosvc.stock.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.ui.ModelMap;

import kr.co.seoulit.logistics.purcstosvc.stock.to.StockChartTO;
import kr.co.seoulit.logistics.purcstosvc.stock.to.StockLogTO;
import kr.co.seoulit.logistics.purcstosvc.stock.to.StockTO;

@Mapper
public interface StockDAO {
	
	public ArrayList<StockTO> selectStockList();
	
	
	public ArrayList<StockTO> selectWarehouseStockList(String warehouseCode);
	
	public void selectStockLogList(HashMap<String, Object> map);  //찾는 앞단이없음
	
	public void warehousing(HashMap<String, Object> map);
	
	public ModelMap updatesafetyAllowance(String itemCode, String itemName,
			String safetyAllowanceAmount);     //찾는 앞단이없음
	
	public ArrayList<StockChartTO> selectStockChart();
	
	public void insertStock(StockTO bean);
	
	public void updateStock(StockTO bean);
	
	public void deleteStock(StockTO bean);
}