package kr.co.seoulit.logistics.logiinfosvc.hr.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import kr.co.seoulit.logistics.logiinfosvc.compinfo.dao.CodeDAO;
import kr.co.seoulit.logistics.logiinfosvc.compinfo.to.CodeDetailTO;
import kr.co.seoulit.logistics.logiinfosvc.hr.dao.AuthorityDAO;
import kr.co.seoulit.logistics.logiinfosvc.hr.dao.EmpDAO;
import kr.co.seoulit.logistics.logiinfosvc.hr.exception.IdNotFoundException;
import kr.co.seoulit.logistics.logiinfosvc.hr.exception.PwMissMatchException;
import kr.co.seoulit.logistics.logiinfosvc.hr.exception.PwNotFoundException;
import kr.co.seoulit.logistics.logiinfosvc.hr.to.AuthorityGroupMenuTO;
import kr.co.seoulit.logistics.logiinfosvc.hr.to.AuthorityGroupTO;
import kr.co.seoulit.logistics.logiinfosvc.hr.to.AuthorityInfoGroupTO;
import kr.co.seoulit.logistics.logiinfosvc.hr.to.EmpInfoTO;
import kr.co.seoulit.logistics.logiinfosvc.hr.to.EmployeeAuthorityTO;
import kr.co.seoulit.logistics.logiinfosvc.hr.to.EmployeeBasicTO;
import kr.co.seoulit.logistics.logiinfosvc.hr.to.EmployeeDetailTO;
import kr.co.seoulit.logistics.logiinfosvc.hr.to.EmployeeSecretTO;
import kr.co.seoulit.logistics.logiinfosvc.hr.to.MenuAuthorityTO;
import kr.co.seoulit.logistics.logiinfosvc.hr.to.MenuTO;

@Component
public class HRServiceImpl implements HRService {
	
	@Autowired
	private EmpDAO empDAO;
	
	@Autowired
	private CodeDAO codeDAO;

	@Autowired
	private AuthorityDAO authorityDAO;
	


	@Override
	public ArrayList<EmpInfoTO> getAllEmpList(String searchCondition, String[] paramArray) {

		ArrayList<EmpInfoTO> empList = null;

		empList = empDAO.selectAllEmpList(searchCondition, paramArray);

		for (EmpInfoTO bean : empList) {

			bean.setEmpDetailTOList(
					empDAO.selectEmployeeDetailList(bean.getCompanyCode(), bean.getEmpCode()));

			bean.setEmpSecretTOList(
					empDAO.selectEmployeeSecretList(bean.getCompanyCode(), bean.getEmpCode()));

		}

		return empList;
	}

	@Override
	public EmpInfoTO getEmpInfo(String companyCode, String empCode) {

		EmpInfoTO TO = new EmpInfoTO();
		
		ArrayList<EmployeeDetailTO> empDetailTOList = empDAO.selectEmployeeDetailList(companyCode, empCode);

		ArrayList<EmployeeSecretTO> empSecretTOList = empDAO.selectEmployeeSecretList(companyCode, empCode);

		TO.setEmpDetailTOList(empDetailTOList);
		TO.setEmpSecretTOList(empSecretTOList);

		EmployeeBasicTO basicTo = empDAO.selectEmployeeBasicTO(companyCode, empCode);

		if (basicTo != null) {

			TO.setCompanyCode(companyCode);
			TO.setEmpCode(empCode);
			TO.setEmpName(basicTo.getEmpName());
			TO.setEmpEngName(basicTo.getEmpEngName());
			TO.setSocialSecurityNumber(basicTo.getSocialSecurityNumber());
			TO.setHireDate(basicTo.getHireDate());
			TO.setRetirementDate(basicTo.getRetirementDate());
			TO.setUserOrNot(basicTo.getUserOrNot());
			TO.setBirthDate(basicTo.getBirthDate());
			TO.setGender(basicTo.getGender());

		}

		return TO;
	}

	@Override
	public String getNewEmpCode(String companyCode) {

		ArrayList<EmployeeBasicTO> empBasicList = null;
		String newEmpCode = null;

		empBasicList = empDAO.selectEmployeeBasicList(companyCode);

		TreeSet<Integer> empCodeNoSet = new TreeSet<>();

		for (EmployeeBasicTO TO : empBasicList) {

			if (TO.getEmpCode().startsWith("EMP-")) {

				try {

					Integer no = Integer.parseInt(TO.getEmpCode().split("EMP-")[1]);
					empCodeNoSet.add(no);

				} catch (NumberFormatException e) {

				}

			}

		}

		if (empCodeNoSet.isEmpty()) {
			newEmpCode = "EMP-" + String.format("%03d", 1);
		} else {
			newEmpCode = "EMP-" + String.format("%03d", empCodeNoSet.pollLast() + 1);
		}

		return newEmpCode;
	}

	@Override
	public ModelMap batchEmpBasicListProcess(ArrayList<EmployeeBasicTO> empBasicList) {

		ModelMap resultMap = new ModelMap();
		
		ArrayList<String> insertList = new ArrayList<>();
		// ArrayList<String> updateList = new ArrayList<>();
		// ArrayList<String> deleteList = new ArrayList<>();

		CodeDetailTO detailCodeTO = new CodeDetailTO();

		for (EmployeeBasicTO TO : empBasicList) {

			String status = TO.getStatus();

			switch (status) {

			case "INSERT":

				empDAO.insertEmployeeBasic(TO);

				insertList.add(TO.getEmpCode());

				detailCodeTO.setDivisionCodeNo("HR-02");
				detailCodeTO.setDetailCode(TO.getEmpCode());
				detailCodeTO.setDetailCodeName(TO.getEmpEngName());

				codeDAO.insertDetailCode(detailCodeTO);

				break;

			}

		}

		resultMap.put("INSERT", insertList);
		// resultMap.put("UPDATE", updateList);
		// resultMap.put("DELETE", deleteList);

		return resultMap;
	}

	@Override
	public ModelMap batchEmpDetailListProcess(ArrayList<EmployeeDetailTO> empDetailList) {

		ModelMap resultMap = new ModelMap();

		ArrayList<String> insertList = new ArrayList<>();
		// ArrayList<String> updateList = new ArrayList<>();
		// ArrayList<String> deleteList = new ArrayList<>();

		for (EmployeeDetailTO bean : empDetailList) {

			String status = bean.getStatus();

			switch (status) {

			case "INSERT":

				empDAO.insertEmployeeDetail(bean);
				insertList.add(bean.getEmpCode());

				if (bean.getUpdateHistory().equals("?????? ??????")) {

					changeEmpAccountUserStatus(bean.getCompanyCode(), bean.getEmpCode(), "N");

					int newSeq = empDAO.selectUserPassWordCount(bean.getCompanyCode(), bean.getEmpCode());

					EmployeeSecretTO newSecretBean = new EmployeeSecretTO();

					newSecretBean.setCompanyCode(bean.getCompanyCode());
					newSecretBean.setEmpCode(bean.getEmpCode());
					newSecretBean.setSeq(newSeq);

					empDAO.insertEmployeeSecret(newSecretBean);

				}

				break;

			}

		}

		resultMap.put("INSERT", insertList);
		// resultMap.put("UPDATE", updateList);
		// resultMap.put("DELETE", deleteList);

		return resultMap;
	}

	@Override
	public ModelMap batchEmpSecretListProcess(ArrayList<EmployeeSecretTO> empSecretList) {

		ModelMap resultMap = new ModelMap();

		ArrayList<String> insertList = new ArrayList<>();
		// ArrayList<String> updateList = new ArrayList<>();
		// ArrayList<String> deleteList = new ArrayList<>();

		for (EmployeeSecretTO TO : empSecretList) {

			String status = TO.getStatus();

			switch (status) {

			case "INSERT":

				empDAO.insertEmployeeSecret(TO);

				insertList.add(TO.getEmpCode());

				break;

			}

		}

		resultMap.put("INSERT", insertList);
		// resultMap.put("UPDATE", updateList);
		// resultMap.put("DELETE", deleteList);

		return resultMap;
	}

	@Override
	public Boolean checkUserIdDuplication(String companyCode, String newUserId) {

		ArrayList<EmployeeDetailTO> empDetailList = null;
		Boolean duplicated = false;

		empDetailList = empDAO.selectUserIdList(companyCode);

		for (EmployeeDetailTO TO : empDetailList) {

			if (TO.getUserId().equals(newUserId)) {

				duplicated = true;

			}

		}

		return duplicated;
	}

	@Override
	public Boolean checkEmpCodeDuplication(String companyCode, String newEmpCode) {

		Boolean duplicated = false;
		ArrayList<EmployeeBasicTO> empBasicList = null;

		empBasicList = empDAO.selectEmployeeBasicList(companyCode);

		for (EmployeeBasicTO TO : empBasicList) {

			if (TO.getEmpCode().equals(newEmpCode)) {

				duplicated = true;

			}

		}

		return duplicated;
	}

	public void changeEmpAccountUserStatus(String companyCode, String empCode, String userStatus) {

			empDAO.changeUserAccountStatus(companyCode, empCode, userStatus);

	}

	@Override
	public EmpInfoTO accessToAuthority(String companyCode, String workplaceCode, String inputId, String inputPassWord)
			throws IdNotFoundException, PwMissMatchException, PwNotFoundException {

		System.out.println("??????1");
		EmpInfoTO TO = null;

		TO = checkEmpInfo(companyCode, workplaceCode, inputId);	// ????????? ????????? ?????? ????????? ????????? ???????????? ????????? ?????? ???????????? ???????????? ????????? ???????????? ???????????? ????????? bean ??? ?????????
		System.out.println(TO);
		System.out.println("??????2");	

		checkPassWord(companyCode, TO.getEmpCode(), inputPassWord); // ??????????????? ?????? ????????? ?????????
		System.out.println("??????3");	
		
		
		
		String[] userAuthorityGroupList = getUserAuthorityGroupList(TO.getEmpCode()); // ???????????? ???????????? ???????????? ???????????? ?????????
		System.out.println("??????4");
		TO.setAuthorityGroupList(userAuthorityGroupList); 
		System.out.println("??????5");
		String[] menuList = getUserAuthorityGroupMenu(TO.getEmpCode()); // ??????????????? ?????? ???????????? ???????????? ????????? 
		System.out.println("??????6");
		TO.setAuthorityGroupMenuList(menuList); 

		System.out.println("??????7");
		
		return TO;
	}

	@Override
	public String[] getAllMenuList() {

		String[] allMenuList = new String[3];
		
		// ????????? nav????????? ?????? ??????
		StringBuffer menuList = new StringBuffer();
		StringBuffer menuList_b = new StringBuffer();
		StringBuffer navMenuList = new StringBuffer();

		// nav?????? ????????? ?????? treemap
		TreeMap<Integer, MenuTO> treeMap = new TreeMap<>();

		
		ArrayList<MenuTO> allMenuTOList = authorityDAO.selectAllMenuList();

		ArrayList<MenuTO> lv0 = new ArrayList<>();
		ArrayList<MenuTO> lv1 = new ArrayList<>();
		ArrayList<MenuTO> lv2 = new ArrayList<>();
			
		for (MenuTO bean : allMenuTOList) {
			if (bean.getMenuURL() != null) {
				String lv = bean.getMenuLevel();
				switch (lv) {
				case "0":
					lv0.add(bean); break;
				case "1":
					lv1.add(bean); break;
				default:
					lv2.add(bean);
				}
			}
		}

		menuList.append("<ul class='list-unstyled components mb-5' id='menuUlTag'>");

		for (MenuTO bean0 : lv0) {

			menuList.append("<li>");
			menuList.append("<a href=" + bean0.getMenuURL()
					+ " data-toggle='collapse' aria-expanded='false' class='dropdown-toggle'>");
			menuList.append(bean0.getMenuName() + "</a>");
			menuList.append("<ul class='collapse list-unstyled' id=" + bean0.getMenuURL().substring(1) + ">");

			// ??????1 ??????
			for (MenuTO bean1 : lv1) {

				menuList.append("<li>");

				// ????????? ?????? ??????1 ??????
				if (bean1.getChildMenu() == null && bean1.getParentMenuCode().equals(bean0.getMenuCode())) {

					menuList.append("<a href=" + bean1.getMenuURL() + " id=" + bean1.getMenuCode() + " class='m'>"
							+ bean1.getMenuName() + "</a>");

					if (bean1.getNavMenu() != null) {
						treeMap.put(Integer.parseInt(bean1.getNavMenu()), bean1);
					}

					// ????????? ?????? ??????1 ??????
				} else if (bean1.getChildMenu() != null && bean1.getParentMenuCode().equals(bean0.getMenuCode())) {

					menuList.append("<a href=" + bean1.getMenuURL()
							+ " data-toggle='collapse' aria-expanded='false' class='dropdown-toggle' ");
					menuList.append("id=" + bean1.getMenuCode() + ">" + bean1.getMenuName() + "</a>");
					menuList.append(
							"<ul class='collapse list-unstyled' id=" + bean1.getMenuURL().substring(1) + ">");

					// ??????2 ??????
					for (MenuTO bean2 : lv2) {

						if (bean2.getParentMenuCode().equals(bean1.getMenuCode())) {
							menuList.append("<li>");
							menuList.append("<a href=" + bean2.getMenuURL() + " id=" + bean2.getMenuCode()
									+ " class='m'>" + bean2.getMenuName() + "</a>");
							menuList.append("</li>");
						}

						if (bean2.getNavMenu() != null) {
							treeMap.put(Integer.parseInt(bean2.getNavMenu()), bean2);
						}
					}
					menuList.append("</ul>");
				}
				menuList.append("</li>");
			}
			menuList.append("</ul>");
			menuList.append("</li>");
		}
		menuList.append("</ul>");

		//******************************************************************************************
			
		int l=0, j=0, k=0;
		menuList_b.append("<nav class='navbar navbar-expand-sm navbar-light bg-light'>");
		menuList_b.append("<button class='navbar-toggler' type='button' "
				+ "data-toggle='collapse' data-target='#navbarSupportedContent' "
				+ "aria-controls='navbarSupportedContent' aria-expanded='false' aria-label='Toggle navigation'>");
		menuList_b.append("<span class='navbar-toggler-icon'></span>");
		menuList_b.append("</button>");
		menuList_b.append("<div class='collapse navbar-collapse' id='navbarSupportedContent'>");
		//lv0
		for (MenuTO bean0 : lv0) {
			menuList_b.append("<ul class='nav-item dropdown'>");
			menuList_b.append("<a href='#' data-toggle='dropdown' id='navbarDropdown' role='button'"
					+ "aria-haspopup='true' aria-expanded='false' class='nav-link dropdown-toggle'>");
			menuList_b.append(bean0.getMenuName());
			menuList_b.append("</a>&nbsp");
			//lv1
			menuList_b.append("<div class='dropdown-menu' aria-labelledby='navbarDropdown'>");
			for (MenuTO bean1 : lv1) {
				if(bean1.getChildMenu() == null && bean1.getParentMenuCode().equals(bean0.getMenuCode())) {
					if(l!=0) menuList_b.append("<div class='dropdown-divider'></div>");
					menuList_b.append("<a href='"+ bean1.getMenuURL() 
					+"'class='dropdown-item'>" + bean1.getMenuName() + "</a>");
					l++;
				} else if (bean1.getChildMenu() != null && bean1.getParentMenuCode().equals(bean0.getMenuCode())) {
					if(j!=0) {menuList_b.append("<div class='dropdown-divider'></div>");}
					j++;l++;
					menuList_b.append("<a href='#' class='dropdown-item'"
							+ "role='button' id='" + bean1.getMenuCode() +"'"
							+ "data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>"
							+ bean1.getMenuName() + "</a>");
					menuList_b.append("<ul id='"+bean1.getMenuURL().substring(1)+"'>");
					//lv2
					for (MenuTO bean2 : lv2) {
						if (bean2.getParentMenuCode().equals(bean1.getMenuCode())) {
							menuList_b.append("<li style='list-style-type:disc;'>");
							k++;
							if(k!=0) {menuList_b.append("<div class='dropdown-divider'></div>");}
							
							menuList_b.append("<a href='"+ bean2.getMenuURL() +"'"
									+ "id='" + bean2.getMenuCode()+ "'"
									+ "'class='dropdown-item'>" + bean2.getMenuName() + "</a>");
							menuList_b.append("</li>");
						}
					}
					k=0;
					menuList_b.append("</ul>");
				}
			}
			l=0; 
			menuList_b.append("</div>");
			menuList_b.append("</ul>");
		}
		menuList_b.append("</div>");
		menuList_b.append("</nav>");
			
		//******************************************************************************************
			
		// nav??????
		navMenuList.append("<ul class='nav navbar-nav ml-auto'>");
		for (Integer i : treeMap.keySet()) {
			MenuTO bean = treeMap.get(i);
			navMenuList.append("<li class='nav-item'>");
			navMenuList
					.append("<a class='nav-link m' href=" + bean.getMenuURL() + " id=" + bean.getMenuCode() + ">");
			navMenuList.append(bean.getNavMenuName() + "</a>");
			navMenuList.append("</li>");
		}
		navMenuList.append("</ul>");
		
		allMenuList[0] = menuList.toString();
		allMenuList[1] = navMenuList.toString();
		allMenuList[2] = menuList_b.toString();

		return allMenuList;
	}
	
	@Override
	public ArrayList<AuthorityInfoGroupTO> getAuthorityGroup() {

		ArrayList<AuthorityInfoGroupTO> authorityGroupTOList = authorityDAO.selectAuthorityGroupList();

		return authorityGroupTOList;
	}

	@Override
	public ArrayList<AuthorityGroupTO> getUserAuthorityGroup(String empCode) {

		ArrayList<AuthorityGroupTO> authorityGroupTOList = authorityDAO.selectUserAuthorityGroupList(empCode);

		return authorityGroupTOList;
		
	}

	@Override
	public void insertEmployeeAuthorityGroup(String empCode, ArrayList<EmployeeAuthorityTO> employeeAuthorityTOList) {

	   	  authorityDAO.deleteEmployeeAuthorityGroup(empCode);
	    	  
	   	  for(EmployeeAuthorityTO bean : employeeAuthorityTOList) {
	    		  
	    	  authorityDAO.insertEmployeeAuthorityGroup(bean);
	    		  
	   	  }

	}

	@Override
	public ArrayList<MenuAuthorityTO> getMenuAuthority(String authorityGroupCode) {

		ArrayList<MenuAuthorityTO> menuAuthorityTOList =  null;
		
		menuAuthorityTOList = authorityDAO.selectMenuAuthorityList(authorityGroupCode);

		return menuAuthorityTOList;
	}

	@Override
	public void insertMenuAuthority(String authorityGroupCode, ArrayList<MenuAuthorityTO> menuAuthorityTOList) {

		authorityDAO.deleteMenuAuthority(authorityGroupCode);

		for (MenuAuthorityTO bean : menuAuthorityTOList) {

			authorityDAO.insertMenuAuthority(bean);
				
		}

	}

	private EmpInfoTO checkEmpInfo(String companyCode, String workplaceCode, String inputId)
			throws IdNotFoundException {
		System.out.println("??????0");

		EmpInfoTO bean = null;
		ArrayList<EmpInfoTO> empInfoTOList = null;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("companyCode", companyCode);
		map.put("workplaceCode", workplaceCode);
		map.put("userId", inputId);
		empInfoTOList = empDAO.getTotalEmpInfo(map);
		System.out.println("??????1");

		if (empInfoTOList.size() == 1) {

			for (EmpInfoTO e : empInfoTOList) {
				bean = e;
			}

		} else if (empInfoTOList.size() == 0) {
			throw new IdNotFoundException("????????? ????????? ???????????? ????????? ????????????.");
		}
		System.out.println("??????2");
		return bean;
	}
	
	private void checkPassWord(String companyCode, String empCode, String inputPassWord)
			throws PwMissMatchException, PwNotFoundException {

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("companyCode", companyCode);
		map.put("empCode", empCode);
		EmployeeSecretTO bean = empDAO.selectUserPassWord(map);

		StringBuffer userPassWord = new StringBuffer();
		if (bean != null) {
			userPassWord.append(bean.getUserPassword());

		} else if (bean == null || bean.getUserPassword().equals("") || bean.getUserPassword() == null) {
			throw new PwNotFoundException("???????????? ????????? ?????? ??? ????????????.");
		}

		if (!inputPassWord.equals(userPassWord.toString())) {
			throw new PwMissMatchException("??????????????? ????????? ????????? ?????? ????????????.");
		}

	}
	
	private String[] getUserAuthorityGroupList(String empCode) {
		
		String[] userAuthorityGroupList = null;

		ArrayList<AuthorityGroupTO> userAuthorityGroupTOList = authorityDAO.selectUserAuthorityGroupList(empCode);

		Iterator<AuthorityGroupTO> iter=userAuthorityGroupTOList.iterator();
		while(iter.hasNext()){	
			if(iter.next().getAuthority().equals("0")) {
				iter.remove();
			}
		}
			
		// ArrayList ????????? ????????? ?????? 
		int size = userAuthorityGroupTOList.size();
		userAuthorityGroupList = new String[size];
		  for(int i=0; i<size; i++ ) { 
			  userAuthorityGroupList[i] = userAuthorityGroupTOList.get(i).getUserAuthorityGroupCode(); 
		  }

		return userAuthorityGroupList;
	}

	private String[] getUserAuthorityGroupMenu(String empCode) {

		String[] authorityGroupMenuList = null;

		ArrayList<AuthorityGroupMenuTO> authorityGroupMenuTOList = authorityDAO.selectUserMenuAuthorityList(empCode);

		// ArrayList ????????? ????????? ?????? 
		int size = authorityGroupMenuTOList.size();
		authorityGroupMenuList = new String[size];
		  for(int i=0; i<size; i++ ) { 
			  authorityGroupMenuList[i] = authorityGroupMenuTOList.get(i).getMenuCode(); 
		  }

		return authorityGroupMenuList;
	}

}
