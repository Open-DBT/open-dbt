package com.highgo.opendbt.common.bean;

import com.baomidou.mybatisplus.annotation.TableField;

public class PageTO {
	@TableField(exist = false)
	private int current = 1; // 当前页
	@TableField(exist = false)
	private int pageSize = 10; // 每页条数

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public String toString() {
		return "PageTO [current=" + current + ", pageSize=" + pageSize + "]";
	}

}
