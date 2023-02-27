package com.highgo.opendbt.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.highgo.opendbt.sclass.domain.model.SclassUserPage;
import com.highgo.opendbt.system.domain.entity.UserInfo;
import com.highgo.opendbt.system.domain.model.UserInfoPage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
 public interface UserInfoMapper extends BaseMapper<UserInfo> {

	/**
	 * 获取用户
	 *
	 * @param userInfoPage
	 * @return
	 * @
	 */
	 List<UserInfo> getUser(UserInfoPage userInfoPage) ;

	/**
	 * 通过角色id获取用户
	 *
	 * @param userInfoPage
	 * @return
	 * @
	 */
	 List<UserInfo> getUserByRolrId(UserInfoPage userInfoPage) ;

	/**
	 * 通过学号获取用户
	 *
	 * @param code
	 * @return
	 * @
	 */
	 List<UserInfo> getUserByCode(@Param("code") String code);

	/**
	 * 新增用户
	 *
	 * @param userInfo
	 * @return
	 * @
	 */
	 Integer addUser(UserInfo userInfo) ;

	/**
	 * 修改用户
	 *
	 * @param userInfo
	 * @return
	 * @
	 */
	 Integer updateUser(UserInfo userInfo) ;

	/**
	 * 重置和修改密码
	 *
	 * @param userInfo
	 * @return
	 * @
	 */
	 Integer resetUserPassword(UserInfo userInfo) ;

	/**
	 * 修改用户是否停用
	 *
	 * @param userInfo
	 * @return
	 * @
	 */
	 Integer updateUserIsStop(UserInfo userInfo) ;

	/**
	 * 修改用户默认角色
	 *
	 * @param userInfo
	 * @return
	 * @
	 */
	 Integer updateUserDefRole(UserInfo userInfo) ;

	/**
	 * 删除用户和角色的关联关系
	 *
	 * @param userId
	 * @
	 */
	 void deleteUserRoleByUserId(@Param("userId") int userId) ;

	/**
	 * 添加用户和角色的关联关系
	 *
	 * @param userId
	 * @param roleIds
	 * @
	 */
	 void addUserRole(@Param("userId") int userId, @Param("roleIds") int[] roleIds) ;

	/**
	 * 获取班级的学生信息
	 *
	 * @param sclassId 班级id
	 * @param userName 学生姓名
	 * @param code 学号
	 * @return
	 * @
	 */
	 List<UserInfo> getSclassStu(@Param("sclassId") int sclassId, @Param("userName") String userName,@Param("code") String code) ;

	/**
	 * 获取班级的学生信息list
	 *
	 * @param sclassUserPage
	 * @return
	 * @
	 */
	 List<UserInfo> getSclassStudentList(SclassUserPage sclassUserPage);

	/**
	 * 修改用户头像
	 *
	 * @param userId 用户id
	 * @return
	 * @
	 */
	 Integer updateAvatar(@Param("userId") int userId, @Param("avatar") String avatar) ;

	/**
	 * 修改账号信息
	 *
	 * @param userInfo 账号信息
	 * @return
	 * @
	 */
	 Integer updateAccountInfo(UserInfo userInfo) ;

	 List<UserInfo> getTeachers(@Param("userId") int userId) ;
	/**
	 * @description:根据据班级id查询班级内学生信息
	 * @author:
	 * @date: 2022/7/20 10:41
	 * @param: sclassId 班级id
	 * @return:
	 **/
	 List<UserInfo> getStudentListByClassId(@Param("sclassId") int sclassId);
}
