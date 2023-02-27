package com.highgo.opendbt.system.service;

import com.github.pagehelper.PageInfo;
import com.highgo.opendbt.common.bean.ResourceTreeTO;
import com.highgo.opendbt.common.bean.ResultTO;
import com.highgo.opendbt.system.domain.model.ResourceInfo;
import com.highgo.opendbt.system.domain.model.ResourceInfoPage;

import java.util.List;

public interface ResourceInfoService {

	public ResultTO<PageInfo<ResourceInfo>> getResource(ResourceInfoPage resourceInfoPage);

	public ResultTO<List<ResourceTreeTO>> getResourceTree();

}
