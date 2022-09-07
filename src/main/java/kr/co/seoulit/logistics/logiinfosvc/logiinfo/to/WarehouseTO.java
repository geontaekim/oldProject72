package kr.co.seoulit.logistics.logiinfosvc.logiinfo.to;

import kr.co.seoulit.logistics.logiinfosvc.compinfo.to.BaseTO;

public class WarehouseTO extends BaseTO {
	private String warehouseCode;
	private String warehouseName;
	private String warehouseUseOrNot;
	private String description;
	
	
	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public String getWarehouseUseOrNot() {
		return warehouseUseOrNot;
	}

	public void setWarehouseUseOrNot(String warehouseUseOrNot) {
		this.warehouseUseOrNot = warehouseUseOrNot;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}