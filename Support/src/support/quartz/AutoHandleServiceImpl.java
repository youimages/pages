package support.quartz;

import java.text.ParseException;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.scheduling.quartz.CronTriggerBean;

public class AutoHandleServiceImpl {
	//������ܹ����� 
    private Scheduler scheduler;  
    //������ҵ�ʵ�ַ���  
    private AliyunPushServiceImpl autoService;  
      
    public void init(){
    	//��һ����ʱ��ʱ�������
        try {                
            CronTriggerBean trigger = (CronTriggerBean) scheduler.getTrigger("cronTrigger",Scheduler.DEFAULT_GROUP);  
            //������ڶ�ʱ������ʱ�� 
            String originConExpression = trigger.getCronExpression();  
            //����ݿ�������ʱ��  
            String pushTime = autoService.getTime();  
            if(!originConExpression.equals(pushTime)){
	            //���¾������¶�ʱ������ã�����Ϳ��Դﵽ��̬����Ч��  
	            trigger.setCronExpression(pushTime);  
	            scheduler.rescheduleJob("cronTrigger", Scheduler.DEFAULT_GROUP, trigger);
            }
        } catch (SchedulerException e) {   
            e.printStackTrace();  
        } catch (ParseException e) {  
            e.printStackTrace();  
        }
        
        try {                
            CronTriggerBean trigger = (CronTriggerBean) scheduler.getTrigger("cronTrigger2",Scheduler.DEFAULT_GROUP);  
            //������ڶ�ʱ������ʱ�� 
            String originConExpression = trigger.getCronExpression();  
            //����ݿ�������ʱ��  
            String pushTime = autoService.getTime();  
            if(!originConExpression.equals(pushTime)){
	            //���¾������¶�ʱ������ã�����Ϳ��Դﵽ��̬����Ч��  
	            trigger.setCronExpression(pushTime);  
	            scheduler.rescheduleJob("cronTrigger2", Scheduler.DEFAULT_GROUP, trigger);
            }
        } catch (SchedulerException e) {   
            e.printStackTrace();  
        } catch (ParseException e) {  
            e.printStackTrace(); 
        }
    } 
    
    /** 
     * ֹͣ���ȴ�������ص�job 
     * @param list 
     * @return 
     * @throws SchedulerException 
     */  
    public void unscheduleJob(String triggerName) {
    	try {
    		CronTriggerBean trigger = (CronTriggerBean) scheduler.getTrigger(triggerName,Scheduler.DEFAULT_GROUP);
			scheduler.pauseJob(trigger.getJobName(), Scheduler.DEFAULT_GROUP);
		} catch (SchedulerException e) {
			e.printStackTrace();
		} 
    }
    
    public void restartJob(String triggerName) {
    	CronTriggerBean trigger;
		try {
			trigger = (CronTriggerBean) scheduler.getTrigger(triggerName,Scheduler.DEFAULT_GROUP);
			scheduler.rescheduleJob(triggerName, Scheduler.DEFAULT_GROUP, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
    }
    
  
    public Scheduler getScheduler() {  
        return scheduler;  
    }  
  
    public void setScheduler(Scheduler scheduler) {  
        this.scheduler = scheduler;  
    }

	public AliyunPushServiceImpl getAutoService() {
		return autoService;
	}

	public void setAutoService(AliyunPushServiceImpl autoService) {
		this.autoService = autoService;
	}  
  
}
