package com.highgo.opendbt.system.domain.model;

import com.highgo.opendbt.common.bean.PageTO;

public class ResourceInfoPage extends PageTO {

	private String parentName; //模块父类名
	private String resourceName; //模块名

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	@Override
	public String toString() {
		return "ResourceInfoPage [parentName=" + parentName + ", resourceName=" + resourceName + "]";
	}

}
