package io.renren.modules.activiti.test.entity;


import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;

import io.renren.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@TableName("apply_leave")
public class ApplyLeaveEntity extends BaseEntity implements Serializable{
	 private static final long serialVersionUID = 1L;
	 
	 private Long id;
	 
	 /**
	  * 申请人
	  */
	 private String name;
	 
	 /**
	  * 申请状态
	  */
	 private Integer status;
	 
	 /**
	  * 驳回层级
	  */
	 private Integer returnLevel;
	 
	 /**
	  * 办理人
	  */
	 private String assigne;
	 
	 /**
	  * 流程实例ID
	  */
	 private String processInstanceId;
	 
}
