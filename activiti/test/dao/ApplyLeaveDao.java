package io.renren.modules.activiti.test.dao;


import org.apache.ibatis.annotations.Mapper;

import io.lettuce.core.dynamic.annotation.Param;
import io.renren.common.dao.BaseDao;
import io.renren.modules.activiti.test.entity.ApplyLeaveEntity;


@Mapper
public interface ApplyLeaveDao  extends BaseDao<ApplyLeaveEntity>{
	
	void updateApplyLeave(ApplyLeaveEntity applyLeaveEntity);
	
	ApplyLeaveEntity selectApplyInfo(@Param("processInstanceId") String processInstanceId );
	
}
