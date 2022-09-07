
package kr.co.seoulit.logistics.busisvc.logisales.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import kr.co.seoulit.logistics.busisvc.logisales.service.LogisalesService;
import kr.co.seoulit.logistics.busisvc.logisales.to.ContractDetailTO;
import kr.co.seoulit.logistics.busisvc.logisales.to.ContractInfoTO;
import kr.co.seoulit.logistics.busisvc.logisales.to.EstimateTO;

@RestController
@RequestMapping("/busicon/*")
public class ContractController{

	@Autowired
	private LogisalesService logisalesService;
	
	
	ModelMap map=null;
		
	private static Gson gson = new GsonBuilder().serializeNulls().create(); 

	@PostMapping(value="/searchContractList.do")
	public ModelMap searchContract(HttpServletRequest request, HttpServletResponse response) {
		
		String searchCondition = request.getParameter("searchCondition");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String customerCode = request.getParameter("customerCode");
		System.out.println("searchCondition?"+searchCondition);
		System.out.println("startDate?"+startDate);
		System.out.println("endDate?"+endDate);
		System.out.println("customerCode?"+customerCode);
		map = new ModelMap();

		ArrayList<ContractInfoTO> contractInfoTOList = null;

		
		if (searchCondition.equals("searchByDate")) {

			String[] paramArray = { startDate, endDate };
			contractInfoTOList = logisalesService.getContractList("searchByDate", paramArray);

		} else {

			String[] paramArray = { customerCode };
			 contractInfoTOList = logisalesService.getContractList("searchByCustomer",paramArray);
		}

		map.put("gridRowJson", contractInfoTOList);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");
		
		
		return map;
	}

	//지금 이 메소드롤 호출하는 부분이 contractInfo.jsp에 없다.
	//그러므로 @RequestMapping을 걸어줄 수 없다.
	/*
	public ModelAndView searchContractNO(HttpServletRequest request, HttpServletResponse response) {

		String searchCondition = request.getParameter("searchCondition");

		map = new ModelMap();

		ArrayList<ContractInfoTO> contractInfoTOList = null;
		if (searchCondition.equals("searchByDate")) {
			String customerCode = "";
			String[] paramArray = { customerCode };
			contractInfoTOList = logisalesService.getContractList("searchByCustomer", paramArray);

		}

		map.put("gridRowJson", contractInfoTOList);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공"); 

		mav = new ModelAndView("jsonView", map);
		return mav;
	}
	*/
	
	@RequestMapping(value="/searchContractDetail.do" , method=RequestMethod.POST)
	public ModelMap searchContractDetail(HttpServletRequest request, HttpServletResponse response) {

		map = new ModelMap();

		String contractNo = request.getParameter("contractNo");

		ArrayList<ContractDetailTO> contractDetailTOList = logisalesService.getContractDetailList(contractNo);

		map.put("gridRowJson", contractDetailTOList);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");

		
		return map;
	}

	@RequestMapping(value= "/searchEstimateInContractAvailable.do", method=RequestMethod.POST)
	public ModelMap searchEstimateInContractAvailable(HttpServletRequest request, HttpServletResponse response) {

		map = new ModelMap();

		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");

		ArrayList<EstimateTO> estimateListInContractAvailable = logisalesService
				.getEstimateListInContractAvailable(startDate, endDate);

		map.put("gridRowJson", estimateListInContractAvailable);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");
			
		
		return map;
	}

	@RequestMapping(value="/addNewContract.do" , method=RequestMethod.POST)
	public ModelMap addNewContract(HttpServletRequest request, HttpServletResponse response) {

		map = new ModelMap();
		
		String batchList = request.getParameter("batchList"); 
		
		HashMap<String,String[]>workingContractList = gson.fromJson(batchList,new TypeToken<HashMap<String,String[]>>() {}.getType()) ;
		map = logisalesService.addNewContract(workingContractList);
		
		
		return map;
		
	}

	//이거 호출하는 xxx.jsp페이지가 없다.
	@RequestMapping(value="/cancleEstimate.do" , method=RequestMethod.POST)
	public ModelMap cancleEstimate(HttpServletRequest request, HttpServletResponse response) {

		map = new ModelMap();
		String estimateNo = request.getParameter("estimateNo");

		logisalesService.changeContractStatusInEstimate(estimateNo, "N");

		map.put("errorCode", 1);
		map.put("errorMsg", "성공");
		map.put("cancledEstimateNo", estimateNo);

		
		return map;
	}
}