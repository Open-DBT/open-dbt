package com.highgo.opendbt.course.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.highgo.opendbt.common.bean.CourseKnowledgeTO;
import com.highgo.opendbt.common.bean.KnowledgeTreeTO;
import com.highgo.opendbt.common.exception.enums.BusinessResponseEnum;
import com.highgo.opendbt.common.utils.Message;
import com.highgo.opendbt.course.domain.entity.Course;
import com.highgo.opendbt.course.domain.entity.Knowledge;
import com.highgo.opendbt.course.mapper.CourseMapper;
import com.highgo.opendbt.course.mapper.ExerciseKnowledgeMapper;
import com.highgo.opendbt.course.mapper.KnowledgeMapper;
import com.highgo.opendbt.course.service.KnowledgeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class KnowledgeServiceImpl extends ServiceImpl<KnowledgeMapper, Knowledge> implements KnowledgeService {

    Logger logger = LoggerFactory.getLogger(getClass());

    private final KnowledgeMapper knowledgeMapper;

    private final CourseMapper courseMapper;

    private final ExerciseKnowledgeMapper exerciseKnowledgeMapper;

    public KnowledgeServiceImpl(KnowledgeMapper knowledgeMapper, CourseMapper courseMapper, ExerciseKnowledgeMapper exerciseKnowledgeMapper) {
        this.knowledgeMapper = knowledgeMapper;
        this.courseMapper = courseMapper;
        this.exerciseKnowledgeMapper = exerciseKnowledgeMapper;
    }

    @Override
    public String getKnowledge(int courseId) {
        Course course = courseMapper.getCourseByCourseId(courseId);
        BusinessResponseEnum.GETKNOWLEDGETREEEXERCISE.assertNotNull(course);
        return course.getKnowledgeTree();
    }

    @Override
    public List<Knowledge> getKnowledgeNotTree(int courseId) {
        // 用于前端模糊查询习题，添加一个无知识点的选择
        List<Knowledge> knowledges = knowledgeMapper.getKnowledge(courseId);
        knowledges.add(new Knowledge(0, Message.get("NotKnowledge"), ""));
        return knowledges;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateKnowledge(HttpServletRequest request, CourseKnowledgeTO courseKnowledge) {
        // 获取课程的所有知识点
        List<Knowledge> knowledgeList = knowledgeMapper.getKnowledge(courseKnowledge.getCourseId());

        int courseId = courseKnowledge.getCourseId();

        if (knowledgeList == null || knowledgeList.isEmpty()) {

            KnowledgeTreeTO knowledgeTree = courseKnowledge.getKnowledgeTree();

            // 新增Knowledge的根节点
            Knowledge knowledge = new Knowledge(courseId, 0, knowledgeTree.getText(), knowledgeTree.getKeyword());
            knowledgeMapper.addKnowledge(knowledge);

            // 修改树对象中的节点的id为新增加节点的id
            knowledgeTree.setId(knowledge.getKnowledgeId());

            // 树对象的节点有子节点继续递归更新树对象中的数据为新数据
            if (knowledgeTree.getChildren() != null && !knowledgeTree.getChildren().isEmpty()) {
                updateTree(courseId, knowledgeTree.getId(), knowledgeList, knowledgeTree.getChildren());
            }

            // 验证课程是否编辑完成并修改课程完成状态
//				courseService.verifyCourseIsFinish(courseId, 2);
        } else {
            KnowledgeTreeTO knowledgeTree = courseKnowledge.getKnowledgeTree();

            // 修改Knowledge的根节点
            Knowledge knowledge = new Knowledge(knowledgeTree.getId(), knowledgeTree.getText(), knowledgeTree.getKeyword());
            knowledgeMapper.updateKnowledge(knowledge);

            // 移除原list中的根节点的数据
            for (int i = 0; i < knowledgeList.size(); i++) {
                if (knowledgeList.get(i).getParentId() == 0) {
                    knowledgeList.remove(i);
                    break;
                }
            }

            // 树对象的节点有子节点继续递归更新树对象中的数据为新数据
            if (courseKnowledge.getKnowledgeTree().getChildren() != null && !courseKnowledge.getKnowledgeTree().getChildren().isEmpty()) {
                updateTree(courseId, knowledgeTree.getId(), knowledgeList, courseKnowledge.getKnowledgeTree().getChildren());
            }

            // 原list中剩余的节点数据为新树中需要删除的节点
            for (int i = 0; i < knowledgeList.size(); i++) {
                // 通过知识点id删除知识点习题关联表数据
                exerciseKnowledgeMapper.deleteByKnowledgeId(knowledgeList.get(i).getKnowledgeId());
                // 通过知识点id删除知识点表中数据
                knowledgeMapper.deleteKnowledge(knowledgeList.get(i).getKnowledgeId());
            }
        }

        // 把修改好id的树对象转换成json字符串，并修改课程表中数据
        Gson gson = new Gson();
        String knowledgeTreeStr = gson.toJson(courseKnowledge.getKnowledgeTree());
        // 获取课程信息，用于避免修改课程表时其他值修改成默认值的问题以及用于生成通知
        Course course = courseMapper.getCourseByCourseId(courseId);
        BusinessResponseEnum.KNOWLEDGETREECOURSE.assertNotNull(course);
        course.setKnowledgeTree(knowledgeTreeStr);
        return courseMapper.updateCourse(course);
    }

    private void updateTree(int courseId, int parentId, List<Knowledge> knowledgeList, List<KnowledgeTreeTO> treeDataList) {
        for (int i = 0; i < treeDataList.size(); i++) {
            KnowledgeTreeTO treeData = treeDataList.get(i);
            if (treeData.getId() < 0) {
                // 新增Knowledge的根节点
                Knowledge knowledge = new Knowledge(courseId, parentId, treeData.getText(), treeData.getKeyword());
                knowledgeMapper.addKnowledge(knowledge);
                // 修改树对象中的节点的id为新增加节点的id
                treeData.setId(knowledge.getKnowledgeId());
            } else {
                // 与原数据list对比
                for (int j = 0; j < knowledgeList.size(); j++) {
                    Knowledge oldKnowledge = knowledgeList.get(j);
                    // 有树对象节点id与原数据知识点id相等的则进行修改
                    if (treeData.getId() == oldKnowledge.getKnowledgeId()) {
                        // 修改Knowledge的节点
                        Knowledge knowledge = new Knowledge(treeData.getId(), treeData.getText(), treeData.getKeyword());
                        knowledgeMapper.updateKnowledge(knowledge);
                        // 移除原list中的数据
                        knowledgeList.remove(j);
                        break;
                    }
                }
            }
            // 树对象的节点有子节点继续递归更新树对象中的数据为新数据
            if (treeData.getChildren() != null && !treeData.getChildren().isEmpty()) {
                updateTree(courseId, treeData.getId(), knowledgeList, treeData.getChildren());
            }
        }
    }

}
