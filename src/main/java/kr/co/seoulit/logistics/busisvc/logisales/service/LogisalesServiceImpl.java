package kr.co.seoulit.logistics.busisvc.logisales.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import kr.co.seoulit.logistics.busisvc.logisales.dao.ContractDAO;
import kr.co.seoulit.logistics.busisvc.logisales.dao.EstimateDAO;
import kr.co.seoulit.logistics.busisvc.logisales.to.ContractDetailTO;
import kr.co.seoulit.logistics.busisvc.logisales.to.ContractInfoTO;
import kr.co.seoulit.logistics.busisvc.logisales.to.EstimateDetailTO;
import kr.co.seoulit.logistics.busisvc.logisales.to.EstimateTO;
import kr.co.seoulit.logistics.logiinfosvc.help.to.boardTO2;

@Component
public class LogisalesServiceImpl implements LogisalesService {
	
	@Autowired
	private EstimateDAO estimateDAO;
	
	@Autowired
	private ContractDAO contractDAO;


	@Override
	public ArrayList<EstimateTO> getEstimateList(String dateSearchCondition, String startDate, String endDate) {

		ArrayList<EstimateTO> estimateTOList = null;

		
		System.out.println("3");
		System.out.println(endDate);
		System.out.println(dateSearchCondition);
		System.out.println(startDate);
		Map<String,String> map = new HashMap<String, String>();
		map.put("dateSearchCondition",dateSearchCondition);
		map.put("startDate",startDate);
		map.put("endDate",endDate);
		estimateTOList = estimateDAO.selectEstimateList(map);
		System.out.println(estimateTOList);
		System.out.println("4");

		
		return estimateTOList;
	}

	@Override
	public ArrayList<EstimateDetailTO> getEstimateDetailList(String estimateNo) {

		ArrayList<EstimateDetailTO> estimateDetailTOList = null;

		estimateDetailTOList = estimateDAO.selectEstimateDetailList(estimateNo);

		return estimateDetailTOList;
	}

	@Override
	public ModelMap addNewEstimate(String estimateDate, EstimateTO newEstimateTO) {

		ModelMap resultMap = null;

		//새로운 견적 번호 생성
		String newEstimateNo = getNewEstimateNo(estimateDate);

		newEstimateTO.setEstimateNo(newEstimateNo);

		System.out.println("@@동기2@@");
		estimateDAO.insertEstimate(newEstimateTO);
		System.out.println("@@동기3@@-견적 insert 완료");
			

	
		ArrayList<EstimateDetailTO> estimateDetailTOList = newEstimateTO.getEstimateDetailTOList(); //bean객체
			
		for (EstimateDetailTO bean : estimateDetailTOList) {
			String newEstimateDetailNo = getNewEstimateDetailNo(newEstimateNo);
				
			bean.setEstimateNo(newEstimateNo);
				
			bean.setEstimateDetailNo(newEstimateDetailNo);
		}

		resultMap = batchEstimateDetailListProcess(estimateDetailTOList,newEstimateNo);

		resultMap.put("newEstimateNo", newEstimateNo);

		return resultMap;
	}
	
	@Override
	public String getNewEstimateNo(String estimateDate) {

		StringBuffer newEstimateNo = null;

		int i = estimateDAO.selectEstimateCount(estimateDate);

		newEstimateNo = new StringBuffer();
		newEstimateNo.append("ES");
		newEstimateNo.append(estimateDate.replace("-", ""));
		newEstimateNo.append(String.format("%02d", i)); 
			
		return newEstimateNo.toString();
	}
	
	@Override
	public String getNewEstimateDetailNo(String estimateNo) {

		StringBuffer newEstimateDetailNo = null;

		int i = estimateDAO.selectEstimateDetailSeq(estimateNo);

		newEstimateDetailNo = new StringBuffer();
		newEstimateDetailNo.append("ES");
		newEstimateDetailNo.append(estimateNo.replace("-", ""));
		newEstimateDetailNo.append("-"); 
		newEstimateDetailNo.append(String.format("%02d", i));		   

		return newEstimateDetailNo.toString();
	}

	@Override
	public void removeEstimate(Map<String,Object> map2){

		ModelMap resultMap = null;
		
		
			 estimateDAO.deleteEstimate(map2);
		
	
		/*
		ArrayList<EstimateDetailTO> estimateDetailTOList = getEstimateDetailList(estimateNo); //bean객체
			
		for (EstimateDetailTO bean : estimateDetailTOList) {
				
			bean.setStatus(status);		//set에 status 등록
			System.out.println("status??"+status);    //DELETE
				
		}
			
		resultMap = batchEstimateDetailListProcess(estimateDetailTOList,estimateNo);     //상세정보리스트 , 일련번호
			
		resultMap.put("removeEstimateNo", estimateNo);		//키 = removeEstimateNo 값 = 일련번호
		*/
			
		
	}

	@Override
	public ModelMap batchEstimateDetailListProcess(ArrayList<EstimateDetailTO> estimateDetailTOList,String estimateNo) {
		
		ModelMap resultMap = new ModelMap();
		
		ArrayList<String> insertList = new ArrayList<>();
		ArrayList<String> updateList = new ArrayList<>();
		ArrayList<String> deleteList = new ArrayList<>();

		estimateDAO.initDetailSeq("EST_DETAIL_SEQ");
				
		//count가 null이라는건 등록된 상세견적이 없다는거임
		//지금 이 로직은 등록된 상세견적이 있는지 없는지 확인하는 로직이다.
		String count = estimateDAO.selectEstimateDetailCount(estimateNo);		//dao단에서 상세일련번호 가져옴
		//count = 상세일련번호
	
		int cnt=1;
		if(count!=null) cnt=Integer.parseInt((count.split("-")[1]).substring(1)); 
		/* if(count!=null) cnt=Integer.parseInt(count); */
		
		boolean isDelete=false;
		
		for (EstimateDetailTO bean : estimateDetailTOList) {

			String status = bean.getStatus();
			//status = DELETE or INSERT or UPDATE

			switch (status) {

			case "INSERT":
				if(cnt==1) {  //원래 cnt==1 이라고 되어있는데 이 의미가 등록된 상				
					estimateDAO.insertEstimateDetail(bean);
				}else {
					String newCnt= estimateDAO.selectEstimateDetailCount(estimateNo); //새로운 견적을 추가하고싶을때인데 안됨
					StringBuffer newEstimateDetailNo = new StringBuffer();
					newEstimateDetailNo.append("ES");
					newEstimateDetailNo.append(estimateNo.replace("-", ""));
					newEstimateDetailNo.append("-"); 
					newEstimateDetailNo.append(String.format("%02d", newCnt));	
					bean.setEstimateDetailNo(newEstimateDetailNo.toString());
					estimateDAO.insertEstimateDetail(bean);
					System.out.println("@@동기5@@-insert완료-cnt!=1");
				}
				insertList.add(bean.getEstimateDetailNo());
				break;
					
			case "UPDATE":
				estimateDAO.updateEstimateDetail(bean);
				updateList.add(bean.getEstimateDetailNo());
				break;
			
			case "DELETE":
				estimateDAO.deleteEstimateDetail(bean);   //for (EstimateDetailTO bean : estimateDetailTOList) 
				deleteList.add(bean.getEstimateDetailNo());   //상세일련번호
				isDelete=true;
				break;
			}
		}
		if(isDelete==true) {
			for (EstimateDetailTO bean : estimateDetailTOList) {
				int i = estimateDAO.selectEstimateDetailSeq(estimateNo);	
				System.out.println("i값은???"+i);  
				String newSeq = String.format("%02d", i);		
				estimateDAO.reArrangeEstimateDetail(bean,newSeq);
			}
			estimateDAO.initDetailSeq("EST_DETAIL_SEQ");
		}
		resultMap.put("INSERT", insertList);
		resultMap.put("UPDATE", updateList);
		resultMap.put("DELETE", deleteList);

		return resultMap;
	}

	@Override
	public ArrayList<ContractInfoTO> getContractList(String searchCondition, String[] paramArray) {

		ArrayList<ContractInfoTO> contractInfoTOList = null;
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		switch (searchCondition) {

		case "searchByDate":

			String startDate = paramArray[0];
			String endDate = paramArray[1];
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			contractInfoTOList = contractDAO.selectContractListByDate(map);

			break;

		case "searchByCustomer":

			String customerCode = paramArray[0];

			contractInfoTOList = contractDAO.selectContractListByCustomer(customerCode);

			break;

		}

		for (ContractInfoTO bean : contractInfoTOList) {

			bean.setContractDetailTOList(contractDAO.selectContractDetailList(bean.getContractNo()));

		}

		return contractInfoTOList;
	}

	
	@Override
	public ArrayList<ContractDetailTO> getContractDetailList(String contractNo) {

		ArrayList<ContractDetailTO> contractDetailTOList = null;

		contractDetailTOList = contractDAO.selectContractDetailList(contractNo);

		return contractDetailTOList;
	}

	@Override
	public ArrayList<EstimateTO> getEstimateListInContractAvailable(String startDate, String endDate) {

		ArrayList<EstimateTO> estimateListInContractAvailable = null;

		System.out.println("동기 startDate-"+startDate);
		System.out.println("동기 startDate-"+endDate);

		HashMap<String,String> map =new HashMap<>();
		map.put("startDate",startDate);
		map.put("endDate",endDate);
		
		estimateListInContractAvailable = contractDAO.selectEstimateListInContractAvailable(map);

		for (EstimateTO bean : estimateListInContractAvailable) {

			bean.setEstimateDetailTOList(estimateDAO.selectEstimateDetailList(bean.getEstimateNo()));//ES2022011360

		}

		return estimateListInContractAvailable;
	}
	
	

	@Override
	public ModelMap addNewContract(HashMap<String,String[]>  workingContractList) {

		ModelMap resultMap = new ModelMap();
		HashMap<String,String> setValue = null;
		StringBuffer str = null;

		setValue=new HashMap<String,String>();
		for(String key:workingContractList.keySet()) {
			str=new StringBuffer();
				
			for(String value:workingContractList.get(key)) {
				if(key.equals("contractDate")) {
					String newContractNo=getNewContractNo(value);	
					str.append(newContractNo+",");
				}
				else str.append(value+",");
			}

			str.substring(0, str.length()-1);
			if(key.equals("contractDate")) 
				setValue.put("newContractNo", str.toString()); 
					
			else 
			setValue.put(key, str.toString());
		}
		contractDAO.insertContractDetail(setValue);
		
		resultMap.put("gridRowJSON", setValue.get("RESULT"));
		resultMap.put("errorCode", setValue.get("ERROR_MSG"));
		resultMap.put("errorMsg", setValue.get("ERROR_CODE"));
		
		return resultMap;
	}

	public String getNewContractNo(String contractDate) {
		
		StringBuffer newContractNo = null;

		int i = contractDAO.selectContractCount(contractDate);
		newContractNo = new StringBuffer();
		newContractNo.append("CO"); //CO
		newContractNo.append(contractDate.replace("-", "")); 
		newContractNo.append(String.format("%02d", i));

		return newContractNo.toString();
	}
	
	@Override
	public ModelMap batchContractDetailListProcess(ArrayList<ContractDetailTO> contractDetailTOList) {

		ModelMap resultMap = new ModelMap();

		ArrayList<String> insertList = new ArrayList<>();
		ArrayList<String> updateList = new ArrayList<>();
		ArrayList<String> deleteList = new ArrayList<>();
		
		for (ContractDetailTO bean : contractDetailTOList) {

			String status = bean.getStatus();

			switch (status) {

			case "INSERT":

				insertList.add(bean.getContractDetailNo());

				break;

			case "UPDATE":

				updateList.add(bean.getContractDetailNo());

				break;
					
			case "DELETE":

				contractDAO.deleteContractDetail(bean);
				deleteList.add(bean.getContractDetailNo());

				break;
			}
		}

		resultMap.put("INSERT", insertList);
		resultMap.put("UPDATE", updateList);
		resultMap.put("DELETE", deleteList);

		return resultMap;
	}

	@Override
	public void changeContractStatusInEstimate(String estimateNo, String contractStatus) {

		estimateDAO.changeContractStatusOfEstimate(estimateNo, contractStatus);
	}
	@Override
	public ArrayList<EstimateDetailTO> getEstimateDetailNo(String estimateNo) {
		
			System.out.println("sf에서 일련번호"+estimateNo);
				
			 
			
			ArrayList<EstimateDetailTO> k = estimateDAO.selectEstimateDetailNo(estimateNo);
			System.out.println("동규 : "+k);
			return k;		
					
		}
		
	}
