package com.highgo.opendbt.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.highgo.opendbt.system.domain.entity.UserInfo;

import java.util.Calendar;

/**
 * @Description: jwt 工具类
 * @Title: JwtUtil
 * @Package com.highgo.opendbt.common.utils
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/11/14 13:54
 */
public class JwtUtil {

    // 用于JWT进行签名加密的秘钥
    private static String SECRET = "code-duck-*%#@*!&";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "authorization";
    public static final int TOKEN_TIME = 12;
    //	public static final int TOKEN_TIME_UNIT = Calendar.SECOND;
//	public static final int TOKEN_TIME_UNIT = Calendar.MINUTE;
    public static final int TOKEN_TIME_UNIT = Calendar.HOUR;
    public static final String USER_CODE = "usercode";

    /**
     * @Param: 传入需要设置的payload信息
     * @return: 返回token
     */
    public static String generateToken(UserInfo userInfo) {
        Calendar instance = Calendar.getInstance();
        instance.add(JwtUtil.TOKEN_TIME_UNIT, JwtUtil.TOKEN_TIME);

        return JWT.create().withClaim(JwtUtil.USER_CODE, userInfo.getCode())// 存储内容
                .withExpiresAt(instance.getTime())// 失效时间
                .sign(Algorithm.HMAC256(SECRET));// 加密
    }

    /**
     * @Param: 传入token
     * @return:
     */
    public static void verify(String token) throws Exception {
        JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的实体类
     * @Param: 传入token
     */
    public static UserInfo getUser(String token) {
        DecodedJWT jwt = JWT.decode(token);
        UserInfo user = new UserInfo();
        user.setCode(jwt.getClaim(JwtUtil.USER_CODE).asString());
        return user;
    }

}
