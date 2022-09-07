package kr.co.seoulit.logistics.purcstosvc.purchase.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import kr.co.seoulit.logistics.purcstosvc.purchase.to.OutSourcingTO;

@Mapper
public interface OutSourcingDAO {

	ArrayList<OutSourcingTO> selectOutSourcingList(String fromDate, String toDate, String customerCode,String itemCode,String materialStatus);

}
