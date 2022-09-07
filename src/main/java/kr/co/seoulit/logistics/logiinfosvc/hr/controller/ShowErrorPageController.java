package kr.co.seoulit.logistics.logiinfosvc.hr.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/hr/*")
public class ShowErrorPageController{
	
	ModelAndView mav = null;
	ModelMap map = null;
	@RequestMapping("/handleRequestInternal.do")
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {

		String viewName = "redirect:" + request.getContextPath() + "/hello.html";

		ModelMap model = new ModelMap();

		if (request.getRequestURI().contains("accessDenied")) {
			model.put("errorCode", -1);
			model.put("errorTitle", "Access Denied");
			model.put("errorMsg", "액세스 거부되었습니다");
			viewName = "errorPage";
		}

		mav = new ModelAndView(viewName, model);

		return mav;
	}

}
