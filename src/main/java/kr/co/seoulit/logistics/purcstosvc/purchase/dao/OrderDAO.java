package kr.co.seoulit.logistics.purcstosvc.purchase.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.ui.ModelMap;

import kr.co.seoulit.logistics.purcstosvc.purchase.to.OrderInfoTO;

@Mapper
public interface OrderDAO {
	
	 public void getOrderList(HashMap<String, Object> map);
	 
	 public void getOrderDialogInfo(HashMap<String, Object> map);
	 
	 public ArrayList<OrderInfoTO> getOrderInfoListOnDelivery();
	 
	 public ArrayList<OrderInfoTO> getOrderInfoList(HashMap<String, Object> map);

	 public void order(HashMap<String, Object> map); 
	 
	 public void optionOrder(HashMap<String, Object> map);

	public void updateOrderInfo(HashMap<String, Object> map);
	
}
