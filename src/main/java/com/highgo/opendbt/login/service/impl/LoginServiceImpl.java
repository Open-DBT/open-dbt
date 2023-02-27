package com.highgo.opendbt.login.service.impl;

import com.highgo.opendbt.common.bean.ResultTO;
import com.highgo.opendbt.common.exception.enums.BusinessResponseEnum;
import com.highgo.opendbt.common.utils.JwtUtil;
import com.highgo.opendbt.login.service.LoginService;
import com.highgo.opendbt.system.domain.entity.UserInfo;
import com.highgo.opendbt.system.mapper.UserInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final UserInfoMapper userInfoMapper;

    @Override
    public ResultTO<Void> login(UserInfo userInfo) {
        List<UserInfo> savedUserList = userInfoMapper.getUserByCode(userInfo.getCode().trim());
        // 账号不存在
        BusinessResponseEnum.LOGINACCOUNTNOTEXIST.assertIsNotEmpty(savedUserList, userInfo.getCode().trim());
        // 登录密码错误
        BusinessResponseEnum.LOGINPASSWORDERROR.assertIsTrue(userInfo.getPassword().trim().equals(savedUserList.get(0).getPassword()), userInfo.getCode().trim());
        // 用户暂停
        BusinessResponseEnum.ACCOUNTISSTOP.assertIsTrue(savedUserList.get(0).getIsStop() != 1, userInfo.getCode().trim());
        return ResultTO.TOKEN(JwtUtil.generateToken(userInfo));

    }

}
