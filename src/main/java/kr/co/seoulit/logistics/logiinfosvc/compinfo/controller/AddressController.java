package kr.co.seoulit.logistics.logiinfosvc.compinfo.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import kr.co.seoulit.logistics.logiinfosvc.compinfo.service.CompInfoService;
import kr.co.seoulit.logistics.logiinfosvc.compinfo.to.AddressTO;

@RestController
@RequestMapping("/compinfo/*")
public class AddressController{

	@Autowired
	private CompInfoService compInfoService;
	

	ModelMap map = null;
	
	@RequestMapping("/searchAddressList.do")
	public ModelMap searchAddressList(HttpServletRequest request, HttpServletResponse response) {

		String sidoName = request.getParameter("sidoName");
		String searchAddressType = request.getParameter("searchAddressType");
		String searchValue = request.getParameter("searchValue");
		String mainNumber = request.getParameter("mainNumber");
		map = new ModelMap();

		ArrayList<AddressTO> addressList = compInfoService.getAddressList(sidoName, searchAddressType, searchValue,
				mainNumber);

		map.put("addressList", addressList);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공!");

		return map;

	}

}
