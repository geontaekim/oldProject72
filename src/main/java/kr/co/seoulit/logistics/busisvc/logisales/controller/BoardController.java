package kr.co.seoulit.logistics.busisvc.logisales.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/logisales/*")
public class BoardController {
		
	@RequestMapping(value="registerForm.do",method=RequestMethod.GET)
	public String registerForm() {
		
		return "boardRegister";
	}
	
	
	
	
	
}
