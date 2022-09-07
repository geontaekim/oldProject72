package kr.co.seoulit.logistics.logiinfosvc.compinfo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.seoulit.logistics.logiinfosvc.compinfo.dao.CompInfoDAO;
import kr.co.seoulit.logistics.logiinfosvc.compinfo.dao.CodeDAO;
import kr.co.seoulit.logistics.logiinfosvc.compinfo.to.AddressTO;
import kr.co.seoulit.logistics.logiinfosvc.compinfo.to.CodeDetailTO;
import kr.co.seoulit.logistics.logiinfosvc.compinfo.to.CodeTO;
import kr.co.seoulit.logistics.logiinfosvc.compinfo.to.CompanyTO;
import kr.co.seoulit.logistics.logiinfosvc.compinfo.to.ContractReportTO;
import kr.co.seoulit.logistics.logiinfosvc.compinfo.to.CustomerTO;
import kr.co.seoulit.logistics.logiinfosvc.compinfo.to.DepartmentTO;
import kr.co.seoulit.logistics.logiinfosvc.compinfo.to.EstimateReportTO;
import kr.co.seoulit.logistics.logiinfosvc.compinfo.to.FinancialAccountAssociatesTO;
import kr.co.seoulit.logistics.logiinfosvc.compinfo.to.ImageTO;
import kr.co.seoulit.logistics.logiinfosvc.compinfo.to.LatLngTO;
import kr.co.seoulit.logistics.logiinfosvc.compinfo.to.WorkplaceTO;

@Component
public class CompInfoServiceImpl implements CompInfoService {
	
	@Autowired
	private CodeDAO codeDAO;
	
	@Autowired
	private CompInfoDAO compInfoDAO;
	
	@Override
	public ArrayList<CodeDetailTO> getDetailCodeList(String divisionCode) {

		ArrayList<CodeDetailTO> codeDetailList = null;

		codeDetailList = codeDAO.selectDetailCodeList(divisionCode);

		return codeDetailList;
	}
	
	@Override
	public ArrayList<CodeTO> getCodeList() {		//완료

		ArrayList<CodeTO> codeList = null;

		codeList = codeDAO.selectCodeList();

		return codeList;
	}
	
	
	@Override
	public Boolean checkCodeDuplication(String divisionCode, String newDetailCode) {

		Boolean flag = false;
		ArrayList<CodeDetailTO> detailCodeList = null;

		detailCodeList = codeDAO.selectDetailCodeList(divisionCode);

		for (CodeDetailTO bean : detailCodeList) {

			if (bean.getDetailCode().equals(newDetailCode)) {
				
				flag = true; // 입력받은 newDetailCode 와 같은 값이 있으면 중복된 코드임
									//변수명 duplicated인지 flag인지 확인해볼 것
			}

		}
		
		return flag;
	}

	@Override
	public HashMap<String, Object> batchCodeListProcess(ArrayList<CodeTO> codeList) {

		HashMap<String, Object> resultMap = new HashMap<>();
		
		ArrayList<String> insertList = new ArrayList<>();
		ArrayList<String> updateList = new ArrayList<>();
		ArrayList<String> deleteList = new ArrayList<>();

		for (CodeTO bean : codeList) {

			String status = bean.getStatus();

			switch (status) {

			case "INSERT":

				codeDAO.insertCode(bean);

				insertList.add(bean.getDivisionCodeNo());

				break;

			case "UPDATE":

				codeDAO.updateCode(bean);
					
				updateList.add(bean.getDivisionCodeNo());

				break;

			case "DELETE":

				codeDAO.deleteCode(bean);

				deleteList.add(bean.getDivisionCodeNo());

				break;

			}

		}

		resultMap.put("INSERT", insertList);
		resultMap.put("UPDATE", updateList);
		resultMap.put("DELETE", deleteList);

		return resultMap;
	}

	@Override
	public HashMap<String, Object> batchDetailCodeListProcess(ArrayList<CodeDetailTO> detailCodeList) {

		HashMap<String, Object> resultMap = new HashMap<>();
		
		ArrayList<String> insertList = new ArrayList<>();
		ArrayList<String> updateList = new ArrayList<>();
		ArrayList<String> deleteList = new ArrayList<>();

		for (CodeDetailTO bean : detailCodeList) {

			String status = bean.getStatus();

			switch (status) {

			case "INSERT":

				codeDAO.insertDetailCode(bean);

				insertList.add(bean.getDivisionCodeNo() + " / " + bean.getDetailCode());

				break;

			case "UPDATE":

				codeDAO.updateDetailCode(bean);

				updateList.add(bean.getDivisionCodeNo() + " / " + bean.getDetailCode());

				break;

			case "DELETE":

				codeDAO.deleteDetailCode(bean);

				deleteList.add(bean.getDivisionCodeNo() + " / " + bean.getDetailCode());

				break;

			}

		}

		resultMap.put("INSERT", insertList);
		resultMap.put("UPDATE", updateList);
		resultMap.put("DELETE", deleteList);

		return resultMap;
	}

	@Override
	public HashMap<String, Object> changeCodeUseCheckProcess(ArrayList<CodeDetailTO> detailCodeList) {

		HashMap<String, Object> resultMap = new HashMap<>();

		ArrayList<String> codeUseList = new ArrayList<>();
		ArrayList<String> codeNotUseList = new ArrayList<>();

		for (CodeDetailTO bean : detailCodeList) {

			String codeUseCheck = ((bean.getCodeUseCheck() == null) ? "" : bean.getCodeUseCheck().toUpperCase());

			switch (codeUseCheck) {

			case "N":

				codeDAO.changeCodeUseCheck(bean.getDivisionCodeNo(), bean.getDetailCode(), "N");

				codeNotUseList.add(bean.getDivisionCodeNo() + " / " + bean.getDetailCode());

				break;

			default:

				codeDAO.changeCodeUseCheck(bean.getDivisionCodeNo(), bean.getDetailCode(), "");
					
				codeUseList.add(bean.getDivisionCodeNo() + " / " + bean.getDetailCode());
				System.out.println("codeUseList?"+bean.getDivisionCodeNo() + " / " + bean.getDetailCode());

				break;

			}

		}

		resultMap.put("USE", codeUseList);
		resultMap.put("NOT_USE", codeNotUseList);

		return resultMap;
	}

	@Override
	public ArrayList<AddressTO> getAddressList(String sidoName, String searchAddressType, String searchValue, String mainNumber) {

		ArrayList<AddressTO> addressList = null;

		String sidoCode = compInfoDAO.selectSidoCode(sidoName);

		switch (searchAddressType) {

		case "roadNameAddress":

			String buildingMainNumber = mainNumber;
				
			addressList = compInfoDAO.selectRoadNameAddressList(sidoCode, searchValue, buildingMainNumber);

			break;

		case "jibunAddress":

			String jibunMainAddress = mainNumber;

			addressList = compInfoDAO.selectJibunAddressList(sidoCode, searchValue, jibunMainAddress);

			break;

		}

		return addressList;
	}
	
	 @Override
	   public ArrayList<CodeDetailTO> getCodeDetailList(String CodeDetail) {

		 ArrayList<CodeDetailTO> codeLists = null;

	      codeLists = codeDAO.selectDetailCodeList(CodeDetail);

	      return codeLists;
	   }

	   @Override
	   public void addCodeInFormation(ArrayList<CodeTO>  CodeTOList) {
	      
			for (CodeTO bean : CodeTOList) {
				String status = bean.getStatus();
				switch (status) {
					case "DELETE":
						codeDAO.deleteCode(bean);
						break;
					case "INSERT":
						codeDAO.insertCode(bean);
						break;
					case "UPDATE":
						codeDAO.updateCode(bean);
				}
				for (CodeDetailTO detailbean : bean.getCodeDetailTOList()) {
					String status1 = detailbean.getStatus();
					switch (status1) {
						case "INSERT":
							codeDAO.insertDetailCode(detailbean);
							break;
						case "UPDATE":
							codeDAO.updateDetailCode(detailbean);
							break;
						case "DELETE":
							codeDAO.deleteDetailCode(detailbean);
							break;
					}
				}
			}
	   }

	    @Override
	    public ArrayList<CustomerTO> getCustomerList(String searchCondition, String companyCode, String workplaceCode,String itemGroupCode) {

	        ArrayList<CustomerTO> customerList = null;

			switch (searchCondition) {

				case "ALL":

				customerList = compInfoDAO.selectCustomerListByCompany();
				break;

			case "WORKPLACE":

				customerList = compInfoDAO.selectCustomerListByWorkplace(workplaceCode);
				break;
				
			case "ITEM":
				customerList = compInfoDAO.selectCustomerListByItem(itemGroupCode);
				break;
			}
			
	        return customerList;
	    }

	    @Override
	    public HashMap<String, Object> batchCustomerListProcess(ArrayList<CustomerTO> customerList) {

	        HashMap<String, Object> resultMap = new HashMap<>();

			ArrayList<String> insertList = new ArrayList<>();
			ArrayList<String> updateList = new ArrayList<>();
			ArrayList<String> deleteList = new ArrayList<>();

			CodeDetailTO detailCodeBean = new CodeDetailTO();

			for (CustomerTO bean : customerList) {

				String status = bean.getStatus();

				switch (status) {

				case "INSERT":

					String newCustomerCode = getNewCustomerCode(bean.getWorkplaceCode());
					bean.setCustomerCode(newCustomerCode);

					compInfoDAO.insertCustomer(bean);
					insertList.add(bean.getCustomerCode());

					detailCodeBean.setDivisionCodeNo("CL-01");
					detailCodeBean.setDetailCode(bean.getCustomerCode());
					detailCodeBean.setDetailCodeName(bean.getCustomerName());

					codeDAO.insertDetailCode(detailCodeBean);

					break;

				case "UPDATE":

					compInfoDAO.updateCustomer(bean);
					updateList.add(bean.getCustomerCode());

					detailCodeBean.setDivisionCodeNo("CL-01");
					detailCodeBean.setDetailCode(bean.getCustomerCode());
					detailCodeBean.setDetailCodeName(bean.getCustomerName());

					codeDAO.updateDetailCode(detailCodeBean);

					break;

				case "DELETE":

					compInfoDAO.deleteCustomer(bean);
					deleteList.add(bean.getCustomerCode());

					detailCodeBean.setDivisionCodeNo("CL-01");
					detailCodeBean.setDetailCode(bean.getCustomerCode());
					detailCodeBean.setDetailCodeName(bean.getCustomerName());
						
					codeDAO.deleteDetailCode(detailCodeBean);

					break;

				}
				
			}

			resultMap.put("INSERT", insertList);
			resultMap.put("UPDATE", updateList);
			resultMap.put("DELETE", deleteList);

	        return resultMap;

	    }

	    @Override
	    public ArrayList<FinancialAccountAssociatesTO> getFinancialAccountAssociatesList(String searchCondition,
	                                                                                     String workplaceCode) {

	        ArrayList<FinancialAccountAssociatesTO> financialAccountAssociatesList = null;

			switch (searchCondition) {

			case "ALL":

				financialAccountAssociatesList = compInfoDAO.selectFinancialAccountAssociatesListByCompany();
				break;

			case "WORKPLACE":

				financialAccountAssociatesList = compInfoDAO
						.selectFinancialAccountAssociatesListByWorkplace(workplaceCode);
				break;

			}
	        
			return financialAccountAssociatesList;

	    }

	    @Override
	    public HashMap<String, Object> batchFinancialAccountAssociatesListProcess(
	            ArrayList<FinancialAccountAssociatesTO> financialAccountAssociatesList) {

	        HashMap<String, Object> resultMap = new HashMap<>();

			ArrayList<String> insertList = new ArrayList<>();
			ArrayList<String> updateList = new ArrayList<>();
			ArrayList<String> deleteList = new ArrayList<>();

			CodeDetailTO detailCodeBean = new CodeDetailTO();

			for (FinancialAccountAssociatesTO bean : financialAccountAssociatesList) {

				String status = bean.getStatus();

				switch (status) {

				case "INSERT":

					String newFinancialAccountAssociatesCode = getNewFinancialAccountAssociatesCode();
					bean.setAccountAssociatesCode(newFinancialAccountAssociatesCode);

					compInfoDAO.insertFinancialAccountAssociates(bean);
					insertList.add(bean.getAccountAssociatesCode());

					detailCodeBean.setDivisionCodeNo("CL-02");
					detailCodeBean.setDetailCode(bean.getAccountAssociatesCode());
					detailCodeBean.setDetailCodeName(bean.getAccountAssociatesName());

					codeDAO.insertDetailCode(detailCodeBean);

					break;

				case "UPDATE":

					compInfoDAO.updateFinancialAccountAssociates(bean);
					updateList.add(bean.getAccountAssociatesCode());

					detailCodeBean.setDivisionCodeNo("CL-02");
					detailCodeBean.setDetailCode(bean.getAccountAssociatesCode());
					detailCodeBean.setDetailCodeName(bean.getAccountAssociatesName());

					codeDAO.updateDetailCode(detailCodeBean);

					break;

				case "DELETE":

					compInfoDAO.deleteFinancialAccountAssociates(bean);
					deleteList.add(bean.getAccountAssociatesCode());

					detailCodeBean.setDivisionCodeNo("CL-02");
					detailCodeBean.setDetailCode(bean.getAccountAssociatesCode());
					detailCodeBean.setDetailCodeName(bean.getAccountAssociatesName());

					codeDAO.deleteDetailCode(detailCodeBean);

					break;

				}

			}

			resultMap.put("INSERT", insertList);
			resultMap.put("UPDATE", updateList);
			resultMap.put("DELETE", deleteList);

	        return resultMap;
	    }
	    @Override
		public ArrayList<CompanyTO> getCompanyList() {

			ArrayList<CompanyTO> companyList = null;
			
			companyList = compInfoDAO.selectCompanyList();
			
			return companyList;
		}

		@Override
		public ArrayList<WorkplaceTO> getWorkplaceList(String companyCode) {

			ArrayList<WorkplaceTO> workplaceList = null;
			
			workplaceList = compInfoDAO.selectWorkplaceList(companyCode);

			return workplaceList;
		}

		@Override
		public ArrayList<DepartmentTO> getDepartmentList(String searchCondition, String companyCode,
				String workplaceCode) {

			ArrayList<DepartmentTO> departmentList = null;

			switch (searchCondition) {

			case "ALL":

				departmentList = compInfoDAO.selectDepartmentListByCompany(companyCode);
				break;

			case "WORKPLACE":

				departmentList = compInfoDAO.selectDepartmentListByWorkplace(workplaceCode);
				break;
					
			}

			return departmentList;
		}

		@Override
		public HashMap<String, Object> batchCompanyListProcess(ArrayList<CompanyTO> companyList) {

			HashMap<String, Object> resultMap = new HashMap<>();

			ArrayList<String> insertList = new ArrayList<>();
			ArrayList<String> updateList = new ArrayList<>();
			ArrayList<String> deleteList = new ArrayList<>();

			CodeDetailTO detailCodeBean = new CodeDetailTO();
				
			for (CompanyTO bean : companyList) {

				String status = bean.getStatus();

				switch (status) {   

				case "INSERT":

					String newCompanyCode = getNewCompanyCode();
					bean.setCompanyCode(newCompanyCode);

					compInfoDAO.insertCompany(bean);
					insertList.add(bean.getCompanyCode());

					detailCodeBean.setDivisionCodeNo("CO-01");
					detailCodeBean.setDetailCode(bean.getCompanyCode());
					detailCodeBean.setDetailCodeName(bean.getCompanyName());

					codeDAO.insertDetailCode(detailCodeBean);

					break;

				}

			}

			for (CompanyTO bean : companyList) {    // 2차 반복 : UPDATE , DELETE 만 실행

				String status = bean.getStatus();

				switch (status) {

				case "UPDATE":

					compInfoDAO.updateCompany(bean);
					updateList.add(bean.getCompanyCode());

					detailCodeBean.setDivisionCodeNo("CO-01");
					detailCodeBean.setDetailCode(bean.getCompanyCode());
					detailCodeBean.setDetailCodeName(bean.getCompanyName());

					codeDAO.updateDetailCode(detailCodeBean);

					break;

				case "DELETE":

					compInfoDAO.deleteCompany(bean);
					deleteList.add(bean.getCompanyCode());

					detailCodeBean.setDivisionCodeNo("CO-01");
					detailCodeBean.setDetailCode(bean.getCompanyCode());
					detailCodeBean.setDetailCodeName(bean.getCompanyName());

					codeDAO.deleteDetailCode(detailCodeBean);

					break;

				}

			}

			resultMap.put("INSERT", insertList);
			resultMap.put("UPDATE", updateList);
			resultMap.put("DELETE", deleteList);

			return resultMap;
		}

		@Override
		public HashMap<String, Object> batchWorkplaceListProcess(ArrayList<WorkplaceTO> workplaceList) {

			HashMap<String, Object> resultMap = new HashMap<>();

			ArrayList<String> insertList = new ArrayList<>();
			ArrayList<String> updateList = new ArrayList<>();
			ArrayList<String> deleteList = new ArrayList<>();

			CodeDetailTO detailCodeBean = new CodeDetailTO();

			for (WorkplaceTO bean : workplaceList) {

				String status = bean.getStatus();

				switch (status) {

				case "INSERT":

					String newWorkplaceCode = getNewWorkplaceCode(bean.getCompanyCode());
					bean.setWorkplaceCode(newWorkplaceCode);

					compInfoDAO.insertWorkplace(bean);
					insertList.add(bean.getWorkplaceCode());

					detailCodeBean.setDivisionCodeNo("CO-02");
					detailCodeBean.setDetailCode(bean.getWorkplaceCode());
					detailCodeBean.setDetailCodeName(bean.getWorkplaceName());

					codeDAO.insertDetailCode(detailCodeBean);

					break;

				case "UPDATE":

					compInfoDAO.updateWorkplace(bean);
					updateList.add(bean.getWorkplaceCode());

					detailCodeBean.setDivisionCodeNo("CO-02");
					detailCodeBean.setDetailCode(bean.getWorkplaceCode());
					detailCodeBean.setDetailCodeName(bean.getWorkplaceName());

					codeDAO.updateDetailCode(detailCodeBean);

					break;

				case "DELETE":

					compInfoDAO.deleteWorkplace(bean);
					deleteList.add(bean.getWorkplaceCode());

					detailCodeBean.setDivisionCodeNo("CO-02");
					detailCodeBean.setDetailCode(bean.getWorkplaceCode());
					detailCodeBean.setDetailCodeName(bean.getWorkplaceName());

					codeDAO.deleteDetailCode(detailCodeBean);

					break;

				}

			}

			resultMap.put("INSERT", insertList);
			resultMap.put("UPDATE", updateList);
			resultMap.put("DELETE", deleteList);

			return resultMap;
		}

		@Override
		public HashMap<String, Object> batchDepartmentListProcess(ArrayList<DepartmentTO> departmentList) {

			HashMap<String, Object> resultMap = new HashMap<>();

			ArrayList<String> insertList = new ArrayList<>();
			ArrayList<String> updateList = new ArrayList<>();
			ArrayList<String> deleteList = new ArrayList<>();

			CodeDetailTO detailCodeBean = new CodeDetailTO();

			for (DepartmentTO bean : departmentList) {

				String status = bean.getStatus();

				switch (status) {

				case "INSERT":

					String newDepartmentCode = getNewDepartmentCode(bean.getCompanyCode());
					bean.setDeptCode(newDepartmentCode);

					compInfoDAO.insertDepartment(bean);
					insertList.add(bean.getDeptCode());
						
					detailCodeBean.setDivisionCodeNo("CO-03");
					detailCodeBean.setDetailCode(bean.getDeptCode());
					detailCodeBean.setDetailCodeName(bean.getDeptName());

					codeDAO.insertDetailCode(detailCodeBean);

					break;

				case "UPDATE":

					compInfoDAO.updateDepartment(bean);
					updateList.add(bean.getDeptCode());

					detailCodeBean.setDivisionCodeNo("CO-03");
					detailCodeBean.setDetailCode(bean.getDeptCode());
					detailCodeBean.setDetailCodeName(bean.getDeptName());

					codeDAO.updateDetailCode(detailCodeBean);

					break;

				case "DELETE":

					compInfoDAO.deleteDepartment(bean);
					deleteList.add(bean.getDeptCode());

					detailCodeBean.setDivisionCodeNo("CO-03");
					detailCodeBean.setDetailCode(bean.getDeptCode());
					detailCodeBean.setDetailCodeName(bean.getDeptName());

					codeDAO.deleteDetailCode(detailCodeBean);

					break;

				}

			}

			resultMap.put("INSERT", insertList);
			resultMap.put("UPDATE", updateList);
			resultMap.put("DELETE", deleteList);

			return resultMap;
		}

		public String getNewCustomerCode(String companyCode) {

			ArrayList<CustomerTO> customerList = null;
			String newCustomerCode = null;
			
			customerList = compInfoDAO.selectCustomerListByCompany();

			TreeSet<Integer> customerCodeNoSet = new TreeSet<>();

			for (CustomerTO bean : customerList) {

				if (bean.getCustomerCode().startsWith("PTN-")) {

					try {

						Integer no = Integer.parseInt(bean.getCustomerCode().split("PTN-")[1]);
						customerCodeNoSet.add(no);

					} catch (NumberFormatException e) {

					}

				}

			}

			if (customerCodeNoSet.isEmpty()) {
				newCustomerCode = "PTN-" + String.format("%02d", 1);
			} else {
				newCustomerCode = "PTN-" + String.format("%02d", customerCodeNoSet.pollLast() + 1);
			}

			return newCustomerCode;
		}

		public String getNewFinancialAccountAssociatesCode() {

			ArrayList<FinancialAccountAssociatesTO> financialAccountAssociatesList = null;
			String newFinancialAccountAssociatesCode = null;

			financialAccountAssociatesList = compInfoDAO
					.selectFinancialAccountAssociatesListByCompany();

			TreeSet<Integer> financialAccountAssociatesCodeNoSet = new TreeSet<>();

			for (FinancialAccountAssociatesTO bean : financialAccountAssociatesList) {

				if (bean.getAccountAssociatesCode().startsWith("FPT-")) {

					try {

						Integer no = Integer.parseInt(bean.getAccountAssociatesCode().split("FPT-")[1]);
						financialAccountAssociatesCodeNoSet.add(no);

					} catch (NumberFormatException e) {

					}

				}

			}

			if (financialAccountAssociatesCodeNoSet.isEmpty()) {
				newFinancialAccountAssociatesCode = "FPT-" + String.format("%02d", 1);
			} else {
				newFinancialAccountAssociatesCode = "FPT-"
						+ String.format("%02d", financialAccountAssociatesCodeNoSet.pollLast() + 1);
			}

			return newFinancialAccountAssociatesCode;
		}

		public String getNewCompanyCode() {

			ArrayList<CompanyTO> companyList = null;
			String newCompanyCode = null;

			companyList = compInfoDAO.selectCompanyList();

			TreeSet<Integer> companyCodeNoSet = new TreeSet<>();

			for (CompanyTO bean : companyList) {

				if (bean.getCompanyCode().startsWith("COM-")) {

					try {

						Integer no = Integer.parseInt(bean.getCompanyCode().split("COM-")[1]);
						companyCodeNoSet.add(no);

					} catch (NumberFormatException e) {

					}

				}

			}

			if (companyCodeNoSet.isEmpty()) {
				newCompanyCode = "COM-" + String.format("%02d", 1);
			} else {
				newCompanyCode = "COM-" + String.format("%02d", companyCodeNoSet.pollLast() + 1);
			}

			return newCompanyCode;
		}

		public String getNewWorkplaceCode(String companyCode) {

			ArrayList<WorkplaceTO> workplaceList = null;
			String newWorkplaceCode = null;

			workplaceList = compInfoDAO.selectWorkplaceList(companyCode);

			TreeSet<Integer> workplaceCodeNoSet = new TreeSet<>();

			for (WorkplaceTO bean : workplaceList) {

				if (bean.getWorkplaceCode().startsWith("BRC-")) {

					try {

						Integer no = Integer.parseInt(bean.getWorkplaceCode().split("BRC-")[1]);
						workplaceCodeNoSet.add(no);

					} catch (NumberFormatException e) {

					}

				}

			}

			if (workplaceCodeNoSet.isEmpty()) {
				newWorkplaceCode = "BRC-" + String.format("%02d", 1);
			} else {
				newWorkplaceCode = "BRC-" + String.format("%02d", workplaceCodeNoSet.pollLast() + 1);
			}

			return newWorkplaceCode;
		}

		public String getNewDepartmentCode(String companyCode) {

			ArrayList<DepartmentTO> departmentList = null;
			String newDepartmentCode = null;

			departmentList = compInfoDAO.selectDepartmentListByCompany(companyCode);

			TreeSet<Integer> departmentCodeNoSet = new TreeSet<>();
			
			for (DepartmentTO bean : departmentList) {

				if (bean.getDeptCode().startsWith("DPT-")) {

					try {

						Integer no = Integer.parseInt(bean.getDeptCode().split("DPT-")[1]);
						departmentCodeNoSet.add(no);

					} catch (NumberFormatException e) {

					}

				}

			}

			if (departmentCodeNoSet.isEmpty()) {
				newDepartmentCode = "DPT-" + String.format("%02d", 1);
			} else {
				newDepartmentCode = "DPT-" + String.format("%02d", departmentCodeNoSet.pollLast() + 1);
			}

			return newDepartmentCode;
		}

		@Override
		  public ArrayList<LatLngTO> getLatLngList(String wareHouseCodeNo) {

		      ArrayList<LatLngTO> codeDetailList = null;

		         codeDetailList = codeDAO.selectLatLngList(wareHouseCodeNo);

		      return codeDetailList;
		   }
		
		@Override
		public ArrayList<ImageTO> getDetailItemList(String itemGroupCodeNo) {

		      ArrayList<ImageTO> codeDetailList = null;

		      codeDetailList = codeDAO.selectDetailItemList(itemGroupCodeNo);

		      return codeDetailList;
		   }
		@Override
		public ArrayList<EstimateReportTO> getEstimateReport(String estimateNo) {
			return compInfoDAO.selectEstimateReport(estimateNo);
		}

		@Override
		public ArrayList<ContractReportTO> getContractReport(String contractNo) {
			return compInfoDAO.selectContractReport(contractNo);
		}
}