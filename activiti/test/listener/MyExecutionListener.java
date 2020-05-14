package io.renren.modules.activiti.test.listener;

import java.util.Map;
import java.util.Map.Entry;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component("executionListener")
@Scope("prototype")
public class MyExecutionListener implements ExecutionListener,TaskListener{

	private org.activiti.engine.impl.el.FixedValue day;
	private org.activiti.engine.impl.el.FixedValue reason;
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		String eventName = execution.getEventName();
		if("start".equals(eventName)) {
			System.out.println("start********************");
		}else if("end".equals(eventName)) {
			System.out.println("end*************************");
		}
	}

	@Override
	public void notify(DelegateTask delegateTask) {
		String assigne = delegateTask.getAssignee();
		String activityId = delegateTask.getExecution().getCurrentActivityId();
		String eventName = delegateTask.getEventName();
		System.out.println("delegateTask-------------------"+delegateTask);
		System.out.println("assigne-------------------"+assigne);
		System.out.println("activityId-------------------"+activityId);
		System.out.println("eventName-------------------"+eventName);
		
		System.out.println(day.getValue(null));
		System.out.println(reason.getValue(null));
//		Map<String,Object> map =  delegateTask.getVariables();
//		for(Entry<String, Object> obj : map.entrySet()) {
//			System.out.println("key-------------------"+obj.getKey());
//			System.out.println("value-------------------"+obj.getValue());
//		}
	}


}
