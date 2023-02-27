package com.highgo.opendbt.common.exception.enums;

import com.highgo.opendbt.common.exception.assertion.BusinessExceptionAssert;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>业务</p>
 */
@Getter
@AllArgsConstructor
public enum BusinessResponseEnum implements BusinessExceptionAssert {

    UNCLASSIFIED(5001, "根据课程id={0}，班级id={1}未查询到班级信息"),
    UNEXERCISEiNFO(5002, "根据习题id={0}未查询到习题详情"),
    UNEXERCISE(5003, "习题id={0}不存在"),
    DUMPLICATEEXERCISE(5004, "习题ID={0}已在删除状态，不可重复删除"),
    CANNOTDELETEEXERCISE(5005, "习题ID={0}正在使用中，不可删除"),
    NOMIGRATIONEXERCISE(5006, "没有需要迁移的历史数据"),
    NOHOMEWORKMODEL(5007, "根据作业模板id={0}，未查询到模板信息"),
    UNEXERCISEiNFOBYMODEL(5008, "根据习题id={0},模板id={1}未查询到习题详情"),
    UNHOMEWORKMODEL(5009, "根据模板id={0}未查询到模板信息"),
    UNMODELEXERCISES(5010, "习题集合{0}中包含模板中已存在的习题"),
    UNMODELEXERCISE(5011, "根据模板id={0}，习题id={1}未查询到模板习题"),
    UNHOMEWORKMODELPACKAGE(5012, "根据id={0}未查询到文件夹信息"),
    EQUALSHOMEWORKMODELPACKAGE(5013, "相同目录无需移动"),
    HOMEWORKMODELUNCOMMIT(5014, "未查询到作业{0}的发布信息"),
    UNHOMEWORK(5015, "根据作业id:{0}未查询到作业信息"),
    UNHOMEWORKDISTRIBUTION(5016, "根据作业id：{0}未查询到作业发放信息"),
    BINDINGHOMEWORK(5017, "作业模板【{0}】绑定了作业无法删除"),
    UNEXERCISERESULT(5018, "习题答案不能为空"),
    UNSTUDENTHOMEWORK(5019, "根据作业id:{0},学生id:{1}未查询到学生作业信息"),
    UNUSERINFO(5020, "根据学生id:{0}未查询到学生信息"),
    UNCLASS(5021, "根据班级id:{0}未查询到班级信息"),
    NOTSAMERES(5022, "同一个目录下不能存在相同的资源id:{0}"),
    UNFINDCLASS(5023, "根据当前登录人{0}，课程id{1}，未查询到所在班级"),
    DUMPLICATECOURSE(5024, "课程名称{0}重复"),
    UNCLASSBYCOURSE(5025, "根据课程id：{0}查询到班级信息，无法删除"),
    UNCOURSE(5026, "根据课程id：{0}，未查询到课程信息"),
    FAILCOPYCOURSE(5027, "复制课程失败"),
    GETKNOWLEDGETREEEXERCISE(5028, "知识树获取异常"),
    KNOWLEDGETREECOURSE(5029, "知识树所属的课程信息获取失败"),
    GETCOPYSCENEFAIL(5030, "复制的场景信息获取失败"),
    SCENEISUSENOTDELETE(5031, "场景已被习题使用，不可删除"),
    FAILPUBLISH(5032, "发布失败"),
    FAILINIT(5033, "新增学生时初始化发布信息失败"),
    KEEPDIRECTORY(5034, "课程目录下至少保留一个目录"),
    UNALLOWDELETE(5035, "目录有子节点不允许删除"),
    FAILMOVE(5036, "目录移动失败"),
    NOTFOUNDDIRECTORY(5037, "未找到目录id为{0}的目录"),
    UNABLETOMOVEDOWN(5038, "目录在最底部无法移动"),
    UNABLETOMOVEUP(5038, "目录在最顶部无法移动"),
    ONLYTODIRECTORY(5039, "只能移动到文件夹"),
    FAILDELETEEXERCISEINFO(5040, "删除选项失败"),
    DUMPLICATEINFO(5041, "根据id{0}查询到多个选项"),
    FAILAPPROVAL(5042, "批阅提交失败"),
    FAILACALLBACK(5043, "打回失败"),
    NOTFOUNDEXERCISETYPE(5044, "未查询到习题类型{0}"),
    USERALREADYEXIST(5045, "学号(工号)为{0}的用户已经存在"),
    USERNOTEXIST(5046, "账号不存在"),
    ERRORPASSWORD(5047, "原密码输入错误"),
    LOGINACCOUNTNOTEXIST(5048, "{0}账号不存在"),
    LOGINPASSWORDERROR(5049, "{0}账号密码错误"),
    ACCOUNTISSTOP(5050, "{0}账号已停用，可联系管理员恢复使用"),
    FAILUPLOAD(5051, "上传失败"),
    NOTFOUNDRESOURCES(5052, "根据资源id{0}未查询到资源信息"),
    STUDENTALREADYCLASS(5053, "学号为【{0}】的学生已在本班级"),
    FAILBINDINGCLASS(5054, "绑定班级失败"),
    ANSWERISPROBLEM(5055, "参考答案出现问题，请联系老师: {0}"),
    EXECUTANSWERGETRESULTFILE(5056, "执行答案后获取结果集异常，请联系老师: {0}"),
    EXAMINFOGETFILE(5057, "作业信息获取失败"),
    FAILSEARCHSTUDYPROCESS(5058, "未查询到学生学习进度"),


    SAVEFAIL(6001, "保存失败"),
    UPDATEFAIL(6002, "更新失败"),
    DELFAIL(6003, "删除失败"),
    SAVEORUPDATEFAIL(6004, "保存更新失败"),
    ;
    /**
     * 返回码
     */
    private int code;
    /**
     * 返回消息
     */
    private String message;


}
