package kr.co.seoulit.logistics.logiinfosvc.hr.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import kr.co.seoulit.logistics.logiinfosvc.hr.service.HRService;
import kr.co.seoulit.logistics.logiinfosvc.hr.to.EmpInfoTO;
import kr.co.seoulit.logistics.logiinfosvc.hr.to.EmployeeBasicTO;
import kr.co.seoulit.logistics.logiinfosvc.hr.to.EmployeeDetailTO;
import kr.co.seoulit.logistics.logiinfosvc.hr.to.EmployeeSecretTO;

@RestController
@RequestMapping("/hr/*")
public class EmpController{

	@Autowired
	private HRService hrService;
	
	
	ModelMap map = null;

	// GSON 라이브러리
	private static Gson gson = new GsonBuilder().serializeNulls().create(); // 속성값이 null 인 속성도 json 변환

	@RequestMapping(value="/searchAllEmpList.do", method=RequestMethod.POST)
	public ModelMap searchAllEmpList(HttpServletRequest request, HttpServletResponse response) {

		String searchCondition = request.getParameter("searchCondition");
		String companyCode = request.getParameter("companyCode");
		String workplaceCode = request.getParameter("workplaceCode");
		String deptCode = request.getParameter("deptCode");

		ArrayList<EmpInfoTO> empList = null;
		String[] paramArray = null;

		map = new ModelMap();

		switch (searchCondition) {

		case "ALL":

			paramArray = new String[] { companyCode };
			break;

		case "WORKPLACE":

			paramArray = new String[] { companyCode, workplaceCode };
			break;

		case "DEPT":

			paramArray = new String[] { companyCode, deptCode };
			break;

		case "RETIREMENT":

			paramArray = new String[] { companyCode };
			break;

		}

		empList = hrService.getAllEmpList(searchCondition, paramArray);

		map.put("gridRowJson", empList);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");

		return map;
	}

//	public ModelAndView searchEmpInfo(HttpServletRequest request, HttpServletResponse response) {
//
//		String companyCode = request.getParameter("companyCode");
//		String empCode = request.getParameter("empCode");
//
//		EmpInfoTO empInfoTO = null;
//
//		map = new ModelMap();
//
//		empInfoTO = hrService.getEmpInfo(companyCode, empCode);
//
//		map.put("empInfo", empInfoTO);
//		map.put("errorCode", 1);
//		map.put("errorMsg", "성공");
//
//		mav = new ModelAndView("jsonView",map);
//		return mav;
//
//	}

	@RequestMapping(value="/checkUserIdDuplication.do", method=RequestMethod.POST)
	public ModelMap checkUserIdDuplication(HttpServletRequest request, HttpServletResponse response) {

		String companyCode = request.getParameter("companyCode");
		String newUserId = request.getParameter("newUseId");
		
		map = new ModelMap();

		Boolean flag = hrService.checkUserIdDuplication(companyCode, newUserId);

		map.put("result", flag);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");

		return map;
	}

	
	@RequestMapping(value="/checkEmpCodeDuplication.do", method=RequestMethod.POST)
	public ModelMap checkEmpCodeDuplication(HttpServletRequest request, HttpServletResponse response) {

		String companyCode = request.getParameter("companyCode");
		String newEmpCode = request.getParameter("newEmpCode");

		map = new ModelMap();

		Boolean flag = hrService.checkEmpCodeDuplication(companyCode, newEmpCode);

		map.put("result", flag);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");

		return map;
		
	}
	
	@RequestMapping(value="/getNewEmpCode.do", method=RequestMethod.POST)
	public ModelMap getNewEmpCode(HttpServletRequest request, HttpServletResponse response) {
		
		String companyCode = request.getParameter("companyCode");
		String newEmpCode = null;
		
		map = new ModelMap();

		newEmpCode = hrService.getNewEmpCode(companyCode);

		map.put("newEmpCode", newEmpCode);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");

		return map;
		
	}
	
	@RequestMapping(value="/batchListProcess.do", method=RequestMethod.POST)
	public ModelMap batchListProcess(HttpServletRequest request, HttpServletResponse response) {

		String batchList = request.getParameter("batchList");
		String tableName = request.getParameter("tableName");

		map = new ModelMap();

		ArrayList<EmployeeBasicTO> empBasicList = null;
		ArrayList<EmployeeDetailTO> empDetailList = null;
		ArrayList<EmployeeSecretTO> empSecretList = null;

		ModelMap resultMap = null;

		if (tableName.equals("BASIC")) {

			empBasicList = gson.fromJson(batchList, new TypeToken<ArrayList<EmployeeBasicTO>>() {
			}.getType());

			resultMap = hrService.batchEmpBasicListProcess(empBasicList);

		} else if (tableName.equals("DETAIL")) {

			empDetailList = gson.fromJson(batchList, new TypeToken<ArrayList<EmployeeDetailTO>>() {
			}.getType());
				
			resultMap = hrService.batchEmpDetailListProcess(empDetailList);

		} else if (tableName.equals("SECRET")) {

			empSecretList = gson.fromJson(batchList, new TypeToken<ArrayList<EmployeeSecretTO>>() {
			}.getType());

			resultMap = hrService.batchEmpSecretListProcess(empSecretList);

		}

		map.put("result", resultMap);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");

		return map;
	}

}
