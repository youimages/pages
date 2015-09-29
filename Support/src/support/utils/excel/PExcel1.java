package support.utils.excel;

public class PExcel1 {
	private String name;
	private String cid;
	private String sid;
	private String sex;
	private String birthday;
	private String prvo;
	private String city;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getPrvo() {
		return prvo;
	}
	public void setPrvo(String prvo) {
		this.prvo = prvo;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	@Override
	public String toString() {
		return "PExcel1 [name=" + name + ", cid=" + cid + ", sid=" + sid
				+ ", sex=" + sex + ", birthday=" + birthday + ", prvo=" + prvo
				+ ", city=" + city + "]";
	}
	
	
}
