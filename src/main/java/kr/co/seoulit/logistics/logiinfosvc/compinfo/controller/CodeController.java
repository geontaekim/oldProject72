package kr.co.seoulit.logistics.logiinfosvc.compinfo.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import kr.co.seoulit.logistics.logiinfosvc.compinfo.service.CompInfoService;
import kr.co.seoulit.logistics.logiinfosvc.compinfo.to.CodeDetailTO;
import kr.co.seoulit.logistics.logiinfosvc.compinfo.to.CodeTO;

@RestController
@RequestMapping("/compinfo/*")
public class CodeController{
	
	@Autowired
	private CompInfoService compInfoService;
	
	ModelMap map = null;

	private static Gson gson = new GsonBuilder().serializeNulls().create();

	@RequestMapping(value = "/searchCodeList2.do", method = RequestMethod.POST)	//코드상세조회
	public ModelMap findCodeDetailList(HttpServletRequest request, HttpServletResponse response) {

		map = new ModelMap();

		String CodeDetail  = request.getParameter("divisionCodeNo");

		ArrayList<CodeDetailTO> codeLists = compInfoService.getCodeDetailList(CodeDetail);

		map.put("codeLists", codeLists);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공");

		return map;
	}

	@RequestMapping(value = "/codechange.do", method = RequestMethod.POST)
	public ModelMap addCodeInFormation(HttpServletRequest request, HttpServletResponse response) {

		map = new ModelMap();

		String newcodeInfo = request.getParameter("newCodeInfo");

		ArrayList<CodeTO> CodeTOList = gson.fromJson(newcodeInfo,new TypeToken<ArrayList<CodeTO>>() {
		}.getType());

		compInfoService.addCodeInFormation(CodeTOList);

			map.put("errorCode", 1);
			map.put("errorMsg", "성공");

			return map;
	}

	@RequestMapping(value = "/searchCodeList.do", method = RequestMethod.POST)   //코드조회
	public ModelMap findCodeList(HttpServletRequest request, HttpServletResponse response) {

		map = new ModelMap();
		ArrayList<CodeTO> codeList = compInfoService.getCodeList();

		map.put("codeList", codeList);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공!");

		return map;
	}

	@RequestMapping(value = "/codeList.do", method = RequestMethod.POST)
	public ModelMap findDetailCodeList(HttpServletRequest request, HttpServletResponse response) {

		String divisionCode = request.getParameter("divisionCodeNo");

		map = new ModelMap();

			ArrayList<CodeDetailTO> detailCodeList = compInfoService.getDetailCodeList(divisionCode);

			map.put("detailCodeList", detailCodeList);
			map.put("errorCode", 1);
			map.put("errorMsg", "성공!");

			return map;
	}

	//요청하는 jsp 페이지가 없다.
	@RequestMapping(value = "/checkCodeDuplication.do", method = RequestMethod.POST)
	public ModelMap checkCodeDuplication(HttpServletRequest request, HttpServletResponse response) {

		String divisionCode = request.getParameter("divisionCode");
		String newDetailCode = request.getParameter("newCode");

		map = new ModelMap();

			Boolean flag = compInfoService.checkCodeDuplication(divisionCode, newDetailCode);

			map.put("result", flag);
			map.put("errorCode", 1);
			map.put("errorMsg", "성공!");
			
		return map;
	}
	
	@RequestMapping(value = "/batchListProcess.do", method = RequestMethod.POST)
	public ModelMap batchListProcess(HttpServletRequest request, HttpServletResponse response) {

		String batchList = request.getParameter("batchList");
		String tableName = request.getParameter("tableName");

		map = new ModelMap();

		ArrayList<CodeTO> codeList = null;
		ArrayList<CodeDetailTO> detailCodeList = null;
		HashMap<String, Object> resultMap = null;

		if (tableName.equals("CODE")) {

			codeList = gson.fromJson(batchList, new TypeToken<ArrayList<CodeTO>>() {
			}.getType());
			resultMap = compInfoService.batchCodeListProcess(codeList);

		} else if (tableName.equals("CODE_DETAIL")) {
			detailCodeList = gson.fromJson(batchList, new TypeToken<ArrayList<CodeDetailTO>>() {
			}.getType());
			resultMap = compInfoService.batchDetailCodeListProcess(detailCodeList);

		}

		map.put("result", resultMap);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공!");

		return map;
	}
	
	//요청하는 jsp 페이지가 없다.
	@RequestMapping(value = "/changeCodeUseCheckProcess.do", method = RequestMethod.POST)
	public ModelMap changeCodeUseCheckProcess(HttpServletRequest request, HttpServletResponse response) {

		String batchList = request.getParameter("batchList");

		map = new ModelMap();

		ArrayList<CodeDetailTO> detailCodeList = null;
		HashMap<String, Object> resultMap = null;

		detailCodeList = gson.fromJson(batchList, new TypeToken<ArrayList<CodeDetailTO>>() {
		}.getType());
		resultMap = compInfoService.changeCodeUseCheckProcess(detailCodeList);

		map.put("result", resultMap);
		map.put("errorCode", 1);
		map.put("errorMsg", "성공!");

		return map;
	}	   

}