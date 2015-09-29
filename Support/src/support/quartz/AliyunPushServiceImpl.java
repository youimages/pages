package support.quartz;

public class AliyunPushServiceImpl {
	private String time;

	public String getTime(){
		return "0 0/15 * * * ? ";
		//return time;
	}
	
	public void setTime(String time){
		this.time=time;
	}
}
