package io.renren.modules.activiti.test.controller;

import java.util.List;
import java.util.Map;
import org.activiti.engine.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.renren.common.annotation.LogOperation;
import io.renren.common.utils.Result;
import io.renren.modules.activiti.dto.TaskDTO;
import io.renren.modules.activiti.test.service.ActivitiDemoService;
import io.renren.modules.activiti.test.service.JumpTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@Api(tags="工作流测试")
@RequestMapping("act/demo")
public class ActivitiDemoController {
	
    @Autowired
    public ActivitiDemoService activitiDemoService;
    
    @Autowired
    public JumpTaskService jumpTaskService;
    
	/**
	 * 部署流程
	 */
    @PostMapping("deploy")
    @ApiOperation("部署流程文件")
    @LogOperation("部署流程文件")
	public Result deploy() {
    	activitiDemoService.deploy();
		return new Result<>().ok("流程部署成功");	 
	}
	
    
	/**
	 * 启动流程
	 */
    @PostMapping("startProcess")
    @ApiOperation("启动流程文件")
    @LogOperation("启动流程文件")
    @ApiImplicitParam(name = "key", value = "流程key", paramType = "query",required = true, dataType="String")
	public Result startProcess(String key) {
    	activitiDemoService.startProcess(key);
		return new Result<>().ok("流程启动成功");	 
	}
	
	/**
	 * 查询个人所有任务
	 */
    @GetMapping("taskList")
    @ApiOperation("个人所有任务")
    @LogOperation("个人所有任务")
    @ApiImplicitParam(name = "assignee", value = "用户", paramType = "query",required = true, dataType="String")
	public Result<List<TaskDTO>> findMyTask(@RequestParam String assignee) {
    	List<TaskDTO> list = activitiDemoService.MyPersonalTask(assignee);
    	return new Result<List<TaskDTO>>().ok(list);
	}
	
    /**
     * 获取任务详情
     */
    @GetMapping("task/{id}")
    @ApiOperation("获取任务详情")
    @LogOperation("获取任务详情")
    public Result getTaskById(@PathVariable("id") String id){
    	TaskDTO task = activitiDemoService.getTaskById(id);
        return new Result().ok(task);
    }
    
    /**
     * 用户提交申请
     * @param taskId
     * @return
     */
    @PostMapping("submit")
    @ApiOperation("提交申请")
    @LogOperation("提交申请")
    @ApiImplicitParam(name = "taskId", value = "任务ID", paramType = "query",required = true, dataType="String")
    public Result submitApplyLeave(@RequestParam String taskId) {
    	activitiDemoService.addApplyLeave(taskId);
		return new Result<>().ok("提交申请成功");
    }
    
    /**
     * 领导审核
     * @param taskId
     * @return
     */
    @PostMapping("check")
    @ApiOperation("领导审核")
    @LogOperation("领导审核")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "taskId", value = "任务ID", paramType = "query", required = true,dataType="String"),
    	@ApiImplicitParam(name = "result", value = "审核结果", paramType = "query",required = true, dataType="Integer"),
    	@ApiImplicitParam(name = "returnLevel", value = "回退层级", paramType = "query", dataType="Integer")
    })
    public Result checkApplyLeave(@ApiIgnore @RequestParam Map<String,Object> map) {
    	activitiDemoService.updateApplyLeave(map);
		return new Result<>().ok("审核成功");
    }
    
    /**
     * 删除流程
     * @param taskId
     * @return
     */
    @DeleteMapping("delete")
    @ApiOperation("删除流程")
    @LogOperation("删除流程")
    @ApiImplicitParam(name = "deploymentId", value = "部署ID", paramType = "query",required = true, dataType="String")
    public Result deleteProcess(@RequestParam String deploymentId) {
    	activitiDemoService.deleteProcess(deploymentId);
		return new Result<>().ok("删除流程成功");
    }
    
    
    @GetMapping("process")
    @ApiOperation("查看流程运行状态")
    @LogOperation("查看流程运行状态")
    @ApiImplicitParam(name = "processInstanceId", value = "流程实例ID", paramType = "query",required = true, dataType="String")
    public Result processStatus(@RequestParam String processInstanceId) {
    	return activitiDemoService.processStatus(processInstanceId);
    }
    
    
    @GetMapping("historytTskList")
    @ApiOperation("查看流程全过程")
    @LogOperation("查看流程全过程")
    @ApiImplicitParam(name = "processInstanceId", value = "流程实例ID", paramType = "query",required = true, dataType="String")
    public List<HistoricTaskInstance> taskListByProcessInstanceId(@RequestParam String processInstanceId) {
    	return activitiDemoService.historyTaskByProcessInstanceId(processInstanceId);
    }
    
    /**
     * 组任务添加成员
     * @param taskId
     * @return
     */
    @PostMapping("addGroupUser")
    @ApiOperation("组任务添加成员")
    @LogOperation("组任务添加成员")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "taskId", value = "任务ID", paramType = "query", required = true,dataType="String"),
    	@ApiImplicitParam(name = "assigne", value = "人员人员", paramType = "query", required = true,dataType="Integer"),
    })
    public Result addGroupUser(String taskId, String assigne) {
    	activitiDemoService.addGroupUser(taskId, assigne);
		return new Result<>().ok("组任务成员添加成功");
    }
    
    /**
     * 组任务删除成员
     * @param taskId
     * @return
     */
    @PostMapping("delGroupUser")
    @ApiOperation("组任务删除成员")
    @LogOperation("组任务删除成员")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "taskId", value = "任务ID", paramType = "query", required = true,dataType="String"),
    	@ApiImplicitParam(name = "assigne", value = "人员人员", paramType = "query", required = true,dataType="Integer"),
    })
    public Result delGroupUser(String taskId, String assigne) {
    	activitiDemoService.delGroupUser(taskId, assigne);
		return new Result<>().ok("组任务删除成员成功");
    }
    
    /**
     * 领取任务
     * @param taskId
     * @return
     */
    @PostMapping("claimTask")
    @ApiOperation("领取任务")
    @LogOperation("领取任务")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "taskId", value = "任务ID", paramType = "query", required = true,dataType="String"),
    	@ApiImplicitParam(name = "assigne", value = "人员人员", paramType = "query", required = true,dataType="Integer"),
    })
    public Result claimTask(String taskId, String assigne) {
    	activitiDemoService.claimTask(taskId, assigne);
		return new Result<>().ok("领取任务成功");
    }
    
    /**
     * 转办任务
     * @param taskId
     * @return
     */
    @PostMapping("releaseTask")
    @ApiOperation("转办任务")
    @LogOperation("转办任务")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "taskId", value = "任务ID", paramType = "query", required = true,dataType="String"),
    	@ApiImplicitParam(name = "assigne", value = "办理人员", paramType = "query",dataType="Integer"),
    })
    public Result releaseTask(String taskId, String assigne) {
    	activitiDemoService.releaseTask(taskId, assigne);
		return new Result<>().ok("转办任务成功");
    }
    
    
    /**
     * 跳转任务
     * @param processId 流程Id
     * @param targetTask 目标节点任务id
     * @return
     */
    @PostMapping("jumpTask")
    @ApiOperation("跳转任务")
    @LogOperation("跳转任务")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "processId", value = "流程Id", paramType = "query", required = true,dataType="String"),
    	@ApiImplicitParam(name = "targetTask", value = "目标节点任务id", paramType = "query",dataType="String"),
    })
    public Result jumpTask(String processId,String targetTask) {
    	jumpTaskService.jump(processId,targetTask);
    	return new Result<>().ok("跳转任务成功");
    }
}
