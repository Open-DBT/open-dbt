package com.highgo.opendbt.system.domain.model;

import java.util.ArrayList;
import java.util.List;

public class ResourceMenuTO {

	private int id; //模块id
	private String name; //模块名
	private String path; //模块url
	private String[] authority; //
	private String icon; //图标
	private List<ResourceMenuTO> routes = new ArrayList<ResourceMenuTO>(); //子类

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String[] getAuthority() {
		return authority;
	}

	public void setAuthority(String[] authority) {
		this.authority = authority;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public List<ResourceMenuTO> getRoutes() {
		return routes;
	}

	public void setRoutes(List<ResourceMenuTO> routes) {
		this.routes = routes;
	}

}
