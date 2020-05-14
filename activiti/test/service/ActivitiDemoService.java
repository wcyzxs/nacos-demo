package io.renren.modules.activiti.test.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import io.renren.common.utils.Result;
import io.renren.modules.activiti.dto.TaskDTO;
import io.renren.modules.activiti.test.ApplyLeaveStatusEnum;
import io.renren.modules.activiti.test.dao.ApplyLeaveDao;
import io.renren.modules.activiti.test.entity.ApplyLeaveEntity;

@Service
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiDemoService {
	private Logger log = LoggerFactory.getLogger(ActivitiDemoService.class);
	 @Autowired
	 protected  RepositoryService repositoryService;
	 
	 @Autowired
	 private RuntimeService runtimeService;
	 
	 @Autowired
	 private TaskService taskService;
	 
	 @Autowired
	 protected  HistoryService historyService;
	 
	 @Autowired
	 private ApplyLeaveDao applyLeaveDao;
	// getManagementService
	 @Test
	 public void te() {
		String name =  applyLeaveDao.selectById(1).getName();
		System.out.println(name);
	 }
	 
	/**
	 * 部署流程
	 */
	public void deploy() {
		Deployment deployment = repositoryService
					 .createDeployment()
					 .name("调休申请")
					 .addClasspathResource("diagrams/groupAssigne.bpmn")
					 .addClasspathResource("diagrams/groupAssigne.png")
//					 .name("监听测试")
//					 .addClasspathResource("diagrams/group.bpmn")
//					 .addClasspathResource("diagrams/group.png")
					 .deploy();
		log.info("部署ID："+deployment.getId());  
		log.info("部署名称："+deployment.getName());
	}
	
	/**
	 * 启动流程
	 * @param key
	 */
	public void startProcess(String key ) {
		Map<String,Object> variables = new HashMap<String, Object>(4);
		variables.put("userIds", "a,b");
		//启动流程
		ProcessInstance pi = runtimeService.startProcessInstanceByKey(key,variables);
		log.info("pid-->"+pi.getId());
	}
	 
	/**
	 * 查询个人所有任务
	 */
	public List<TaskDTO> MyPersonalTask(String assignee){
		
		List<Task> list = taskService.createTaskQuery()//创建任务查询对象
						.taskAssignee(assignee)
						.list();//返回列表
		List<TaskDTO> listDto = new ArrayList<>();
		if(list!=null && list.size()>0){
			for(Task task:list){
				TaskDTO dto = new TaskDTO();
	            this.convertTaskInfo(task, dto);
	            listDto.add(dto);
				log.info("任务ID:"+task.getId());
				log.info("任务名称:"+task.getName());
				log.info("任务的办理人:"+task.getAssignee());
				log.info("流程实例ID："+task.getProcessInstanceId());
				log.info("执行对象ID:"+task.getExecutionId());
				log.info("流程定义ID:"+task.getProcessDefinitionId());
				log.info("*******************");
			}
		}
		 return listDto;
	}
	
	/**
	 * 查询某个任务信息
	 * @param taskId
	 * @return
	 */
	public TaskDTO getTaskById(String taskId) {
		Task task = taskService.createTaskQuery()
				.taskId(taskId)
				.singleResult();
		log.info("任务ID:"+task.getId());
		log.info("任务名称:"+task.getName());
		log.info("任务的办理人:"+task.getAssignee());
		log.info("流程实例ID："+task.getProcessInstanceId());
		log.info("执行对象ID:"+task.getExecutionId());
		log.info("流程定义ID:"+task.getProcessDefinitionId());
		log.info("*******************");
		TaskDTO dto = new TaskDTO();
        this.convertTaskInfo(task, dto);
        return dto;
	}

	/**
	 * 完成任务
	 */
	public void completeeTask(String taskId,Map<String,Object> map) {
		
		//完成任务
		taskService.complete(taskId, map);
		
		log.info("任务完成");
	}
	
	
	/**
	 * 领导审批
	 * @param map：流程变量k-v、taskId
	 * @param taskId
	 */
	@Transactional(readOnly = false)
	public void updateApplyLeave(Map<String,Object> map) {
		String taskId = map.get("taskId").toString();
		TaskDTO task = getTaskById(taskId);
		//因为流程无法回滚，先执行对数据库的更新操作，防止事务回滚
		ApplyLeaveEntity applyLeaveEntity = new ApplyLeaveEntity();
		
		applyLeaveEntity.setStatus(Integer.parseInt(map.get("result").toString()));
		//判断是否存在驳回操作
		if(map.get("returnLevel") != null) {
			applyLeaveEntity.setReturnLevel(Integer.parseInt(map.get("returnLevel").toString()));
		}
		//若是审核同意，则returnLevel赋值为null
		Integer result = 1;
		if(result == Integer.parseInt(map.get("result").toString()) ) {
			applyLeaveEntity.setReturnLevel(null);
		}
		//设置办理人
		applyLeaveEntity.setAssigne(task.getAssignee());
		applyLeaveEntity.setProcessInstanceId(task.getProcessInstanceId());
		//更新表中数据
		Long id = applyLeaveDao.selectApplyInfo(task.getProcessInstanceId()).getId();
		applyLeaveEntity.setId(id);
		applyLeaveDao.updateApplyLeave(applyLeaveEntity);
		
        
        
		//完成任务
		taskService.complete(taskId, map);
	}
	
	/**
	 * 用户提交申请
	 * @param map:流程变量k-v
	 * @param taskId
	 */
	@Transactional(readOnly = false)
	public void addApplyLeave(String taskId) {
		TaskDTO task = getTaskById(taskId);
		ApplyLeaveEntity applyLeaveEntity = new ApplyLeaveEntity();
		applyLeaveEntity.setStatus(ApplyLeaveStatusEnum.applying.value());
		applyLeaveEntity.setName(task.getAssignee());
		applyLeaveEntity.setAssigne(task.getAssignee());
		applyLeaveEntity.setProcessInstanceId(task.getProcessInstanceId());
		//判断表中是否已存在该流程实例对应的数据，存在则更新，不存在则插入
		ApplyLeaveEntity  LeaveEntity  =applyLeaveDao.selectApplyInfo(task.getProcessInstanceId());
		if(LeaveEntity == null) {
			applyLeaveDao.insert(applyLeaveEntity);
		}else {
			applyLeaveEntity.setId(LeaveEntity.getId());
			applyLeaveDao.updateApplyLeave(applyLeaveEntity);
		}
		
		
		//完成任务
		taskService.complete(taskId);
	}
	
	
	/**
	 * 删除流程(不论流程是否启动)
	 * @param deploymentId
	 */
	public void deleteProcess(String deploymentId) {
		repositoryService.deleteDeployment(deploymentId, true);
	}
	
	
	/**
	 * 查看流程实例状态
	 * @param processInstanceId  流程实例ID
	 */
	public Result processStatus(String processInstanceId) {
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
    				.processInstanceId(processInstanceId)
    				.singleResult();
		if(processInstance != null) {
			return new Result<>().ok("流程正在执行");
		}else {
			return new Result<>().ok("流程已经结束");
		}
    }

	
	
	/**
	 * 查询某个流程的全过程
	 * @param processInstanceId 流程实例Id
	 */
	public List<HistoricTaskInstance> historyTaskByProcessInstanceId(String processInstanceId) {
		List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
					.processInstanceId(processInstanceId)
					.list();
		return list;
	}
	
	/**
	 * 组任务添加成员
	 * @param taskId
	 * @param assigne
	 */
	public void addGroupUser(String taskId ,String assigne) {
		taskService.addCandidateUser(taskId, assigne);
		log.info("组成员添加成功***********");
	}
	
	/**
	 * 组任务删除成员
	 * @param taskId 
	 * @param assigne 
	 */
	public void delGroupUser(String taskId ,String assigne) {
		taskService.deleteCandidateUser(taskId, assigne);
		log.info("组成员删除成功***********");
	}
	
	/**
	 * 认领任务
	 * @param taskId
	 * @param assigne
	 */
	public void claimTask(String taskId,String assigne) {
		taskService.claim(taskId, assigne);
		log.info("认领任务成功***********");
	}
	
	/**
	 * 转办任务(若assigne，则可回退到组任务，前提是之前是组任务)
	 * @param taskId
	 * @param assigne : 
	 */
	public void releaseTask(String taskId,String assigne) {
		taskService.claim(taskId, assigne);
		log.info("转办任务成功***********");
	}
	
	
	/**
	 * 转Task对象
	 * @param task
	 * @param dto
	 */
	 private void convertTaskInfo(Task task, TaskDTO dto) {
	        dto.setTaskId(task.getId());
	        dto.setTaskName(task.getName());
	        dto.setActivityId(task.getExecutionId());
	        dto.setAssignee(task.getAssignee());
	        dto.setProcessDefinitionId(task.getProcessDefinitionId());
	        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
	        dto.setProcessDefinitionName(processDefinition.getName());
	        dto.setProcessDefinitionKey(processDefinition.getKey());
	        HistoricProcessInstance processInstance =  historyService.createHistoricProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
	        dto.setStartTime(processInstance.getStartTime());
	        dto.setBusinessKey(processInstance.getBusinessKey());
	        dto.setProcessInstanceId(task.getProcessInstanceId());
	        dto.setOwner(task.getOwner());
	        dto.setCreateTime(task.getCreateTime());
	        dto.setDueDate(task.getDueDate());
	 }
}
