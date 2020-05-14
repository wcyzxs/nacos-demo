package io.renren.modules.activiti.test.service;

import java.util.Iterator;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.RuntimeServiceImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JumpTaskService{
	 
	 @Autowired
	 private TaskService taskService;
	 
	 @Autowired
	 private RuntimeService runtimeService;
	 
	 @Autowired
	 protected  RepositoryService repositoryService;
    

    /**
     * 跳转至指定活动节点
     * @param processId  流程Id
     * @param targetTaskDefinitionKey  目标节点任务的id
     */
    public void jump(String processId,String targetTaskDefinitionKey){
    	//获取当前节点任务
        TaskEntity currentTask = (TaskEntity) taskService.createTaskQuery()
                .processInstanceId(processId).singleResult();
        jump(processId,currentTask,targetTaskDefinitionKey);
    }
    
    
    /**
     * @param currentTaskEntity 当前任务节点
     * @param targetTaskDefinitionKey  目标任务节点（在模型定义里面的节点任务Id）
     */
    private void jump(String processInstanceId,final TaskEntity currentTaskEntity, String targetTaskDefinitionKey){
        //获取流程实例信息
    	ProcessInstance processInstance=runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        TaskEntity taskEntity=(TaskEntity) taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();  
        //获取流程定义信息
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processInstance.getProcessDefinitionId());  
    	//当前正在执行任务的节点  
  		ActivityImpl currentActivity = (ActivityImpl)processDefinitionEntity.findActivity(processInstance.getActivityId());
  		//跳转的目标节点  
  		ActivityImpl targetActivity = (ActivityImpl)processDefinitionEntity.findActivity(targetTaskDefinitionKey); 
  		((RuntimeServiceImpl)runtimeService).getCommandExecutor()
            .execute(new Command<java.lang.Void>() {
                public Void execute(CommandContext commandContext) {
                	//获取流程执行实例对象
                	ExecutionEntity executionEntity = (ExecutionEntity) runtimeService
                             .createExecutionQuery().executionId(currentTaskEntity.getExecutionId()).singleResult();
            		executionEntity.setActivity(currentActivity);
            		executionEntity.setEventSource(currentActivity);
                	//根据executionId 获取Task
            		Iterator<TaskEntity> localIterator = Context.getCommandContext().getTaskEntityManager().findTasksByExecutionId(taskEntity.getExecutionId()).iterator();
            		while (localIterator.hasNext()) {
            			TaskEntity taskEntity = (TaskEntity) localIterator.next();
            			//删除任务
            			Context.getCommandContext().getTaskEntityManager().deleteTask(taskEntity, "跳转节点", true);
            		}
            		executionEntity.executeActivity(targetActivity);
                    return null;
                }
        });
    }

}