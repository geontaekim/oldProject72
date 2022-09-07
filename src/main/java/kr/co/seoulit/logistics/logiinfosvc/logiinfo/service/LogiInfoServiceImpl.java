package kr.co.seoulit.logistics.logiinfosvc.logiinfo.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import kr.co.seoulit.logistics.logiinfosvc.compinfo.dao.CodeDAO;
import kr.co.seoulit.logistics.logiinfosvc.compinfo.to.CodeDetailTO;
import kr.co.seoulit.logistics.logiinfosvc.logiinfo.dao.ItemDAO;
import kr.co.seoulit.logistics.logiinfosvc.logiinfo.dao.WarehouseDAO;
import kr.co.seoulit.logistics.logiinfosvc.logiinfo.to.ItemGroupTO;
import kr.co.seoulit.logistics.logiinfosvc.logiinfo.to.ItemInfoTO;
import kr.co.seoulit.logistics.logiinfosvc.logiinfo.to.ItemTO;
import kr.co.seoulit.logistics.logiinfosvc.logiinfo.to.WarehouseTO;
import kr.co.seoulit.logistics.purcstosvc.stock.dao.BomDAO;
import kr.co.seoulit.logistics.purcstosvc.stock.to.BomTO;


@Component
public class LogiInfoServiceImpl implements LogiInfoService {
	
	@Autowired
	private ItemDAO itemDAO;
	
	@Autowired
	private CodeDAO codeDAO;
	
	@Autowired
	private BomDAO bomDAO;
	
	@Autowired
	private WarehouseDAO warehouseDAO;
	

	
	@Override
	public ArrayList<ItemInfoTO> getItemInfoList(String searchCondition, String[] paramArray) {

		ArrayList<ItemInfoTO> itemInfoList = null;

		switch (searchCondition) {

		case "ALL":

			itemInfoList = itemDAO.selectAllItemList();

			break;

		case "ITEM_CLASSIFICATION":

			itemInfoList = itemDAO.selectItemList("ITEM_CLASSIFICATION", paramArray);

			break;

		case "ITEM_GROUP_CODE":

			itemInfoList = itemDAO.selectItemList("ITEM_GROUP_CODE", paramArray);

			break;

		case "STANDARD_UNIT_PRICE":

			itemInfoList = itemDAO.selectItemList("STANDARD_UNIT_PRICE", paramArray);

			break;

		}

		return itemInfoList;
	}

	@Override
	public ModelMap batchItemListProcess(ArrayList<ItemTO> itemTOList) {

		ModelMap resultMap = new ModelMap();

		ArrayList<String> insertList = new ArrayList<>();
		ArrayList<String> updateList = new ArrayList<>();
		ArrayList<String> deleteList = new ArrayList<>();

		CodeDetailTO detailCodeTO = new CodeDetailTO();
		BomTO bomTO = new BomTO();
			
		for (ItemTO TO : itemTOList) {

			String status = TO.getStatus();

			switch (status) {

			case "INSERT":

				itemDAO.insertItem(TO);
				insertList.add(TO.getItemCode());

				detailCodeTO.setDivisionCodeNo(TO.getItemClassification());
				detailCodeTO.setDetailCode(TO.getItemCode());
				detailCodeTO.setDetailCodeName(TO.getItemName());
				detailCodeTO.setDescription(TO.getDescription());

				codeDAO.insertDetailCode(detailCodeTO);

				if( TO.getItemClassification().equals("IT-CI") || TO.getItemClassification().equals("IT-SI") ) {
						
					bomTO.setNo(1);
					bomTO.setParentItemCode("NULL");
					bomTO.setItemCode( TO.getItemCode() );
					bomTO.setNetAmount(1);
						
					bomDAO.insertBom(bomTO);
				}
					
					
				break;

			case "UPDATE":

				itemDAO.updateItem(TO);

				updateList.add(TO.getItemCode());

				detailCodeTO.setDivisionCodeNo(TO.getItemClassification());
				detailCodeTO.setDetailCode(TO.getItemCode());
				detailCodeTO.setDetailCodeName(TO.getItemName());
				detailCodeTO.setDescription(TO.getDescription());

				codeDAO.updateDetailCode(detailCodeTO);

				break;

			case "DELETE":

				itemDAO.deleteItem(TO);

				deleteList.add(TO.getItemCode());

				detailCodeTO.setDivisionCodeNo(TO.getItemClassification());
				detailCodeTO.setDetailCode(TO.getItemCode());
				detailCodeTO.setDetailCodeName(TO.getItemName());
				detailCodeTO.setDescription(TO.getDescription());

				codeDAO.deleteDetailCode(detailCodeTO);

				break;

			}

		}

		resultMap.put("INSERT", insertList);
		resultMap.put("UPDATE", updateList);
		resultMap.put("DELETE", deleteList);

		return resultMap;
	}

	@Override
	public ArrayList<WarehouseTO> getWarehouseInfoList() {

		ArrayList<WarehouseTO> warehouseInfoList = null;

		warehouseInfoList = warehouseDAO.selectWarehouseList();

		return warehouseInfoList;
	}

	@Override
	public void batchWarehouseInfo(ArrayList<WarehouseTO> warehouseTOList) {

		for (WarehouseTO bean : warehouseTOList) {
			String status = bean.getStatus();
			switch (status) {
				case "DELETE":
					warehouseDAO.deleteWarehouse(bean);
					break;
				case "INSERT":
					warehouseDAO.insertWarehouse(bean);
					break;
				case "UPDATE":
					warehouseDAO.updateWarehouse(bean);
			}
		}
	}

	@Override
	public String findLastWarehouseCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getStandardUnitPrice(String itemCode) {

		int price = 0;

		price = itemDAO.getStandardUnitPrice(itemCode);

		return price;
		
	}
	
	@Override
	public int getStandardUnitPriceBox(String itemCode) {

		int price = 0;

		price = itemDAO.getStandardUnitPriceBox(itemCode);

		return price;
		
	}
	
	@Override
	public ArrayList<ItemInfoTO> getitemInfoList(HashMap<String, String> ableSearchConditionInfo) {

		ArrayList<ItemInfoTO> itemCodeList = null;

		itemCodeList = itemDAO.selectitemInfoList(ableSearchConditionInfo);

		return itemCodeList;

	}

	@Override
	public ArrayList<ItemGroupTO> getitemGroupList(HashMap<String, String> ableSearchConditionInfo) {

		ArrayList<ItemGroupTO> itemGroupList = null;

		itemGroupList = itemDAO.selectitemGroupList(ableSearchConditionInfo);

		return itemGroupList;
	}

	//품목그룹삭제
	@Override
	public void getdeleteitemgroup(HashMap<String, String> ableSearchConditionInfo) {
		
		itemDAO.deleteitemgroup(ableSearchConditionInfo);

	}

	//일괄처리
	@Override
	public void getbatchSave(ArrayList<ItemInfoTO> itemTOList) {

		for (ItemInfoTO bean : itemTOList) {
	        	 
				String status = bean.getStatus();
	             
					switch (status) {
	             
						case "DELETE":
								itemDAO.deletebatchSave(bean);
								break;
	                   
						case "INSERT":
							itemDAO.insertbatchSave(bean);
							break;
	                   
						case "UPDATE":
							itemDAO.updatebatchSave(bean);
					}
	          }
	}
}
