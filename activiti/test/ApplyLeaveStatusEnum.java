package io.renren.modules.activiti.test;

/**
 * 申请状态枚举
 * @author Administrator
 *
 */
public enum ApplyLeaveStatusEnum {
	/**
	 * 拒绝
	 */
	refuse(0),
	/**
	 * 同意
	 */
	agree(1),
	/**
	 * 申请中
	 */
	applying(2); 
	
	private Integer value;
	
	ApplyLeaveStatusEnum(Integer value){
		this.value = value;
	}
	
	public Integer value () {
		return this.value;
	}
}
