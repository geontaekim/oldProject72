package kr.co.seoulit.logistics.purcstosvc.purchase.to;

public class OutSourcingTO {
	private String outsourcingNo;
	private String materialStatus;
	private String customerCode;
	private String instructDate;
	private String completeDate;
	private String itemCode;
	private String itemName;
	private String instructAmount;
	private String completeStatus;
	private String checkStatus;
	private String unitPrice;
	private String totalPrice;
	private String outsourcingDate;
	private String customerName;
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName=customerName;
	}
	public String getCompleteDate() {
		return completeDate;
	}
	public void setCompleteDate(String completeDate) {
		this.completeDate=completeDate;
	}
	public String getOutsourcingNo() {
		return outsourcingNo;
	}
	public void setOutsourcingNo(String outsourcingNo) {
		this.outsourcingNo = outsourcingNo;
	}
	public String getMaterialStatus() {
		return materialStatus;
	}
	public void setMaterialStatus(String materialStatus) {
		this.materialStatus = materialStatus;
	}
	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	public String getInstructDate() {
		return instructDate;
	}
	public void setInstructDate(String instructDate) {
		this.instructDate = instructDate;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getInstructAmount() {
		return instructAmount;
	}
	public void setInstructAmount(String instructAmount) {
		this.instructAmount = instructAmount;
	}
	public String getCompleteStatus() {
		return completeStatus;
	}
	public void setCompleteStatus(String completeStatus) {
		this.completeStatus = completeStatus;
	}
	public String getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}
	public String getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}
	public String getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getOutsourcingDate() {
		return outsourcingDate;
	}
	public void setOutsourcingDate(String outsourcingDate) {
		this.outsourcingDate = outsourcingDate;
	}
}
