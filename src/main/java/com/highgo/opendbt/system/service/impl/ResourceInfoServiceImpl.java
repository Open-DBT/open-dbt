package com.highgo.opendbt.system.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.highgo.opendbt.common.bean.ResourceTreeTO;
import com.highgo.opendbt.common.bean.ResultTO;
import com.highgo.opendbt.system.mapper.ResourceInfoMapper;
import com.highgo.opendbt.system.domain.model.ResourceInfo;
import com.highgo.opendbt.system.domain.model.ResourceInfoPage;
import com.highgo.opendbt.system.service.ResourceInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResourceInfoServiceImpl implements ResourceInfoService {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ResourceInfoMapper resourceInfoMapper;

	@Override
	public ResultTO<PageInfo<ResourceInfo>> getResource(ResourceInfoPage resourceInfoPage) {
		try {
			// 分页查询配置
			PageHelper.startPage(resourceInfoPage.getCurrent(), resourceInfoPage.getPageSize());

			List<ResourceInfo> resourceInfoList = new ArrayList<ResourceInfo>();
			resourceInfoList = resourceInfoMapper.getResource(resourceInfoPage);

			return ResultTO.OK(new PageInfo<ResourceInfo>(resourceInfoList));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultTO.FAILURE(e.getMessage());
		}
	}

	@Override
	public ResultTO<List<ResourceTreeTO>> getResourceTree() {
		try {
			List<ResourceInfo> resourceInfoList = resourceInfoMapper.getResource(new ResourceInfoPage());

			List<ResourceTreeTO> treeDataList = new ArrayList<ResourceTreeTO>();
			for (int i = 0; i < resourceInfoList.size(); i++) {
				ResourceInfo resourceInfo = resourceInfoList.get(i);

				// 父模块id为0，说明是没有父模块，是根模块
				if (resourceInfo.getParentId() == 0) {

					ResourceTreeTO treeData = new ResourceTreeTO();
					treeData.setTitle(resourceInfo.getResourceName());
					treeData.setKey(resourceInfo.getResourceId());

					treeDataList.add(treeData);

					// 已经记录的数据移除，防止重复遍历
					resourceInfoList.remove(i);
					i--;
				}
			}

			// 用递归逐级寻找子模块
			convertToTreeData(resourceInfoList, treeDataList);

			return ResultTO.OK(treeDataList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultTO.FAILURE(e.getMessage());
		}
	}

	private void convertToTreeData(List<ResourceInfo> resourceInfoList, List<ResourceTreeTO> treeDataList) throws Exception {
		for (int i = 0; i < treeDataList.size(); i++) {
			ResourceTreeTO treeData = treeDataList.get(i);
			for (int j = 0; j < resourceInfoList.size(); j++) {
				ResourceInfo resourceInfoChildren = resourceInfoList.get(j);

				// 树对象数据的key是模块的id，如果有模块的父id等于key，说明它是该树对象的子模块
				if (treeData.getKey() == resourceInfoChildren.getParentId()) {
					ResourceTreeTO treeDataChildren = new ResourceTreeTO();
					treeDataChildren.setTitle(resourceInfoChildren.getResourceName());
					treeDataChildren.setKey(resourceInfoChildren.getResourceId());

					treeData.getChildren().add(treeDataChildren);

					// 已经记录的数据移除，防止重复遍历
					resourceInfoList.remove(j);
					j--;
				}
			}

			// 树对象的子模块集合不为空就继续寻找子模块的子模块
			if (treeData.getChildren()!=null&&!treeData.getChildren().isEmpty()) {
				convertToTreeData(resourceInfoList, treeData.getChildren());
			}
		}
	}

}
