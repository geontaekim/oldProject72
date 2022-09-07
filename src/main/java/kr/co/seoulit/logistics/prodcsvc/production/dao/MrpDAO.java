package kr.co.seoulit.logistics.prodcsvc.production.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.ui.ModelMap;

import kr.co.seoulit.logistics.prodcsvc.production.to.MrpGatheringTO;
import kr.co.seoulit.logistics.prodcsvc.production.to.MrpTO;

@Mapper
public interface MrpDAO {

	//MRP
	public ArrayList<MrpTO> selectMrpList(String mrpGatheringStatusCondition) ;
	
	public ArrayList<MrpTO> selectMrpListAsDate(HashMap<String, String> map); 

	public ArrayList<MrpTO> selectMrpListAsMrpGatheringNo(String mrpGatheringNo);
	
	public void openMrp(Map<String, Object> mpsNoList);
	
	public int selectMrpCount(String mrpRegisterDate);

	public void insertMrp(MrpTO TO);
	
	public void updateMrp(MrpTO TO);
	
	public void  changeMrpGatheringStatus(HashMap<String, String> map);
	
	public void deleteMrp(MrpTO TO);

	public void insertMrpList(HashMap<String, Object> mrpRegisterDate);
	
	//MRPGathering

	public ArrayList<MrpGatheringTO> getMrpGathering(String mrpNoList);
	
	public ArrayList<MrpGatheringTO> selectMrpGatheringList(HashMap<String, String> map);
	
	public void selectMrpGatheringCount(String mrpGatheringRegisterDate);
	
	public void insertMrpGathering(MrpGatheringTO TO);
	
	public void updateMrpGathering(MrpGatheringTO TO);
	
	public void deleteMrpGathering(MrpGatheringTO TO);

	public void updateMrpGatheringContract(HashMap<String, String> parameter);
	
	public int getMGSeqNo();
	
}
