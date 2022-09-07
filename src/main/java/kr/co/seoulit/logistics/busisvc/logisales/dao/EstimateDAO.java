package kr.co.seoulit.logistics.busisvc.logisales.dao;

import java.util.ArrayList;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.seoulit.logistics.busisvc.logisales.to.EstimateDetailTO;
import kr.co.seoulit.logistics.busisvc.logisales.to.EstimateTO;

@Mapper
public interface EstimateDAO {
	public ArrayList<EstimateTO> selectEstimateList(Map<String,String> map);

	public EstimateTO selectEstimate(String estimateNo);

	public int selectEstimateCount(String estimateDate);

	public void insertEstimate(EstimateTO TO);

	public void updateEstimate(EstimateTO TO);
	
	public void deleteEstimate(Map<String,Object> map2);

	public void changeContractStatusOfEstimate(String estimateNo, String contractStatus);
	
	//EstimateDetail

	public ArrayList<EstimateDetailTO> selectEstimateDetailList(String estimateNo);

	public String selectEstimateDetailCount(String estimateNo);

	public void insertEstimateDetail(EstimateDetailTO TO);

	public void updateEstimateDetail(EstimateDetailTO TO);

	public void deleteEstimateDetail(EstimateDetailTO TO);

	public int selectEstimateDetailSeq(String estimateDate);
	
	public void initDetailSeq(String EST_DETAIL_SEQ);
	
	public void reArrangeEstimateDetail(EstimateDetailTO bean,String newSeq);
	
	public ArrayList<EstimateDetailTO> selectEstimateDetailNo(String estimateNo);

}