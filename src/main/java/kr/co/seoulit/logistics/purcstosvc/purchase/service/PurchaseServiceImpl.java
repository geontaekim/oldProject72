package kr.co.seoulit.logistics.purcstosvc.purchase.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import kr.co.seoulit.logistics.purcstosvc.purchase.dao.OrderDAO;
import kr.co.seoulit.logistics.purcstosvc.purchase.dao.OutSourcingDAO;
import kr.co.seoulit.logistics.purcstosvc.purchase.to.OrderInfoTO;
import kr.co.seoulit.logistics.purcstosvc.purchase.to.OrderTempTO;
import kr.co.seoulit.logistics.purcstosvc.purchase.to.OutSourcingTO;

@Component
public class PurchaseServiceImpl implements PurchaseService{

		@Autowired
		private OutSourcingDAO outSourcingDAO;
		
		@Autowired
		private OrderDAO orderDAO;
		

		@Override
		public ArrayList<OutSourcingTO> searchOutSourcingList(String fromDate, String toDate, String customerCode,String itemCode,String materialStatus) {

			ArrayList<OutSourcingTO> OutSourcingList = null;

			OutSourcingList = outSourcingDAO.selectOutSourcingList(fromDate,toDate,customerCode,itemCode,materialStatus);

			return OutSourcingList;
		}
		
		@Override
		public ModelMap getOrderList(String startDate, String endDate) {

			ModelMap resultMap = new ModelMap();
			
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			System.out.println("map???"+map);
			orderDAO.getOrderList(map);
			System.out.println("resultMap??"+resultMap);
			
			resultMap.put("errorCode", map.get("ERROR_CODE"));
			resultMap.put("errorMsg", map.get("ERROR_MSG"));
			resultMap.put("result", map.get("RESULT"));
			
			System.out.println("resultMap2222??"+resultMap);

			return resultMap;
		}

		@Override
		public ModelMap getOrderDialogInfo(String mrpNoArr) {

			ModelMap resultMap = new ModelMap();
	        
			String mrpNoList = mrpNoArr.replace("[", "").replace("]", "").replace("\"", "");
			
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("mrpNoList", mrpNoList);
			
			orderDAO.getOrderDialogInfo(map);
			
			resultMap.put("errorCode", map.get("ERROR_CODE"));
			resultMap.put("errorMsg", map.get("ERROR_MSG"));
			resultMap.put("result", map.get("RESULT"));

			return resultMap;

		}
		
		
		@Override
		public ModelMap order(ArrayList<String> mrpGaNoArr) {

			ModelMap resultMap =new ModelMap();
			
			String mpsNoList = mrpGaNoArr.toString().replace("[", "").replace("]", "");
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("mpsNoList", mpsNoList);
			orderDAO.order(map);
			System.out.println("map??"+map);
			System.out.println("resultMap??"+resultMap);
			resultMap.put("errorCode", map.get("ERROR_CODE"));
			resultMap.put("errorMsg", map.get("ERROR_MSG"));
			resultMap.put("result", map);
			
	    	return resultMap;
			
		}

		
		@Override
		public ModelMap optionOrder(String itemCode, String itemAmount) {

			ModelMap resultMap = new ModelMap();
			
			
			
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemCode", itemCode);
			map.put("itemAmount", itemAmount);
			System.out.println("map??"+map);
			orderDAO.optionOrder(map);
			resultMap.put("errorCode", map.get("ERROR_CODE"));
			resultMap.put("errorMsg", map.get("ERROR_MSG"));
			System.out.println("map2222??"+map);
			System.out.println("resultMap??"+resultMap);
			
			
	    	return resultMap;
			
		}

		@Override
		public ArrayList<OrderInfoTO> getOrderInfoListOnDelivery() {

			ArrayList<OrderInfoTO> orderInfoListOnDelivery = 
					orderDAO.getOrderInfoListOnDelivery();

			return orderInfoListOnDelivery;

		}

		@Override
		public ArrayList<OrderInfoTO> getOrderInfoList(String startDate, String endDate) {

			ArrayList<OrderInfoTO> orderInfoList  = null;
			
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("startDate", startDate);
			map.put("endDate", endDate);

			orderInfoList = orderDAO.getOrderInfoList(map);

			return orderInfoList;

		}

		@Override
		public ModelMap checkOrderInfo(ArrayList<String> orderNoArr) {

			ModelMap resultMap = new ModelMap();

			String orderNoList = orderNoArr.toString().replace("[", "").replace("]", "");
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("orderNoList", orderNoList);
			orderDAO.updateOrderInfo(map);
			System.out.println("map??"+map);
			resultMap.put("errorCode", map.get("ERROR_CODE"));
			resultMap.put("errorMsg", map.get("ERROR_MSG"));

			return resultMap;
		}
		
}
