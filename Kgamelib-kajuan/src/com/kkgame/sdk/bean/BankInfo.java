package com.kkgame.sdk.bean;
/**
 * 快捷支付信息
 * @author wjy
 *
 */
public class BankInfo {

    
    public String id;
    public String bank_id;
    public String lastno;
    public String bindvalid;
    public String bankname;
    public int discount;
    public String reason;
    public int status;
  
	@Override
	public String toString() {
		return "BankInfo [id=" + id + ", bank_id=" + bank_id + ", lastno="
				+ lastno + ", bindvalid=" + bindvalid + ", bankname="
				+ bankname + ", discount=" + discount + ", reason=" + reason
				+ ", status=" + status + "]";
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBank_id() {
		return bank_id;
	}
	public void setBank_id(String bank_id) {
		this.bank_id = bank_id;
	}
	public String getLastno() {
		return lastno;
	}
	public void setLastno(String lastno) {
		this.lastno = lastno;
	}
	public String getBindvalid() {
		return bindvalid;
	}
	public void setBindvalid(String bindvalid) {
		this.bindvalid = bindvalid;
	}
	public String getBankname() {
		return bankname;
	}
	public void setBankname(String bankname) {
		this.bankname = bankname;
	}
	public int getDiscount() {
		return discount;
	}
	public void setDiscount(int discount) {
		this.discount = discount;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
    
    
}
