package kr.co.seoulit.logistics.purcstosvc.stock.to;

import kr.co.seoulit.logistics.logiinfosvc.compinfo.to.BaseTO;

public class StockChartTO extends BaseTO {
	
	private String itemName, stockAmount, saftyAmount, allowanceAmount;

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getStockAmount() {
		return stockAmount;
	}

	public void setStockAmount(String stockAmount) {
		this.stockAmount = stockAmount;
	}

	public String getSaftyAmount() {
		return saftyAmount;
	}

	public void setSaftyAmount(String saftyAmount) {
		this.saftyAmount = saftyAmount;
	}

	public String getAllowanceAmount() {
		return allowanceAmount;
	}

	public void setAllowanceAmount(String allowanceAmount) {
		this.allowanceAmount = allowanceAmount;
	}



	
}
