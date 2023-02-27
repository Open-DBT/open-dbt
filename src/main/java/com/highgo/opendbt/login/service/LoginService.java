package com.highgo.opendbt.login.service;

import com.highgo.opendbt.common.bean.ResultTO;
import com.highgo.opendbt.system.domain.entity.UserInfo;

public interface LoginService {
    //登录
    public ResultTO<Void> login(UserInfo userInfo);

}
