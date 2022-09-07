package kr.co.seoulit.logistics.logiinfosvc.hr.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import kr.co.seoulit.logistics.logiinfosvc.hr.exception.IdNotFoundException;
import kr.co.seoulit.logistics.logiinfosvc.hr.exception.PwMissMatchException;
import kr.co.seoulit.logistics.logiinfosvc.hr.exception.PwNotFoundException;
import kr.co.seoulit.logistics.logiinfosvc.hr.service.HRService;
import kr.co.seoulit.logistics.logiinfosvc.hr.to.EmpInfoTO;

@RestController
@RequestMapping("/hr/*")
public class MemberLogInController{
    
	@Autowired
	private HRService hrService;
	
	
	ModelMap map = null;
	
	@RequestMapping(value="/login.do", method=RequestMethod.GET)
    public ModelMap LogInCheck(HttpServletRequest request, HttpServletResponse response) {

		map = new ModelMap();

		try {
			HttpSession session = request.getSession();
			String companyCode = request.getParameter("companyCode");
			String workplaceCode = request.getParameter("workplaceCode"); 
			String userId = request.getParameter("userId"); 
			String userPassword = request.getParameter("userPassword");  
	
			System.out.println("@@@@@@@@@@동기@@@@@@@@");
			System.out.println(companyCode);
			System.out.println(workplaceCode);
			System.out.println(userId);
			System.out.println(userPassword);
			System.out.println("@@@@@@@@@@동기@@@@@@@@");
			
			EmpInfoTO TO = hrService.accessToAuthority(companyCode, workplaceCode, userId, userPassword);
			if (TO != null) {
				
				session.setAttribute("sessionID", session.getId());
				session.setAttribute("userId", TO.getUserId());
				session.setAttribute("empCode", TO.getEmpCode());
				session.setAttribute("empName", TO.getEmpName());
				session.setAttribute("deptCode", TO.getDeptCode());
				session.setAttribute("deptName", TO.getDeptName());
				session.setAttribute("positionCode", TO.getPositionCode());
				session.setAttribute("positionName", TO.getPositionName());
				session.setAttribute("companyCode", TO.getCompanyCode());
				session.setAttribute("workplaceCode", workplaceCode);
				session.setAttribute("workplaceName", TO.getWorkplaceName());
				session.setAttribute("image", TO.getImage());
				session.setAttribute("authorityGroupCode", TO.getUserAuthorityGroupList());      
				session.setAttribute("authorityGroupMenuList", TO.getUserAuthorityGroupMenuList());
		                
				System.out.println(companyCode);
				
				 // 메뉴 DB에서 가져오기
				String[] allMenuList = hrService.getAllMenuList();
				session.setAttribute("allMenuList", allMenuList[0]);   // 모든 메뉴 리스트
				session.setAttribute("navMenuList", allMenuList[1]);   // nav바 메뉴 리스트
				session.setAttribute("allMenuList_b", allMenuList[2]);
			}

        } catch (IdNotFoundException e1) {   
            e1.printStackTrace();
            map.put("errorCode", -2);
            map.put("errorMsg", e1.getMessage());
        } catch (PwNotFoundException e2) {
            e2.printStackTrace();
            map.put("errorCode", -3);
            map.put("errorMsg", e2.getMessage());
        } catch (PwMissMatchException e3) {
            e3.printStackTrace();
            map.put("errorCode", -4);
            map.put("errorMsg", e3.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            map.put("errorCode", -5);
            map.put("errorMsg", e.getMessage());
        }

		//mav = new ModelAndView("jsonView",map);
		return map;
    }
	
	 @RequestMapping(value="/logout.do", method=RequestMethod.GET) 
	 public ModelMap Logout(HttpServletRequest request, HttpServletResponse response) {
	  
	  map = new ModelMap();
	  
	  HttpSession session = request.getSession(); session.invalidate();
	  
	  
	  String contextPath = request.getContextPath(); try {
	  response.sendRedirect(contextPath+"/logiinfo/loginForm.html"); } 
	  catch (IOException e) { // TODO Auto-generated catch block e.printStackTrace(); }
	  
	  
	  }
	  return null; 
	 }
	 
}