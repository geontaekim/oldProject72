package kr.co.seoulit.logistics.logiinfosvc.logiinfo.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import kr.co.seoulit.logistics.logiinfosvc.logiinfo.to.WarehouseTO;

@Mapper
public interface WarehouseDAO {
	public ArrayList<WarehouseTO> selectWarehouseList();
	
	public void insertWarehouse(WarehouseTO bean);
	
	public void deleteWarehouse(WarehouseTO bean);
	
	public void updateWarehouse(WarehouseTO bean);
}
