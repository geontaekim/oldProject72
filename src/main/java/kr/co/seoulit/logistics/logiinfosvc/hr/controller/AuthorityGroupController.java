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
import kr.co.seoulit.logistics.logiinfosvc.hr.to.AuthorityGroupTO;
import kr.co.seoulit.logistics.logiinfosvc.hr.to.AuthorityInfoGroupTO;
import kr.co.seoulit.logistics.logiinfosvc.hr.to.EmployeeAuthorityTO;

@RestController
@RequestMapping("/hr/*")
public class AuthorityGroupController{
	
	// gson
	private static Gson gson = new GsonBuilder().serializeNulls().create();
	
	@Autowired
	private HRService hrService;
	
	
	ModelMap map = null;
	
	@RequestMapping(value="/getUserAuthorityGroup.do", method = RequestMethod.POST)
	public ModelMap getUserAuthorityGroup(HttpServletRequest request, HttpServletResponse response) {
		
		String empCode = request.getParameter("empCode");
		map = new ModelMap();
		
		ArrayList<AuthorityGroupTO> authorityGroupTOList = hrService.getUserAuthorityGroup(empCode);
			
		map.put("gridRowJson", authorityGroupTOList);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");

		return map;
	}
	
	@RequestMapping(value="/getAuthorityGroup.do", method = RequestMethod.POST)
	public ModelMap getAuthorityGroup(HttpServletRequest request, HttpServletResponse response) {
		
		map = new ModelMap();
		
		ArrayList<AuthorityInfoGroupTO> authorityGroupTOList = hrService.getAuthorityGroup();
			
		map.put("gridRowJson", authorityGroupTOList);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");
		
		
		return map;
	}
	
	@RequestMapping(value="/insertEmployeeAuthorityGroup.do", method = RequestMethod.POST)
	public ModelMap insertEmployeeAuthorityGroup(HttpServletRequest request, HttpServletResponse response) {
		
		String empCode = request.getParameter("empCode");
		String insertDataList = request.getParameter("insertData");
		ArrayList<EmployeeAuthorityTO> employeeAuthorityTOList = gson.fromJson(insertDataList,
				new TypeToken<ArrayList<EmployeeAuthorityTO>>() {}.getType());
		
		map = new ModelMap();

		hrService.insertEmployeeAuthorityGroup(empCode, employeeAuthorityTOList);

		map.put("errorCode", 1);
		map.put("errorMsg", "성공");

		return map;
	}
	
}
