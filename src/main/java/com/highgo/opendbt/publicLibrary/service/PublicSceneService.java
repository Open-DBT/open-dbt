package com.highgo.opendbt.publicLibrary.service;

import com.github.pagehelper.PageInfo;
import com.highgo.opendbt.common.bean.PageTO;
import com.highgo.opendbt.common.bean.ResultTO;
import com.highgo.opendbt.publicLibrary.model.PublicScene;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PublicSceneService {
    //根据课程id分页获取公共场景
    public PageInfo<PublicScene> getScene(PageTO page);

    //获取所有公共场景的名称
    public List<PublicScene> getSceneNameList();

    //根据场景id获取场景信息
    public PublicScene getSceneDetail(int sceneId);

    //添加和修改场景
    public ResultTO<Integer> updateScene(HttpServletRequest request, PublicScene scene);

    // 删除场景
    public ResultTO<Integer> deleteScene(int sceneId);

    public ResultTO<Integer> testSceneSQLShell(HttpServletRequest request, String initShell);

}
