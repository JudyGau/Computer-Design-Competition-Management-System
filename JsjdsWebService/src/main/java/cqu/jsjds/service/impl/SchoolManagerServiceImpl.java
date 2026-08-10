package cqu.jsjds.service.impl;

import cqu.jsjds.entity.Password;
import cqu.jsjds.entity.Reviewer;
import cqu.jsjds.entity.Teacher;
import cqu.jsjds.entity.Work;
import cqu.jsjds.mapper.*;
import cqu.jsjds.service.SchoolManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cqu.jsjds.utils.Function.splitListIntoArrays;

@Service
public class SchoolManagerServiceImpl implements SchoolManagerService {

    @Autowired
    TeacherMapper teacherMapper;

    @Autowired
    WorkMapper workMapper;

    @Autowired
    ManagerMapper managerMapper;

    @Autowired
    ReviewerReviewWorkMapper reviewerReviewWorkMapper;

    @Autowired
    ReviewerMapper reviewerMapper;

    @Override
    public List<Teacher> getTeacherOfSchool(String school) {
        return teacherMapper.findTeacherBySchool(school);
    }

    @Override
    public int deleteTeacher(String school, String teacherId) {
        try {
            teacherMapper.deleteTeacher(school, teacherId);
            return 1;
        } catch (Exception e) {
            System.err.println(e);
            return 2;
        }
    }

    @Override
    public int updateIdentityOfTeacher(String school, String teacherId, String identity) {
        try {
            teacherMapper.updateIdentityById(identity, school, teacherId);
            return 1;
        } catch (Exception e) {
            System.err.println(e);
            return 2;
        }
    }

    @Override
    public List<Work> getWorkOfSchool(String school, int isSchoolCompetition) {
        return workMapper.findWorkBySchoolAndIsSchoolCompetition(school, isSchoolCompetition);
    }

    @Override
    public List<Work> getProvinceWorkOfSchool(String school, int flag) {
        return workMapper.findWorkBySchoolAndIsProvinceCompetition(school, flag);
    }

    @Override
    public String getProvinceQuotaOfSchool(String school) {
        return managerMapper.findQuotaBySchool(school);
    }


    @Override
    public int autoSendWorkToProvinceCompetition(String school, int quota) {
        try {
            List<Work> workList = workMapper.findWorkBySchoolOrderBySCScoreLimitQuota(school, quota);

            for (Work w : workList) {
                try {
                    sendToProvinceCompetition(w);
                } catch (DuplicateKeyException e) {
                    continue;
                }
            }
            return 1;
//        } catch (DuplicateKeyException e) {
//            System.err.println(e);
//            return 2;
        } catch (Exception e) {
            System.err.println(e);
            return 3;
        }

    }


    public void sendToProvinceCompetition(Work tempWork) {

        List<Reviewer> reviewerList = reviewerMapper.findReviewerByCategoryRandomly(tempWork.getCategory());

        System.out.println("该作品被分配给省赛审核员：" + reviewerList + "-------------");

        if (!reviewerList.isEmpty()) {
            Reviewer tempReviewer = reviewerList.get(0);
            reviewerReviewWorkMapper.insertReviewerReviewWork(tempReviewer.getReviewerId(), tempWork.getWorkName());
        }

        workMapper.updateWorkWithIsProvinceCompetition(1, tempWork.getWorkName());
        return;

    }

    @Override
    public int updatePassword(Password password, String schoolManagerId) {
        try {
            if (!password.getOldPassword().equals(managerMapper.findPasswordById(schoolManagerId))) {
                return 1;
            } else if (!password.getNewPassword().equals(password.getNewRePassword())) {
                return 2;
            } else {
                managerMapper.updatePasswordById(password.getNewPassword(), schoolManagerId);
                return 3;
            }

        } catch (Exception e) {
            System.err.println(e);
            return 4;
        }
    }

    @Override
    public Map<String, Object> getSchoolStatisticsInfo(String school) {
        Map<String, Object> map = new HashMap<>();

        List<Map<String, Integer>> mapList = workMapper.findWorkNumberOfEachCategoryBySchool(school);
        Object[] objects = splitListIntoArrays(mapList);
        map.put("category", objects[0]);
        map.put("categoryWorkNumber", objects[1]);

        mapList = workMapper.findWorkNumberOfEachScoreBySchool(school);
        objects = splitListIntoArrays(mapList);
        map.put("score", objects[0]);
        map.put("scoreWorkNumber", objects[1]);

        mapList = workMapper.findWorkNumberOfEachAwardBySchool(school);
        objects = splitListIntoArrays(mapList);
        map.put("award", objects[0]);
        map.put("awardWorkNumber", objects[1]);

        String quota = managerMapper.findQuotaBySchool(school);
        String n = workMapper.findWorkNumberBySchool(school);
        String s = "本届计算机设计大赛" + school + "共有" + n + "份参赛作品,其中有" + quota + "份优秀作品晋级到省赛。本届大赛我校再创佳绩，晋级到省赛的作品中有" + mapList.get(0).get("一等奖") + "件作品获一等奖，"
                + mapList.get(1).get("二等奖") + "件作品获二等奖，"
                + mapList.get(2).get("三等奖") + "件作品获三等奖。";
        map.put("str", s);

        return map;
    }
}
