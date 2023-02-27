package com.highgo.opendbt.sclass.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.highgo.opendbt.sclass.domain.entity.Sclass;
import com.highgo.opendbt.sclass.domain.entity.TClassStu;
import com.highgo.opendbt.sclass.mapper.TClassStuMapper;
import com.highgo.opendbt.sclass.service.TClassStuService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class TClassStuServiceImpl extends ServiceImpl<TClassStuMapper, TClassStu>
    implements TClassStuService{
    /**
     * @description: 一个学生同一课程在多个班级的情况下，查询最新加入的班级id
     * @author:
     * @date: 2022/9/29 17:37
     * @param: [userId, sclasss]
     * @return: com.highgo.opendbt.sclass.domain.entity.Sclass
     **/
    @Override
    public Integer getCurrentClass(int userId, List<Sclass> sclasss) {
        //筛选班级id
        List<Integer> collect = sclasss.stream().map(Sclass::getId).collect(Collectors.toList());
        //根据学生id和班级id查询班级学生表
        List<TClassStu> list = this.list(new QueryWrapper<TClassStu>()
                .eq("user_id", userId)
                .in("sclass_id", collect));
        //取班级学生表最大的id值
        TClassStu tClassStu = list.stream().max(Comparator.comparingInt(TClassStu::getId)).get();
        //返回该最大值的班级id
        return tClassStu.getSclassId();
    }
}




