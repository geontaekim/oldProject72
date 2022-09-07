package kr.co.seoulit.logistics.prodcsvc.production.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import com.itextpdf.text.log.SysoCounter;

import kr.co.seoulit.logistics.busisvc.logisales.dao.ContractDAO;
import kr.co.seoulit.logistics.busisvc.logisales.to.ContractDetailInMpsAvailableTO;
import kr.co.seoulit.logistics.busisvc.sales.dao.SalesPlanDAO;
import kr.co.seoulit.logistics.prodcsvc.production.dao.MpsDAO;
import kr.co.seoulit.logistics.prodcsvc.production.dao.MrpDAO;
import kr.co.seoulit.logistics.prodcsvc.production.to.MpsTO;
import kr.co.seoulit.logistics.prodcsvc.production.to.MrpGatheringTO;
import kr.co.seoulit.logistics.prodcsvc.production.to.MrpTO;
import kr.co.seoulit.logistics.prodcsvc.production.to.SalesPlanInMpsAvailableTO;

@Component
public class ProductionServiceImpl implements ProductionService {
	
	@Autowired
	private MpsDAO mpsDAO;
	
	@Autowired
	private ContractDAO contractDAO;
	
	@Autowired
	private SalesPlanDAO salesPlanDAO;
	
	@Autowired
	private MrpDAO mrpDAO;
	
	@Override
	public ArrayList<MpsTO> getMpsList(String startDate, String endDate, String includeMrpApply) {

		ArrayList<MpsTO> mpsTOList = null;
		
		Map<String,String> map = new HashMap<>();
		map.put("startDate",startDate);
		map.put("endDate",endDate);
		map.put("includeMrpApply",includeMrpApply);
		
		
		mpsTOList = mpsDAO.selectMpsList(map);

		return mpsTOList;
	}

	@Override
	public ArrayList<ContractDetailInMpsAvailableTO> getContractDetailListInMpsAvailable(String searchCondition,
			String startDate, String endDate) {

		ArrayList<ContractDetailInMpsAvailableTO> contractDetailInMpsAvailableList = null;
		
		
		Map<String,String> map = new HashMap<>();
		
		map.put("searchCondition",searchCondition);
		map.put("startDate",startDate);
		map.put("endDate",endDate);
		
		contractDetailInMpsAvailableList = contractDAO.selectContractDetailListInMpsAvailable(map);
		
		return contractDetailInMpsAvailableList;

	}

	@Override
	public ArrayList<SalesPlanInMpsAvailableTO> getSalesPlanListInMpsAvailable(String searchCondition,
			String startDate, String endDate) {

		ArrayList<SalesPlanInMpsAvailableTO> salesPlanInMpsAvailableList = null;

		salesPlanInMpsAvailableList = salesPlanDAO.selectSalesPlanListInMpsAvailable(searchCondition, startDate, endDate);

		return salesPlanInMpsAvailableList;

	}

	@Override
	public HashMap<String, Object> convertContractDetailToMps(
			ArrayList<ContractDetailInMpsAvailableTO> contractDetailInMpsAvailableList) {
		
		HashMap<String, Object> resultMap = null;

		ArrayList<MpsTO> mpsTOList = new ArrayList<>();

		MpsTO newMpsBean = null;

		System.out.println("동기1");
		
		for (ContractDetailInMpsAvailableTO bean : contractDetailInMpsAvailableList) {

			newMpsBean = new MpsTO();

			newMpsBean.setStatus("INSERT");

			newMpsBean.setMpsPlanClassification(bean.getPlanClassification());
			newMpsBean.setContractDetailNo(bean.getContractDetailNo());
			newMpsBean.setItemCode(bean.getItemCode());
			newMpsBean.setItemName(bean.getItemName());
			newMpsBean.setUnitOfMps(bean.getUnitOfContract());
			newMpsBean.setMpsPlanDate(bean.getMpsPlanDate());
			newMpsBean.setMpsPlanAmount(bean.getProductionRequirement());
			newMpsBean.setDueDateOfMps(bean.getDueDateOfContract());
			newMpsBean.setScheduledEndDate(bean.getScheduledEndDate());
			newMpsBean.setDescription(bean.getDescription());

			mpsTOList.add(newMpsBean);

		}

		resultMap = batchMpsListProcess(mpsTOList); //batchMpsListProcess 메소드 호출

		return resultMap;

	}

	@Override
	public HashMap<String, Object> convertSalesPlanToMps(
			ArrayList<SalesPlanInMpsAvailableTO> salesPlanInMpsAvailableList) {

		HashMap<String, Object> resultMap = null;

		ArrayList<MpsTO> mpsTOList = new ArrayList<>();

		MpsTO newMpsBean = null;

		for (SalesPlanInMpsAvailableTO bean : salesPlanInMpsAvailableList) {

			newMpsBean = new MpsTO();

			newMpsBean.setStatus("INSERT");

			newMpsBean.setMpsPlanClassification(bean.getPlanClassification());
			newMpsBean.setSalesPlanNo(bean.getSalesPlanNo());
			newMpsBean.setItemCode(bean.getItemCode());
			newMpsBean.setItemName(bean.getItemName());
			newMpsBean.setUnitOfMps(bean.getUnitOfSales());
			newMpsBean.setMpsPlanDate(bean.getMpsPlanDate());
			newMpsBean.setMpsPlanAmount(bean.getSalesAmount());
			newMpsBean.setDueDateOfMps(bean.getDueDateOfSales());
			newMpsBean.setScheduledEndDate(bean.getScheduledEndDate());
			newMpsBean.setDescription(bean.getDescription());

			mpsTOList.add(newMpsBean);

		}

		resultMap = batchMpsListProcess(mpsTOList);

		return resultMap;

	}

	@Override
	public HashMap<String, Object> batchMpsListProcess(ArrayList<MpsTO> mpsTOList) {

		HashMap<String, Object> resultMap = new HashMap<>();
		
		ArrayList<String> insertList = new ArrayList<>();
		ArrayList<String> updateList = new ArrayList<>();
		ArrayList<String> deleteList = new ArrayList<>();
		
		
		for (MpsTO bean : mpsTOList) {

			String status = bean.getStatus();
							
			switch (status) {

			case "INSERT":

				String newMpsNo = getNewMpsNo(bean.getMpsPlanDate());

				bean.setMpsNo(newMpsNo);

				mpsDAO.insertMps(bean);

				insertList.add(newMpsNo);//ArrayList에 mps 번호를 넣는다.

				System.out.println("newMpsNo3"+newMpsNo);
				
				if (bean.getContractDetailNo() != null) {
					System.out.println("제발나오게해주세요"+bean.getContractDetailNo());
					changeMpsStatusInContractDetail(bean.getContractDetailNo(), "Y");

				} else if (bean.getSalesPlanNo() != null) {

					changeMpsStatusInSalesPlan(bean.getSalesPlanNo(), "Y");

				}

				break;

			case "UPDATE":

				mpsDAO.updateMps(bean);

				updateList.add(bean.getMpsNo());

				break;

			case "DELETE":

				mpsDAO.deleteMps(bean);

				deleteList.add(bean.getMpsNo());

				break;

			}

		}

		resultMap.put("INSERT", insertList);
		resultMap.put("UPDATE", updateList);
		resultMap.put("DELETE", deleteList);

		System.out.println("동기4");
		
		return resultMap;

	}

	@Override
	public ArrayList<MrpTO> searchMrpList(String mrpGatheringStatusCondition) {

		ArrayList<MrpTO> mrpList = null;

		mrpList = mrpDAO.selectMrpList(mrpGatheringStatusCondition);

		return mrpList;

	}

	@Override
	public ArrayList<MrpTO> searchMrpList(String dateSearchCondtion, String startDate, String endDate) {

		ArrayList<MrpTO> mrpList = null;
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("dateSearchCondtion", dateSearchCondtion);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		

		mrpList = mrpDAO.selectMrpListAsDate(map);

		return mrpList;
	}

	@Override
	public ArrayList<MrpTO> searchMrpListAsMrpGatheringNo(String mrpGatheringNo) {

		ArrayList<MrpTO> mrpList = null;
		
		HashMap<String, String> map = new HashMap<String, String>();

		mrpList = mrpDAO.selectMrpListAsMrpGatheringNo(mrpGatheringNo);

		return mrpList;
	}

	@Override
	public ArrayList<MrpGatheringTO> searchMrpGatheringList(String dateSearchCondtion, String startDate,
			String endDate) {

		ArrayList<MrpGatheringTO> mrpGatheringList = null;
		HashMap<String, String> map = new HashMap<String, String>();
	
		map.put("dateSearchCondtion", dateSearchCondtion);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		mrpGatheringList = mrpDAO.selectMrpGatheringList(map);

		for(MrpGatheringTO bean : mrpGatheringList)    {
	            
	    	bean.setMrpTOList(  mrpDAO.selectMrpListAsMrpGatheringNo( bean.getMrpGatheringNo()) );
	         
		}

		return mrpGatheringList;
	}

	@Override
	public ModelMap openMrp(ArrayList<String> mpsNoArr) {

		ModelMap resultMap = new ModelMap();
		
		String mpsNoList = mpsNoArr.toString().replace("[", "").replace("]", "");		
		//파사드mpsNoArr??[PS2022070701]
		//파사드mpsNoList??PS2022070701
		System.out.println("파사드mpsNoArr??"+mpsNoArr);
		
		System.out.println("파사드mpsNoList??"+mpsNoList);
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		map.put("mpsNoList", mpsNoList);		
		System.out.println("map"+map);
		mrpDAO.openMrp(map); 
	
		System.out.println("파사드resultMap??"+resultMap);
		 resultMap.put("errorCode",map.get("ERROR_CODE"));
		 resultMap.put("errorMsg",map.get("ERROR_MSG"));
		 resultMap.put("result",map.get("RESULT"));
		 
		 System.out.println("파사드오픈resultMap"+resultMap);


		return resultMap;       //resultMap에 널값이뜸 확인해서 넣기 
	}
	
	@Override
	public ModelMap registerMrp(String mrpRegisterDate, ArrayList<String> mpsList) {

		ModelMap resultMap = new ModelMap();
		

		System.out.println("mpsList???"+mpsList);
		System.out.println("mrpRegisterDate???"+mrpRegisterDate);
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		map.put("mrpRegisterDate", mrpRegisterDate);
		
		System.out.println("map에는??"+map);
		 mrpDAO.insertMrpList(map); 
		
		 resultMap.put("errorCode",map.get("ERROR_CODE"));
		 resultMap.put("errorMsg",map.get("ERROR_MSG"));
		 resultMap.put("result",map.get("RESULT"));
		 
		 System.out.println("resultMap??"+resultMap);
		 	
		  for (String mpsNo : mpsList) {
				  map.put("mpsNo",mpsNo);
				  map.put("mrpStatus","Y");
				  mpsDAO.changeMrpApplyStatus(map);
				  
			  }
			  System.out.println("map22222에는??"+map);
			  resultMap.put("mpsmap",map);
			  System.out.println("resultMap에 mps까지더한거"+resultMap);
			
		return resultMap;
	}
	
	@Override
	public ModelMap registerMrpGathering(String mrpGatheringRegisterDate,ArrayList<String> mrpNoArr,HashMap<String, String> mrpNoAndItemCodeMap) {
		
		ModelMap resultMap = null;
		
	    int seq=0;
	    
	    ArrayList<MrpGatheringTO> mrpGatheringList = null;
	    System.out.println("mrpGatheringRegisterDate??"+mrpGatheringRegisterDate);
	    
	    int i =1;
	    
		/* mrpDAO.selectMrpGatheringCount(mrpGatheringRegisterDate); */
	    		 
	    /*
         * ( itemCode : 새로운 mrpGathering 일련번호 ) 키/값 Map => itemCode 로 mrpNo 와
         * mrpGatheringNo 를 매칭
         */
	    
	    HashMap<String, String> itemCodeAndMrpGatheringNoMap = new HashMap<>();
	    HashMap<String, String> map = new HashMap<>();
	    
	    
	    StringBuffer newMrpGatheringNo = new StringBuffer();
	    newMrpGatheringNo.append("MG"); 
	    newMrpGatheringNo.append(mrpGatheringRegisterDate.replace("-", ""));
	    newMrpGatheringNo.append("-");
	    // 새로운 mrpGathering 일련번호 양식 생성 : 등록일자 '2020-04-28' => 일련번호 'MG20200428-'     
	    seq=mrpDAO.getMGSeqNo();  //시퀀스만들기
	         
	    mrpGatheringList = getMrpGathering(mrpNoArr);   

	    for (MrpGatheringTO bean : mrpGatheringList) { 
	    	bean.setMrpGatheringNo(newMrpGatheringNo.toString() + String.format("%03d", seq++));
	    	bean.setStatus("INSERT");
	    	bean.setMrpGatheringSeq(seq);

	    	itemCodeAndMrpGatheringNoMap.put(bean.getItemCode(), bean.getMrpGatheringNo());
	        System.out.println("아이템코드"+bean.getItemCode());
	        System.out.println("아이템코드"+bean.getMrpGatheringNo());
	    }

	    resultMap = batchMrpGatheringListProcess(mrpGatheringList);

	    TreeSet<String> mrpGatheringNoSet = new TreeSet<>();

	    @SuppressWarnings("unchecked")
	    HashMap<String, String> mrpGatheringNoList = (HashMap<String, String>) resultMap.get("INSERT_MAP");//key(ItemCode):value(소요량취합번호)
	         
	    for (String mrpGatheringNo : mrpGatheringNoList.values()) {
	    	
	    	mrpGatheringNoSet.add(mrpGatheringNo);

	    }

	    resultMap.put("firstMrpGatheringNo", mrpGatheringNoSet.pollFirst());
	    resultMap.put("lastMrpGatheringNo", mrpGatheringNoSet.pollLast());

	    for (String mrpNo : mrpNoAndItemCodeMap.keySet()) {
	    	String itemCode = mrpNoAndItemCodeMap.get(mrpNo);
	    	String mrpGatheringNo = itemCodeAndMrpGatheringNoMap.get(itemCode);
	    	map.put("mrpNo", mrpNo);
	    	map.put("mrpGatheringNo", mrpGatheringNo);
	    	map.put("mrpGatheringStatus", "Y");
	    	mrpDAO.changeMrpGatheringStatus(map);
	    }
	         
	    String mrpNoList = mrpNoArr.toString().replace("[", "").replace("]", "");

	    resultMap.put("changeMrpGatheringStatus", mrpNoList);

	    StringBuffer sb = new StringBuffer();
	 		
	    for(String mrpGatheringNo : mrpGatheringNoList.values()) {
	    	sb.append(mrpGatheringNo);
	    	sb.append(",");
	    }
	 		
	    sb.delete(sb.toString().length()-1, sb.toString().length());

	    HashMap<String, String> parameter = new HashMap<>();
	    parameter.put("mrpGatheringNoList", sb.toString());
	    mrpDAO.updateMrpGatheringContract(parameter);
	    
	    resultMap.put("errorCode",0);
	    resultMap.put("errorMsg","성공");

		return resultMap;
	}

	@Override
	public HashMap<String, Object> batchMrpListProcess(ArrayList<MrpTO> mrpTOList) {
		
		HashMap<String, Object> resultMap = new HashMap<>();

		ArrayList<String> insertList = new ArrayList<>();
		ArrayList<String> updateList = new ArrayList<>();
		ArrayList<String> deleteList = new ArrayList<>();

		for (MrpTO bean : mrpTOList) {

			String status = bean.getStatus();

			switch (status) {

				case "INSERT":

	               mrpDAO.insertMrp(bean);
	               // dao 파트 끝

	               insertList.add(bean.getMrpNo());

	               break;

	            case "UPDATE":

	               // dao 파트 시작
	               mrpDAO.updateMrp(bean);
	               // dao 파트 끝

	               updateList.add(bean.getMrpNo());

	               break;

	            case "DELETE":

	               // dao 파트 시작
	               mrpDAO.deleteMrp(bean);
	               // dao 파트 끝

	               deleteList.add(bean.getMrpNo());

	               break;

	            }

	         }

		resultMap.put("INSERT", insertList);
		resultMap.put("UPDATE", updateList);
		resultMap.put("DELETE", deleteList);

		return resultMap;
	}

	@Override
	public ArrayList<MrpGatheringTO> getMrpGathering(ArrayList<String> mrpNoArr) {
		
		ArrayList<MrpGatheringTO> mrpGatheringList = null;

		String mrpNoList = mrpNoArr.toString().replace("[", "").replace("]", "");
		mrpGatheringList = mrpDAO.getMrpGathering(mrpNoList);

		return mrpGatheringList;
	}
	
	
	public String getNewMpsNo(String mpsPlanDate) {
		StringBuffer newEstimateNo = null;
		List<MpsTO> mpsTOlist = mpsDAO.selectMpsCount(mpsPlanDate);
		TreeSet<Integer> intSet = new TreeSet<>();
		for (MpsTO bean : mpsTOlist) {
			String mpsNo = bean.getMpsNo();
			// MPS 일련번호에서 마지막 2자리만 가져오기
			int no = Integer.parseInt(mpsNo.substring(mpsNo.length() - 2, mpsNo.length()));
			intSet.add(no);	
		}
		int i=1;
		if (!intSet.isEmpty()) {
			i=intSet.pollLast() + 1;
		}

		newEstimateNo = new StringBuffer();
		newEstimateNo.append("PS");
		newEstimateNo.append(mpsPlanDate.replace("-", ""));
		newEstimateNo.append(String.format("%02d", i)); //PS2020042401

		return newEstimateNo.toString();
	}

	public void changeMpsStatusInContractDetail(String contractDetailNo, String mpsStatus) {

		Map<String,String> map = new HashMap<>();
		System.out.println("contractDetailNo???"+contractDetailNo);
		System.out.println("mpsStatus???"+mpsStatus);
		map.put("contractDetailNo",contractDetailNo);
		map.put("mpsStatus",mpsStatus);
		System.out.println("changeMpsStatusInContractDetailmap???"+map);
		contractDAO.changeMpsStatusOfContractDetail(map);

	}

	public void changeMpsStatusInSalesPlan(String salesPlanNo, String mpsStatus) {

		Map<String,String> map = new HashMap<>();
		map.put("salesPlanNo",salesPlanNo);
		map.put("mpsStatus",mpsStatus);
		
		salesPlanDAO.changeMpsStatusOfSalesPlan(map);

	}

	public ModelMap batchMrpGatheringListProcess(ArrayList<MrpGatheringTO> mrpGatheringTOList) {

		ModelMap resultMap = new ModelMap();

		 HashMap<String, String> insertListMap = new HashMap<>(); 
		 ArrayList<String> insertList = new ArrayList<>();
		 ArrayList<String> updateList = new ArrayList<>();
		 ArrayList<String> deleteList = new ArrayList<>();

		 for (MrpGatheringTO bean : mrpGatheringTOList) {
		            
			 String status = bean.getStatus();
			 System.out.println("status??"+status);
			 switch (status) {

			 	case "INSERT":

			 			mrpDAO.insertMrpGathering(bean);
		               
			 			insertList.add(bean.getMrpGatheringNo());

			 			insertListMap.put(bean.getItemCode(), bean.getMrpGatheringNo());

			 			break;

			 	case "UPDATE":

			 		mrpDAO.updateMrpGathering(bean);

			 		updateList.add(bean.getMrpGatheringNo());

			 		break;

			 	case "DELETE":

			 		mrpDAO.deleteMrpGathering(bean);

			 		deleteList.add(bean.getMrpGatheringNo());

			 		break;

			 }

		 }

		 resultMap.put("INSERT_MAP", insertListMap); //key(ItemCode) : value(getMrpGatheringNo)
		 resultMap.put("INSERT", insertList); //소요량취합번호
		 resultMap.put("UPDATE", updateList);
		 resultMap.put("DELETE", deleteList);

		 return resultMap;
		   }
}
