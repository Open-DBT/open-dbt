package com.highgo.opendbt.system.domain.model;

public class ResourceInfo {

	private int resourceId = -1; //模块id
	private int parentId; //父模块id
	private String parentName; //父模块名
	private String resourceName; //模块名
	private String resourceDesc; //模块描述
	private String resourceKey; //模块key
	private int resourceType = 0; //模块类型
	private String resourcePath; //模块url
	private int isDelete = 0; //是否停用
	private String icon; //图标

	public int getResourceId() {
		return resourceId;
	}

	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

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

	public String getResourceDesc() {
		return resourceDesc;
	}

	public void setResourceDesc(String resourceDesc) {
		this.resourceDesc = resourceDesc;
	}

	public String getResourceKey() {
		return resourceKey;
	}

	public void setResourceKey(String resourceKey) {
		this.resourceKey = resourceKey;
	}

	public int getResourceType() {
		return resourceType;
	}

	public void setResourceType(int resourceType) {
		this.resourceType = resourceType;
	}

	public String getResourcePath() {
		return resourcePath;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Override
	public String toString() {
		return "ResourceInfo [resourceId=" + resourceId + ", parentId=" + parentId + ", parentName=" + parentName
				+ ", resourceName=" + resourceName + ", resourceDesc=" + resourceDesc + ", resourceKey=" + resourceKey
				+ ", resourceType=" + resourceType + ", resourcePath=" + resourcePath + ", isDelete=" + isDelete
				+ ", icon=" + icon + "]";
	}

}
